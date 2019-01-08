package server.domain.datatypes;

public enum OrderStatus {
    PREORDERED, ONTHEWAY(PREORDERED), CLOSED(ONTHEWAY);

    private final OrderStatus[] previousStates;

    OrderStatus(OrderStatus... state){
        this.previousStates = state;
    }


    public OrderStatus transition(OrderStatus newState){
        for(OrderStatus previous : newState.previousStates){
            if(this == previous){
                return newState;
            }
        }
        throw  new IllegalArgumentException(String.format("Illegal state transition from %s to %s", this.toString(), newState.toString()));
    }

    }
