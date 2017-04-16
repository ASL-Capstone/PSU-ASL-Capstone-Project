package com.example.backendtesting.backend.api;

/**
 * Exception thrown by operations which require exclusive access to an object when the object is in
 * use by something else.
 */
public class ObjectInUseException extends Exception {
    public ObjectInUseException() {
    }

    public ObjectInUseException(String message) {
        super(message);
    }

    public ObjectInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectInUseException(Throwable cause) {
        super(cause);
    }
}
