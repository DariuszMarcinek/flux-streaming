package flux.streaming.application;

import flux.streaming.service.client.CarsClient;
import flux.streaming.service.model.Car;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class CarsTest {

    @Inject
    CarsClient carsClient;

    @Test
    void shouldGetCarList() throws InterruptedException {
        var disposable = carsClient.getCarsWithDelay(1000)
                                   .doOnNext(car -> System.out.println("TEST - NEXT: " + car))
                                   .doOnCancel(() -> System.out.println("TEST - CANCELLED"))
                                   .subscribe();

        Thread.sleep(1500);

        disposable.dispose();

        System.out.println("client: " + carsClient + ", classes: " + carsClient.getClass().getClasses());
    }
}
