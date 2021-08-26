package com.example.backend.device.manager.kafka.services.senders;

import com.example.backend.device.manager.kafka.producer.KafkaBaseEntityProducer;
import com.example.backend.device.manager.kafka.record.control.hub.HubControlType;
import com.example.backend.device.manager.kafka.record.control.hub.KafkaHubControlRecordWrapper;
import com.example.backend.device.manager.model.Hub;
import org.apache.kafka.clients.producer.ProducerRecord;

public class HubControlSenderService extends BaseSenderService<String, KafkaHubControlRecordWrapper> {
    public HubControlSenderService(KafkaBaseEntityProducer<String, KafkaHubControlRecordWrapper> producer, String topic) {
        super(producer, topic);
    }

    public void sendControl(Hub hub, HubControlType controlType) {
        sendNewHubControlSignal(new KafkaHubControlRecordWrapper(hub, controlType));
    }

    private void sendNewHubControlSignal(KafkaHubControlRecordWrapper hubControl) {
        final ProducerRecord<String, KafkaHubControlRecordWrapper> record = new ProducerRecord<>(super.getTopic(), hubControl.getHub().getId(),
                hubControl);
        super.getProducer().produceKafkaRecord(record);
    }
}
