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

        int vendorCount = 2;
        int customerCount = 3;

        Thread[] vendorThreads = new Thread[vendorCount];
        for(int i = 0; i < vendorCount; i++) {
            vendorThreads[i] = new Thread(new Vendor(pool, i + 1));
            vendorThreads[i].start();
        }
        Thread[] customerThreads = new Thread[customerCount];
        for(int i = 0; i < customerCount; i++) {
            customerThreads[i] = new Thread(new Customer(pool, i + 1));
            customerThreads[i].start();
        }


        try {
            for (Thread vendor : vendorThreads) {
                vendor.join();
            }

            for (Thread customer : customerThreads) {
                customer.join();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Main thread interrupted.");
        }

        System.out.println("Ticket processing complete.");
    }
}