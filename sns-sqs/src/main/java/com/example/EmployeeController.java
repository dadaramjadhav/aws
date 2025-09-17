package com.example;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@AllArgsConstructor
public class EmployeeController {

    private final SnsPublisher snsPublisher;

    @PostMapping("/send")
    public String send(@RequestBody Employee emp) throws Exception {
        snsPublisher.publish(emp);
        return "Message published to SNS: " + emp;
    }
}