package in.ser.the_ultimate_scrum_simulator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    @GetMapping(path="/getName")
    public String getName(){
        return "Vatsal";
    }
}
