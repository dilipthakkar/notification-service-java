package com.notification.notify.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.notification.notify.models.notification.AddUserEvent;
import com.notification.notify.models.notification.SuccessfulConnectionEvent;

/**
 * This class provides methods related to users operations.
 * 
 * @author Dilip Thakkar
 */
@Service
public class UserService {

    /**
     * A mapping of user names to their corrosponding SSE (Server-Sent events)
     * emitter connection objects.
     */
    private Map<String, SseEmitter> sseConnections = new HashMap<>();

    @Autowired
    NotifyService notifyService;

    /**
     * Adds a user ID and its emitter connection object to the map of users
     * connection.
     * 
     * @param userId  The ID of connected user.
     * @param emitter The emitter connection object associated with the user.
     */
    public void addUserConnection(String userId, SseEmitter emitter) {
        sseConnections.put(userId, emitter);
    }

    /**
     * Finds the {@code SseEmitter} connection object associated with a given user
     * ID from the map of user connections.
     * Returns {@code null} if a connection not found for the given
     * user ID.
     * 
     * @param userId The ID of the user for which a connection is to be found.
     * @return The {@code SSeEmitter} object if a connection if found
     *         for the given user ID, otherwise return {@code null}.
     * 
     */
    public SseEmitter getConnection(String userId) {
        return sseConnections.get(userId);
    }

    /**
     * Removes the connection object associated with a given user ID from the map of
     * users connections.
     * 
     * @param userId The ID of the user for which the connection is to be removed.
     */
    public void removeConnection(String userId) {
        sseConnections.remove(userId);
    }

    /**
     * Notifies all connected users about a new user being added.
     * 
     * @param userId The ID of a new added user.
     */
    public void sendAddUserNotification(String userId) {

        // Ping all users to remove out all disconnected users.
        pingAllConnectedUsers();

        // Notify all connected user except the new one being added.
        for (String connectedUserId : sseConnections.keySet()) {
            if (!connectedUserId.equals(userId)) {
                SseEmitter emitter = sseConnections.get(connectedUserId);

                // Find all connected users list except the user to which data is to be sent.
                List<String> allUsers = getAllConnectedUsersExcept(connectedUserId, false);
                AddUserEvent addUserEvent = new AddUserEvent(userId, allUsers);
                notifyService.notifyAddUserEvent(emitter, addUserEvent);
            }
        }
    }

    /**
     * Sends Ping to all connected users to verify and remove all disconnected
     * users.
     * 
     * A {@code SseEmitter} will trigger its {@code onError} event function when a
     * event has not been completed successfuly while sending data to the connected
     * client.
     */
    public void pingAllConnectedUsers() {
        for (String connectedUserId : sseConnections.keySet()) {
            notifyService.pingUser(sseConnections.get(connectedUserId));
        }
    }

    /**
     * Retrives a list of user IDs for all connected users from the map of users
     * connections.
     * 
     * @param shouldPing Determines whether to ping all connected users before
     *                   retriving the list.
     *                   Set to true if pinging is required, Otherwise false.
     * @return The list of user IDs for all connected users.
     */
    public List<String> getAllConnectedUsers(Boolean shouldPing) {
        if (shouldPing) {
            pingAllConnectedUsers();
        }
        return new ArrayList<>(sseConnections.keySet());
    }

    /**
     * Retrives a list of user IDs for all connected users except the given user ID
     * from the map of users
     * connections.
     * 
     * @param userId     The user ID to be exclude from the list of all connected
     *                   user IDs.
     * @param shouldPing Determines whether to ping all connected users before
     *                   retriving the list.
     *                   Set to true if pinging is required, Otherwise false.
     * @return The list of user IDs for all connected users except the given user
     *         ID.
     */
    public List<String> getAllConnectedUsersExcept(String userId, Boolean shouldPing) {
        List<String> allUsers = getAllConnectedUsers(shouldPing);
        return allUsers.stream().filter(e -> !e.equals(userId)).toList();
    }

    /**
     * Notifies a user upon a successful connection.
     * 
     * Additionally, It sends a list of all other connected users.
     * 
     * @param emitter The {@code SseEmitter} object connected to the client.
     * @param userId  The user ID of connected user.
     */
    public void sendSuccessfulConnectionNotification(SseEmitter emitter, String userId) {
        List<String> allUsers = getAllConnectedUsersExcept(userId, true);
        SuccessfulConnectionEvent event = new SuccessfulConnectionEvent(allUsers);
        notifyService.sendSuccessfulConnectionNotification(emitter, event);
    }
}
