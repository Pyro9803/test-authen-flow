package com.minhhn.testauthenflow;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/mqtt")
public class ConnectController {

    public record AuthRequest(String token) {}

    @PostMapping("/connect")
    public ResponseEntity<String> connect(@RequestBody AuthRequest authRequest) {
        Mqtt5AsyncClient client = MqttClient.builder()
                .serverHost("localhost")
                .serverPort(1883)
                .useMqttVersion5()
                .buildAsync();

        client.connectWith()
                .simpleAuth()
                .password(authRequest.token().getBytes(StandardCharsets.UTF_8))
                .applySimpleAuth()
                .send()
                .whenComplete((connAck, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                    } else {
                        System.out.println("Connected!");
                    }
                });

        return ResponseEntity.ok("Connecting...");
    }
}
