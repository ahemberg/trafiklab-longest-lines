# TrafikLab Longest Lines
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
The service provides the following endpoints defined in the table below. The response is always a json object.

| Endpoint               | Parameters  | Description                                                                                                                                                                                                              |
|------------------------|-------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/`                    | None        | The root endpoint. Returns no data, but links to the available endpoints.                                                                                                                                                |
| `/longest`             | None        | Returns the ten longest lines, as determined by total number of stops, i.e stops in both directions.                                                                                                                     |
| `/longest/outbound`    | None        | Returns the ten longest lines, as determined by total number of outgoing stops, i.e stops with direction code 1                                                                                                          |
| `/longest/inbound`     | None        | Returns the ten longest lines, as determined by total number of incoming stops, i.e stops with direction code 2.                                                                                                         |
| `/stops/{line-number}` | Line number | Returns all stops for a specific bus line if the bus line exists, otherwise it will return 404.  The bus stops are not ordered along the route but are presented in the order they are retrieved  by the trafikdata api. |

### Example output

#### Root `/` 

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

#### Longest `/longest`, `/longest/outbound`, `/longest/inbound`
The format of the response for the longest endpoints is identical, but the returned data may differ. The format is as follows:

```json
{
  "_embedded": {
    "lineDTOList": [
      {
        "line_number": 1
      },
      {
        "line_number": 2
      },
      ...
    ]
  },
  "_links": {
    "stops": [
      {
        "href": "http://localhost:8080/stops/1"
      },
      {
        "href": "http://localhost:8080/stops/2"
      },
      ...
    ],
    "self": {
      "href": "http://localhost:8080/longest"
    }
  }
}
```
#### Stops `/stops/{line-number}`
```json
{
  "line_number": 1,
  "outbound_stops": [
    {
      "id": 1,
      "name": "Stop One"
    },
    {
      "id": 2,
      "name": "Stop Two"
    }
  ],
  "inbound_stops": [
    {
      "id": 1,
      "name": "Stop One"
    },
    {
      "id": 2,
      "name": "Stop 2"
    }
  ],
  "_links": {
    "self": {
      "href": "http://localhost:8080/stops/636"
    },
    "longest": {
      "href": "http://localhost:8080/longest"
    },
    "longest/outbound": {
      "href": "http://localhost:8080/longest/outbound"
    },
    "longest/inbound": {
      "href": "http://localhost:8080/longest/inbound"
    }
  }
}
```


