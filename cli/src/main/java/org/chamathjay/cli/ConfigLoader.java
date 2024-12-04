package org.chamathjay.cli;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ConfigLoader {

    private static final String CONFIG_FILE_PATH = "config.json";
    private static final Gson gson = new Gson();

    public static Config loadConfig() {
        try (FileReader reader = new FileReader(CONFIG_FILE_PATH)) {
            return gson.fromJson(reader, Config.class);
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
        System.out.print("Enter Total Tickets: ");
        int totalTickets = sc.nextInt();
        System.out.print("Enter Ticket Release Rate: ");
        int ticketReleaseRate = sc.nextInt();
        System.out.print("Enter Customer Ticket Retrieval Rate: ");
        int ticketRetrievalRate = sc.nextInt();
        System.out.print("Enter Maximum Ticket Capacity: ");
        int capacity = sc.nextInt();

        return new Config(totalTickets, ticketReleaseRate, ticketRetrievalRate, capacity);
    }

}