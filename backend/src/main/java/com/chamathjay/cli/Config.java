package com.chamathjay.cli;

import lombok.Getter;

@Getter
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

    //    public boolean isValid() {
//        boolean isValid;
//        if (!isValid) {
//            System.err.println("Invalid Config: Ensure totalTickets >= maxTicketCapacity, and all values are positive.");
//        }
//        return isValid;
//    }
    @Override
    public String toString() {
        return "Config {" + "\n" + "totalTickets=" + totalTickets + "\n" +
                ", ticketReleaseRate=" + ticketReleaseRate + "\n" +
                ", ticketRetrievalRate=" + customerRetrievalRate + "\n" +
                ", capacity=" + maxTicketCapacity + "}";
    }


}
