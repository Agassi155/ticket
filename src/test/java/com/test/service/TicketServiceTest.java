package com.test.service;

import com.test.model.Status;
import com.test.model.Ticket;
import com.test.model.User;
import com.test.repository.TicketRepository;
import com.test.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private TicketService ticketService;

    private User user1;
    private Ticket ticket1;
    private Ticket ticket2;
    private Ticket ticket3;

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
        ticket3 = Ticket.builder().idTicket(4)
                .title("title3")
                .description("description3")
                .status(Status.FINISHED)
                .userOwner(null)
                .build();

        user1.setTickets(List.of(ticket1, ticket2));

    }

    @Test
    void getAllTickets_return_allTickets() {
        given(ticketRepository.findAll()).willReturn(List.of(ticket1, ticket2));
        var expected = List.of(ticket1, ticket2);

        List<Ticket> actual = ticketService.getAllTickets();

        assertEquals(2, actual.size());
        assertEquals(expected, actual);

    }

    @Test
    void getTicketsById_returnEntityException() {
        when(ticketRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            ticketService.getTicketsById(ticket1.getIdTicket());
        });
    }

    @Test
    void getTicketsById_returnOne_entity_Found() {
        var expected = ticket1;
        when(ticketRepository.findById(anyInt())).thenReturn(Optional.of(ticket1));

        Ticket actual = ticketService.getTicketsById(expected.getIdTicket());

        assertEquals(expected, actual);
    }

    @Test
    void createTicket() {
        var expected = ticket1;
        var input = ticket1;
        given(ticketRepository.save(any(Ticket.class))).willAnswer((invocation -> invocation.getArgument(0)));

        var actual = ticketService.createTicket(input);

        assertEquals(expected, actual);
        verify(ticketRepository).save(input);
    }

    @Test
    void updateTicket_EntityNotFoundException_occurred() {
        var idTicket = 43;
        when(ticketRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            ticketService.updateTicket(idTicket, ticket1);
        });
    }

    @Test
    void updateTicket() throws Exception {
        var ancient = ticket1;
        var expected = Ticket.builder().idTicket(ticket1.getIdTicket())
                .userOwner(user1)
                .description("descUpdater")
                .title("titleUpdated")
                .status(Status.CANCELED)
                .build();
        when(ticketRepository.findById(anyInt())).thenReturn(Optional.of(ancient));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user1));
        given(ticketRepository.save(any(Ticket.class))).willAnswer((invocation -> invocation.getArgument(0)));

        var actual = ticketService.updateTicket(ancient.getIdTicket(), expected);

        assertEquals(expected.getIdTicket(), actual.getIdTicket());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getUserOwner().getIdUser(), actual.getUserOwner().getIdUser());
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void updateTicket_error_occurred() throws Exception {
        var idTicket = 34;
        given(ticketRepository.save(any(Ticket.class))).willThrow(RuntimeException.class);

        assertThrows(Exception.class, () -> {
            ticketService.updateTicket(idTicket, ticket1);
        });
    }

    @Test
    void assignTicketToUser_entity_not_found_ticket() {
        var idTickets = 43;
        var idUser = 23;
        when(ticketRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            ticketService.assignTicketToUser(idTickets, idUser);
        });
    }

    @Test
    void assignTicketToUser_entity_not_found_User() {
        var idTickets = 43;
        var idUser = 23;
        when(ticketRepository.findById(anyInt())).thenReturn(Optional.of(ticket1));
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            ticketService.assignTicketToUser(idTickets, idUser);
        });
    }

    @Test
    void assignTicketToUser() throws Exception {
        var ancien = ticket3; //ticket not assigned to user;
        var idUser = user1.getIdUser();
        var idTicket = ticket3.getIdTicket();
        given(ticketRepository.findById(anyInt())).willReturn(Optional.of(ticket3));
        given(userRepository.findById(anyInt())).willReturn(Optional.of(user1));
        given(ticketRepository.save(any(Ticket.class))).willAnswer((invocation -> invocation.getArgument(0)));

        var actual = ticketService.assignTicketToUser(idTicket, idUser);

        assertNotNull(actual.getUserOwner());
        assertEquals(actual.getUserOwner().getIdUser(), idUser);

    }

    @Test
    void deleteUser() throws Exception {
        willDoNothing().given(ticketRepository).deleteById(anyInt());

        ticketService.deleteUser(ticket1.getIdTicket());

        verify(ticketRepository, times(1)).deleteById(ticket1.getIdTicket());
    }

}