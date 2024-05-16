package com.test.service;

import com.test.model.Ticket;
import com.test.model.User;
import com.test.repository.TicketRepository;
import com.test.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * service used by the controller for the Ticket entity
 */
@Service
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    UserRepository userRepository;

    /**
     * method to return all tickets
     *
     * @return list of tickets
     */
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    /**
     * retrieve ticket by id
     *
     * @param idTicket
     * @return ticket found
     */
    public Ticket getTicketsById(int idTicket) {
        Optional<Ticket> ticket = ticketRepository.findById(idTicket);
        if (ticket.isEmpty()) {
            throw new EntityNotFoundException("entity not found with id " + idTicket);
        }
        return ticket.get();
    }

    /**
     * create ticket in db
     *
     * @param ticket
     * @return ticket created
     */
    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    /**
     * method to update ticket
     *
     * @param idTicket
     * @param ticket
     * @return
     * @throws Exception while entity not found or saving not working properly
     */

    public Ticket updateTicket(int idTicket, Ticket ticket) throws Exception {
        Optional<Ticket> ticketFound = ticketRepository.findById(idTicket);
        if (ticketFound.isEmpty()) {
            throw new EntityNotFoundException("Entity not found id: " + idTicket);
        }
        Ticket entity = ticketFound.get();
        entity.setStatus(ticket.getStatus());
        entity.setTitle(ticket.getTitle());
        entity.setDescription(ticket.getDescription());
        if (ticket.getUserOwner() != null) {
            User owner = userRepository.findById(ticket.getUserOwner().getIdUser()).get();
            entity.setUserOwner(owner);
        }
        try {
            return ticketRepository.save(entity);
        } catch (Exception e) {
            throw new Exception("Something went wrong while updating the entity " + e.getMessage());
        }
    }

    /**
     * assign ticket to user
     *
     * @param idTicket
     * @param idUser
     * @return ticket after assign it to user
     * @throws Exception while id not found for ticket or user
     */
    public Ticket assignTicketToUser(int idTicket, int idUser) throws Exception {
        Optional<Ticket> ticket = ticketRepository.findById(idTicket);
        if (ticket.isEmpty()) {
            throw new EntityNotFoundException("ticket not found , id: " + idTicket);
        }
        Optional<User> user = userRepository.findById(idUser);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("user not found , id: " + idUser);
        }

        try {
            Ticket ticketEntity = ticket.get();
            User userEntity = user.get();
            ticketEntity.setUserOwner(userEntity);
            return ticketRepository.save(ticketEntity);
        } catch (Exception e) {
            throw new Exception("assign the ticket to user went wrong " + e.getMessage());
        }

    }

    /**
     * delete ticket from db
     *
     * @param idTicket
     */
    public void deleteUser(int idTicket) throws Exception {
        try {
            ticketRepository.deleteById(idTicket);
        } catch (Exception e) {
            throw new Exception("something went wrong while deleting ticket" + e.getMessage());
        }

    }
}
