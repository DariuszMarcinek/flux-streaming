package flux.streaming.application;

import flux.streaming.service.client.CarsClient;
import flux.streaming.service.model.Car;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Controller
public class CarsAppController {

    private final static Logger log = LoggerFactory.getLogger(CarsAppController.class);

    private final CarsClient carsClient;

    public CarsAppController(CarsClient carsClient) {
        this.carsClient = carsClient;
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

}
