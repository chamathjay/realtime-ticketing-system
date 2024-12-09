package com.chamathjay.realtime_ticketing_backend.Services;

import com.chamathjay.realtime_ticketing_backend.Models.Ticket;
import com.chamathjay.realtime_ticketing_backend.Repository.TicketRepository;
import com.chamathjay.realtime_ticketing_backend.cli.Config;
import com.chamathjay.realtime_ticketing_backend.cli.Customer;
import com.chamathjay.realtime_ticketing_backend.cli.TicketPool;
import com.chamathjay.realtime_ticketing_backend.cli.Vendor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketPool pool;
    private final Config config = new Config();

    private final List<String> logs = new ArrayList<>();
    private boolean simulationRunning = false;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
        this.pool = new TicketPool();
    }

    public void startSimulation() {
        if (!simulationRunning) {
            simulationRunning = true;
            logs.add("Simulation started at " + LocalDateTime.now());
        }

        int vendorCount = 4;
        int customerCount = 4;

        Thread[] vendorThreads = new Thread[vendorCount];
        Vendor[] vendors = new Vendor[vendorCount];

        for (int i = 0; i < vendorCount; i++) {
            vendors[i] = new Vendor(pool, i + 1, config.getTicketReleaseRate());
            vendorThreads[i] = new Thread(vendors[i]);
            vendorThreads[i].start();
            logs.add("Vendor thread " + (i + 1) + " started.");
        }

        Thread[] customerThreads = new Thread[customerCount];
        Customer[] customers = new Customer[customerCount];

        for (int i = 0; i < customerCount; i++) {
            customers[i] = new Customer(pool, i + 1, config.getCustomerRetrievalRate());
            customerThreads[i] = new Thread(customers[i]);
            customerThreads[i].start();
            logs.add("Customer thread " + (i + 1) + " started.");
        }

    }
    public void stopSimulation() {
        if (simulationRunning) {
            simulationRunning = false;
            logs.add("Simulation stopped at " + LocalDateTime.now());
        }
    }


//    public String addTicket(int count, String vendor){
//        for (int i = 0; i < count; i++) {
//            Ticket ticket = new Ticket(vendor);
//            ticketRepository.save(ticket);
//        }
//        return count + " tickets added by " + vendor + ".";
//    }
//
//    public String removeTicket(int count) {
//        List<Ticket> availableTickets = ticketRepository.findAll();
//
//        if (availableTickets.size() < count) {
//            return "Not enough tickets available. Available: " + availableTickets.size();
//        }
//
//        for (int i = 0; i < count; i++) {
//            Ticket ticket = availableTickets.get(i);
//            ticket.setAvailable(false);
//            ticketRepository.save(ticket);
//        }
//
//        return count + " tickets removed.";
//    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Scheduled(fixedRate = 2000)
    public void generateLogs() {
        if (simulationRunning) {
            logs.add("Generated log at " + LocalDateTime.now());
        }
    }
    public List<String> getLogs() {
        return new ArrayList<>(logs);
    }
}
