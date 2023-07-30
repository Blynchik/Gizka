package project.gizka.util;

import lombok.experimental.UtilityClass;
import org.modelmapper.ModelMapper;
import project.gizka.dto.commonDto.AdventurerCommonDto;
import project.gizka.dto.commonDto.AppUserCommonDto;
import project.gizka.dto.commonDto.EnemyCommonDto;
import project.gizka.dto.createDto.CreateAdventurerDto;
import project.gizka.dto.createDto.CreateAppUserDto;
import project.gizka.dto.createDto.CreateBinaryContentDto;
import project.gizka.dto.createDto.CreateEnemyDto;
import project.gizka.model.Adventurer;
import project.gizka.model.AppUser;
import project.gizka.model.BinaryContent;
import project.gizka.model.Enemy;

@UtilityClass
public class Converter {

    private final static ModelMapper modelMapper = new ModelMapper();

    public static AppUser getAppUserFrom(CreateAppUserDto userDto) {
        return modelMapper.map(userDto, AppUser.class);
    }

    public static Adventurer getAdventurerFrom(CreateAdventurerDto adventurerDto) {
        return modelMapper.map(adventurerDto, Adventurer.class);
    }

    public static Enemy getEnemyFrom(CreateEnemyDto enemyDto) {
        return modelMapper.map(enemyDto, Enemy.class);
    }

    public static AppUserCommonDto getUserDtoFrom(AppUser appUser) {
        AppUserCommonDto userDto = modelMapper.map(appUser, AppUserCommonDto.class);
        return userDto;
    }

    public static AdventurerCommonDto getAdventurerDtoFrom(Adventurer adventurer) {
        AdventurerCommonDto adventurerDto = modelMapper.map(adventurer, AdventurerCommonDto.class);
        return adventurerDto;
    }

    public static EnemyCommonDto getEnemyDtoFrom(Enemy enemy) {
        EnemyCommonDto enemyDto = modelMapper.map(enemy, EnemyCommonDto.class);
        return enemyDto;
    }
}
