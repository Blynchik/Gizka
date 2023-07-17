package project.gizka.dto.commonDto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdventurerCommonDto {
    private Long id;
    private Long appUserId;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private int strength;
    private int dexterity;
    private int constitution;
}
