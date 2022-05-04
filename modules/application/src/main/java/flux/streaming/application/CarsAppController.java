package flux.streaming.application;

import flux.streaming.api.client.CarsClient;
import flux.streaming.api.model.Car;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.reactor.http.client.websocket.ReactorWebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

@Controller
public class CarsAppController {

    private final static Logger log = LoggerFactory.getLogger(CarsAppController.class);

    private final CarsClient carsClient;

    private final CarsClientWebSocket carsClientWebSocket;

    private final ReactorWebSocketClient webSocketClient;

    public CarsAppController(CarsClient carsClient, CarsClientWebSocket carsClientWebSocket,
                             @Client("http://localhost:8002") ReactorWebSocketClient webSocketClient) {
        this.carsClient = carsClient;
        this.carsClientWebSocket = carsClientWebSocket;
        this.webSocketClient = webSocketClient;
    }

    @Get("/cars-get")
    public Flux<Car> getCars() {
        return carsClient.getCars()
                         .doOnNext(car -> log.info("App - getCars - NEXT: {}", car))
                         .doOnCancel(() -> log.info("App - getCars - CANCELLED"));
    }

    @Get("/cars-get-cancel/{cancelAfter}")
    public void getCarsAndCancel(@PathVariable("cancelAfter") long cancelAfter) throws InterruptedException {
        var disposable = carsClient.getCarsWithDelay(1000)
                                   .doOnNext(car -> log.info("App - getCarsAndCancel - NEXT: {}", car))
                                   .doOnCancel(() -> log.info("App - getCarsAndCancel - CANCELLED"))
                                   .subscribe();

        Thread.sleep(cancelAfter);

        disposable.dispose();

        log.info("App - client: {}, classes: {}", carsClient, carsClient.getClass().getClasses());
    }

    @Get("/cars-send/{topic}")
    public String sendCars(@PathVariable("topic") String topic) {
        webSocketClient.connect(CarsClientWebSocket.class, "/ws/cars/" + topic)
                       .subscribe(client -> client.sendCars());

        return "sent";
    }
}
