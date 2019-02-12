package uk.co.lingzone.flight.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A collection of Departures flight details
 * Recording a date 
 * 
 * @author gavinling
 *
 */
@Component
public class Departures {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // Is0-8601 day-of-week translation 
    // iso-8601 Monday day-of-week =1 ...  Saturday = 6 but Sunday =7
    // The internal flight schedule day-of-week structure starting at 0, also the flights start with Sunday, not Monday.
    // so for any given Index (iso-8601 Day-of-week (1..7) give the flight schedule index (0..6).
    @JsonIgnore
    private int[] internalDayOfWeekTranslation = {1,2,3,4,5,6,0};
    
//    @JsonProperty
//    public LocalDate date; // typically a Sunday, where a weekly flight schedule is Sun -Sat

    @JsonProperty
    public List<Flight> flights = new ArrayList<>();

    /**
     * default constructor: The current departure model
     */
    public Departures() {
//        date = LocalDate.now(); // Date when Model was loaded.
    }

    /**
     * Alternative constructor used to pass filtered departure model back to client.
     * 
     * @param homebaseIata
     * @param weeklyStartDate
     */
//    public Departures(LocalDate filterRequestDate) {
//        this.date = filterRequestDate; 
//    }

//    public LocalDate getDate() {
//        return date;
//    }
//
//    public void setDate(LocalDate date) {
//        this.date = date;
//    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    @JsonIgnore
    public void addFlight(Flight flight) {
        flights.add(flight);
        logger.info(String.format("Flight %s departing %tY-%tm-%td to %s", flight.getFlightNumber(),
                flight.getDepartureTime(), flight.getDestination()));
    }

    @JsonIgnore
    public boolean isEmpty() {
        return flights.isEmpty();
    }



    
    /**
     * Filter the departures model, for the selected day-of-the-week.
     * 
     * Some friction exists between the different representations of 'days of a week'.
     * Related to the day order difference between 
     * 1. CSV data structure(unknown: Sunday, Monday ... Saturday) 
     * 2. Java localDate (iso-8601:   Monday, Tuesday ... Sunday)
     * 
     * Where an lookup is used to map the different index values
     * @param selectedDate
     * @return Depatures object
     */
    public Departures forDay(LocalDate selectedDate) {
        Departures selectedDepartures = new Departures();
        selectedDepartures.setFlights( flights.stream()
                .filter( flight -> flight.getOperates()[   internalDayOfWeekTranslation[selectedDate.getDayOfWeek().getValue()]   ])
                .collect(Collectors.toList()) );
        return selectedDepartures;
    }

}
