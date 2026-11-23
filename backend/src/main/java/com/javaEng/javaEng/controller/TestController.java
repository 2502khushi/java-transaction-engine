
package com.javaEng.javaEng.controller;

import com.javaEng.javaEng.repository.DatabaseTest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final DatabaseTest databaseTest;

    public TestController(DatabaseTest databaseTest) {
        this.databaseTest = databaseTest;
    }

    @GetMapping("/test-db")
    public String testDatabase() {
        return "Connected to database: "
                + databaseTest.checkConnection();
    }
}
