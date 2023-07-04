package project.gizka.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.gizka.appUser.dto.CreateAppUserDto;
import project.gizka.appUser.model.AppUser;
import project.gizka.service.impl.AppUserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
//@Disabled
//class AppUserControllerUnitTest {
//
//    private static final Long ID_1 = 1L;
//    private static final Long ID_2 = 2L;
//    private static final String CHAT_1 = "telegram chat id";
//    private static final String CHAT_2 = "another chat id";
//
//    private AppUser user_1;
//
//    @Mock
//    AppUserService appUserService;
//
//    @InjectMocks
//    AppUserController controller;
//
//    @BeforeEach
//    void setUp(){
//        user_1 = new AppUser(ID_1, CHAT_1, LocalDateTime.now().minusDays(2), LocalDateTime.now());
//    }
//
//    @Test
//    @DisplayName("GET /api/user/getAll возвращает ответ со статусом 200 и список пользователей")
//    void getAll_ReturnsValidResponseEntityWithUsers() {
//        //given
//        AppUser user_2 = new AppUser(ID_2, CHAT_2, LocalDateTime.now().minusDays(1), LocalDateTime.now());
//        List<AppUser> expectedUsers = Arrays.asList(user_1, user_2);
//        when(appUserService.getAll()).thenReturn(expectedUsers);
//
//        //when
//        ResponseEntity<List<AppUser>> response = controller.getAll();
//
//        //then
//        assertNotNull(response);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        List<AppUser> appUsers = response.getBody();
//
//        assertEquals(expectedUsers.size(), appUsers.size());
//        assertAppUserEquals(expectedUsers.get(0), appUsers.get(0));
//        assertAppUserEquals(expectedUsers.get(1), appUsers.get(1));
//
//        verify(appUserService).getAll();
//    }
//
//
//    @Test
//    @DisplayName("GET api/user/{id} возращает ответ со статусом 200 и пользователя")
//    void getById_ReturnsValidResponseEntityWithAppUser() {
//        //given
//        AppUser expectedUser = user_1;
//        when(appUserService.getById(ID_1)).thenReturn(Optional.of(expectedUser));
//
//        //when
//        ResponseEntity<AppUser> responseEntity = controller.getById(ID_1);
//
//        //then
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//
//        AppUser appUser = responseEntity.getBody();
//
//        assertAppUserEquals(expectedUser, appUser);
//
//        verify(appUserService).getById(ID_1);
//    }
//
//    @Test
//    @DisplayName("POST /api/user/create создает нового пользователя и возвращает его c ответом 201")
//    void create_ReturnsValidResponseEntityWithAppUser() {
//        //given
//        CreateAppUserDto userDto = new CreateAppUserDto(CHAT_1);
//        AppUser expectedUser = new AppUser(ID_1, CHAT_1, LocalDateTime.now(), LocalDateTime.now());
//        when(appUserService.create(any(AppUser.class))).thenReturn(expectedUser);
//
//        //when
//        ResponseEntity<AppUser> responseEntity = controller.create(userDto);
//
//        //then
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//
//        AppUser createdUser = responseEntity.getBody();
//
//        assertAppUserEquals(expectedUser, createdUser);
//        assertTrue(expectedUser.getRegisteredAt().isEqual(createdUser.getUpdatedAt()));
//
//        verify(appUserService).create(any(AppUser.class));
//    }
//
//    @Test
//    @DisplayName("PUT /api/user/{id}/edit изменяет пользователя и возвращает его с ответом 200")
//    void edit_ReturnsValidResponseEntityWithAppUser() {
//        //given
//        CreateAppUserDto userDto = new CreateAppUserDto(CHAT_1);
//        AppUser expectedUser = user_1;
//        when(appUserService.update(eq(ID_1), any(AppUser.class))).thenReturn(expectedUser);
//
//        //when
//        ResponseEntity<AppUser> response = controller.edit(ID_1, userDto);
//
//        //then
//        assertNotNull(response);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        AppUser updatedUser = response.getBody();
//
//        assertAppUserEquals(expectedUser, updatedUser);
//        assertFalse(expectedUser.getRegisteredAt().isEqual(updatedUser.getUpdatedAt()));
//
//        verify(appUserService).update(eq(ID_1), any(AppUser.class));
//    }
//
//    @Test
//    @DisplayName("DELETE /api/user/{id}/delete удаляет пользователя и возвращает ответ 204")
//    void deleteById_ReturnsValidResponseEntity() {
//        // given
//        doNothing().when(appUserService).delete(ID_1);
//
//        // when
//        ResponseEntity<HttpStatus> response = controller.deleteById(ID_1);
//
//        // then
//        assertNotNull(response);
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//
//        verify(appUserService).delete(ID_1);
//    }
//
//    private void assertAppUserEquals(AppUser expectedUser, AppUser actualUser) {
//        assertEquals(expectedUser.getId(), actualUser.getId());
//        assertEquals(expectedUser.getChat(), actualUser.getChat());
//        assertEquals(expectedUser.getRegisteredAt(), actualUser.getRegisteredAt());
//        assertEquals(expectedUser.getUpdatedAt(), actualUser.getUpdatedAt());
//    }
//}
