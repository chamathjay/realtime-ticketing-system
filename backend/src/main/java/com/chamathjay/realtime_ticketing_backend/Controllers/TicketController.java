package com.chamathjay.realtime_ticketing_backend.Controllers;

import java.util.*;
import com.chamathjay.realtime_ticketing_backend.Databases.TicketRepository;
import com.chamathjay.realtime_ticketing_backend.Models.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TicketController {
    @Autowired
    private TicketRepository ticketRepository;
    public TicketController(TicketRepository ticketRepository) {}

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

}
