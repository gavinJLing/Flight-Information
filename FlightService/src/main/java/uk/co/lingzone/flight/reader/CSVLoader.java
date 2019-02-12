package uk.co.lingzone.flight.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import uk.co.lingzone.flight.exception.CSVLoaderException;
import uk.co.lingzone.flight.model.Departures;
import uk.co.lingzone.flight.model.Flight;

/**
 * Reads a CSV file called './data/Lights.csv' into a collection of flight
 * pojo's
 * 
 * @author gavinling
 *
 */
@Component
public class CSVLoader {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        CSVLoader csvLoader = new CSVLoader();
        csvLoader.exec();

    }

    private void exec() {
        Departures departures = new Departures();
        readCSVData(departures, "data/flights.csv");

    }

    public void readCSVData(Departures departures, String csvFileName) {
        logger.warn("Loading CSV data for service");
        List<Flight> naturalOrderFlights = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFileName));
                // build the CVS parser - tailoring the Default format it to match example
                // data...
                CSVParser csvParser = new CSVParser(reader,
                        CSVFormat.DEFAULT
                                .withHeader("Departure Time", "Destination", "Destination Airport IATA", "Flight No",
                                        "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
                                .withSkipHeaderRecord().withIgnoreHeaderCase().withTrim().withQuote(null));) {
            for (CSVRecord csvRecord : csvParser) {
                naturalOrderFlights.add( new Flight(LocalTime.parse(csvRecord.get("Departure Time")),
                                                                csvRecord.get("Destination"), 
                                                                csvRecord.get("Destination Airport IATA"),
                                                                csvRecord.get("Flight No"), 
                                                                operatingDays(csvRecord))
                );

            }
            
            // Sort the CSV flights into chronological order for the model.
            // So that the  micro service only has to filter, not filter and sort
            // for each request.
            departures.setFlights( naturalOrderFlights.stream().sorted(Comparator.comparing(Flight::getDepartureTime)).collect(Collectors.toList()) );
            
            // dump flights if logger is in debub mode.
            departures.getFlights().forEach( f -> logger.debug(  f.toString()) );
            
             
        } catch (IOException e) {
            throw new CSVLoaderException(String.format("Problem encountered loading CSV file [%s]", csvFileName));
            
        }
        logger.warn("Loaded CSV data for service");

    }

    /**
     * Establish the boolean array which is used to 
     * pick day-of-week match against requests.
     * 
     * @param csvRecord
     * @return
     */
    private boolean[] operatingDays(CSVRecord csvRecord) {
        boolean[] operates = new boolean[7];
        operates[0] = csvRecord.get("Sunday").equalsIgnoreCase("X");
        operates[1] = csvRecord.get("Monday").equalsIgnoreCase("X");
        operates[2] = csvRecord.get("Tuesday").equalsIgnoreCase("X");
        operates[3] = csvRecord.get("Wednesday").equalsIgnoreCase("X");
        operates[4] = csvRecord.get("Thursday").equalsIgnoreCase("X");
        operates[5] = csvRecord.get("Friday").equalsIgnoreCase("X");
        operates[6] = csvRecord.get("Saturday").equalsIgnoreCase("X");
        return operates;
    }
}
