package com.test.controller;

import com.test.dto.TicketDTO;
import com.test.dto.UserDTO;
import com.test.model.Ticket;
import com.test.model.User;
import com.test.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.test.utils.Utils;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * retrieve all users form DB
     *
     * @return response of list of users
     */
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<?> getAllUsers() {
        List<UserDTO> list = Utils.dtoFromEntitiesUser(userService.getAllUser());
        return ResponseEntity.ok(list);
    }

    /**
     * retrieve all tickets assigned to users form DB
     *
     * @return response list of tickets or exception with message
     * @params idUser owner of the tickets for the display
     */
    @GetMapping(value = "/users/{idUser}/ticket", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<?> getTicketAssignedToUser(@PathVariable int idUser) {
        try {
            List<Ticket> tickets = userService.getTicketAssignedToUser(idUser);
            List<TicketDTO> list = Utils.dtoFromEntitiesTicket(tickets);
            return ResponseEntity.ok(list);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * used for create entity user , only for admin
     *
     * @param user
     * @return response with the user created by admin
     */
    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody User user) throws Exception {
        try {
            UserDTO dto = Utils.dtoFromEntityUser(userService.createUser(user));
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * used for update entity user , only for admin
     *
     * @param id,user
     * @return response with the user updating by admin
     */
    @PutMapping(value = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable("id") int id, @RequestBody User user) throws Exception {
        try {
            UserDTO dto = Utils.dtoFromEntityUser(userService.updateUser(id, user));
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
