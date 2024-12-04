package com.chamathjay.cli;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Config config;

        System.out.print("Do you want to load parameters from the config file? (y/N): ");
        String choice = sc.next();

        if (choice.equalsIgnoreCase("y")) {
            config = ConfigLoader.loadConfig();
            if (config == null || !config.isValid()) {
                System.err.println("Invalid config file, switching to manual input...");
                config = ConfigLoader.getConfigFromUser(sc);
                ConfigLoader.saveConfig(config);
            } else {
                System.out.println("Successfully loaded config file!");
            }
        } else {
            config = ConfigLoader.getConfigFromUser(sc);
            ConfigLoader.saveConfig(config);
        }

        if (!config.isValid()) {
            System.err.println("Invalid config file, exiting program...");
            return;
        }

        TicketPool pool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets());

        int vendorCount = 4;
        int customerCount = 4;

//        Thread[] vendorThreads = new Thread[vendorCount];
//        for(int i = 0; i < vendorCount; i++) {
//            vendorThreads[i] = new Thread(new Vendor(pool, i + 1, config.getTicketReleaseRate()));
//            vendorThreads[i].start();
//        }
//        Thread[] customerThreads = new Thread[customerCount];
//        for(int i = 0; i < customerCount; i++) {
//            customerThreads[i] = new Thread(new Customer(pool, i + 1, config.getCustomerRetrievalRate()));
//            customerThreads[i].start();
//        }


//        lochana

        Vendor[] vendors = new Vendor[vendorCount];  //Array of vendors
        for (int i = 0; i < vendors.length; i++) {
            vendors[i] = new Vendor(pool, i, config.getTicketReleaseRate());
            Thread vendorThread = new Thread(vendors[i]);
            vendorThread.start();
        }

        Customer[] customers = new Customer[customerCount];//Array of customers
        for (int i = 0; i < 5; i++) {
            customers[i] = new Customer(pool, i, config.getCustomerRetrievalRate());
            Thread customerThread = new Thread(customers[i]);
            customerThread.start();
        }


//        try {
//            for (Thread vendor : vendorThreads) {
//                vendor.join();
//            }
//
//            for (Thread customer : customerThreads) {
//                customer.join();
//            }
//
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            System.out.println("Main thread interrupted.");
//        }

        System.out.println("Ticket processing complete.");
        sc.close();
    }
}