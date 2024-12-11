package com.chamathjay.realtime_ticketing_backend.Controllers;

import com.chamathjay.realtime_ticketing_backend.Models.TicketStatus;
import com.chamathjay.realtime_ticketing_backend.Services.TicketService;
import com.chamathjay.realtime_ticketing_backend.cli.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticketing")
@CrossOrigin
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/start")
    public String startSimulation(@RequestBody Config config) {
        ticketService.startSimulation(
                config.getTotalTickets(),
                config.getTicketReleaseRate(),
                config.getCustomerRetrievalRate(),
                config.getMaxTicketCapacity()
        );
        return "Simulation started.";
    }

    @PostMapping("/stop")
    public String stopSimulation() {
        ticketService.stopSimulation();
        return "Simulation stopped?";
    }

    @GetMapping("/status")
    public ResponseEntity<TicketStatus> getStatus() {
        TicketStatus status = ticketService.getTicketStatus();
        return ResponseEntity.ok(status);
    }

    @GetMapping("/logs")
    public List<String> getLogs() {
        return (ticketService.getLogs());
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> resetSimulation() {
        ticketService.reset();
        return ResponseEntity.ok().build();
    }
}
