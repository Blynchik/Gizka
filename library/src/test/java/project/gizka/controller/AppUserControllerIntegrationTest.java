package project.gizka.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import project.gizka.dto.CreateAppUserDto;
import project.gizka.model.AppUser;
import project.gizka.service.impl.AppUserService;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Disabled
class AppUserControllerIntegrationTest {

    private static final Long ID_1 = 1L;
    private static final Long ID_2 = 2L;
    private static final String CHAT_1 = "telegram chat id";
    private static final String CHAT_2 = "another chat id";
    private final MockMvc mockMvc;
    private final AppUserService appUserService;
    private AppUser user_1;

    @Autowired
    AppUserControllerIntegrationTest(MockMvc mockMvc, AppUserService appUserService) {
        this.mockMvc = mockMvc;
        this.appUserService = appUserService;
    }

    @BeforeEach
    void setUp() {
        user_1 = new AppUser(ID_1, CHAT_1, LocalDateTime.now().minusDays(2), LocalDateTime.now());
    }

    @Test
    @DisplayName("GET /api/user/getAll возвращает ответ со статусом 200 и список пользователей")
    void getAll_ReturnsValidResponseEntityWithUsers() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user/getAll");
        AppUser expectedUser = user_1;
        AppUser anotherUser = new AppUser(ID_2, CHAT_2, LocalDateTime.now().minusHours(15), LocalDateTime.now());
        //when
        mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                  {
                                    "id": 1,
                                    "chat": "telegram chat id"
                                  },
                                  {
                                    "id": 2,
                                    "chat": "another chat id"
                                  }
                                ]
                                """),
                        jsonPath("$", hasSize(2)),

                        jsonPath("$[0].id", equalTo(expectedUser.getId().intValue())),
                        jsonPath("$[0].chat", equalTo(expectedUser.getChat())),
                        jsonPath("$[0].registeredAt").exists(),
                        jsonPath("$[0].updatedAt").exists(),

                        jsonPath("$[1].id", equalTo(anotherUser.getId().intValue())),
                        jsonPath("$[1].chat", equalTo(anotherUser.getChat())),
                        jsonPath("$[1].registeredAt").exists(),
                        jsonPath("$[1].updatedAt").exists()
                );

        Assertions.assertEquals(2, appUserService.getAll().size());
    }

    @Test
    @DisplayName("GET api/user/{id} возращает ответ со статусом 200 и пользователя")
    void getById_ReturnsValidResponseEntityWithUser() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user/1");
        AppUser expectedUser = user_1;

        //when
        mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                    "id": 1,
                                    "chat": "telegram chat id"
                                  }
                                """),
                        jsonPath("$.id", equalTo(expectedUser.getId().intValue())),
                        jsonPath("$.chat", equalTo(expectedUser.getChat())),
                        jsonPath("$.registeredAt").exists(),
                        jsonPath("$.updatedAt").exists());
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
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                  {
                                    "id": 3,
                                    "chat": "test chat id"
                                  }
                                """),
                        jsonPath("$.id", equalTo(expectedUser.getId().intValue())),
                        jsonPath("$.chat", equalTo(expectedUser.getChat())),
                        jsonPath("$.registeredAt").exists(),
                        jsonPath("$.updatedAt").exists());

        Assertions.assertEquals(3, appUserService.getAll().size());
    }

    @Test
    @Transactional
    @DirtiesContext
    @DisplayName("PUT /api/user/{id}/edit изменяет пользователя и возвращает его с ответом 200")
    void edit_ReturnsValidResponseEntityWithAppUser() throws Exception {
        //given
        CreateAppUserDto userDto = new CreateAppUserDto("updated test chat id");
        AppUser expectedUser = new AppUser(ID_1, "updated test chat id", user_1.getRegisteredAt(), LocalDateTime.now());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/user/1/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDto));

        //when
        mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                  {
                                    "id": 1,
                                    "chat": "updated test chat id"
                                  }
                                """),
                        jsonPath("$.id", equalTo(ID_1.intValue())),
                        jsonPath("$.chat", equalTo(expectedUser.getChat())),
                        jsonPath("$.registeredAt").exists(),
                        jsonPath("$.updatedAt").exists());
    }

    @Test
    @Transactional
    @DirtiesContext
    @DisplayName("DELETE /api/user/{id}/delete удаляет пользователя и возвращает ответ 204")
    void deleteById_ReturnsValidResponseEntity() throws Exception {
        //given
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/user/" + ID_1 + "/delete");

        //when
        mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        status().isNoContent()
                );

        Assertions.assertTrue(appUserService.getById(ID_1).isEmpty());
    }
}
