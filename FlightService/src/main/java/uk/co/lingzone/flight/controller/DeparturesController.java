package uk.co.lingzone.flight.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import uk.co.lingzone.flight.exception.CSVLoaderException;
import uk.co.lingzone.flight.model.Departures;
import uk.co.lingzone.flight.model.Flight;
import uk.co.lingzone.flight.reader.CSVLoader;

/**
 * Flight Departures REST service api
 * Typically to serve up date specific flight departures.
 * 
 * @author gavinling
 *
 */
@RestController
@RequestMapping("/flight/api/")
public class DeparturesController {
    


   
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CSVLoader csvLoader;

    @Autowired
    Departures departures;

    /**
     * GET /departures/yyyymmdd The method will return flight departures for a date.
     *
     * e.g. HTTP GET Http://localhost:8080//flight/api/departures/20190212
     * 
     * returns: HTTP 200 + [Json structure] if found HTTP 404 if not found HTTP 503
     * if something when wrong... (i.e.no C)
     * 
     * 
     */

    @RequestMapping(value = "/departures/{departureYYYYMMDD}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<?> getDeparturesByDate(@PathVariable String departureYYYYMMDD) {
        HttpStatus httpStatus = HttpStatus.OK;
        String msg = String.format("Processed ok [%s]", departureYYYYMMDD);
        Departures selectDepartures=null;
        try {

            //validate the url path param - 
            LocalDate departureDate = validateDepartureDate(departureYYYYMMDD);
            
            
            
  
            if (departures.isEmpty()) {
                throw new IllegalStateException("No flight depature deail are available.");
            }
            
            
            // filter the current departure model for the specified date 
            selectDepartures = departures.forDay(departureDate);
            
            // dump flights if logger is in debug mode.
            departures.getFlights().forEach( f -> logger.debug(  f.toString()) );
           
            

        } catch (IllegalStateException ise) {
            httpStatus = HttpStatus.BAD_REQUEST; // http 400
            msg = String.format("Invalid departure date [%s]. Expected format YYYYMMDD",departureYYYYMMDD, ise.getMessage());
        
        } catch (CSVLoaderException csve) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; // http 500
            msg = String.format("Failed to read csv data for [%tY-%tm-%td]. %s",departureYYYYMMDD, csve.getMessage());
        
        } catch (Throwable t) {
            httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
            msg = String.format("A serious error occured, attempting to service request. Departure date [%s] Error: %s",departureYYYYMMDD, t.getMessage());

        } finally {
            logger.info(msg);
        
        }

        return new ResponseEntity<>(selectDepartures, httpStatus);

    }

    private LocalDate validateDepartureDate(String departureYYYYMMDD) {
        try {
            LocalDate date = LocalDate.parse(departureYYYYMMDD, DateTimeFormatter.ofPattern("yyyyMMdd"));
            return date;
        } catch(Exception e) {
            throw new IllegalStateException(String.format("Unable to parse date [%s] - %s", departureYYYYMMDD, e.getMessage()));
        }
    }

}
