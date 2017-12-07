package com.kaishengit.service.exception;

import javax.xml.ws.Service;

/**
 * @author zhao
 */
public class ServiceException extends RuntimeException {

    public ServiceException() {}

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable th) {
        super(th);
    }

    public ServiceException(String message, Throwable th) {
        super(message, th);
    }

}
