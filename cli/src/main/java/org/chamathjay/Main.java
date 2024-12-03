package org.chamathjay;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Ticket Pool Capacity: ");
        int poolCapacity = sc.nextInt();
        System.out.print("Enter Vendors' Ticket Release Rate: ");
        int ticketReleaseRate = sc.nextInt();
        System.out.print("Enter Customer Ticket Retrieval Rate: ");
        int ticketRetrievalRate = sc.nextInt();

        TicketPool pool = new TicketPool(poolCapacity);

        Thread vendor1 = new Thread(new Vendor(pool, 1, ticketReleaseRate));
        Thread vendor2 = new Thread(new Vendor(pool, 2, ticketReleaseRate));

        Thread customer1 = new Thread(new Customer(pool, 1, ticketRetrievalRate));
        Thread customer2 = new Thread(new Customer(pool, 2, ticketRetrievalRate));
        Thread customer3 = new Thread(new Customer(pool, 3, ticketRetrievalRate));

        vendor1.start();
        vendor2.start();

        customer1.start();
        customer2.start();
        customer3.start();

        try {
            vendor1.join();
            vendor2.join();
            customer1.join();
            customer2.join();
            customer3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Main thread interrupted.");
        }

        System.out.println("Ticket processing complete.");
    }
}