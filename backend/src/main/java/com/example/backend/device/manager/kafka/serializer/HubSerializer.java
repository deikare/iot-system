package com.example.backend.device.manager.kafka.serializer;

import com.example.backend.device.manager.model.Hub;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

//TODO perhaps it's not necessary, will see
public class HubSerializer implements Serializer<Hub> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String s, Hub hub) {
        return new byte[0];
    }

    @Override
    public byte[] serialize(String topic, Headers headers, Hub data) {
        return Serializer.super.serialize(topic, headers, data);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
