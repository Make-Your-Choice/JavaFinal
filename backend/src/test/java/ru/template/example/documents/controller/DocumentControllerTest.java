package ru.template.example.documents.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.template.example.configuration.JacksonConfiguration;
import ru.template.example.documents.controller.dto.DocumentDto;
import ru.template.example.documents.controller.dto.IdDto;
import ru.template.example.documents.controller.dto.IdsDto;
import ru.template.example.documents.controller.dto.Status;
import ru.template.example.documents.service.DocumentServiceImpl;
import ru.template.example.documents.service.MessageOutService;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Класс, тестирующий контроллер документов
 */
@WebMvcTest(DocumentController.class)
public class DocumentControllerTest {
    /**
     * Путь по умолчанию
     */
    private static final String BASE_PATH = "/documents";
    /**
     * Маппер
     */
    private final ObjectMapper mapper = new JacksonConfiguration().objectMapper();
    /**
     * Имитация контроллера
     */
    @Autowired
    private MockMvc mockMvc;
    /**
     * Сервис по работе с документами
     */
    @MockBean
    private DocumentServiceImpl service;
    /**
     * Сервис по работе с исходящими сообщениями
     */
    @MockBean
    private MessageOutService messageOutService;

    /**
     * Тест на сохранение документа
     *
     * @throws Exception
     */
    @Test
    public void saveTest() throws Exception {
        DocumentDto dto = new DocumentDto(1L, "type1", "organization1", "description1", "patient1", new Date(), Status.of("NEW", "Новый"));
        when(service.save(dto)).thenReturn(dto);
        mockMvc.perform(postAction(BASE_PATH, dto)).andExpect(status().isOk());
    }

    /**
     * Тест на получение документа
     *
     * @throws Exception
     */
    @Test
    public void getTest() throws Exception {
        DocumentDto dto = new DocumentDto(1L, "type1", "organization1", "description1", "patient1", new Date(), Status.of("NEW", "Новый"));
        when(service.get(1L)).thenReturn(Optional.of(dto));
        mockMvc.perform(getAction(BASE_PATH, dto)).andExpect(status().isOk());
    }

    /**
     * Тест на отправку документа
     *
     * @throws Exception
     */
    @Test
    public void sendTest() throws Exception {
        DocumentDto dto = new DocumentDto(1L, "type1", "organization1", "description1", "patient1", new Date(), Status.of("NEW", "Новый"));
        IdDto idDto = new IdDto(1L);
        when(service.send(idDto.getId())).thenReturn(dto);
        mockMvc.perform(postAction(BASE_PATH + "/send", idDto)).andExpect(status().isOk());
    }

    /**
     * Тест на удаление документа
     *
     * @throws Exception
     */
    @Test
    public void deleteTest() throws Exception {
        Long id = 1L;
        doNothing().when(service).delete(id);
        mockMvc.perform(deleteAction(BASE_PATH + "/1", id)).andExpect(status().isOk());
    }

    /**
     * Тест на удаление нескольких документов
     *
     * @throws Exception
     */
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
