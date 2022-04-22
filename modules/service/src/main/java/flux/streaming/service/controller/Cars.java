package flux.streaming.service.controller;

import flux.streaming.service.model.Car;
import reactor.core.publisher.Flux;

public interface Cars {

    Flux<Car> getCars();

    Flux<Car> getCarsWithDelay(int delay);
}
