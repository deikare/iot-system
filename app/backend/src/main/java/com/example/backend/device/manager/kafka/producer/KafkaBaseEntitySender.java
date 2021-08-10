package com.example.backend.device.manager.kafka.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

public class KafkaBaseEntitySender<K, V> {
    private final KafkaTemplate<K, V> template;

    public KafkaBaseEntitySender(KafkaTemplate<K, V> template) {
        this.template = template;
    }

    public void sendKafkaRecord(ProducerRecord<K, V> record) {
        ListenableFuture<SendResult<K, V>> future = template.send(record);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SendResult<K, V> result) {

            }
        });
    }
}
