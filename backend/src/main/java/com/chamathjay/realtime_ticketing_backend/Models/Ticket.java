package com.chamathjay.realtime_ticketing_backend.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Ticket {
    @Getter
    @Id
    private Long id;
    @Getter
    @Setter
    private String vendor;
    @Setter
    private boolean available = true;

    public Ticket() {}

    public Ticket(String vendor) {
        this.vendor = vendor;
    }

}
