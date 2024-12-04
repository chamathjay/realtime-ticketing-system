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
    private final List<String> tickets;
    private final int capacity;
//    private int lastTicketId;

    private static final String LOG_FILE = "tickets_log.txt";

    public TicketPool(int capacity, int initialTickets) {
        this.capacity = capacity;
        this.tickets = Collections.synchronizedList(new LinkedList<>());
        for (int i = 1; i <= initialTickets; i++) {
            tickets.add("" + i);
        }
    }

    public void addTicket(String ticketId, int vendorId) throws InterruptedException {
        synchronized (tickets) {
            while (tickets.size() >= capacity) {
                try {
                    System.out.println("Vendor-" + (vendorId + 1) + " tried to add tickets but the ticket pool is full, waiting...");
                    writeLog("Vendor-" + (vendorId + 1) + " tried to add tickets but the ticket pool is full, waiting...");
                    tickets.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            tickets.add(ticketId);
            System.out.println("Vendor-" + (vendorId+1) + " added ticket: " + ticketId + ", Tickets available: " + tickets.size());
            writeLog("Vendor-" + (vendorId+1) + " added ticket: " + ticketId + ", Tickets available: " + tickets.size());
            tickets.notifyAll();
        }
    }

    public void removeTicket(int customerId) throws InterruptedException {
        synchronized (tickets) {
            while (tickets.isEmpty()) {
                try {
                    System.out.println("Customer-" + customerId + " tried to remove tickets but the ticket pool is empty, waiting...");
                    writeLog("Customer " + customerId + " tried to remove tickets but the ticket pool is empty, waiting...");
                    tickets.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            String ticketId = tickets.remove(0);
            System.out.println("Customer " + customerId + " bought ticket: " + ticketId + ", Tickets available: " + tickets.size());
            writeLog("Customer " + customerId + " bought ticket: " + ticketId + ", Tickets available: " + tickets.size());
            tickets.notifyAll();
        }
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


