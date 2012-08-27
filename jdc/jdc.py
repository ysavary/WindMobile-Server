import requests
import pymongo

connection = pymongo.Connection('localhost', 27017)
db = connection.jdc

r = requests.get('http://www.jdc.ch/phpmadd/get_data.php?user=vls&table=stations')
stations = r.json
print(r.content)

r = requests.get('http://www.jdc.ch/phpmadd/get_data.php?user=vls&table=sensor&where=station_id=1002')
sensors = r.json
print(sensors)