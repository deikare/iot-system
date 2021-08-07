package com.example.backend.device.manager.kafka.producer;

import com.example.backend.device.manager.kafka.record.KafkaRecordWrapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

public class KafkaEntitySender<K, V> {
    private final KafkaTemplate<K, KafkaRecordWrapper<V>> template;

    public KafkaEntitySender(KafkaTemplate<K, KafkaRecordWrapper<V>> template) {
        this.template = template;
    }

    public void sendKafkaRecord(ProducerRecord<K, KafkaRecordWrapper<V>> record) {
        ListenableFuture<SendResult<K, KafkaRecordWrapper<V>>> future = template.send(record);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SendResult<K, KafkaRecordWrapper<V>> result) {

            }
        });
    }
}
