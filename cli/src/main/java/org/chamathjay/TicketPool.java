package org.chamathjay;

import java.util.LinkedList;
import java.util.Queue;

public class TicketPool {
    private final Queue<Integer> tickets = new LinkedList<>();
    private final int capacity;

    public TicketPool(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void addTicket(int ticketId, int vendorId) throws InterruptedException {
        while (tickets.size() >= capacity) {
            try {
                System.out.println("Ticket pool is full, waiting...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        tickets.add(ticketId);
        System.out.println("Vendor " + vendorId + " added ticket: " + ticketId);
    }

    public synchronized int buyTicket(int customerId) throws InterruptedException {
        try {
            while (tickets.isEmpty()) {
                wait();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        int ticketId = tickets.poll();
        System.out.println("Customer " + customerId + " bought ticket: " + ticketId);
        return ticketId;

    }

}


