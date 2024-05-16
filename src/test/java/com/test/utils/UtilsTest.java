package com.test.utils;

import com.test.dto.TicketDTO;
import com.test.dto.UserDTO;
import com.test.model.Status;
import com.test.model.Ticket;
import com.test.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsTest {

    private User user1;
    private User user2;
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
        user2 = User.builder()
                .idUser(4)
                .username("tata")
                .email("email")
                .password("tata")
                .roles("USER")
                .build();
        ticket1 = Ticket.builder().idTicket(45)
                .title("title1")
                .description("description2")
                .status(Status.IN_PROGRESS)
                .userOwner(user1)
                .build();
        ticket2 = Ticket.builder().idTicket(5)
                .title("title2")
                .description("description4")
                .status(Status.IN_PROGRESS)
                .userOwner(user1)
                .build();
        user1.setTickets(List.of(ticket1));
    }

    @Test
    void dtoFromEntitiesUser() {
        var dto1 = UserDTO.builder().idUser(45)
                .username("toto")
                .email("email")
                .build();
        var dto2 = UserDTO.builder().idUser(4)
                .username("tata")
                .email("email")
                .build();
        var expected = List.of(dto1, dto2);

        var actual = Utils.dtoFromEntitiesUser(List.of(user1, user2));

        assertEquals(expected, actual);
    }

    @Test
    void dtoFromEntitiesTicket() {
        var user1 = UserDTO.builder().idUser(45)
                .username("toto")
                .email("email")
                .build();
        var dto1 = TicketDTO.builder()
                .idTicket(45)
                .title("title1")
                .description("description2")
                .status(Status.IN_PROGRESS)
                .owner(user1).build();
        var dto2 = TicketDTO.builder()
                .idTicket(5)
                .title("title2")
                .description("description4")
                .status(Status.IN_PROGRESS)
                .owner(user1)
                .build();
        var expected = List.of(dto1, dto2);

        var actual = Utils.dtoFromEntitiesTicket(List.of(ticket1, ticket2));

        assertEquals(expected, actual);
    }

    @Test
    void dtoFromEntityUser() {
        var expected = UserDTO.builder().idUser(45)
                .username("toto")
                .email("email")
                .build();

        var actual = Utils.dtoFromEntityUser(user1);

        assertEquals(expected, actual);
    }

    @Test
    void dtoFromEntityTicket() {
        var user1 = UserDTO.builder().idUser(45)
                .username("toto")
                .email("email")
                .build();
        var expected = TicketDTO.builder()
                .idTicket(45)
                .title("title1")
                .description("description2")
                .status(Status.IN_PROGRESS)
                .owner(user1).build();

        var actual = Utils.dtoFromEntityTicket(ticket1);

        assertEquals(expected, actual);
    }
}