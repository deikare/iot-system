from influxdb_client import InfluxDBClient, Point
from influxdb_client.client.write_api import ASYNCHRONOUS
import random
import time
import logging

logging.basicConfig(level=logging.INFO)

client = InfluxDBClient(url="http://localhost:8086", token="my-token", org="my-org")
buckets_api = client.buckets_api()

#buckets_api.create_bucket(bucket="logs")

write_api = client.write_api(write_options=ASYNCHRONOUS)

hubIDs = ["A", "B", "C"]
deviceIDs = ["X", "Y", "Z"]

measurementTypes = ["temperature [K]", "humidity [%]", "pressure [hPa]"]

logTypes = ["health", "status"]
logValues = ["OK", "NOT_OK"]

buckets = ["data", "logs"]

while True:
    bucket = random.choice(buckets)
    hubId = random.choice(hubIDs)
    deviceId = random.choice(deviceIDs)

    if bucket == "logs":
        logType = random.choice(logTypes)
        logValue = random.choice(logValues)
        p = Point("log").tag("hubId", hubId).tag("deviceId", deviceId).tag("logType", logType).field("value", logValue)

        logging.info("Updating logs \n" + \
            "hudId: " + hubId + "\n" + \
            "deviceId: " + deviceId + "\n" + \
            "logType: " + logType + "\n" + \
            "value: " + logValue + "\n")
    else :
        measurementType = random.choice(measurementTypes)
        measurementValue = random.uniform(0, 100)
        p = Point("data").tag("hubId", hubId).tag("deviceId", deviceId).tag("measurementType", measurementType).field("value", measurementValue)

        logging.info("Updating data \n" + \
            "hudId: " + hubId + "\n" + \
            "deviceId: " + deviceId + "\n" + \
            "measurementType: " + measurementType + "\n" + \
            "value: " + str(measurementValue) + "\n")

    result = write_api.write(bucket=bucket, record=p)
    result.get()
    time.sleep(1)