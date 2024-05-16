package com.test.service;

import com.test.model.Status;
import com.test.model.Ticket;
import com.test.model.User;
import com.test.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;

    private User user1;
    private User user2;
    private Ticket ticket1;

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
        user1.setTickets(List.of(ticket1));
    }

    @Test
    void getAllUser() {
        var expected = List.of(user1, user2);
        given(userRepository.findAll()).willReturn(List.of(user1, user2));

        var actual = userService.getAllUser();

        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    void getTicketAssignedToUser() {
        var expected = List.of(ticket1);
        given(userRepository.findById(anyInt())).willReturn(Optional.of(user1));

        var actual = userService.getTicketAssignedToUser(user1.getIdUser());

        assertEquals(expected, actual);
    }

    @Test
    void getTicketAssignedToUser_Entity_Not_Found() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.getTicketAssignedToUser(user1.getIdUser());
        });
    }

    @Test
    void createUser() {
        var expected = user1;
        expected.setPassword(passwordEncoder.encode(expected.getPassword()));
        given(userRepository.save(any(User.class))).willAnswer((invocation -> invocation.getArgument(0)));

        var actual = userService.createUser(user1);

        assertEquals(expected, actual);
    }

    @Test
    void updateUser() throws Exception {
        var ancien = user1;
        var expected = User.builder().idUser(user1.getIdUser())
                .username("nameUpdated")
                .email("emailUpdated")
                .password(passwordEncoder.encode("passUpdated"))
                .roles("ADMIN")
                .build();
        given(userRepository.findById(anyInt())).willReturn(Optional.of(ancien));
        given(userRepository.save(any(User.class))).willAnswer((invocation -> invocation.getArgument(0)));

        var actual = userService.updateUser(ancien.getIdUser(), expected);

        assertEquals(expected.getIdUser(), actual.getIdUser());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getRoles().toString(), actual.getRoles().toString());
    }

    @Test
    void updateUser_entityNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.updateUser(user2.getIdUser(), user2);
        });
    }
}