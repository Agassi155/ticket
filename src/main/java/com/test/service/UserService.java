package com.test.service;

import com.test.model.Ticket;
import com.test.model.User;
import com.test.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * service used by the controller for the User entity
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * method to retrieve all user
     *
     * @return list of user
     */
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    /**
     * method to retrieve the ticket assigned to user
     *
     * @param idUser
     * @return list tickets assigned to user
     */
    public List<Ticket> getTicketAssignedToUser(int idUser) {
        Optional<User> user = userRepository.findById(idUser);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Entity not found id: " + idUser);
        }
        return user.get().getTickets();
    }

    /**
     * method for creating user
     *
     * @param user
     * @return the saved user
     */
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * used for updating entity
     *
     * @param idUser
     * @param user
     * @return updated entity
     * @throws Exception while not found or not working
     */
    public User updateUser(int idUser, User user) throws Exception {
        Optional<User> userFound = userRepository.findById(idUser);
        if (userFound.isEmpty()) {
            throw new EntityNotFoundException("Entity not found id: " + idUser);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User entity = userFound.get();
        entity.setIdUser(user.getIdUser());
        entity.setPassword(user.getPassword());
        entity.setEmail(user.getEmail());
        entity.setRoles(user.getRoles());
        entity.setUsername(user.getUsername());

        try {
            return userRepository.save(entity);
        } catch (Exception e) {
            throw new Exception("Something went wrong while updating the entity" + e.getMessage());
        }

    }
}
