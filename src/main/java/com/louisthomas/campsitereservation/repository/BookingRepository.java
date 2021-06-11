package com.louisthomas.campsitereservation.repository;

import com.louisthomas.campsitereservation.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    @Query("select b from Booking b "
            + "where ((b.startDate < ?1 and ?2 < b.endDate) "
            + "or (b.endDate > ?1 and b.endDate <= ?2) "
            + "or (b.startDate >= ?1 and b.startDate <= ?2)) "
            + "order by b.startDate asc")
    List<Booking> findByDateRange(LocalDate startDate, LocalDate endDate);
}
