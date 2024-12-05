package com.chamathjay.cli;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TicketPool {
    private final List<Integer> tickets;
    private final int capacity;
    private int totalTicketsRemaining;

    private static final String LOG_FILE = "tickets_log.txt";

    public TicketPool(int capacity, int totalTickets) {
        this.capacity = capacity;
        this.tickets = Collections.synchronizedList(new LinkedList<>());
        this.totalTicketsRemaining = totalTickets;
    }

    public void addTicket(int ticketId, int vendorId) throws InterruptedException {
        synchronized (tickets) {
            while (tickets.size() >= capacity || totalTicketsRemaining <= 0) {
                if (totalTicketsRemaining <= 0) {
                    System.out.println("Vendor-" + (vendorId) + " tried to add tickets but no tickets available.");
                    writeLog("Vendor-" + (vendorId) + " tried to add tickets but no tickets available.");
                    return;
                }
                System.out.println("Vendor-" + vendorId + " tried to add tickets but the pool is full, waiting...");
                writeLog("Vendor-" + vendorId + " tried to add tickets but the pool is full, waiting...");
                tickets.wait();
            }
            tickets.add(ticketId);
            totalTicketsRemaining--;
            System.out.println("Vendor-" + (vendorId) + " added ticket: " + ticketId + ", Tickets available: " + tickets.size());
            writeLog("Vendor-" + (vendorId) + " added ticket: " + ticketId + ", Tickets available: " + tickets.size());
            tickets.notifyAll();
        }
    }


    public void removeTicket(int customerId) throws InterruptedException {
        synchronized (tickets) {
            while (tickets.isEmpty()) {
                System.out.println("Customer-" + customerId + " tried to buy a ticket but the pool is empty, waiting...");
                writeLog("Customer-" + customerId + " tried to buy a ticket but the pool is empty, waiting...");
                tickets.wait();
            }
            int ticketId = tickets.remove(0);
            System.out.println("Customer " + customerId + " bought ticket: " + ticketId + ", Tickets available: " + tickets.size());
            writeLog("Customer " + customerId + " bought ticket: " + ticketId + ", Tickets available: " + tickets.size());
            tickets.notifyAll();
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


