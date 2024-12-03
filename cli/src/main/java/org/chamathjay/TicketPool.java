package org.chamathjay;

import java.util.*;

public class TicketPool {
    private final List<String> tickets = Collections.synchronizedList(new ArrayList<>());
    private final int capacity;

    public TicketPool(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void addTicket(String ticketId, int vendorId) throws InterruptedException {
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
        notifyAll();
    }

    public synchronized void removeTicket(int customerId) throws InterruptedException {
        try {
            while (tickets.isEmpty()) {
                wait();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String ticketId = tickets.removeFirst();
        System.out.println("Customer " + customerId + " bought ticket: " + ticketId);
        notifyAll();
    }

}


