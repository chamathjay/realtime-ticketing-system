package com.chamathjay.realtime_ticketing_backend.Controllers;

import com.chamathjay.realtime_ticketing_backend.Services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ticketing")
public class TicketController {
    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/start")
    public String startSimulation() {
        ticketService.startSimulation();
        return "Simulation started.";
    }

    @PostMapping("/stop")
    public String stopSimulation() {
        ticketService.stopSimulation();
        return "Simulation stopped.";
    }

    @GetMapping("/logs")
    public List<String> getLogs() {
        List<String> logs = ticketService.getLogs();
        return (logs);
    }
}
