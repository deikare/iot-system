package com.example.backend.device.manager.kafka.producer;

import com.example.backend.device.manager.kafka.record.KafkaRecordWrapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

public class KafkaEntitySender<B> {
    private final KafkaTemplate<Long, KafkaRecordWrapper<B>> template;

    public KafkaEntitySender(KafkaTemplate<Long, KafkaRecordWrapper<B>> template) {
        this.template = template;
    }

    public void sendKafkaRecord(ProducerRecord<Long, KafkaRecordWrapper<B>> record) {
        ListenableFuture<SendResult<Long, KafkaRecordWrapper<B>>> future = template.send(record);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SendResult<Long, KafkaRecordWrapper<B>> result) {

            }
        });
    }
}
