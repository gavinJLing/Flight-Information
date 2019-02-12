package uk.co.lingzone.flight.model;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Flight implements Comparable<Flight> {
    @JsonProperty
	private LocalTime departureTime;
    
    @JsonProperty
	private String    destination;
    
    @JsonProperty
	private String    destIATA;
    
    @JsonProperty
	private String    flightNumber;
    
    @JsonProperty
	private boolean[] operates;
	
	
	public Flight( LocalTime time, String dest, String iata, String number, boolean[] operates) {
	    this.departureTime = time;
	    this.destination   = dest;
	    this.destIATA      = iata;
	    this.flightNumber  = number;
	    this.operates    = operates;
	}

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestIATA() {
        return destIATA;
    }

    public void setDestIATA(String destIATA) {
        this.destIATA = destIATA;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public boolean[] getOperates() {
        return operates;
    }

    public void setOperates(boolean[] operates) {
        this.operates = operates;
    }

    
    @Override
    public int compareTo(Flight ob) {
        return flightNumber.compareTo(ob.getFlightNumber());
    }
    
    
    @Override
    public boolean equals(final Object other) {
      if (other == null) {
         return false;
      }
      final Flight otherFlight = (Flight) other;
      if (this == otherFlight) {
         return true;
      } else {
         return (this.flightNumber.equals(otherFlight.flightNumber) 
                 && (this.departureTime == otherFlight.departureTime));
      }
    }
    @Override
    public int hashCode() {
      int hashno = 7;
      hashno = 13 * hashno + (flightNumber == null ? 0 : flightNumber.hashCode());
      return hashno;
    }
	
    
    @Override
    public String toString() {
        return String.format("Flight %s departing %s to %s", flightNumber, departureTime, destination );
        
       
    }
}
