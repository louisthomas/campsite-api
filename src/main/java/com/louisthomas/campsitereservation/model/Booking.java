package com.louisthomas.campsitereservation.model;

import lombok.Data;
import lombok.Generated;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "booking", schema = "campsite")
@Data
@Generated
@EntityListeners(AuditingEntityListener.class)
public class Booking {

    @Id
    @GeneratedValue
    private UUID id;

    private String email;

    private String fullName;

    private LocalDate startDate;

    private LocalDate endDate;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;

}
