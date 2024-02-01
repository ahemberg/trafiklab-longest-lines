# trafiklab-longest-lines
Spring boot application that provides an API to query to retrieve the ten longest SL lines. It also provides and api to list all stops for these lines.

## Running the service
The simplest way to start up the service is to use the included mvn wrapper like so

```shell
 ./mvnw clean spring-boot:run
```

This will load the service onto `localhost:8080`. 

### Specify an API key
In order to be able to retrieve data from trafiklab you need to provide an API key. A key can be obtained from here: https://www.trafiklab.se/api/trafiklab-apis/sl/stops-and-lines-2/
Once you have a key you need to change the config property `trafiklab-api-key` in `src/main/resources/application.properties`

```shell
trafiklab.api-key=<ADD-KEY-HERE>
```

## API
The service provides the following endpoints

`/` The root endpoint. Returns no data, but links to the available endpoints, example:

```json
{
  "href":"http://localhost:8080/longest",
  "_links": {
    "longest/outbound": {
      "href":"http://localhost:8080/longest/outbound"
    },
    "longest/inbound": {
      "href":"http://localhost:8080/longest/inbound"
    },
    "self": {
      "href":"http://localhost:8080/"
    }
  }
}
```


