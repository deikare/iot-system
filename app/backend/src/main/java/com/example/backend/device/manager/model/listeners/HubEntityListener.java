package com.example.backend.device.manager.model.listeners;

import com.example.backend.device.manager.model.Hub;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.persistence.PrePersist;

public class HubEntityListener {
    @Autowired
    private KafkaTemplate<Long, String> template;

    @PrePersist
    public void preSaveCallback(Hub hub) {
        final ProducerRecord<Long, String> record = new ProducerRecord<>("hubs", hub.getId(), hub.toString());
        ListenableFuture<SendResult<Long, String>> future = template.send(record);
        future.addCallback(new ListenableFutureCallback<SendResult<Long, String>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SendResult<Long, String> result) {

            }
        });
    }

}
