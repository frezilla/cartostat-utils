package com.cartostatutils.replacelinkutils.processor;

public final class ProcessorException extends Exception {
    
    public ProcessorException(String string) {
        super(string);
    }
    
    public ProcessorException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }
    
    public ProcessorException(Throwable thrwbl) {
        super(thrwbl);
    }
    
    
    
}
