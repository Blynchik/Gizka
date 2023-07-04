package project.gizka.util;

import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;
import project.gizka.appUser.dto.CreateAppUserDto;
import project.gizka.appUser.model.AppUser;

@UtilityClass
public class Converter {

    private final static ModelMapper modelMapper = new ModelMapper();

    public static AppUser getAppUserFrom(CreateAppUserDto userDto){
        return modelMapper.map(userDto, AppUser.class);
    }
}
