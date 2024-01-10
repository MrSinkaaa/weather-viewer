package ru.mrsinkaaa.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.mrsinkaaa.dto.UserDTO;
import ru.mrsinkaaa.entity.User;
import ru.mrsinkaaa.exceptions.user.UserAlreadyExistsException;
import ru.mrsinkaaa.exceptions.user.UserInputException;
import ru.mrsinkaaa.exceptions.user.UserNotFoundException;
import ru.mrsinkaaa.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_Success() {
        Integer id = 1;
        User user = User.builder()
                .id(id)
                .login("testUser")
                .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        UserDTO result = userService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void testFindById_NotFound() {
        Integer id = 3;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(id));
    }

    @Test
    void testFindByLogin_Success() {
        String login = "tester";
        User user = User.builder()
                .id(5)
                .login(login)
                .build();

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));
        UserDTO result = userService.findByLogin(login);

        assertNotNull(result);
        assertEquals(login, result.getLogin());
    }

    @Test
    void testFindByLogin_NotFound() {
        String login = "tester";

        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByLogin(login));
    }

    @Test
    void testSave_Success() {
        UserDTO userDTO = UserDTO.builder()
                .login("testUser")
                .password("testPassword")
                .build();

        userService.save(userDTO);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testSave_AlreadyExists() {
        UserDTO userDTO = UserDTO.builder()
                .login("testUser")
                .password("testPassword")
                .build();

        userService.save(userDTO);

        assertThrows(UserAlreadyExistsException.class, () -> userService.save(userDTO));
    }

    @Test
    void testRegister_Success() {
        userService.register("testUser1", "testPassword");

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_InvalidLogin() {
        String login = "short";
        String password = "validPassword";

        assertThrows(UserInputException.class, () -> userService.register(login, password));
    }

    @Test
    void testRegister_InvalidPassword() {
        String login = "validLogin";
        String password = "short";

        assertThrows(UserInputException.class, () -> userService.register(login, password));
    }

    @Test
    void testRegister_DuplicateUser() {
        String login = "duplicateLogin";
        String password = "validPassword";

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(new User()));

        assertThrows(UserInputException.class, () -> userService.register(login, password));
    }

    @Test
    void testLogin_Success() {
        String login = "validLogin";
        String password = "validPassword";
        User mockUser = User.builder()
                .login(login)
                .password(BCrypt.hashpw(password, BCrypt.gensalt()))
                .build();

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(mockUser));

        UserDTO result = userService.login(login, password);

        assertNotNull(result);
        assertEquals(login, result.getLogin());
    }

    @Test
    void testLogin_WrongPassword() {
        String login = "validLogin";
        String password = "otherPassword";
        User mockUser = User.builder()
                .login(login)
                .password(BCrypt.hashpw(password, BCrypt.gensalt()))
                .build();

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(mockUser));

        assertThrows(UserInputException.class, () -> userService.login(login, password));
    }

    @Test
    void testLogin_UserNotFound() {
        String login = "nonExistentLogin";
        String password = "validPassword";

        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.login(login, password));
    }

}
