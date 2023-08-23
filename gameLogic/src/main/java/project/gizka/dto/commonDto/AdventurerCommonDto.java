package project.gizka.dto.commonDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdventurerCommonDto extends FighterDto{
    private Long id;
    private Long appUserId;
    private String lastName;
    private LocalDateTime createdAt;
}
