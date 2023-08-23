package project.gizka.dto.commonDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EnemyCommonDto extends FighterDto{

    private Long id;
    private String description;
    private LocalDateTime createdAt;
}
