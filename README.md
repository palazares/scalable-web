# scalable-web
Binary data comparison service. WAES coding assignment 

## Assignment details

* Provide 2 http endpoints that accepts JSON base64 encoded binary data on both endpoints
    * <host>/v1/diff/<ID>/left and <host>/v1/diff/<ID>/right
* The provided data needs to be diff-ed and the results shall be available on a third endpoint
    * <host>/v1/diff/<ID>
* The results shall provide the following info in JSON format:
    * If equal return that
    * If not of equal size just return that
    * If of same size provide insight in where the diffs are, actual diffs are not needed. So mainly offsets + length in the data
* Make assumptions in the implementation explicit, choices are good but need to be communicated

## Libs used

* Java 8
* Gradle 4
* Spring Boot 2
* Lombok
* Swagger 2
* JUnitParams 1.1 (for testing)

## Build and run

Clone git repo:

```
git clone https://github.com/palazares/scalable-web.git
```

Build application:

```
gradle build 
```

Run


```
gradle bootRun
```

## Tests

Run unit and integration tests:

```
gradle test
```

## API details

URI | HTTP Method | Content | Description
--- | --- | --- | ---
`<host>/v1/diff/<ID>/left` | PUT | left Base64 encoded string  | Validate and store left doc
`<host>/v1/diff/<ID>/right` | PUT | right Base64 encoded string  | Validate and store right doc
`<host>/v1/diff/<ID>` | GET | - | Generates difference if valid parts are present 

## Further improvements
* [Actuator](https://www.baeldung.com/spring-boot-actuators) service for PROD monitoring 
* [Docker](https://spring.io/guides/gs/spring-boot-docker/) for deployment
* Spring security template prod config has been added to illustrate the way of X.509 authentication. Please note that tests and dev profile are left without security for simplification. 
The one could write integration tests with security implementation. Local dev run could also use predefined certs and security config.
* Switching spring [profiles](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-profiles.html) were used for easy security config 
* Return revison number of DifferenceRecord in PUT response so that client might get an idea if he gets difference for his revision of document.
Keep storing only one revision - last one. 
* [MongoDB](https://www.mongodb.com/) is NoSQL document-oriented database. It supports easy horizontal scalability with sharding. Such choice will allow us to make service highly scalable in future.
* In enterprise environment it's better to use one facing [balancing service](https://en.wikipedia.org/wiki/Load_balancing_(computing)) (still with DR), which would proxy actual work to easily scalable service instances like the current one.
This facing service could also take all security responsibility (authentication+authorization) and proxy request to DMZ zone where simple services live.
Facing balancer could be used for partial deployment also, when part of working nodes are excluded from farm and updated with new software.
This allows to avoid downtime.

