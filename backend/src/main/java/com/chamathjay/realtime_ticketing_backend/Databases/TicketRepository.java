package com.chamathjay.realtime_ticketing_backend.Databases;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface TicketRepository extends JpaRepository {
}
