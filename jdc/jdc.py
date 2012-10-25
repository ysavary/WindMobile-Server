from datetime import datetime
import requests
import pymongo
import apscheduler.scheduler

scheduler = apscheduler.scheduler.Scheduler(daemonic=False)

connection = pymongo.Connection('localhost', 27017)
db = connection.windmobile
stations_collection = db.stations

@scheduler.interval_schedule(seconds=60)
def fetch_jdc_data():
    print("Processing JDC data...")
    result = requests.get("http://meteo.jdc.ch/API/?Action=StationView&flags=unactive|active|maintenance|test")
    for jdc_station in result.json['Stations']:
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
        stations_collection.update({'_id': station_id}, station, True)

        # Asking 2 days of data
        result = requests.get(
            "http://meteo.jdc.ch/API/?Action=DataView&serial={jdc_id}&duration=172800".format(jdc_id=jdc_id))
        if result.json['ERROR'] == 'OK':
            try:
                kwargs = {'capped': True, 'size': 500000, 'max': 5000}
                values_collection = db.create_collection("values_" + station_id, **kwargs)
            except pymongo.errors.CollectionInvalid:
                values_collection = db["values_" + station_id]

            jdc_measurements = result.json['data']['measurements']
            for jdc_measurement in jdc_measurements:
                measurement = {'_id': jdc_measurement['unix-time'],
                               'wind-direction': jdc_measurement['wind-direction'],
                               'wind-average': jdc_measurement['wind-average'],
                               'wind-maximum': jdc_measurement['wind-maximum'],
                               'temperature': jdc_measurement['temperature'],
                               'humidity': jdc_measurement['humidity']}
                values_collection.insert(measurement)

            start_date = datetime.fromtimestamp(jdc_measurements[0]["unix-time"])
            end_date = datetime.fromtimestamp(jdc_measurements[-1]["unix-time"])
            print(jdc_station['shortname'] + "(" + station_id + ") => " + str(
                len(jdc_measurements)) + " values from " + str(start_date) + " to " + str(end_date))

# Fetch at startup
fetch_jdc_data()
scheduler.start()