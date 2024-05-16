package com.test.controller;

import com.test.model.Status;
import com.test.model.Ticket;
import com.test.model.User;
import com.test.service.UserService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;

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
                .idUser(5)
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
        ticket2 = Ticket.builder().idTicket(54)
                .title("title2")
                .description("description2")
                .status(Status.IN_PROGRESS)
                .userOwner(user1)
                .build();

        user1.setTickets(List.of(ticket1, ticket2));

    }

    @Test
    void getAllUsers() {
        var listDTO = Utils.dtoFromEntitiesUser(List.of(user1, user2));
        var expected = ResponseEntity.ok(listDTO);
        when(userService.getAllUser()).thenReturn(List.of(user1, user2));

        var actual = userController.getAllUsers();

        assertEquals(expected, actual);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void getTicketAssignedToUser() {
        var dtoList = Utils.dtoFromEntitiesTicket(List.of(ticket1, ticket2));
        var expected = ResponseEntity.ok(dtoList);
        when(userService.getTicketAssignedToUser(anyInt())).thenReturn(List.of(ticket1, ticket2));

        var actual = userController.getTicketAssignedToUser(user1.getIdUser());

        assertEquals(expected, actual);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void getTicketAssignedToUser_entityNotFound() {
        when(userService.getTicketAssignedToUser(anyInt())).thenThrow(EntityNotFoundException.class);

        var actual = userController.getTicketAssignedToUser(user1.getIdUser());

        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    @Test
    void createUser() throws Exception {
        var dto = Utils.dtoFromEntityUser(user1);
        var expected = ResponseEntity.ok(dto);
        when(userService.createUser(any(User.class))).thenReturn(user1);

        var actual = userController.createUser(user1);

        assertEquals(expected, actual);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void createUser_errorOccurred() throws Exception {
        when(userService.createUser(any(User.class))).thenThrow(RuntimeException.class);

        var actual = userController.createUser(user1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    }

    @Test
    void updateUser() throws Exception {
        var dto = Utils.dtoFromEntityUser(user1);
        var expected = ResponseEntity.ok(dto);
        when(userService.updateUser(anyInt(), any(User.class))).thenReturn(user1);

        var actual = userController.updateUser(user1.getIdUser(), user1);

        assertEquals(expected, actual);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
    }

    @Test
    void updateUser_entityNotFound() throws Exception {
        when(userService.updateUser(anyInt(), any(User.class))).thenThrow(EntityNotFoundException.class);

        var actual = userController.updateUser(user1.getIdUser(), user1);

        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    @Test
    void updateUser_errorOccurred() throws Exception {
        when(userService.updateUser(anyInt(), any(User.class))).thenThrow(RuntimeException.class);

        var actual = userController.updateUser(user1.getIdUser(), user1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actual.getStatusCode());
    }
}