# TrafikLab Longest Lines

## Overview

This Spring Boot application offers an API to fetch the ten longest SL lines and lists all stops for
a specified line. It provides both JSON and plaintext responses

## Setup

### Requirements

The service runs on Java 21 and is compiled using maven, thus you need to have java runtime 21
installed and a recent version of maven

## Running the service

The simplest way to start up the service is to use the included mvn wrapper:

```shell
 ./mvnw clean spring-boot:run
```

The service will be accesible at `localhost:8080`.

### Configuration

An API key from [Trafiklab](https://www.trafiklab.se/api/trafiklab-apis/sl/stops-and-lines-2/) is
required. Update trafiklab-api-key in `src/main/resources/application.properties`:

```shell
trafiklab.api-key=<ADD-KEY-HERE>
```

## API Endpoints

The service provides two endpoints, json and clear-text.

### Json Endpoints

The json endpoint is available from the root and has the following endpoints defined in the table
below. The response is always a json object.

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
  "href": "http://localhost:8080/longest",
  "_links": {
    "longest/outbound": {
      "href": "http://localhost:8080/longest/outbound"
    },
    "longest/inbound": {
      "href": "http://localhost:8080/longest/inbound"
    },
    "self": {
      "href": "http://localhost:8080/"
    }
  }
}
```

#### Longest `/longest`, `/longest/outbound`, `/longest/inbound`

The format of the response for the longest endpoints is identical, but the returned data may differ.
The format is as follows:

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

### Clear text Endpoints

The clear text api is available under `/clear-text` and has the following endpoints defined in the
table below. The response is a comma separated string.

| Endpoint                                  | Parameters  | Description                                                                                                                                                                                                                       |
|-------------------------------------------|-------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------| 
| `clear-text/longest`                      | None        | Returns the ten longest lines, as determined by total number of stops, i.e stops in both directions.                                                                                                                              |      
| `clear-text/longest/outbound`             | None        | Returns the ten longest lines, as determined by total number of outgoing stops, i.e stops with direction code 1                                                                                                                   |
| `clear-text/longest/inbound`              | None        | Returns the ten longest lines, as determined by total number of incoming stops, i.e stops with direction code 2.                                                                                                                  |
| `clear-text/stops/{line-number}`          | Line number | Returns all stops for a specific bus line if the bus line exists, otherwise it will return 404.  The bus stops are not ordered along the route but are presented in the order they are retrieved  by the trafikdata api.          |
| `clear-text/stops/{line-number}/outbound` | Line number | Returns all outbound stops for a specific bus line if the bus line exists, otherwise it will return 404.  The bus stops are not ordered along the route but are presented in the order they are retrieved  by the trafikdata api. |
| `clear-text/stops/{line-number}/inbound`  | Line number | Returns all inbound stops for a specific bus line if the bus line exists, otherwise it will return 404.  The bus stops are not ordered along the route but are presented in the order they are retrieved  by the trafikdata api.  |

## Architecture

The data returned by the endpoints provided by this service is retrieved from an in memory database.
This means that as the service starts there are no dependencies on any database connections, but it
also means that no data is persisted during restarts. To load the data the service therefore calls
the SL Api on start up, modifies the data to calculate the longest lines and stores this data in the
database. This data is then periodically refreshed by scheduling the loader to run once per hour.
The refresh rate of one hour is arbitrary and can be made longer as the SL api typically only
updates once per day.

The class responsible for loading the data is
called [TrafikLabLoaderService.java](https://github.com/ahemberg/trafiklab-longest-lines/blob/master/src/main/java/eu/alehem/longestlines/service/TrafikLabLoaderService.java)
and it is responsible for calling the service that makes the API call to Trafiklab, and then calling
the services that store the data in the database. The business logic of calculating the longest
lines happens here.

The reason for loading the data into a database periodically as opposed to loading the data
continuously on every api call is to reduce the number of calls to the external API, as they are
quite slow and compute heavy. It would be possible to make the calls to the external API on every
request if a cache layer was used, but since the calls are so slow this would slow down any call
that happens to be made right after the cache was invalidated. For a real production service it
would be better to write a separate batch job that can write to a centralized database whenever the
upstream data changes, but this is probably out of scope for this task.

### Algorithm for calculating the longest lines

When storing bus lines in the database two datasets are joined, JourneyPatternPointOnLine and Line.
All JourneyPatterns are transformed into two maps, where the key represents the bus line and the
value stored is a list representing either all outgoing or ingoing lines. This data is then joined
by all bus lines from the dataset Line into a table with the following columns:

| LINE_NUMBER | TOTAL_INBOUND | TOTAL_OUTBOUND | 	TOTAL_STOPS | 	INBOUND_STOPS | OUTBOUND_STOPS |
|-------------|---------------|----------------|--------------|----------------|----------------|

When the longest lines are requested it is then a simple matter of querying the database for
this information, for example like:

```sql
SELECT * FROM bus_lines ORDER BY total_stops desc LIMIT :limit
```

The order by is changed depending on if total, outbound or inbound is requested.

### Listing the bus stops for a line

When the data is loaded, the TrafikLabLoaderService loads the dataset StopPoints into a database
table. This table is simple, containing only the bus stop id and the corresponding name for all bus
stops. When the API call is made to get all stops for a line, the line is first retrieved from the
table of lines, and then the corresponding bus stops are selected from the table of bus stops using
the query:

```sql
SELECT * FROM bus_stops WHERE bus_stop_id IN(:stops)
```

This query is made twice, once for outgoing and once for ingoing stops.

### Loading data from the SL API

The data is loaded from the SL API by the
class [TrafikLabService.java](https://github.com/ahemberg/trafiklab-longest-lines/blob/master/src/main/java/eu/alehem/longestlines/service/TrafikLabService.java).
This class handles all the API calls to the external API. It uses Webflux to load the data, which
enables the data to be loaded asynchronously. The class exposes three public methods, one for each
dataset to be loaded. These methods are called by the TrafikLabLoaderService service.




