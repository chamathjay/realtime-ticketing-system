package org.chamathjay;

public class Vendor implements Runnable {
    private TicketPool pool;
    private int vendorId;
    private int ticketId;
    private int ticketReleaseRate;

    public Vendor(TicketPool pool, int vendorId) {
        this.pool = pool;
        this.vendorId = vendorId;
//        this.ticketReleaseRate = ticketReleaseRate;
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
                Thread.sleep(ticketReleaseRate * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Vendor " + vendorId + " was interrupted");
            }
        }

    }
}
