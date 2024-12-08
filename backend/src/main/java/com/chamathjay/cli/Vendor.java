package com.chamathjay.cli;

public class Vendor implements Runnable {
    private final TicketPool pool;
    private final int vendorId;
    private final int ticketReleaseRate;

    public Vendor(TicketPool pool, int vendorId, int ticketReleaseRate) {
        this.pool = pool;
        this.vendorId = vendorId;
        this.ticketReleaseRate = ticketReleaseRate;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (pool.getTotalTicketsRemaining() <= 0) {
                    System.out.println("Vendor- " + vendorId + " stopping, no tickets remaining.");
                    break;
                }
                Thread.sleep(1000 * ticketReleaseRate);
                pool.addTicket(vendorId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Vendor " + vendorId + " was interrupted");
                break;
            }
        }

    }
}
