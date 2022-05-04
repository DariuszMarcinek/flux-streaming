package flux.streaming.api;

import flux.streaming.api.model.Car;
import reactor.core.publisher.Flux;

public interface Cars {

    Flux<Car> getCars();

    Flux<Car> getCarsWithDelay(int delay);
}
