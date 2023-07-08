package com.notification.notify.models.notification;

import java.util.List;

import lombok.Data;

/**
 * Event class for SSE Event
 * <p>
 * This Event has been triggered when a new user is connected to our
 * application.
 * </p>
 * 
 * @author Dilip Thakkar
 */
@Data
public class AddUserEvent {

    public static final String EVENT_NAME = "ADD_USER";
    private String newAddedUserId;
    private List<String> allUsers;

    public AddUserEvent(String newAddedUserId, List<String> allUsers) {
        this.newAddedUserId = newAddedUserId;
        this.allUsers = allUsers;
    }
}
