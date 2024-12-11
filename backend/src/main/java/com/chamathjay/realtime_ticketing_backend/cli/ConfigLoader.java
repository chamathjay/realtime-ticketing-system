package com.chamathjay.realtime_ticketing_backend.cli;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ConfigLoader {

    private static final String CONFIG_FILE_PATH = "config.json";
    private static final Gson gson = new Gson();

    public static Config loadConfig() {
        try (FileReader reader = new FileReader(CONFIG_FILE_PATH)) {
            Config config = gson.fromJson(reader, Config.class);

            if (config != null && validateConfig(config)) {
                return config;
            } else {
                System.err.println("Invalid config file.");
            }
        } catch (IOException e) {
            System.err.println("Error reading config file: " + e.getMessage());
        } catch (JsonSyntaxException e) {
            System.err.println("Invalid JSON syntax in the config file: " + e.getMessage());
        }
        return null;
    }

    public static void saveConfig(Config config) {
        try (FileWriter writer = new FileWriter(CONFIG_FILE_PATH)) {
            gson.toJson(config, writer);
            System.out.println("Configuration saved to " + CONFIG_FILE_PATH);
        } catch (JsonIOException | IOException e) {
            System.err.println("Error writing config file: " + e.getMessage());
        }
    }

    public static Config getConfigFromUser(Scanner sc) {

        System.out.println("------------------------------------------------------------");
        int totalTickets = getPositiveInput(sc, "Enter Total Tickets: ");
        int ticketReleaseRate = getPositiveInput(sc, "Enter Vendor Ticket Release Rate (seconds): ");
        int customerRetrievalRate = getPositiveInput(sc, "Enter Customer Ticket Retrieval Rate (seconds): ");
        int maxTicketCapacity;
        do {
            maxTicketCapacity = getPositiveInput(sc, "Enter Maximum TicketPool Capacity: ");

        } while (maxTicketCapacity > totalTickets);
        System.out.println("------------------------------------------------------------");

        return new Config(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
    }

    private static int getPositiveInput(Scanner sc, String prompt) {
        int value = -1;
        while (value <= 0) {
            try {
                System.out.print(prompt);
                value = sc.nextInt();
                if (value <= 0) {
                    System.err.println("Value must be greater than 0. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Invalid input: Please enter a valid integer.");
                sc.nextLine();
            }
        }
        return value;
    }

    private static boolean validateConfig(Config config) {
        return config.getTotalTickets() > 0 &&
                config.getTicketReleaseRate() > 0 &&
                config.getCustomerRetrievalRate() > 0 &&
                config.getMaxTicketCapacity() <= config.getTotalTickets() ;
    }

}