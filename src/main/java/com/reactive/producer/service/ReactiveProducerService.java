package com.reactive.producer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ReactiveProducerService {
    @Value(value = "${topic.dlq}")
    private String dlqTopic;
    private ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate;

    @Autowired
    public ReactiveProducerService(ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate) {
        this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
    }
    public Mono<String> sendMessage(String topic, String message) {
         return Mono.just(reactiveKafkaProducerTemplate.send(topic, message)
                .doOnSuccess(senderResult -> System.out.println("Reactive producer sent message:"+message))
                 .onErrorContinue((throwable, o) ->
                         //push failed message to dead letter queue
                         reactiveKafkaProducerTemplate.send(dlqTopic,message))
                 .subscribe()).thenReturn("request completed");
    }
}
