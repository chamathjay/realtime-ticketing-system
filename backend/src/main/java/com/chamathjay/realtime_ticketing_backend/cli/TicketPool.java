package com.chamathjay.realtime_ticketing_backend.cli;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {
    private final List<Integer> tickets;
    private int maxTicketCapacity;
    private int totalTicketsRemaining;

    private final AtomicInteger nextTicketId = new AtomicInteger(1);
    public static final List<String> inMemoryLogs = Collections.synchronizedList(new LinkedList<>());

    private static final String LOG_FILE = "tickets_log.txt";

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public TicketPool() {
        this.tickets = new LinkedList<>();
    }

    public TicketPool(int totalTickets, int capacity) {
        this.tickets = Collections.synchronizedList(new LinkedList<>());
        this.totalTicketsRemaining = totalTickets;
        this.maxTicketCapacity = capacity;
    }

    public void addTicket(int vendorId) throws InterruptedException {
        lock.lock();
        try {
            while (tickets.size() >= maxTicketCapacity || totalTicketsRemaining <= 0) {
                if (totalTicketsRemaining <= 0) {
                    System.out.println("Vendor-" + (vendorId) + " tried to add tickets but no tickets available.");
                    TicketPool.writeLog("Vendor-" + (vendorId) + " tried to add tickets but no tickets available.");
                    return;
                }
                System.out.println("Vendor-" + vendorId + " tried to add tickets but the pool is full, waiting...");
                TicketPool.writeLog("Vendor-" + vendorId + " tried to add tickets but the pool is full, waiting...");
                notFull.await();
            }
            int ticketId = nextTicketId.getAndIncrement();
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
        try {
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

    public int getTotalTicketsRemaining() {
        lock.lock();
        try {
            return totalTicketsRemaining;
        } finally {
            lock.unlock();
        }
    }

    public static void writeLog(String msg) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logMsg = timestamp + " - " + msg;

        synchronized (inMemoryLogs) {
            inMemoryLogs.add(logMsg);
            if (inMemoryLogs.size() > 100){
                inMemoryLogs.remove(0);
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logMsg);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing log file: " + e.getMessage());
        }

    }

}
