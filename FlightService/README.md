# Flight Departure Micro service
A simple micro serivce to present Flight departures for a specified date. Based on a CVS data model of flight information. Which is cache internally upon startup of teh service. 

## Assumptions:
1. Its unclear what mechanism triggers the flight schedule to be updated, and what this means to the micro service. I assume that the micro service is simply restarted, where upon it will reread the currently available 'flights.csv'. As part of a refresh process.
2. Its also unclear if the schedule if just 7 days long. The example indicates just 7 days, So I'm assuming that after the Saturday, the whole week repeats.
3. Its also assumed that the flight schedule is rendered with a specific departure airport in mind. e.g. London Gatwick. Not that this in important,just would seem natureal that the flights depart from a constant known location.


## Get the example
```
git clone git@github.com:gavinJLing/Flight-Information.git
cd Flight-Information/FlightService
```

## Flights.csv
The flight information data model. can be located at...
```
./data/flights.csv
```
Replace thisfile with a differnet cvs, then relaunch the microservice.

## Building and launching the Flight Departure Micro service
This is a standard Gradle build where Gradle actions such as ... 
```
./gradlew clean
./gradlew bootJar
./gradlew bootRun
```
Will perform a clean build of the Flight Departure Micro service and execute it using default configuration.  
The 'bootJar' command will create a standard Java .jar deliverable which is a self executing .jar e.g.
```
java -jar build/libs/FlightService-0.0.1-SNAPSHOT.jar
```


## Test and code coverage reports
To run the unit tests, or produce a code coverage report (Jacoco)
```
 ./gradlew clean test jacocoTestReport    
```
To see the Gradle test html report 
```
open  ./build/reports/tests/test/index.html
```

To see the Jacoco Codecoverage report
```
open ./build/reports/jacoco/test/html/index.html 
```

## GET a Flight departure Request
Perform a HTTP GET with url param in the YYYYMMDD, to obtain a JSON repsonse containing flight departures for any day of the  year.
Various tools can be used for this 'PostMan' plugin for the Chrome browser is propular, however other tools exist such as 'curl' and 'wget'.  See below for an example of 'curl' POST'ing a logical request as a JSON body to the Flight api endpoint.
```
curl -v  -H "Content-Type: application/json" -X GET localhost:8080/flight/api/departures/20180210
```
where the expected response should appear as:
```
*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8080 (#0)
> GET /flight/api/departures/20180210 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.54.0
> Accept: */*
> Content-Type: application/json
> 
< HTTP/1.1 200 
< Content-Type: application/json;charset=UTF-8
< Transfer-Encoding: chunked
< Date: Tue, 12 Feb 2019 23:00:12 GMT
< 
* Connection #0 to host localhost left intact
{"flights":[
   {"departureTime":"09:00:00","destination":"St Lucia","destIATA":"UVF","flightNumber":"VS097","operates":[true,false,false,false,false,false,false]},
   {"departureTime":"09:00:00","destination":"Tobago","destIATA":"TAB","flightNumber":"VS097","operates":[true,false,false,false,false,false,false]},
   {"departureTime":"10:10:00","destination":"Orlando","destIATA":"MCO","flightNumber":"VS049","operates":[true,false,false,false,false,false,false]},
   {"departureTime":"10:15:00","destination":"Las Vegas","destIATA":"LAS","flightNumber":"VS043","operates":[true,false,false,false,false,true,true]},
   {"departureTime":"11:05:00","destination":"Barbados","destIATA":"BGI","flightNumber":"VS029","operates":[true,true,true,true,true,true,true]},
   {"departureTime":"11:45:00","destination":"Orlando","destIATA":"MCO","flightNumber":"VS027","operates":[true,false,true,false,false,false,false]},
   {"departureTime":"12:40:00","destination":"Montego Bay","destIATA":"MBJ","flightNumber":"VS065","operates":[true,false,false,false,false,false,false]},
   {"departureTime":"13:00:00","destination":"Orlando","destIATA":"MCO","flightNumber":"VS015","operates":[true,true,true,true,true,true,true]},
   {"departureTime":"15:35:00","destination":"Las Vegas","destIATA":"LAS","flightNumber":"VS044","operates":[true,true,true,true,true,true,true]}
]}
```

## Serice URL
```
Http://localhost:8080/flight/api/departures/yyyymmdd
```
Where URL parameter 'yyyymmdd' is the date of departure being requested.

## HTTP Response codes
HTTP Status     | meaning 
-------------|---------------------
200  | Ok - JSON Flight depatures matching this date will be presented.
400  | Bad Request. Possible malformed Date url paramter.
500  | Internal failure. Typically a problem loading and parsing the CSV
503  | Unknown internal fault determined.



### Configuration overrides
The standard code is defaulting to Dev default values. These can be overridden typically for HTTP Listener port.

To apply different runtime configuration e.g.  

Service Property     | Default | Alternative Value
-------------|-----------|----------
server.port  | 8080  | 8081




use the following command line switches

```
java -jar build/libs/FlightService-0.0.1-SNAPSHOT.ja     --server.port=8081 
```
