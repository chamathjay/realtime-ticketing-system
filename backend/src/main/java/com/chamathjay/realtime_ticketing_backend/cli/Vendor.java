package com.chamathjay.realtime_ticketing_backend.cli;

public class Vendor implements Runnable {
    private final TicketPool pool;
    private final int vendorId;
    private final int ticketReleaseRate;
    private boolean running = true;

    public Vendor(TicketPool pool, int vendorId, int ticketReleaseRate) {
        this.pool = pool;
        this.vendorId = vendorId;
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public void stop() {
        this.running = false;
    }

    @Override
    public void run() {
        while (running) {
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
