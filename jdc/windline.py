from datetime import datetime
import logging, logging.handlers

# Modules
import requests
import pymongo
import MySQLdb

logger = logging.getLogger('windline')
# Limit file to 5Mb
handler = logging.handlers.RotatingFileHandler('log/windline.log', maxBytes=5 * 10 ** 6)
fmt = logging.Formatter('%(levelname)s %(asctime)s %(message)s', '%Y-%m-%dT%H:%M:%S%z')
handler.setFormatter(fmt)
logger.addHandler(handler)
logger.setLevel(logging.INFO)

mongo_connection = pymongo.Connection('localhost', 27017)
db = mongo_connection.windmobile
stations_collection = db.windline_stations

mysql_connection = MySQLdb.connect(host='',
    user='',
    passwd='',
    db='')
cursor = mysql_connection.cursor()

def get_property_id(key):
    cursor.execute('SELECT tblstationpropertylistno FROM tblstationpropertylist WHERE uniquename="{0}"'.format(key))
    return cursor.fetchone()[0]


def get_property_value(station_no, property_id):
    cursor.execute(
        'SELECT value FROM tblstationproperty WHERE tblstationno={0} AND tblstationpropertylistno={1}'.format(
            station_no, property_id))
    return cursor.fetchone()[0]

# unactive, maintenance, test, active, unknown
def get_status(status):
    if status == 'offline':
        return 'unactive'
    elif status == 'maintenance':
        return 'maintenance'
    elif status == 'demo':
        return 'test'
    elif status == 'online':
        return 'active'
    else:
        return "unknown"


def get_historic_data(station_id, data_id, start_time):
    cursor.execute(
        'SELECT measuredate, data FROM tblstationdata WHERE stationid={0} AND dataid={1} AND measuredate>"2012-10-31 12:00"'.format(
            station_id, data_id, start_time))
    return cursor.fetchall()


def get_int_measure(dict, key):
    if key in dict:
        return dict[key]
    else:
        return 0

try:
    logger.info("Processing WINDLINE data...")

    status_property_id = get_property_id('status')
    altitude_property_id = get_property_id('altitude')
    longitude_property_id = get_property_id('longitude')
    latitude_property_id = get_property_id('latitude')

    cursor.execute('SELECT * FROM tblstation')
    rows = cursor.fetchall()

    stations_collection.remove({})
    for windline_station in rows:
        try:
            station_no = windline_station[0]
            station_id = str(windline_station[1])
            station = {'_id': station_id,
                       'shortname': windline_station[2],
                       'name': windline_station[2]
            }

            station['status'] = get_status(get_property_value(station_no, status_property_id))
            station['altitude'] = get_property_value(station_no, altitude_property_id)
            station['longitude'] = get_property_value(station_no, longitude_property_id)
            station['latitude'] = get_property_value(station_no, latitude_property_id)

            stations_collection.insert(station)
        except Exception as e:
            logger.error("Error while fetching station <{0}>: {1}".format(station_id, e))

        try:
            try:
                kwargs = {'capped': True, 'size': 500000, 'max': 5000}
                values_collection = db.create_collection("windline_values_" + station_id, **kwargs)
            except pymongo.errors.CollectionInvalid:
                values_collection = db["windline_values_" + station_id]

            # Wind direction
            wind_direction_data = get_historic_data(station_id, 16404, 0)

            logger.info("--> from " + station['shortname'] + "(" + station_id + "): " + str(len(
                wind_direction_data)) + " values inserted")

        except Exception as e:
            logger.error("Error while fetching data for station <{0}>': {1}".format(station_id, e))

except Exception as e:
    logger.error("Error while processing WINDLINE data: {0}".format(e))
