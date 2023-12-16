package ru.mrsinkaaa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SessionDTO {

    private String id;
    private int userId;
    private LocalDateTime expiresAt;
}
