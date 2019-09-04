package com.waes.palazares.scalableweb;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.net.URI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@SpringBootApplication
public class ScalableWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScalableWebApplication.class, args);
	}

	@Bean
	RouterFunction<ServerResponse> routerFunction() {
		return route(GET("/"), req ->
				ServerResponse.temporaryRedirect(URI.create("swagger-ui.html"))
						.build());
	}
}
