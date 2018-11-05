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
`<host>/v1/diff/<ID>` | GET | -none- | Generates difference if valid parts are present 

## Further improvements
* [Actuator](https://www.baeldung.com/spring-boot-actuators) service for PROD monitoring 
* Security consideration (SSL cert for api calls)
* Docker for deployment
* DEV/PROD profiles
* Return revison number of DifferenceRecord in PUT response so that client might get an idea if he gets difference for his revision of document.
Keep storing only only one revision - last one. 
* MongoDB is NoSQL distributed data base. It supports easy scalability, eg with sharding. Choosing such DB we could make service more scalable in future.

