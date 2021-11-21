package com.urise.webapp.exception;

import java.io.IOException;

public class StorageException extends RuntimeException {
    private final String uuid;

    public StorageException(String message, String uuid) {
        super(message);
        this.uuid = uuid;
    }

    public StorageException(String message, Exception cause) {
        this(message,null,cause);
    }

    public StorageException(String message, String uuid, Exception e) {
        super(message,e);
        this.uuid = uuid;
    }

    public StorageException(String message) {
        this(message,null,null);
    }


    public String getUuid(){
        return uuid;
    }
}
