package flux.streaming.api.client;

import flux.streaming.api.Cars;
import flux.streaming.api.model.Car;
import io.micronaut.http.HttpVersion;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Flux;

@Client(value = "cars", httpVersion = HttpVersion.HTTP_1_1)
public interface CarsClient extends Cars {

    @Override
    @Get(uri = "/cars")
    Flux<Car> getCars();

    @Get(uri = "/cars-delay/{delay}")
    Flux<Car> getCarsWithDelay(@PathVariable("delay") int delay);
}
