package com.test.dto;

import com.test.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDTO {
    private int idTicket;
    private String title;
    private String description;
    private Status status;
    private UserDTO owner;
}
