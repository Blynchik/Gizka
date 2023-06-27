package project.gizka.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import project.gizka.dto.CreateAppUserDto;
import project.gizka.model.AppUser;
import project.gizka.service.impl.AppUserService;

import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class AppUserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AppUserService appUserService;

    @Test
    @DisplayName("GET /api/user/getAll возвращает ответ со статусом 200 и список пользователей")
    void getAll_ReturnsValidResponseEntityWithUsers() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user/getAll");
        AppUser expectedUser = new AppUser(1L, "default telegram chat id", LocalDateTime.now(), LocalDateTime.now());
        AppUser anotherUser = new AppUser(2L, "another chat id", LocalDateTime.now(), LocalDateTime.now());
        //when
        mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json("""
                                [
                                  {
                                    "id": 1,
                                    "chat": "default telegram chat id"
                                  },
                                  {
                                    "id": 2,
                                    "chat": "another chat id"
                                  }
                                ]
                                """),
                        MockMvcResultMatchers.jsonPath("$[0].registeredAt").exists(),
                        MockMvcResultMatchers.jsonPath("$[0].updatedAt").exists(),
                        MockMvcResultMatchers.jsonPath("$[1].registeredAt").exists(),
                        MockMvcResultMatchers.jsonPath("$[1].updatedAt").exists(),
                        MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)),
                        MockMvcResultMatchers.jsonPath("$[0].id", Matchers.equalTo(expectedUser.getId().intValue())),
                        MockMvcResultMatchers.jsonPath("$[0].chat", Matchers.equalTo(expectedUser.getChat())),
                        MockMvcResultMatchers.jsonPath("$[1].id", Matchers.equalTo(anotherUser.getId().intValue())),
                        MockMvcResultMatchers.jsonPath("$[1].chat", Matchers.equalTo(anotherUser.getChat())));

        Assertions.assertEquals(2, appUserService.getAll().size());

    }


    @Test
    @DisplayName("GET api/user/{id} возращает ответ со статусом 200 и пользователя")
    void getById_ReturnsValidResponseEntityWithUser() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user/1");
        AppUser expectedUser = new AppUser(1L, "default telegram chat id", LocalDateTime.now(), LocalDateTime.now());

        //when
        mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json("""
                                {
                                    "id": 1,
                                    "chat": "default telegram chat id"
                                  }
                                """),
                        MockMvcResultMatchers.jsonPath("$.registeredAt").exists(),
                        MockMvcResultMatchers.jsonPath("$.updatedAt").exists(),
                        MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedUser.getId().intValue())),
                        MockMvcResultMatchers.jsonPath("$.chat", Matchers.equalTo(expectedUser.getChat())));
    }

    @Test
    @Transactional
    @DirtiesContext
    @DisplayName("POST /api/user/create создает нового пользователя и возвращает его c ответом 201")
    void create_ReturnsValidResponseEntityWithCreateUser() throws Exception {
        //given
        CreateAppUserDto userDto = new CreateAppUserDto("test chat id");
        AppUser expectedUser = new AppUser(3L, "test chat id", LocalDateTime.now(), LocalDateTime.now());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDto));

        //when
        mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json("""
                                  {
                                    "id": 3,
                                    "chat": "test chat id"
                                  }
                                """),
                        MockMvcResultMatchers.jsonPath("$.registeredAt").exists(),
                        MockMvcResultMatchers.jsonPath("$.updatedAt").exists(),
                        MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedUser.getId().intValue())),
                        MockMvcResultMatchers.jsonPath("$.chat", Matchers.equalTo(expectedUser.getChat())));

        Assertions.assertEquals(3, appUserService.getAll().size());
    }

    @Test
    @Transactional
    @DirtiesContext
    @DisplayName("PUT /api/user/{id}/edit изменяет пользователя и возвращает его с ответом 200")
    void edit_ReturnsValidResponseEntityWithAppUser() throws Exception {
        //given
        Long id = 1L;
        CreateAppUserDto userDto = new CreateAppUserDto("updated test chat id");
        AppUser expectedUser = new AppUser(id, "updated test chat id", LocalDateTime.now(), LocalDateTime.now());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/user/1/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDto));

        //when
        mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json("""
                                  {
                                    "id": 1,
                                    "chat": "updated test chat id"
                                  }
                                """),
                        MockMvcResultMatchers.jsonPath("$.registeredAt").exists(),
                        MockMvcResultMatchers.jsonPath("$.updatedAt").exists(),
                        MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(id.intValue())),
                        MockMvcResultMatchers.jsonPath("$.chat", Matchers.equalTo(expectedUser.getChat())));
    }

    @Test
    @Transactional
    @DirtiesContext
    @DisplayName("DELETE /api/user/{id}/delete удаляет пользователя и возвращает ответ 204")
    void deleteById_ReturnsValidResponseEntity() throws Exception {
        Long id = 1L;
        //given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/user/" + id + "/delete");

        //when
        mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        MockMvcResultMatchers.status().isNoContent()
                );

        Assertions.assertTrue(appUserService.getById(id).isEmpty());
    }
}
