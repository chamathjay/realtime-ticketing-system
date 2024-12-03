package org.example;

public class Customer implements Runnable{
    private final TicketPool pool;
    private final int customerId;
    private final int ticketRetrievalRate;

    public Customer(TicketPool pool, int customerId, int ticketRetrievalRate) {
        this.pool = pool;
        this.customerId = customerId;
        this.ticketRetrievalRate = ticketRetrievalRate;
    }

    @Override
    public void run() {
        while(true){
            try {
                pool.buyTicket(customerId);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
