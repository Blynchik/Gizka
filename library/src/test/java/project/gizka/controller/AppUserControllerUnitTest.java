package project.gizka.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.gizka.dto.CreateAppUserDto;
import project.gizka.model.AppUser;
import project.gizka.service.impl.AppUserService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class AppUserControllerUnitTest {

    @Mock
    AppUserService appUserService;

    @InjectMocks
    AppUserController controller;

    @Test
    @DisplayName("GET /api/user/getAll возвращает ответ со статусом 200 и список пользователей")
    void getAll_ReturnsValidResponseEntityWithUsers() {
        //given
        List<AppUser> expectedUsers = Arrays.asList(
                new AppUser(1L, "telegram chat id 1", LocalDateTime.now(), LocalDateTime.now()),
                new AppUser(2L, "telegram chat id 2", LocalDateTime.now(), LocalDateTime.now())
        );
        Mockito.when(appUserService.getAll()).thenReturn(expectedUsers);

        //when
        ResponseEntity<List<AppUser>> response = controller.getAll();

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        List<AppUser> appUsers = response.getBody();
        Assertions.assertEquals(expectedUsers.size(), appUsers.size());
    }


    @Test
    @DisplayName("GET api/user/{id} возращает ответ со статусом 200 и пользователя")
    void getById_ReturnsValidResponseEntityWithAppUser() {
        //given
        Long id = 1L;
        AppUser expectedAppUser = new AppUser(id, "test chat id", LocalDateTime.now(), LocalDateTime.now());
        Mockito.when(appUserService.getById(id)).thenReturn(Optional.of(expectedAppUser));

        //when
        ResponseEntity<AppUser> responseEntity = controller.getById(id);

        //then
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        AppUser appUser = responseEntity.getBody();
        Assertions.assertEquals(expectedAppUser.getId(), appUser.getId());
        Assertions.assertEquals(expectedAppUser.getChat(), appUser.getChat());
    }

    @Test
    @DisplayName("POST /api/user/create создает нового пользователя и возвращает его c ответом 201")
    void create_ReturnsValidResponseEntity() {
        //given
        CreateAppUserDto userDto = new CreateAppUserDto("test chat id");
        AppUser appUser = new AppUser(1L, "test chat id", LocalDateTime.now(), LocalDateTime.now());
        Mockito.when(appUserService.create(Mockito.any(AppUser.class))).thenReturn(appUser);

        //when
        ResponseEntity<AppUser> responseEntity = controller.create(userDto);

        //then
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        AppUser createdUser = responseEntity.getBody();
        Assertions.assertEquals(appUser.getId(), createdUser.getId());
        Assertions.assertEquals(userDto.getChat(), createdUser.getChat());

        Mockito.verify(appUserService).create(Mockito.any(AppUser.class));
    }

    @Test
    @DisplayName("PUT /api/user/{id}/edit изменяет пользователя и возвращает его с ответом 200")
    void edit_ReturnsValidResponseEntity() {
        //given
        Long id = 1L;
        CreateAppUserDto userDto = new CreateAppUserDto("new telegram chat id");
        AppUser expectedUser = new AppUser(id, "new telegram chat id", LocalDateTime.now().minusDays(7), LocalDateTime.now());
        Mockito.when(appUserService.update(Mockito.eq(id), Mockito.any(AppUser.class))).thenReturn(expectedUser);

        //when
        ResponseEntity<AppUser> response = controller.edit(id, userDto);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        AppUser updatedUser = response.getBody();
        Assertions.assertEquals(expectedUser.getId(), updatedUser.getId());
        Assertions.assertEquals(expectedUser.getChat(), updatedUser.getChat());
        Assertions.assertTrue(Duration.between(updatedUser.getRegisteredAt(), expectedUser.getRegisteredAt()).getSeconds() < 1);
        Assertions.assertTrue(Duration.between(updatedUser.getUpdatedAt(), LocalDateTime.now()).getSeconds() < 1);

        Mockito.verify(appUserService).update(Mockito.eq(id), Mockito.any(AppUser.class));
    }

    @Test
    @DisplayName("DELETE /api/user/{id}/delete удаляет пользователя и возвращает ответ 204")
    public void delete_ReturnsValidResponseEntity() {
        // given
        Long id = 1L;
        Mockito.doNothing().when(appUserService).delete(id);

        // when
        ResponseEntity<HttpStatus> response = controller.deleteById(id);

        // then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        Mockito.verify(appUserService).delete(id);
    }
}
