package project.gizka.dto.commonDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUserCommonDto {
    private Long id;
    private String chat;
    private String line;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
}
