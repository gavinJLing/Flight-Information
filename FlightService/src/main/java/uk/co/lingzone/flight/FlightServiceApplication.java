package uk.co.lingzone.flight;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

import uk.co.lingzone.flight.model.Departures;
import uk.co.lingzone.flight.reader.CSVLoader;

@SpringBootApplication
public class FlightServiceApplication {

    @Autowired 
    CSVLoader csvLoader;
    
    @Autowired
    Departures departures;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
	    SpringApplication.run(FlightServiceApplication.class, args);
	
	}
	
	
	
	/**
	 * This event fires before the ApplicationReadyEvent
	 * 
	 */
	@EventListener(ApplicationStartedEvent.class)
    public void doApplicationStartedEvent() {
	    // ensure the model has been populated....
        // Its not clear for the description how the CSV file is made avail. 
	    // based on the example provided. My assumption is that only one week of flight schedule if provided at a time.
	    
        // Its also unclear how we trigger a differnet csv to be rendered...?
	   
        csvLoader.readCSVData(departures, "./data/flights.csv"); 
    }
	
	
	@EventListener(ApplicationReadyEvent.class)
    public void doApplicationReadyEvent() {
        logger.info("Flight departure information system is open for business.");
    }
}

