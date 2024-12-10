package com.chamathjay.realtime_ticketing_backend.cli;

public class Customer implements Runnable {
    private final TicketPool pool;
    private final int customerId;
    private final int customerRetrievalRate;
    private boolean running = true;

    public Customer(TicketPool pool, int customerId, int customerRetrievalRate) {
        this.pool = pool;
        this.customerId = customerId;
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public void stop(){
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000 * customerRetrievalRate);
                pool.removeTicket(customerId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Customer " + customerId + " was interrupted.");
            }
        }
    }
}
