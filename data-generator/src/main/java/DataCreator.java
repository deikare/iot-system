import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import model.InfluxDeviceDataPojo;
import model.InfluxDeviceLogPojo;
import model.InfluxHubLogPojo;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

public class DataCreator {
    private static final String url = "http://localhost:8086";
    private static final char[] token = "my-token".toCharArray();
    private static final String org = "my-org";

    private static final WriteApiBlocking writeApiLogs = InfluxDBClientFactory.create(url, token, org, "logs").getWriteApiBlocking();
    private static final WriteApiBlocking writeApiData = InfluxDBClientFactory.create(url, token, org, "data").getWriteApiBlocking();
    private static final WriteApiBlocking writeApiHubs = InfluxDBClientFactory.create(url, token, org, "hubs").getWriteApiBlocking();


    private static final String[] hubIds = {UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString()};
    private static final String[] deviceIds = {"4", "5", "6"};

    private static final String[] measurementTypes = {"temperature [K]", "humidity [%]", "pressure [hPa]"};

    private static final String[] logTypes = {"health", "status"};
    private static final String[] logValues = {"OK", "NOT_OK"};

    private static final String[] buckets = {"data", "logs"};

    public static void main(String[] args) {
        for (String hubId : hubIds) {
            InfluxHubLogPojo influxHubLogPojo = new InfluxHubLogPojo(Instant.now(), hubId, "CREATED");
            writeApiHubs.writeMeasurement(WritePrecision.MS, influxHubLogPojo);
            System.out.println("saved " + influxHubLogPojo + "\n");
        }

            while(true) {
            String bucket = randomElement(buckets);
            String hubId = randomElement(hubIds);
            String deviceId = randomElement(deviceIds);

            if (bucket.equals("logs")) {
                String logType = randomElement(logTypes);
                String logValue = randomElement(logValues);

                InfluxDeviceLogPojo logPojo = new InfluxDeviceLogPojo(Instant.now(), hubId, deviceId, logValue, logType);
                writeApiLogs.writeMeasurement(WritePrecision.MS, logPojo);
                System.out.println("saved " + logPojo + " \n");
            }
            else {
                String measurementType = randomElement(measurementTypes);
                double measurementValue = 100.0D * (new Random().nextDouble());

                InfluxDeviceDataPojo dataPojo = new InfluxDeviceDataPojo(Instant.now(), hubId, deviceId, measurementValue, measurementType);
                writeApiData.writeMeasurement(WritePrecision.MS, dataPojo);
                System.out.println("saved " + dataPojo + " \n");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static <T> T randomElement(T[] array) {
        return array[new Random().nextInt(array.length)];
    }
}
