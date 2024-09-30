package com.example.GroupedOpenApiBug;

import jakarta.annotation.PostConstruct;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GroupedOpenApiBugApplication {

	@Autowired
	private SwaggerUiConfigParameters swaggerUiConfigParameters;

	public static void main(String[] args) {
		SpringApplication.run(GroupedOpenApiBugApplication.class, args);
	}

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		return builder
			.routes()
			.route("First service Open Api", ps -> ps
				.path("/v3/api-docs/microservice")
				.filters(gatewayFilterSpec -> gatewayFilterSpec.rewritePath("^.*$", "/v3/api-docs")
				)
				.uri("http://localhost:8081")
			)
			.build();
	}

	@Bean
	public GroupedOpenApi groupedOpenApi() {
		swaggerUiConfigParameters.addGroup("microservice", "Microservice");
		return GroupedOpenApi
			.builder()
			.group("internalservice")
			.packagesToScan("com.example.GroupedOpenApiBug")
			.build();
	}

//  Comment GroupedOpenApi Bean above and uncomment PostConstruct to get external OpenAPI spec working
//	@PostConstruct
//	public void postConstruct() {
//		swaggerUiConfigParameters.addGroup("microservice", "Microservice");
//	}
}
