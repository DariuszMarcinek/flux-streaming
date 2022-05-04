package flux.streaming.application;

import flux.streaming.api.model.Car;
import io.micronaut.http.HttpRequest;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.ClientWebSocket;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.stream.Stream;

@ClientWebSocket("/ws/cars/{topic}")
public abstract class CarsClientWebSocket implements AutoCloseable {

    private final static Logger log = LoggerFactory.getLogger(CarsClientWebSocket.class);

    private WebSocketSession session;

    @OnOpen
    void onOpen(String topic, WebSocketSession session, HttpRequest request) {
        this.session = session;
        log.info("onOpen - topic: {}, request: {}", topic, request);
    }

    @OnMessage
    void onMessage(String message) {
        log.info("onMessage - acknowledge: {}", message);
    }

    public void sendCars() {

        Flux.fromStream(Stream.of(
                    new Car("Volvo", "S60", "2016"),
                    new Car("BMW", "X6", "2020"),
                    new Car("Mercedes", "W124", "1990"),
                    new Car("BMW", "520i E39", "1997")
            ))
            .flatMap(o -> session.send(o))
            .subscribe();
    }

}
