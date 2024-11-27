package org.example;

public class Vendor implements Runnable {
    private final TicketPool pool;
    private final int vendorId;
    private static int ticketId;
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
                pool.addTicket(ticketId++, vendorId);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Vendor " + vendorId + " was interrupted");
            }
        }

    }
}
