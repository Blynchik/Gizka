package project.gizka.dto.commonDto;

import lombok.Data;

@Data
public class BinaryContentDto {
    private String type;
    private byte[] bytes;
}
