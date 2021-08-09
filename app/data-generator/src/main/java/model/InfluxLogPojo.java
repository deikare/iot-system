package model;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.Instant;

@Measurement(name = "log")
public class InfluxLogPojo extends InfluxBasePojo {
    @Column(tag = true)
    private String logType;

    @Column
    private String value;

    public InfluxLogPojo(Instant time, String hubId, String deviceId, String value, String logType) {
        super(time, hubId, deviceId);
        this.logType = logType;
        this.value = value;
    }

    // default constructor for query result mapping
    public InfluxLogPojo() {
    }

    public String getLogType() {
        return logType;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "InfluxLogPojo{" +
                "time=" + time +
                ", hubId='" + hubId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", logType='" + logType + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
