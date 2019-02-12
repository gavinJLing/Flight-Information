package uk.co.lingzone.flight.util;

public enum FlightScheduleDayOfWeek {
    
    Sunday(7, 0),
    Monday( 1, 1),
    Tuesday(2, 2),
    Wednesday(3,3),
    Thursday(4,4),
    Friday(5,5),
    Saturday(6,6);
    
    
    private  int iso8691Index;
    private  int internalIndex;
   
    
    FlightScheduleDayOfWeek( int iso8691Index, int internalIndex ){
        this.iso8691Index = iso8691Index;
        this.internalIndex= internalIndex;
    }
    
   
}
