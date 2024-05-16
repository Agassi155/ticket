package com.test.controller;

import com.test.model.Status;
import com.test.model.Ticket;
import com.test.model.User;
import com.test.service.TicketService;
import com.test.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = TicketController.class)
class TicketControllerTest {

    @MockBean
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    private User user1;
    private Ticket ticket1;
    private Ticket ticket2;

    @BeforeEach
    public void setup() {
        user1 = User.builder()
                .idUser(45)
                .username("toto")
                .email("email")
                .password("toto")
                .roles("ADMIN")
                .build();

        ticket1 = Ticket.builder().idTicket(45)
                .title("title1")
                .description("description2")
                .status(Status.IN_PROGRESS)
                .userOwner(user1)
                .build();
        ticket2 = Ticket.builder().idTicket(54)
                .title("title2")
                .description("description2")
                .status(Status.IN_PROGRESS)
                .userOwner(user1)
                .build();

        user1.setTickets(List.of(ticket1, ticket2));

    }

    @Test
    void getAllTickets() {
        var listDTO = Utils.dtoFromEntitiesTicket(List.of(ticket1, ticket2));
        var expected = ResponseEntity.ok(listDTO);
        given(ticketService.getAllTickets()).willReturn(List.of(ticket1, ticket2));

        var actual = ticketController.getAllTickets();

        assertEquals(expected, actual);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void getTicketsById() {
        var dto = Utils.dtoFromEntityTicket(ticket1);
        var expected = ResponseEntity.ok(dto);
        given(ticketService.getTicketsById(anyInt())).willReturn(ticket1);

        var actual = ticketController.getTicketsById(ticket1.getIdTicket());

        assertEquals(expected, actual);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void getTicketsById_entityNotFoundException() {
        when(ticketService.getTicketsById(123)).thenThrow(EntityNotFoundException.class);

        var actual = ticketController.getTicketsById(123);

        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    @Test
    void createTicket() {
        var dto = Utils.dtoFromEntityTicket(ticket1);
        var expected = ResponseEntity.ok(dto);
        given(ticketService.createTicket(any(Ticket.class))).willReturn(ticket1);

        var actual = ticketController.createTicket(ticket1);

        assertEquals(expected, actual);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void createTicket_internalServerError() {
        when(ticketService.createTicket(any(Ticket.class))).thenThrow(RuntimeException.class);

        var actual = ticketController.createTicket(ticket1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    }

    @Test
    void updateTicket() throws Exception {
        var dto = Utils.dtoFromEntityTicket(ticket1);
        var expected = ResponseEntity.ok(dto);
        given(ticketService.updateTicket(anyInt(), any(Ticket.class))).willReturn(ticket1);

        var actual = ticketController.updateTicket(ticket1.getIdTicket(), ticket1);

        assertEquals(expected, actual);
        assertEquals(HttpStatus.OK, actual.getStatusCode());

    }

    @Test
    void updateTicket_notFoundEntity() throws Exception {
        when(ticketService.updateTicket(anyInt(), any(Ticket.class))).thenThrow(EntityNotFoundException.class);

        var actual = ticketController.updateTicket(ticket1.getIdTicket(), ticket1);

        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    @Test
    void updateTicket_internalServerError() throws Exception {
        when(ticketService.updateTicket(anyInt(), any(Ticket.class))).thenThrow(RuntimeException.class);

        var actual = ticketController.updateTicket(ticket1.getIdTicket(), ticket1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    }

    @Test
    void assignTicketToUser() throws Exception {
        var dto = Utils.dtoFromEntityTicket(ticket1);
        var expected = ResponseEntity.ok(dto);
        given(ticketService.assignTicketToUser(anyInt(), anyInt())).willReturn(ticket1);

        var actual = ticketController.assignTicketToUser(ticket1.getIdTicket(), user1.getIdUser());

        assertEquals(expected, actual);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void assignTicketToUser_EntityNotFound() throws Exception {
        when(ticketService.assignTicketToUser(anyInt(), anyInt())).thenThrow(EntityNotFoundException.class);

        var actual = ticketController.assignTicketToUser(ticket1.getIdTicket(), user1.getIdUser());

        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    @Test
    void assignTicketToUser_Exception() throws Exception {
        when(ticketService.assignTicketToUser(anyInt(), anyInt())).thenThrow(RuntimeException.class);

        var actual = ticketController.assignTicketToUser(ticket1.getIdTicket(), user1.getIdUser());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    }

    @Test
    void deleteTicket() {
        var actual = ticketController.deleteTicket(ticket1.getIdTicket());

        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void deleteTicket_errorOccurred() throws Exception {
        doThrow(RuntimeException.class).when(ticketService).deleteUser(anyInt());

        var actual = ticketController.deleteTicket(ticket1.getIdTicket());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    }
}