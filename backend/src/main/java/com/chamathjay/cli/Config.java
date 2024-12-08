package com.chamathjay.cli;

public class Config {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    public Config() {
    }

    public Config(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    @Override
    public String toString() {
        return "Config {" + "\n" + "totalTickets=" + totalTickets + "\n" +
                ", ticketReleaseRate=" + ticketReleaseRate + "\n" +
                ", ticketRetrievalRate=" + customerRetrievalRate + "\n" +
                ", capacity=" + maxTicketCapacity + "}";
    }
}
