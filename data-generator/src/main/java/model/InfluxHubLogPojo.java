package model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "hubLog")
public class InfluxHubLogPojo extends InfluxBasePojo{
    @Column
    private String status;

    public InfluxHubLogPojo(Instant time, String hubId, String status) {
        super(time, hubId);
        this.status = status;
    }

    public InfluxHubLogPojo() {

    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "InfluxHubLogPojo{" +
                "time=" + time +
                ", hubId='" + hubId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
