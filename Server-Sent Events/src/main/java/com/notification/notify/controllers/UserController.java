package com.notification.notify.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.notification.notify.service.NotifyService;
import com.notification.notify.service.UserService;

/**
 * This class provides controller for handling client request for users related
 * features.
 * 
 * @author Dilip Thakkar
 */
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    NotifyService notifyService;

    /**
     * Method to provide endpoint for SSE connection.
     * 
     * @param userName
     * @return
     */
    @GetMapping("/connect")
    public SseEmitter connectUser(@RequestParam("userName") String userName) {
        String userId = userName;
        SseEmitter emitter = new SseEmitter(1000000000000l);

        userService.addUserConnection(userId, emitter);

        // emitter callbacks
        emitter.onCompletion(() -> {
            System.out.println("socket connection complete");
            userService.removeConnection(userId);
        });

        emitter.onError((e) -> {
            System.out.println("socket connection error");
            userService.removeConnection(userId);
        });

        emitter.onTimeout(() -> {
            System.out.println("socket connection timeout");
            userService.removeConnection(userId);
        });

        userService.sendAddUserNotification(userId);
        userService.sendSuccessfulConnectionNotification(emitter, userId);

        System.out.println("User Connected : " + userName);

        return emitter;
    }

}
