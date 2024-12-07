package com.chamathjay.cli;

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

            if (config != null && config.getMaxTicketCapacity() > 0 && config.getTicketReleaseRate() > 0 && config.getCustomerRetrievalRate() > 0 && config.getTotalTickets() >= config.getMaxTicketCapacity()) {
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

//    static boolean validInput = false;

    public static Config getConfigFromUser(Scanner sc) {
        int totalTickets = 0, ticketReleaseRate = 0, customerRetrievalRate = 0, maxTicketCapacity = 0;

        while (true) {
            try {

                System.out.print("Enter Total Tickets: ");
                totalTickets = sc.nextInt();

                System.out.print("Enter Ticket Release Rate: ");
                ticketReleaseRate = sc.nextInt();

                System.out.print("Enter Customer Ticket Retrieval Rate: ");
                customerRetrievalRate = sc.nextInt();

                System.out.print("Enter Maximum Ticket Capacity: ");
                maxTicketCapacity = sc.nextInt();

                if (ticketReleaseRate > 0 && customerRetrievalRate > 0 && maxTicketCapacity > 0 && totalTickets >= maxTicketCapacity) {
                    return new Config(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
                } else {
                    System.err.println("invalid input: Total tickets must be >= max capacity, and all values must be positive. Please try again.");
                    sc.nextLine();
                }
            } catch (InputMismatchException e) {
                System.err.println("invalid input: Please enter integers only. Try again.");
                sc.nextLine();
            }
        }

    }

}