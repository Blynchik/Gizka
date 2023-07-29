package project.gizka.dto.commonDto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AppUserCommonDto {
    private Long id;
    private String chat;
    private String line;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
}
