package com.chamathjay.realtime_ticketing_backend.Controllers;

import com.chamathjay.realtime_ticketing_backend.Services.TicketService;
import com.chamathjay.realtime_ticketing_backend.cli.Config;
import com.chamathjay.realtime_ticketing_backend.cli.TicketPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ticketing")
@CrossOrigin
public class TicketController {
    private final TicketService ticketService;

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
        TicketPool.writeLog("Simulation started.");
        return "Simulation started.";
    }

    @PostMapping("/stop")
    public String stopSimulation() {
        ticketService.stopSimulation();
        TicketPool.writeLog("Simulation stopped.");
        return "Simulation stopped.";
    }

    @GetMapping("/status")
    public String getTicketStatus() {
        return "Tickets available: " + ticketService.getAllTickets();
    }

    @GetMapping("/logs")
    public List<String> getLogs(@RequestParam(required = false, defaultValue = "100") int limit) {
        List<String> logs = ticketService.getLogs();
        return (logs);
    }

    @PostMapping("/reset")
    public String resetSimulation() {
        ticketService.reset();
        return "Simulation reset successfully.";
    }
}
