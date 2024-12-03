package org.chamathjay;

public class Customer implements Runnable{
    private TicketPool pool;
    private int customerId;
    private int customerRetrievalRate;

    public Customer(TicketPool pool, int customerId) {
        this.pool = pool;
        this.customerId = customerId;
//        this.customerRetrievalRate = customerRetrievalRate;
    }

    @Override
    public void run() {
        while(true){
            try {
                pool.removeTicket(customerId);
                try {
                    Thread.sleep(customerRetrievalRate);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
