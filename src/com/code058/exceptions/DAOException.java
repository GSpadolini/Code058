package com.code058.exceptions;

public class DAOException extends Exception {

    public DAOException(String msg) {
        super(msg);
    }
    public DAOException(Throwable cause) {
        super(cause);
    }
}
