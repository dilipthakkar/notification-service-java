package com.notification.notify.models.notification;

import java.util.List;

import lombok.Data;

/**
 * Event class for SSE Event
 * <p>
 * This Event has been triggered to notify a client about its successful
 * connection to our application.
 * </p>
 * 
 * @author Dilip Thakkar
 */
@Data
public class SuccessfulConnectionEvent {
    public static final String EVENT_NAME = "CONNECTED";

    List<String> allUsers;

    public SuccessfulConnectionEvent(List<String> allUsers) {
        this.allUsers = allUsers;
    }
}
