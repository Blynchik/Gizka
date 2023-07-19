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
public class AdventurerCommonDto extends FighterDto{
    private Long id;
    private Long appUserId;
    private String lastName;
    private LocalDateTime createdAt;
}
