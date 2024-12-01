package com.reactive.producer.controller;

import com.reactive.producer.service.ReactiveProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class ReactiveProducerController {
    ReactiveProducerService reactiveProducerService;
    @Autowired
    public ReactiveProducerController(ReactiveProducerService reactiveProducerService){
        this.reactiveProducerService=reactiveProducerService;
    }
    @PostMapping("/push")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<String> publishMessage(
            @RequestParam String topic,
            @RequestBody String message) {
        return reactiveProducerService.sendMessage(topic, message);
    }
}
