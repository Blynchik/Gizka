package project.gizka.util;

import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;
import project.gizka.dto.commonDto.AdventurerCommonDto;
import project.gizka.dto.commonDto.AppUserCommonDto;
import project.gizka.dto.creatDto.CreatAdventurerDto;
import project.gizka.dto.creatDto.CreatAppUserDto;
import project.gizka.model.Adventurer;
import project.gizka.model.AppUser;

@UtilityClass
public class Converter {

    private final static ModelMapper modelMapper = new ModelMapper();

    public static AppUser getAppUserFrom(CreatAppUserDto userDto){
        return modelMapper.map(userDto, AppUser.class);
    }

    public static Adventurer getAdventurerFrom(CreatAdventurerDto adventurerDto){
        return modelMapper.map(adventurerDto, Adventurer.class);
    }

    public static AppUserCommonDto getUserDtoFrom(AppUser appUser) {
        AppUserCommonDto userDto = modelMapper.map(appUser, AppUserCommonDto.class);
        return userDto;
    }

    public static AdventurerCommonDto getAdventurerDtoFrom(Adventurer adventurer){
        AdventurerCommonDto adventurerDto = modelMapper.map(adventurer, AdventurerCommonDto.class);
        return adventurerDto;
    }
}
