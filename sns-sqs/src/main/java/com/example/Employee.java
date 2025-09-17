package com.example;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Employee {
    private int id;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joiningDate;
}
