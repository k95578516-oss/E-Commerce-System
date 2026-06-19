package com.example.demo.outbox;

import com.example.demo.OrderEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OutboxPublisher(
            OutboxRepository outboxRepository,
            KafkaTemplate<String, Object> kafkaTemplate,
            ObjectMapper objectMapper) {

        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishEvents() {

        List<OutboxEvent> events =
                outboxRepository.findByProcessedFalse();

        if (events.isEmpty()) {
            return;
        }

        System.out.println(
                "Found " + events.size() +
                        " pending outbox events"
        );

        for (OutboxEvent event : events) {

            try {

                OrderEvent orderEvent =
                        objectMapper.readValue(
                                event.getPayload(),
                                OrderEvent.class
                        );

                kafkaTemplate.send(
                        "order-events",
                        orderEvent
                );

                event.setProcessed(true);

                outboxRepository.save(event);

                System.out.println(
                        "Published event: "
                                + event.getId()
                );

            } catch (Exception e) {

                System.out.println(
                        "Failed to publish event: "
                                + event.getId()
                );

                e.printStackTrace();
            }
        }
    }
}