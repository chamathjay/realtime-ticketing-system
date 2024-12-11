package com.chamathjay.realtime_ticketing_backend.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Ticket {
    @Getter
    @Id@GeneratedValue()
    private Long id;
    @Getter
    @Setter
    private String vendor;
    @Getter
    @Setter
    private String event;
    @Setter
    private boolean available = true;

    public Ticket() {}

    public Ticket(String vendor) {
        this.vendor = vendor;
    }

}
