package com.api.adoptify.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/session")
public class SessionController {

    // Retrieve session attribute
    @GetMapping("/get")
    public ResponseEntity<?> getSessionData(HttpSession session) {
        String appUserName = (String) session.getAttribute("appUserName");
        if (appUserName != null) {
            return ResponseEntity.ok(appUserName);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No session data found");
        }
    }
}
