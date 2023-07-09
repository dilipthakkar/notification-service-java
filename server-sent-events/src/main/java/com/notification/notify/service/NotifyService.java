package com.notification.notify.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import com.notification.notify.models.notification.AddUserEvent;
import com.notification.notify.models.notification.PingUserEvent;
import com.notification.notify.models.notification.SuccessfulConnectionEvent;

/**
 * <h3>This class provides methods to send notification to users.</h3>
 * 
 * This is used to emit server side events to the connected clients.
 * 
 * @author Dilip Thakkar
 */
@Service
public class NotifyService {

    /**
     * Emits an event to a connected client.
     * 
     * The <code>emitter</code> paramter is an instance of <code>SseEmitter</code>,
     * Which represents a connection to a specific client that will receive the
     * event.
     * 
     * @param <T>       Type of event.
     * @param emitter   The <code>SseEmitter</code> connected to the client.
     * @param eventName The event to be sent to the client.
     * @param data      The data to be sent to the client.
     */
    public <T> void sendNotification(SseEmitter emitter, String eventName, T data) {
        SseEventBuilder eventBuilder = SseEmitter.event().name(eventName).data(data);
        try {
            emitter.send(eventBuilder);
        } catch (IOException e) {
            System.out.println("Event Exception " + eventName);
            // e.printStackTrace();
        }
    }

    /**
     * Notifies all connected users about a new user being added.
     * 
     * @param emitter      The <code>SseEmitter</code> connected to the client.
     * @param addUserEvent The event representing the new user being added.
     */
    public void notifyAddUserEvent(SseEmitter emitter, AddUserEvent addUserEvent) {
        sendNotification(emitter, AddUserEvent.EVENT_NAME, addUserEvent);
    }

    /**
     * Notifies a user about its successful connection to our application.
     * 
     * This method sends a confirmation event to the user upon successful connection
     * to our application. Additionally, it sends a list of all other connected
     * users.
     *
     * @param emitter The <code>SseEmitter</code> connected to the client.
     * @param event   The event representing the successful connection.
     */
    public void sendSuccessfulConnectionNotification(SseEmitter emitter, SuccessfulConnectionEvent event) {
        new Thread(() -> {
            sendNotification(emitter, SuccessfulConnectionEvent.EVENT_NAME, event);
        }).start();
    }

    /**
     * Sends a ping to a connected user to verify its connectivity.
     * 
     * If the user is disconnected, the <code>emitter</code> triggers
     * its <code>onError</code> event, which removes the user from the list of all
     * connected users.
     * 
     * @param emitter The <code>SseEmitter</code> connected to the client.
     */
    public void pingUser(SseEmitter emitter) {
        sendNotification(emitter, PingUserEvent.EVENT_NAME, "");
    }

}
