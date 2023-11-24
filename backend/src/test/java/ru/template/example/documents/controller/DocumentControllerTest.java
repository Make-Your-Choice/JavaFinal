package ru.template.example.documents.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.template.example.configuration.JacksonConfiguration;
import ru.template.example.documents.controller.dto.DocumentDto;
import ru.template.example.documents.controller.dto.IdDto;
import ru.template.example.documents.controller.dto.IdsDto;
import ru.template.example.documents.controller.dto.Status;
import ru.template.example.documents.service.DocumentServiceImpl;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({ SpringExtension.class, MockitoExtension.class })
public class DocumentControllerTest {
    private static final String BASE_PATH = "/documents";
    private final ObjectMapper mapper = new JacksonConfiguration().objectMapper();
    private MockMvc mockMvc;

    @MockBean
    private DocumentServiceImpl service;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void saveTest() throws Exception {
        DocumentDto dto = new DocumentDto(1L, "type1", "organization1", "description1", "patient1", new Date(), Status.of("NEW", "Новый"));
        when(service.save(dto)).thenReturn(dto);
        mockMvc.perform(postAction(BASE_PATH, dto)).andExpect(status().isOk());
        //Mockito.verify(service, Mockito.times(1)).save(dto);
    }

    @Test
    public void getTest() throws Exception {
        DocumentDto dto = new DocumentDto(1L, "type1", "organization1", "description1", "patient1", new Date(), Status.of("NEW", "Новый"));
        when(service.get(1L)).thenReturn(Optional.of(dto));
        mockMvc.perform(getAction(BASE_PATH, dto)).andExpect(status().isOk());
    }

    @Test
    public void sendTest() throws Exception {
        DocumentDto dto = new DocumentDto(1L, "type1", "organization1", "description1", "patient1", new Date(), Status.of("NEW", "Новый"));
        IdDto idDto = new IdDto(1L);
        when(service.send(dto)).thenReturn(dto);
        mockMvc.perform(postAction(BASE_PATH + "/send", idDto)).andExpect(status().isOk());
    }

    @Test
    public void deleteTest() throws Exception {
        Long id = 1L;
        doNothing().when(service).delete(id);
        mockMvc.perform(deleteAction(BASE_PATH + "/1", id)).andExpect(status().isOk());
    }

    @Test
    public void deleteAllTest() throws Exception {
        IdsDto idsDto = new IdsDto(Set.of(1L, 2L, 3L));
        doNothing().when(service).deleteAll(idsDto.getIds());
        mockMvc.perform(deleteAction(BASE_PATH, idsDto)).andExpect(status().isOk());
    }

    private MockHttpServletRequestBuilder postAction(String uri, Object dto) throws JsonProcessingException {
        return post(uri)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto));
    }

    private MockHttpServletRequestBuilder getAction(String uri, Object dto) throws JsonProcessingException {
        return get(uri)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto));
    }

    private MockHttpServletRequestBuilder deleteAction(String uri, Object dto) throws JsonProcessingException {
        return delete(uri)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto));
    }
}
