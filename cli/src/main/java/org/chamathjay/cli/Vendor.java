package org.chamathjay.cli;

public class Vendor implements Runnable {
    private final TicketPool pool;
    private final int vendorId;
    private int ticketCount = 0;
    private final int ticketReleaseRate;
    private volatile boolean isRunning = true;

    public Vendor(TicketPool pool, int vendorId, int ticketReleaseRate) {
        this.pool = pool;
        this.vendorId = vendorId;
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public void stop() {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                String ticketId = vendorId + "-" + (++ticketCount);
                Thread.sleep(1000 * ticketReleaseRate);
                pool.addTicket(ticketId, vendorId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Vendor " + vendorId + " was interrupted");
                break;
            }
        }

    }
}
