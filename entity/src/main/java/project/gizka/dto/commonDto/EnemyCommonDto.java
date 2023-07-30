package project.gizka.dto.commonDto;

import jakarta.persistence.Transient;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class EnemyCommonDto extends FighterDto{

    private Long id;
    private String description;
    private LocalDateTime createdAt;
}
