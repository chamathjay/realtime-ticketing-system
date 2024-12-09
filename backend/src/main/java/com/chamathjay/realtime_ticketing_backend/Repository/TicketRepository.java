package com.chamathjay.realtime_ticketing_backend.Repository;

import com.chamathjay.realtime_ticketing_backend.Models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
