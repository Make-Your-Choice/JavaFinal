package ru.template.example.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;

/**
 * При необходимости можно реагировать на перебалансировку если нельзя потерять оффсеты
 */
public class ConsumerRebalanceListenerImpl implements ConsumerRebalanceListener {

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {

    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {

    }
}