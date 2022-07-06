package com.skytouch.task.services.exceptions;

/**
 * Exception used to identify cases where an expected reply message was not received.
 *
 * @author Waldo Terry
 */
public class NoResultsReceivedException extends Exception{
    public NoResultsReceivedException(String message) {
        super(message);
    }
}
