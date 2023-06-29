package project.gizka.util;

import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;
import project.gizka.dto.CreateAppUserDto;
import project.gizka.model.AppUser;

@UtilityClass
public class Converter {

    private final static ModelMapper modelMapper = new ModelMapper();

    public static AppUser getAppUserFrom(CreateAppUserDto userDto){
        return modelMapper.map(userDto, AppUser.class);
    }
}
