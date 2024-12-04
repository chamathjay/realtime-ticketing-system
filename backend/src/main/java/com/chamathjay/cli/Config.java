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

    public boolean isValid() {
        boolean isValid = totalTickets > 0 && ticketReleaseRate > 0 && customerRetrievalRate > 0 && maxTicketCapacity > totalTickets;
        if (!isValid) {
            System.err.println("Invalid Config: Ensure totalTickets > 0, ticketReleaseRate > 0, ticketRetrievalRate > 0, and capacity > totalTickets.");
        }
        return isValid;
    }

    @Override
    public String toString() {
        return "Config {" + "totalTickets=" + totalTickets +
                ", ticketReleaseRate=" + ticketReleaseRate +
                ", ticketRetrievalRate=" + customerRetrievalRate +
                ", capacity=" + maxTicketCapacity + "}";
    }


}
