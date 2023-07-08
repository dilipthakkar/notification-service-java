package com.notification.notify.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This class provides controller for handling endpoints for entry page.
 * 
 * @author Dilip Thakkar
 */
@Controller
public class HomeController {

    /**
     * Method to provide entry point of our application.
     * 
     * @return
     */
    @GetMapping("/")
    public String home() {
        return "index.html";
    }

}
