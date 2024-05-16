package com.test.utils;

import com.test.dto.TicketDTO;
import com.test.dto.UserDTO;
import com.test.model.Ticket;
import com.test.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    /**
     * convert list of entities User to list dto list
     *
     * @param entities
     * @return list of user dto
     */
    public static List<UserDTO> dtoFromEntitiesUser(List<User> entities) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<UserDTO>();
        }
        List<UserDTO> dtoList = entities.stream().map(user -> {
            UserDTO dto = UserDTO.builder()
                    .idUser(user.getIdUser())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();
            return dto;
        }).collect(Collectors.toList());
        return dtoList;
    }

    /**
     * convert list of entity Ticket to list dto
     *
     * @param entities
     * @return list of TicketDTO
     */
    public static List<TicketDTO> dtoFromEntitiesTicket(List<Ticket> entities) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<TicketDTO>();
        }
        List<TicketDTO> dtoList = entities.stream().map(ticket -> {
            TicketDTO dto = TicketDTO.builder()
                    .idTicket(ticket.getIdTicket())
                    .title(ticket.getTitle())
                    .description(ticket.getDescription())
                    .status(ticket.getStatus())
                    .owner(dtoFromEntityUser(ticket.getUserOwner()))
                    .build();
            return dto;
        }).collect(Collectors.toList());
        return dtoList;
    }

    /**
     * convert user entity to dto
     *
     * @param entity user
     * @return user dto
     */
    public static UserDTO dtoFromEntityUser(User entity) {
        if (entity == null) {
            return null;
        }
        return UserDTO.builder()
                .idUser(entity.getIdUser())
                .email(entity.getEmail())
                .username(entity.getUsername()).build();
    }

    /**
     * convert ticket entity to dto
     *
     * @param entity
     * @return ticket dto
     */
    public static TicketDTO dtoFromEntityTicket(Ticket entity) {
        if (entity == null) {
            return null;
        }
        return TicketDTO.builder()
                .idTicket(entity.getIdTicket())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .owner(dtoFromEntityUser(entity.getUserOwner()))
                .build();
    }

}
