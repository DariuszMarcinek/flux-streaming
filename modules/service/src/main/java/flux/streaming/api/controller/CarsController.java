package flux.streaming.api.controller;

import flux.streaming.api.Cars;
import flux.streaming.api.model.Car;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Controller
public class CarsController implements Cars {

    private static final Logger log = LoggerFactory.getLogger(CarsController.class);

    @Get(value = "/cars", produces = MediaType.APPLICATION_JSON_STREAM)
    public Flux<Car> getCars() {
        return Flux.fromIterable(
                List.of(
                        new Car("Fiat", "500", "2002"),
                        new Car("VW", "Passat", "2009"),
                        new Car("Toyota", "Corolla", "2020")
                )
        );
    }

    @Get(uri = "/cars-delay/{delay}")
    public Flux<Car> getCarsWithDelay(@PathVariable("delay") int delay) {
        return getCars().delayElements(Duration.ofMillis(delay))
                        .doOnNext(car -> log.info("Service - CARS WITH DELAY - NEXT: {}", car))
                        .doOnCancel(() -> log.info("Service - CARS WITH DELAY - CANCELLED"));
    }

//    @Get(uri = "/testws/{message}")
//    public Flux<Car> getCarsWithDelay(@PathVariable("message") String message) {
//        return carsWebSocket.
//    }

}
