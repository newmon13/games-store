    package dev.jlipka.gamesstore.game;

    import com.fasterxml.jackson.databind.ObjectMapper;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.http.MediaType;
    import org.springframework.test.web.servlet.MockMvc;
    import org.springframework.test.web.servlet.MvcResult;

    import java.math.BigDecimal;
    import java.time.LocalDate;
    import java.util.Base64;
    import java.util.Set;

    import static org.hamcrest.Matchers.*;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

    @SpringBootTest
    @AutoConfigureMockMvc
    class GameE2ETest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        private String adminAuthHeader;

        @BeforeEach
        void setUp() {
            adminAuthHeader = "Basic " + Base64.getEncoder().encodeToString("admin:admin".getBytes());
        }

        private GameAddRequestDto buildAddRequest() {
            return new GameAddRequestDto(
                    "Dummy Game",
                    "Dummy Developer",
                    "Dummy Description",
                    null,
                    "Dummy Publisher",
                    LocalDate.of(2020, 1, 1),
                    new BigDecimal("60.00"),
                    null,
                    null,
                    true,
                    Set.of("Rpg"),
                    Set.of("Pc"),
                    Set.of("English"),
                    Set.of("Open world"),
                    Set.of("Achievements")
            );
        }

        @Test
        void fullGameLifecycle_adminCanAddUpdateAndDeleteGame() throws Exception {

            // Step 1: admin adds a new game
            String addResponse = mockMvc.perform(post("/api/v1/internal/games")
                            .header("Authorization", adminAuthHeader)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(buildAddRequest())))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Dummy Game"))
                    .andExpect(jsonPath("$.developer").value("Dummy Developer"))
                    .andExpect(jsonPath("$.basePrice").value(60.00))
                    .andExpect(jsonPath("$.releaseStatus").value("RELEASED"))
                    .andReturn().getResponse().getContentAsString();


            // Step 2: game shows up in search
            String filterResponse = mockMvc.perform(get("/api/v1/games")
                            .param("name", "Dummy"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))))
                    .andExpect(jsonPath("$.content[0].name").value("Dummy Game"))
                    .andReturn().getResponse().getContentAsString();

            Long id = objectMapper.readTree(filterResponse).get("content").get(0).get("id").asLong();


            // Step 3: anonymous user fetches game by id
            mockMvc.perform(get("/api/v1/games/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Dummy Game"))
                    .andExpect(jsonPath("$.developer").value("Dummy Developer"));


            // Step 4: admin updates the game
            GameUpdateRequestDto updateRequest = new GameUpdateRequestDto();
            updateRequest.setName("Updated Dummy Game");
            updateRequest.setBasePrice(new BigDecimal("49.99"));
            updateRequest.setDiscountedPrice(new BigDecimal("39.99"));

            mockMvc.perform(patch("/api/v1/internal/games/{id}", id)
                            .header("Authorization", adminAuthHeader)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Updated Dummy Game"))
                    .andExpect(jsonPath("$.basePrice").value(49.99))
                    .andExpect(jsonPath("$.discountedPrice").value(39.99));

            // Step 5: anonymous user verifies the update
            mockMvc.perform(get("/api/v1/games/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Updated Dummy Game"));

            // Step 6: admin deletes the game
            mockMvc.perform(delete("/api/v1/internal/games/{id}", id)
                            .header("Authorization", adminAuthHeader))
                    .andExpect(status().isNoContent());

            // Step 7: anonymous user verifies the game no longer exists
            mockMvc.perform(get("/api/v1/games/{id}", id))
                    .andExpect(status().isNotFound());
        }

        @Test
        void anonymousUser_canBrowseAndFilterButCannotAddUpdateOrDeleteGame() throws Exception {

            // Step 1: admin adds a game
            String addResponse = mockMvc.perform(post("/api/v1/internal/games")
                            .header("Authorization", adminAuthHeader)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(buildAddRequest())))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            Long id = objectMapper.readTree(addResponse).get("id").asLong();

            // Step 2: anonymous user can browse all games
            mockMvc.perform(get("/api/v1/games"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))));

            // Step 3: anonymous user can filter games
            mockMvc.perform(get("/api/v1/games")
                            .param("name", "Dummy"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].name").value("Dummy Game"));

            // Step 4: anonymous user can fetch game details by id
            mockMvc.perform(get("/api/v1/games/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Dummy Game"));

            // Step 5: anonymous user cannot add a game
            mockMvc.perform(post("/api/v1/internal/games")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(buildAddRequest())))
                    .andExpect(status().isUnauthorized());

            // Step 6: anonymous user cannot update a game
            GameUpdateRequestDto updateRequest = new GameUpdateRequestDto();
            updateRequest.setName("Updated Dummy Game");

            mockMvc.perform(patch("/api/v1/internal/games/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isUnauthorized());

            // Step 7: anonymous user cannot delete a game
            mockMvc.perform(delete("/api/v1/internal/games/{id}", id))
                    .andExpect(status().isUnauthorized());
        }
    }