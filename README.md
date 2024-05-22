# MeltIngestion

MeltIngestion is a repo to ingest melt data to NR API's exposed.

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)
## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.project.MeltIngestion.MeltIngestionApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

Use the below GET end point in browser to start ingestion process
http://localhost:8080/ingest.

Check if the data got ingested by logging into the NR application.
