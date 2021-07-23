from influxdb_client import InfluxDBClient, Point
from influxdb_client.client.write_api import ASYNCHRONOUS
import random
import time
import logging

bucket = "data"

logging.basicConfig(level=logging.INFO)

client = InfluxDBClient(url="http://localhost:8086", token="my-token", org="my-org")
write_api = client.write_api(write_options=ASYNCHRONOUS)

hubIDs = ["A", "B", "C"]
deviceIDs = ["X", "Y", "Z"]

messageTypes = ["log", "measurement"]

measurementTypes = ["temperature [K]", "humidity [%]", "pressure [hPa]"]

logTypes = ["health", "status"]
logValues = ["OK", "NOT_OK"]

while True:
    messageType = random.choice(messageTypes)
    hubID = random.choice(hubIDs)
    deviceID = random.choice(deviceIDs)

    if messageType == "log":
        logType = random.choice(logTypes)
        logValue = random.choice(logValues)
        p = Point(messageType).tag("hubID", hubID).tag("deviceID", deviceID).tag("logType", logType).field("value", logValue)

        logging.info("Updating point \n" + \
            "hudID: " + hubID + "\n" + \
            "deviceID: " + deviceID + "\n" + \
            "logType: " + logType + "\n" + \
            "value: " + logValue)
    else :
        measurementType = random.choice(measurementTypes)
        measurementValue = random.uniform(0, 100)
        p = Point(messageType).tag("hubID", hubID).tag("deviceID", deviceID).tag("measurementType", measurementType).field("value", measurementValue)

        logging.info("Updating point \n" + \
            "hudID: " + hubID + "\n" + \
            "deviceID: " + deviceID + "\n" + \
            "measurementType: " + measurementType + "\n" + \
            "value: " + str(measurementValue))

    result = write_api.write(bucket=bucket, record=p)
    result.get()
    time.sleep(1)