package com.example.lms;

import com.example.lms.dao.TeacherRepository;
import com.example.lms.dto.TeacherCreateDto;
import com.example.lms.dto.TeacherDto;
import com.example.lms.exception.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeacherControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TeacherRepository teacherRepository;

    @BeforeEach
    void setUp() {
        teacherRepository.deleteAll();
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    // POST /api/v1/teachers

    @Test
    void create_validDto_returns201AndCreatedTeacher() {
        ResponseEntity<TeacherDto> response = restTemplate.postForEntity(
                url("/api/v1/teachers"), new TeacherCreateDto("Иван", "Иванов"), TeacherDto.class);

        TeacherDto body = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(body.id()).isNotNull();
        assertThat(body.name()).isEqualTo("Иван");
        assertThat(body.surname()).isEqualTo("Иванов");
    }

    @Test
    void create_blankName_returns400() {
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                url("/api/v1/teachers"), new TeacherCreateDto("", "Иванов"), ErrorResponse.class);

        ErrorResponse body = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(body.getMessage()).contains("name");
    }

    @Test
    void create_blankSurname_returns400() {
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                url("/api/v1/teachers"), new TeacherCreateDto("Иван", ""), ErrorResponse.class);

        ErrorResponse body = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(body.getMessage()).contains("surname");
    }

    // GET /api/v1/teachers/{id}

    @Test
    void getById_existingId_returns200AndTeacher() {
        ResponseEntity<TeacherDto> createResponse = restTemplate.postForEntity(
                url("/api/v1/teachers"), new TeacherCreateDto("Мария", "Петрова"), TeacherDto.class);
        UUID createdId = createResponse.getBody().id();

        ResponseEntity<TeacherDto> response = restTemplate.getForEntity(
                url("/api/v1/teachers/{id}"), TeacherDto.class, createdId);

        TeacherDto body = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body.name()).isEqualTo("Мария");
        assertThat(body.surname()).isEqualTo("Петрова");
    }

    @Test
    void getById_nonExistentId_returns404() {
        ResponseEntity<Void> response = restTemplate.getForEntity(
                url("/api/v1/teachers/{id}"), Void.class, UUID.randomUUID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // GET /api/v1/teachers

    @Test
    void getAll_noTeachers_returnsEmptyPage() {
        ResponseEntity<String> response = restTemplate.getForEntity(url("/api/v1/teachers"), String.class);

        String body = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).contains("\"totalElements\":0");
    }

    @Test
    void getAll_twoTeachers_returnsBothInPage() {
        restTemplate.postForEntity(url("/api/v1/teachers"), new TeacherCreateDto("Алексей", "Смирнов"), TeacherDto.class);
        restTemplate.postForEntity(url("/api/v1/teachers"), new TeacherCreateDto("Елена", "Кузнецова"), TeacherDto.class);

        ResponseEntity<String> response = restTemplate.getForEntity(url("/api/v1/teachers"), String.class);

        String body = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).contains("\"totalElements\":2");
    }

    // PUT /api/v1/teachers/{id}

    @Test
    void update_existingId_returns200WithUpdatedData() {
        ResponseEntity<TeacherDto> createResponse = restTemplate.postForEntity(
                url("/api/v1/teachers"), new TeacherCreateDto("Старое", "Имя"), TeacherDto.class);
        UUID createdId = createResponse.getBody().id();

        ResponseEntity<TeacherDto> response = restTemplate.exchange(
                url("/api/v1/teachers/{id}"), HttpMethod.PUT,
                new HttpEntity<>(new TeacherCreateDto("Новое", "Имя")),
                TeacherDto.class, createdId);

        TeacherDto body = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body.id()).isEqualTo(createdId);
        assertThat(body.name()).isEqualTo("Новое");
    }

    @Test
    void update_nonExistentId_returns404() {
        ResponseEntity<Void> response = restTemplate.exchange(
                url("/api/v1/teachers/{id}"), HttpMethod.PUT,
                new HttpEntity<>(new TeacherCreateDto("Имя", "Фамилия")),
                Void.class, UUID.randomUUID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void update_blankName_returns400() {
        ResponseEntity<TeacherDto> createResponse = restTemplate.postForEntity(
                url("/api/v1/teachers"), new TeacherCreateDto("Иван", "Иванов"), TeacherDto.class);
        UUID createdId = createResponse.getBody().id();

        ResponseEntity<Void> response = restTemplate.exchange(
                url("/api/v1/teachers/{id}"), HttpMethod.PUT,
                new HttpEntity<>(new TeacherCreateDto("", "Иванов")),
                Void.class, createdId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // DELETE /api/v1/teachers/{id}

    @Test
    void delete_existingId_returns204AndRemovedFromDb() {
        ResponseEntity<TeacherDto> createResponse = restTemplate.postForEntity(
                url("/api/v1/teachers"), new TeacherCreateDto("Удаляемый", "Учитель"), TeacherDto.class);
        UUID createdId = createResponse.getBody().id();

        restTemplate.delete(url("/api/v1/teachers/{id}"), createdId);

        assertThat(teacherRepository.findById(createdId)).isEmpty();
    }

    @Test
    void delete_nonExistentId_returns404() {
        ResponseEntity<Void> response = restTemplate.exchange(
                url("/api/v1/teachers/{id}"), HttpMethod.DELETE,
                HttpEntity.EMPTY, Void.class, UUID.randomUUID());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
