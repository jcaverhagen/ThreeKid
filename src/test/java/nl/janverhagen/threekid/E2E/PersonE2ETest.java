package nl.janverhagen.threekid.E2E;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.janverhagen.threekid.dto.PersonIdentityRequest;
import nl.janverhagen.threekid.dto.PersonRequest;
import nl.janverhagen.threekid.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void resetDatabase() {
        personRepository.clear();
    }

    @Test
    void postPerson_withNoMatchingFamily_returns444() throws Exception {
        // given: a person with partner and children, but children not yet stored
        PersonRequest request = new PersonRequest(
                201L,
                "Parent",
                LocalDate.of(1990, 1, 1),
                null,
                null,
                new PersonIdentityRequest(202L),
                List.of(
                        new PersonIdentityRequest(203L),
                        new PersonIdentityRequest(204L),
                        new PersonIdentityRequest(205L)
                )
        );

        // when + then
        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(444));
    }

    @Test
    void postPersons_familyMatchesPattern_returns200() throws Exception {
        // given: post partner
        PersonRequest partner = new PersonRequest(
                2L,
                "Partner",
                LocalDate.of(1990, 1, 1),
                null, null, null, List.of()
        );

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partner)))
                .andExpect(status().is(444));

        // given: post 3 children who list partner as parent
        PersonRequest child1 = new PersonRequest(
                3L,
                "Child1",
                LocalDate.now().minusYears(10),
                new PersonIdentityRequest(2L),
                null,
                null,
                List.of()
        );
        PersonRequest child2 = new PersonRequest(
                4L,
                "Child2",
                LocalDate.now().minusYears(20),
                new PersonIdentityRequest(2L),
                null,
                null,
                List.of()
        );
        PersonRequest child3 = new PersonRequest(
                5L,
                "Child3",
                LocalDate.now().minusYears(25),
                new PersonIdentityRequest(2L),
                null,
                null,
                List.of()
        );

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(child1)))
                .andExpect(status().is(444));

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(child2)))
                .andExpect(status().is(444));

        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(child3)))
                .andExpect(status().is(444));

        // given: post parent with partner + 3 children
        PersonRequest parent = new PersonRequest(
                1L,
                "Parent",
                LocalDate.of(1990, 1, 1),
                null, null,
                new PersonIdentityRequest(2L),
                List.of(
                        new PersonIdentityRequest(3L),
                        new PersonIdentityRequest(4L),
                        new PersonIdentityRequest(5L)
                )
        );

        // when + then: expect 200 and that the matching person is returned
        mockMvc.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(parent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));
    }

}
