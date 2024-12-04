package com.chamathjay.cli;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Config config;

        TicketPool.writeLog("Ticketing System Started");

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
            TicketPool.writeLog("Invalid config file, exiting program.");
            return;
        }

        TicketPool pool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets());

        int vendorCount = 4;
        int customerCount = 4;

//        lochana

        Vendor[] vendors = new Vendor[vendorCount];
        for (int i = 0; i < vendorCount; i++) {
            vendors[i] = new Vendor(pool, i, config.getTicketReleaseRate());
            Thread vendorThread = new Thread(vendors[i]);
            vendorThread.start();
        }

        Customer[] customers = new Customer[customerCount];
        for (int i = 0; i < customerCount; i++) {
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
//        System.out.println("Ticket processing complete.");
        sc.close();
    }
}