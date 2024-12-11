package com.chamathjay.realtime_ticketing_backend.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TicketStatus {
    private int ticketsAvailable;
    private int ticketsSold;
    private int ticketsInPool;



}
