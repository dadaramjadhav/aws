package com.example;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "Admin content";
    }

    @GetMapping("/user/profile")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String user() {
        return "User content";
    }
}