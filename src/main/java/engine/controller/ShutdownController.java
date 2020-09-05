package engine.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/actuator/shutdown")
public class ShutdownController {

    @PostMapping
    public void shutdown() {
        // Do nothing, it is only needed for the tests
    }
}
