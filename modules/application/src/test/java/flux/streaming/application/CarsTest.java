package flux.streaming.application;

import flux.streaming.api.client.CarsClient;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

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
