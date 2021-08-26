package com.example.backend.device.manager.kafka.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Arrays;

public class KafkaBaseEntityProducer<K, V> {
    private final KafkaTemplate<K, V> template;

    private final Logger logger = LoggerFactory.getLogger(KafkaBaseEntityProducer.class);

    public KafkaBaseEntityProducer(KafkaTemplate<K, V> template) {
        this.template = template;
    }

    public void produceKafkaRecord(ProducerRecord<K, V> record) {
        ListenableFuture<SendResult<K, V>> future = template.send(record);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(@NotNull Throwable throwable) {
                logger.info("Error during sending: " + Arrays.toString(throwable.getStackTrace()));
            }

            @Override
            public void onSuccess(SendResult<K, V> result) {
                logger.info("Successfully sent " + result);
            }
        });
    }
}
