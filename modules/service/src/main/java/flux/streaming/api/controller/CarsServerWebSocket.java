package flux.streaming.api.controller;

import flux.streaming.api.model.Car;
import io.micronaut.http.HttpRequest;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@ServerWebSocket("/ws/cars/{topic}")
public class CarsServerWebSocket {
    private static final Logger log = LoggerFactory.getLogger(CarsServerWebSocket.class);

    private WebSocketSession session;

    @OnOpen
    public void onOpen(WebSocketSession session, HttpRequest request, String topic) {
        this.session = session;
        log.info("onOpen - topic: {}, request: {}", topic, request);
    }

    @OnMessage
    public void onMessage(Car car) throws InterruptedException {
        log.info("onMessage - received car: {}", car);

        Mono.fromRunnable(() -> sendAcknowledge(car.name()))
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe();
    }

    private void sendAcknowledge(String carName) {
        log.info("onMessage - processing: {}", carName);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.error("error", e);
        }
        session.sendSync("received: " + carName);
        log.info("onMessage - response sent");
    }

    @OnClose
    public void onClose(WebSocketSession session) {
        log.info("onClose - isSessionOpen: {}", session.isOpen());
    }
}
