package project.gizka.dto.commonDto;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnemyCommonDto extends FighterDto{

    private Long id;
    private String description;
    private LocalDateTime createdAt;
}
