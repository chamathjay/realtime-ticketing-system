package com.chamathjay.realtime_ticketing_backend.cli;

public class Customer implements Runnable {
    private final TicketPool pool;
    private final int customerId;
    private boolean running = true;
    private final Config config;

    public Customer(TicketPool pool, int customerId, Config config) {
        this.pool = pool;
        this.customerId = customerId;
        this.config = config;
    }

    public void stop(){
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep( 1000 * config.getCustomerRetrievalRate());
                pool.removeTicket(customerId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Customer " + customerId + " was interrupted.");
                break;
            }
        }
    }
}
