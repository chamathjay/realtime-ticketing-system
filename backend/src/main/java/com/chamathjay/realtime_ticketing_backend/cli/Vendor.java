package com.chamathjay.realtime_ticketing_backend.cli;

public class Vendor implements Runnable {
    private final TicketPool pool;
    private final int vendorId;
    private boolean running = true;
    private final Config config;

    public Vendor(TicketPool pool, int vendorId, Config config) {
        this.pool = pool;
        this.vendorId = vendorId;
        this.config = config;
    }

    public void stop() {
        this.running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep( 1000 * config.getTicketReleaseRate());
                if (pool.getTicketsRemaining() <= 0) {
                    System.out.println("Vendor- " + vendorId + " stopping, no tickets remaining.");
                    break;
                }
                pool.addTicket(vendorId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Vendor " + vendorId + " was interrupted");
                break;
            }
        }

    }
}
