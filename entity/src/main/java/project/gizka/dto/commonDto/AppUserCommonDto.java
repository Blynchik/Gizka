package project.gizka.dto.commonDto;

import lombok.*;

import java.time.LocalDateTime;

@Data
public class AppUserCommonDto {
    private Long id;
    private String chat;
    private String line;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
}
