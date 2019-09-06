package com.waes.palazares.scalableweb;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.net.URI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.waes.palazares.scalableweb.domain.DifferenceRecord;
import com.waes.palazares.scalableweb.domain.DifferenceResult;
import com.waes.palazares.scalableweb.exception.InavlidIdException;
import com.waes.palazares.scalableweb.service.DifferenceService;

import reactor.core.publisher.Mono;

@SpringBootApplication
public class ScalableWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScalableWebApplication.class, args);
	}

	@Bean
    RouterFunction<ServerResponse> routerFunction(DifferenceService differenceService) {
        return
                route(GET("/"), req ->
                        ServerResponse.temporaryRedirect(URI.create("swagger-ui.html")).build())

                        .andRoute(GET("/v1/diff/{id}"),
                                req -> ok().body(
                                        differenceService.getDifference(req.pathVariable("id")).map(DifferenceRecord::getResult), DifferenceResult.class))

                        .andRoute(PUT("/v1/diff/{id}/left"),
                                req -> req.bodyToMono(String.class)
                                        .switchIfEmpty(Mono.error(new InavlidIdException()))
                                        .map(b -> differenceService.putLeft(req.pathVariable("id"), b))
                                        .flatMap(res -> ok().body(res, DifferenceRecord.class))
                                        .onErrorStop())

                        .andRoute(PUT("/v1/diff/{id}/right"),
                                req -> req.bodyToMono(String.class)
                                        .switchIfEmpty(Mono.error(new InavlidIdException()))
                                        .map(b -> differenceService.putRight(req.pathVariable("id"), b))
                                        .flatMap(res -> ok().body(res, DifferenceRecord.class)));
	}
}
