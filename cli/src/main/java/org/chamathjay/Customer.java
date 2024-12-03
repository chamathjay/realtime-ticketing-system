package org.chamathjay;

public class Customer implements Runnable{
    private TicketPool pool;
    private int customerId;
    private int ticketRetrievalRate;

    public Customer(TicketPool pool, int customerId) {
        this.pool = pool;
        this.customerId = customerId;
//        this.ticketRetrievalRate = ticketRetrievalRate;
    }

    @Override
    public void run() {
        while(true){
            try {
                pool.removeTicket(customerId);
                try {
                    Thread.sleep(ticketRetrievalRate * 1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
