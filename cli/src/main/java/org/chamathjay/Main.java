package org.chamathjay;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Config config;

        System.out.print("Do you want to load parameters from the config file? (y/N): ");
        String choice = sc.next();

        if(choice.equalsIgnoreCase("y")) {
            config = ConfigLoader.loadConfig();
            if (config == null || !config.isValid()) {
                System.err.println("Invalid config file, switching to manual input...");
                config = ConfigLoader.getConfigFromUser(sc);
                ConfigLoader.saveConfig(config);
                System.out.println("Successfully saved to config file!");
            } else {
                System.out.println("Successfully loaded config file!");
            }
        } else {
            config = ConfigLoader.getConfigFromUser(sc);
            ConfigLoader.saveConfig(config);
            System.out.println("Successfully saved to config file!");
        }

        if (!config.isValid()) {
            System.err.println("Invalid config file, exiting program...");
            return;
        }

        TicketPool pool = new TicketPool(config.getMaxTicketCapacity());

        int vendorCount = 2;
        int customerCount = 3;

        Thread[] vendorThreads = new Thread[vendorCount];
        for(int i = 0; i < vendorCount; i++) {
            vendorThreads[i] = new Thread(new Vendor(pool, i + 1, config.getTicketReleaseRate()));
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