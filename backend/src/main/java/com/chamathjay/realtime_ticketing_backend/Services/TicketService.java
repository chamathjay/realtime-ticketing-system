package com.chamathjay.realtime_ticketing_backend.Services;

import com.chamathjay.realtime_ticketing_backend.Models.Ticket;
import com.chamathjay.realtime_ticketing_backend.Models.TicketStatus;
import com.chamathjay.realtime_ticketing_backend.Repository.TicketRepository;
import com.chamathjay.realtime_ticketing_backend.cli.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private TicketPool pool;
    private final AtomicBoolean simulationRunning = new AtomicBoolean(false);

    private Thread[] vendorThreads;
    private Vendor[] vendors;

    private Thread[] customerThreads;
    private Customer[] customers;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
        this.pool = new TicketPool();
    }


    public synchronized void startSimulation(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        if (simulationRunning.get()) {
            simulationRunning.set(false);
        }
        pool = new TicketPool(totalTickets, maxTicketCapacity);
        TicketPool.writeLog("Simulation started with total tickets " + totalTickets + " and max capacity " + maxTicketCapacity);
        Config config = new Config(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);

        ConfigLoader.saveConfig(config);
        TicketPool.writeLog("TicketPool initialized with total: " + totalTickets + " tickets, and max capacity: " + maxTicketCapacity);

        int vendorCount = 2;
        int customerCount = 2;

        vendorThreads = new Thread[vendorCount];
        vendors = new Vendor[vendorCount];

        for (int i = 0; i < vendorCount; i++) {
            vendors[i] = new Vendor(pool, i + 1, config);
            vendorThreads[i] = new Thread(vendors[i]);
            vendorThreads[i].start();
            TicketPool.writeLog("Vendor thread " + (i + 1) + " started.");
        }

        customerThreads = new Thread[customerCount];
        customers = new Customer[customerCount];
        for (int i = 0; i < customerCount; i++) {
            customers[i] = new Customer(pool, i + 1, config);
            customerThreads[i] = new Thread(customers[i]);
            customerThreads[i].start();
            TicketPool.writeLog("Customer thread " + (i + 1) + " started.");
        }

        simulationRunning.set(true);
    }

    public synchronized void stopSimulation() {
        if (!simulationRunning.get()) {
            return;
        }
        simulationRunning.set(false);

        for (Vendor vendor : vendors) {
            vendor.stop();
        }
        for (Customer customer : customers) {
            customer.stop();
        }

        try {
            for (Thread thread : vendorThreads) {
                if (thread != null) {
                    thread.join();
                }
            }
            for (Thread thread : customerThreads) {
                if (thread != null) {
                    thread.join();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            TicketPool.writeLog("Thread interrupted during shutdown.");
        }
        TicketPool.writeLog("Simulation stopped.");
    }


    public synchronized void reset() {
        if (simulationRunning.get()) {
            stopSimulation();
        }
        pool = null;
        TicketPool.writeLog("Simulation reset.");
    }

    public TicketStatus getTicketStatus() {
        if (pool == null) {
            throw new IllegalStateException("Pool is not initialized.");
        }
        int ticketsRemaining = pool.getTicketsRemaining();
        int ticketsSold = pool.getTicketsSold();
        int ticketsInPool = pool.getTicketsInPool();

        return new TicketStatus(ticketsRemaining, ticketsSold, ticketsInPool);
    }

    public synchronized List<String> getLogs() {
        return TicketPool.inMemoryLogs.stream().toList();
    }
}
