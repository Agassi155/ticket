package com.test.controller;

import com.test.dto.TicketDTO;
import com.test.model.Ticket;
import com.test.service.TicketService;
import com.test.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TicketController {

    @Autowired
    private TicketService ticketService;

    /**
     * retrieve all tickets
     *
     * @return response with list of tickets
     */
    @GetMapping(value = "/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<?> getAllTickets() {
        List<TicketDTO> list = Utils.dtoFromEntitiesTicket(ticketService.getAllTickets());
        return ResponseEntity.ok(list);
    }

    /**
     * retrieve tickets by its id
     *
     * @return response with the tickets found
     */
    @GetMapping(value = "/tickets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<?> getTicketsById(@PathVariable int id) {
        try {
            TicketDTO dto = Utils.dtoFromEntityTicket(ticketService.getTicketsById(id));
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * create new entity ticket, only for admin
     *
     * @param ticket
     * @return response with ticket created
     */
    @PostMapping(value = "/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createTicket(@RequestBody Ticket ticket) {
        try {
            TicketDTO dto = Utils.dtoFromEntityTicket(ticketService.createTicket(ticket));
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * update ticket entity , only for admin
     *
     * @param idTicket
     * @param ticket
     * @return response with data updated
     */
    @PutMapping(value = "/tickets/{idTicket}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateTicket(@PathVariable int idTicket, @RequestBody Ticket ticket) {
        try {
            TicketDTO dto = Utils.dtoFromEntityTicket(ticketService.updateTicket(idTicket, ticket));
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * assign ticket to user , only for admin
     *
     * @param idTicket
     * @param idUser
     * @return response with the ticket assign to user
     */
    @PutMapping(value = "/tickets/{idTicket}/assign/{idUser}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> assignTicketToUser(@PathVariable int idTicket, @PathVariable int idUser) {
        try {
            Ticket entity = ticketService.assignTicketToUser(idTicket, idUser);
            TicketDTO dto = Utils.dtoFromEntityTicket(entity);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * delete tickets , only for admin
     *
     * @param idTicket
     * @return response with message confirm that tickets deleted successfully
     */
    @DeleteMapping(value = "/tickets/{idTicket}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteTicket(@PathVariable int idTicket) {
        try {
            ticketService.deleteUser(idTicket);
            return ResponseEntity.ok("Ticket deleted successfully id:" + idTicket);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
