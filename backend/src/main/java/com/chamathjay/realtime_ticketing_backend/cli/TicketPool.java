package com.chamathjay.realtime_ticketing_backend.cli;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {
    private List<Integer> tickets;
    private int capacity;
    private int totalTicketsRemaining;
    private int nextTicketId = 1;

    private static final String LOG_FILE = "tickets_log.txt";

    private ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public TicketPool(List<Integer> tickets, int capacity) {
        this.tickets = tickets;
        this.capacity = capacity;
    }

    public TicketPool(int capacity, int totalTickets) {
        this.capacity = capacity;
        this.tickets = Collections.synchronizedList(new LinkedList<>());
        this.totalTicketsRemaining = totalTickets;
    }

    public TicketPool() {

    }

    public void addTicket(int vendorId) throws InterruptedException {
        lock.lock();
        try {
            while (tickets.size() >= capacity || totalTicketsRemaining <= 0) {
                if (totalTicketsRemaining <= 0) {
                    System.out.println("Vendor-" + (vendorId) + " tried to add tickets but no tickets available.");
                    writeLog("Vendor-" + (vendorId) + " tried to add tickets but no tickets available.");
                    return;
                }
                System.out.println("Vendor-" + vendorId + " tried to add tickets but the pool is full, waiting...");
                writeLog("Vendor-" + vendorId + " tried to add tickets but the pool is full, waiting...");
                notFull.await();
            }
            int ticketId = nextTicketId++;
            tickets.add(ticketId);
            totalTicketsRemaining--;
            System.out.println("Vendor-" + (vendorId) + " added ticket: " + ticketId + ", Tickets available: " + tickets.size());
            writeLog("Vendor-" + (vendorId) + " added ticket: " + ticketId + ", Tickets available: " + tickets.size());
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }


    public void removeTicket(int customerId) throws InterruptedException {
        lock.lock();
        try{
            while (tickets.isEmpty()) {
                System.out.println("Customer-" + customerId + " tried to buy a ticket but the pool is empty, waiting...");
                writeLog("Customer-" + customerId + " tried to buy a ticket but the pool is empty, waiting...");
                notEmpty.await();
            }
            int ticketId = tickets.remove(0);
            System.out.println("Customer " + customerId + " bought ticket: " + ticketId + ", Tickets available: " + tickets.size());
            writeLog("Customer " + customerId + " bought ticket: " + ticketId + ", Tickets available: " + tickets.size());
            notFull.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public synchronized int getTotalTicketsRemaining() {
        return totalTicketsRemaining;
    }

    public static void writeLog(String msg) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logMsg = timestamp + " - " + msg;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logMsg);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing log file: " + e.getMessage());
        }

    }
}
