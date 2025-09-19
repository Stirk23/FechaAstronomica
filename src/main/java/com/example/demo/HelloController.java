package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/get")
    public String hello() {
        return "Hello, World!";
    }

    @PostMapping
    public String postHello() {
        return "Created item";
    }

    @PutMapping("/{id}")
    public String putHello(@PathVariable String id) {
        return "Updated item with ID: " + id;
    }

    @DeleteMapping("/{id}")
    public String deleteHello(@PathVariable String id) {
        return "Deleted item with ID: " + id;
    }

}
