package com.chamathjay.realtime_ticketing_backend.Services;

import com.chamathjay.realtime_ticketing_backend.Models.TicketStatus;
import com.chamathjay.realtime_ticketing_backend.cli.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TicketService {

    private TicketPool pool;
    private final AtomicBoolean simulationRunning = new AtomicBoolean(false);
    private final ReentrantLock lock = new ReentrantLock();

    private Thread[] vendorThreads;
    private Vendor[] vendors;

    private Thread[] customerThreads;
    private Customer[] customers;

    public TicketService() {
        this.pool = new TicketPool();
    }


    public void startSimulation(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        lock.lock();
        try {
            if (simulationRunning.get()) {
                simulationRunning.set(false);
            }
            pool = new TicketPool(totalTickets, maxTicketCapacity);
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
        } finally {
            lock.unlock();
        }
    }

    public void stopSimulation() {
        lock.lock();
        try {
            if (!simulationRunning.get()) {
                return;
            }

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
            simulationRunning.set(false);
            TicketPool.writeLog("Simulation stopped.");
        } finally {
            lock.unlock();
        }
    }


    public void reset() {
        lock.lock();
        try {
            if (simulationRunning.get()) {
                stopSimulation();
            }
            pool = null;
            TicketPool.writeLog("Simulation reset.");
        } finally {
            lock.unlock();
        }
    }

    public TicketStatus getTicketStatus() {
        lock.lock();
        try {
            if (pool == null) {
                throw new IllegalStateException("Pool is not initialized.");
            }
            int ticketsRemaining = pool.getTicketsRemaining();
            int ticketsSold = pool.getTicketsSold();
            int ticketsInPool = pool.getTicketsInPool();

            return new TicketStatus(ticketsRemaining, ticketsSold, ticketsInPool);
        } finally {
            lock.unlock();
        }
    }

    public List<String> getLogs() {
        lock.lock();
        try {
            return TicketPool.inMemoryLogs.stream().toList();
        } finally {
            lock.unlock();
        }
    }
}
