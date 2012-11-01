from datetime import datetime
import logging, logging.handlers

# Modules
import requests
import pymongo

logger = logging.getLogger('jdc')
# Limit file to 5Mb
handler = logging.handlers.RotatingFileHandler('log/jdc.log', maxBytes=5 * 10 ** 6)
fmt = logging.Formatter('%(levelname)s %(asctime)s %(message)s', '%Y-%m-%dT%H:%M:%S%z')
handler.setFormatter(fmt)
logger.addHandler(handler)
logger.setLevel(logging.INFO)

mongo_connection = pymongo.Connection('localhost', 27017)
db = mongo_connection.windmobile
stations_collection = db.jdc_stations

def get_int_measure(dict, key):
    if key in dict:
        return dict[key]
    else:
        return 0

try:
    logger.info("Processing JDC data...")
    result = requests.get("http://meteo.jdc.ch/API/?Action=StationView&flags=unactive|active|maintenance|test")

    stations_collection.remove({})
    for jdc_station in result.json['Stations']:
        try:
            jdc_id = jdc_station['serial']
            station_id = str(jdc_id)
            station = {'_id': station_id,
                       'shortname': jdc_station['shortname'],
                       'name': jdc_station['name'],
                       'altitude': jdc_station['altitude'],
                       'latitude': jdc_station['latitude'],
                       'longitude': jdc_station['longitude'],
                       'status': jdc_station['status'],
                       'timezone': jdc_station['timezone'],
                       'last-measurements': jdc_station['last-measurements']
            }
            stations_collection.insert(station)
        except Exception as e:
            logger.error("Error while fetching station <{0}>: {1}".format(station_id, e))

        try:
            # Asking 2 days of data
            result = requests.get(
                "http://meteo.jdc.ch/API/?Action=DataView&serial={jdc_id}&duration=172800".format(jdc_id=jdc_id))
            if result.json['ERROR'] == 'OK':
                try:
                    kwargs = {'capped': True, 'size': 500000, 'max': 5000}
                    values_collection = db.create_collection("jdc_values_" + station_id, **kwargs)
                except pymongo.errors.CollectionInvalid:
                    values_collection = db["jdc_values_" + station_id]

                jdc_measurements = result.json['data']['measurements']
                for jdc_measurement in jdc_measurements:
                    measurement = {'_id': jdc_measurement['unix-time'],
                                   'wind-direction': get_int_measure(jdc_measurement, 'wind-direction'),
                                   'wind-average': get_int_measure(jdc_measurement, 'wind-average'),
                                   'wind-maximum': get_int_measure(jdc_measurement, 'wind-maximum'),
                                   'temperature': get_int_measure(jdc_measurement, 'temperature'),
                                   'humidity': get_int_measure(jdc_measurement, 'humidity')}
                    values_collection.insert(measurement)

                start_date = datetime.fromtimestamp(jdc_measurements[0]["unix-time"])
                end_date = datetime.fromtimestamp(jdc_measurements[-1]["unix-time"])
                logger.info("--> from " + start_date.strftime('%Y-%m-%dT%H:%M:%S') + " to " + end_date.strftime(
                    '%Y-%m-%dT%H:%M:%S') + ", " + station['shortname'] + "(" + station_id + "): " + str(len(
                    jdc_measurements)) + " values inserted")

        except Exception as e:
            logger.error("Error while fetching data for station <{0}>': {1}".format(station_id, e))

except Exception as e:
    logger.error("Error while processing JDC data: {0}".format(e))
