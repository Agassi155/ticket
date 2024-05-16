package com.test.repository;

import com.test.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository for Ticket Entity
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
}
