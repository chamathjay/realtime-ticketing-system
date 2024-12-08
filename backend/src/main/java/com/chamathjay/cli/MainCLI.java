package com.chamathjay.cli;

import com.chamathjay.realtime_ticketing_backend.RealtimeTicketingBackendApplication;
import org.springframework.boot.SpringApplication;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainCLI {

    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {
            Config config;
            System.out.println("Starting Ticketing System...");
            TicketPool.writeLog("Ticketing System Started");
            SpringApplication app = new SpringApplication(RealtimeTicketingBackendApplication.class);

//        app.setBannerMode(Banner.Mode.OFF);

            System.out.print("Do you want to load parameters from the config file? (y/N): ");
            String choice = sc.next();

            if (choice.equalsIgnoreCase("y")) {
                config = ConfigLoader.loadConfig();
                if (config == null) {
                    System.err.println("Invalid config file, switching to manual input...");
                    TicketPool.writeLog("Invalid config file, switched to manual input.");
                    config = ConfigLoader.getConfigFromUser(sc);
                    ConfigLoader.saveConfig(config);
                } else {
                    System.out.println("Successfully loaded config file!");
                    TicketPool.writeLog("Config file successfully loaded.");
                }
            } else {
                config = ConfigLoader.getConfigFromUser(sc);
                ConfigLoader.saveConfig(config);
            }

//            ConfigLoader.getConfigFromUser(sc);

            System.out.print("Press Enter to start the simulation...");
            sc.nextLine();
            sc.nextLine();

            TicketPool pool = new TicketPool(config.getMaxTicketCapacity(), config.getTotalTickets());
            TicketPool.writeLog("TicketPool initialized with max capacity: " + config.getMaxTicketCapacity());

            int vendorCount = 4;
            int customerCount = 4;

            Thread[] vendorThreads = new Thread[vendorCount];
            Vendor[] vendors = new Vendor[vendorCount];

            for (int i = 0; i < vendorCount; i++) {
                vendors[i] = new Vendor(pool, i + 1, config.getTicketReleaseRate());
                vendorThreads[i] = new Thread(vendors[i]);
                vendorThreads[i].start();
                TicketPool.writeLog("Vendor thread " + (i + 1) + " started.");
            }

            Thread[] customerThreads = new Thread[customerCount];
            Customer[] customers = new Customer[customerCount];

            for (int i = 0; i < customerCount; i++) {
                customers[i] = new Customer(pool, i + 1, config.getCustomerRetrievalRate());
                customerThreads[i] = new Thread(customers[i]);
                customerThreads[i].start();
                TicketPool.writeLog("Customer thread " + (i + 1) + " started.");
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
                TicketPool.writeLog("Main thread interrupted during shutdown.");
                System.out.println("Main thread interrupted.");
            }

            System.out.print("Press Enter to stop the simulation...");
            sc.nextLine();
            sc.nextLine();


            System.out.println("Ticket processing complete.");
            TicketPool.writeLog("Ticket processing complete.");
        } catch (InputMismatchException e) {
            System.err.println("Invalid input.");
            TicketPool.writeLog("Invalid input.");
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            TicketPool.writeLog("An unexpected error occurred.");
        } finally {
            TicketPool.writeLog("Ticket processing shut down.");
        }
    }
}