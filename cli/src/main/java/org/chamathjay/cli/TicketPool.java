package org.chamathjay.cli;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TicketPool {
    private final List<String> tickets;
    private final int capacity;
    private int lastTicketId;

    public TicketPool(int capacity, int initialTickets) {
        this.capacity = capacity;
        this.tickets = Collections.synchronizedList(new LinkedList<>());
        this.lastTicketId = initialTickets;
    }

    public void addTicket(String ticketId, int vendorId) throws InterruptedException {
        synchronized (tickets) {
            while (tickets.size() >= capacity) {
                try {
                    System.out.println("Vendor: " + vendorId + " tried to add tickets but the ticket pool is full, waiting...");
                    tickets.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            tickets.add(ticketId);
            System.out.println("Vendor " + vendorId + " added ticket: " + ticketId + ", Tickets available: " + tickets.size());
            tickets.notifyAll();
        }
    }

    public void removeTicket(int customerId) throws InterruptedException {
        synchronized (tickets) {
            while (tickets.isEmpty()) {
                try {
                    System.out.println("Customer " + customerId + " tried to remove tickets but the ticket pool is empty, waiting...");
                    tickets.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            String ticketId = tickets.remove(0);
            System.out.println("Customer " + customerId + " bought ticket: " + ticketId + ", Tickets available: " + tickets.size());
            tickets.notifyAll();
        }
    }
}


