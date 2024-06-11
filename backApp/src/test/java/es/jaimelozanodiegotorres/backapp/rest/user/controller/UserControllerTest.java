package es.jaimelozanodiegotorres.backapp.rest.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.jaimelozanodiegotorres.backapp.pagination.PageResponse;
import es.jaimelozanodiegotorres.backapp.rest.user.dto.UserDto;
import es.jaimelozanodiegotorres.backapp.rest.user.dto.UserResponseDto;
import es.jaimelozanodiegotorres.backapp.rest.user.filters.UserFilters;
import es.jaimelozanodiegotorres.backapp.rest.user.mapper.UserResponseMapper;
import es.jaimelozanodiegotorres.backapp.rest.user.mapper.UserSaveMapper;
import es.jaimelozanodiegotorres.backapp.rest.user.models.User;
import es.jaimelozanodiegotorres.backapp.rest.user.service.UserServicePgSql;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN", "USER"})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServicePgSql userService;

    @InjectMocks
    private UserController userController;

    private final UUID userId = UUID.randomUUID();
    private final User user = User.builder()
            .id(userId)
            .name("John")
            .surname("Doe")
            .username("test")
            .email("test@example.com")
            .password("password")
            .build();
    UserDto userDto = UserDto.builder()
            .name("John")
            .surname("Doe")
            .username("test")
            .email("test@example.com")
            .password("password")
            .build();

    UserResponseDto responseDto = UserResponseMapper.INSTANCE.modelToDto(user);

    @Test
    void listAll_Success() throws Exception {
        List<User> userList = Arrays.asList(user);

        when(userService.listAll()).thenReturn(userList);

        MockHttpServletResponse response = mockMvc.perform(
                        get("/users")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
        verify(userService, times(1)).listAll();
    }

    @Test
    void findById_Success() throws Exception {
        when(userService.findById(any(UUID.class))).thenReturn(user);

        MockHttpServletResponse response = mockMvc.perform(
                        get("/users/" + userId)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
        verify(userService, times(1)).findById(any(UUID.class));
    }

    @Test
    void saveUser_Success() throws Exception {

        when(userService.save(any(UserDto.class))).thenReturn(user);

        MockHttpServletResponse response = mockMvc.perform(
                        post("/users/saveUser")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(userDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(201, response.getStatus());
        verify(userService, times(1)).save(any(UserDto.class));
    }

    @Test
    void updateUser_Success() throws Exception {
        when(userService.update(any(UUID.class), any(UserDto.class))).thenReturn(user);

        MockHttpServletResponse response = mockMvc.perform(
                        put("/users/updateUser/" + userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(userDto))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
        verify(userService, times(1)).update(any(UUID.class), any(UserDto.class));
    }

    @Test
    void deleteById_Success() throws Exception {
        when(userService.deleteById(any(UUID.class))).thenReturn(true);

        MockHttpServletResponse response = mockMvc.perform(
                        delete("/users/deleteUser/" + userId)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(204, response.getStatus());
        verify(userService, times(1)).deleteById(any(UUID.class));
    }

    @Test
    void details_Success() throws Exception {
        when(userService.details(any(User.class))).thenReturn(responseDto);

        MockHttpServletResponse response = mockMvc.perform(
                        get("/users/me/details")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(200, response.getStatus());
        verify(userService, times(1)).details(any());
    }
}
