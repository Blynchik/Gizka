package project.gizka.dto.commonDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppUserCommonDto {
    private Long id;
    private String name;
    private String chat;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
}
