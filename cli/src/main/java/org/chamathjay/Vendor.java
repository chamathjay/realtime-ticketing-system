package org.chamathjay;

public class Vendor implements Runnable {
    private final TicketPool pool;
    private final int vendorId;
    private int ticketCount = 0;
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
                String ticketId = vendorId + "-" + (++ticketCount);
                pool.addTicket(ticketId, vendorId);
                Thread.sleep(ticketReleaseRate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Vendor " + vendorId + " was interrupted");
                break;
            }
        }

    }
}
