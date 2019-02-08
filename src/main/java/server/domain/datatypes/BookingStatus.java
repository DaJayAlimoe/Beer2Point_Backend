package server.domain.datatypes;

public enum BookingStatus {
    PREORDERED, ONTHEWAY(PREORDERED), CANCELED(PREORDERED), CLOSED(ONTHEWAY);

    private final BookingStatus[] previousStates;

    BookingStatus(BookingStatus... state){
        this.previousStates = state;
    }


    public BookingStatus transition(BookingStatus newState){
        for(BookingStatus previous : newState.previousStates){
            if(this == previous){
                return newState;
            }
        }
        throw  new IllegalArgumentException(String.format("Illegal state transition from %s to %s", this.toString(), newState.toString()));
    }

    }
