package com.tea.server.exception;

import lombok.Data;

@Data
public class RemoteException extends RuntimeException {

    public RemoteException(String message){
        super(message);
    }

}
