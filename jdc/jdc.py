import requests
import pymongo

connection = pymongo.Connection('localhost', 27017)
db = connection.windmobile
station_collection = db.stations

result = requests.get("http://meteo.jdc.ch/API/?Action=StationView&flags=unactive|active|maintenance|test")
for jdc_station in result.json['Stations']:
    jdc_id = jdc_station['serial']
    station_id = 'jdc:' + str(jdc_id)
    station = {'_id': station_id,
               'name': jdc_station['name'],
               'shortname': jdc_station['shortname'],
               'last-measurements': jdc_station['last-measurements']}
    station_collection.update({'_id': station_id}, station, True)

    # Asking 2 days of data
    result = requests.get(
        "http://meteo.jdc.ch/API/?Action=DataView&serial={jdc_id}&duration=172800".format(jdc_id=jdc_id))
    if result.json['ERROR'] == 'OK':
        try:
            kwargs = {'capped': True, 'size': 500000, 'max': 5000}
            values_collection = db.create_collection(station_id, **kwargs)
        except pymongo.errors.CollectionInvalid:
            values_collection = db[station_id]

        print(jdc_station['shortname'] + "(" + station_id + ") => " + str(
            len(result.json['data']['measurements'])) + " measurements")
        for jdc_measurement in result.json['data']['measurements']:
            measurement = {'_id': jdc_measurement['unix-time'],
                           'wind-direction': jdc_measurement['wind-direction'],
                           'wind-average': jdc_measurement['wind-average'],
                           'wind-maximum': jdc_measurement['wind-maximum'],
                           'temperature': jdc_measurement['temperature'],
                           'humidity': jdc_measurement['humidity']}
            values_collection.insert(measurement)