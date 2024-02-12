package ru.mrsinkaaa.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import ru.mrsinkaaa.dto.UserDTO;
import ru.mrsinkaaa.entity.User;
import ru.mrsinkaaa.exceptions.user.UserAlreadyExistsException;
import ru.mrsinkaaa.exceptions.user.UserInputException;
import ru.mrsinkaaa.exceptions.user.UserNotFoundException;
import ru.mrsinkaaa.repository.UserRepository;
import ru.mrsinkaaa.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    @BeforeEach
    void setUp() {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.userService = new UserService(userRepository);
    }

    @Test
    void testFindById_Success() {
        User user = User.builder()
                .id(1)
                .build();

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));
        UserDTO result = userService.findById(user.getId());

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
    }

    @Test
    void testFindById_NotFound() {
        Mockito.doReturn(Optional.empty()).when(userRepository).findById(2);

        assertThrows(UserNotFoundException.class, () -> userService.findById(2));
    }

    @Test
    void testFindByLogin_Success() {
        String login = "tester";
        User user = User.builder()
                .login(login)
                .build();

        Mockito.doReturn(Optional.of(user)).when(userRepository).findByLogin(login);
        UserDTO result = userService.findByLogin(login);

        assertNotNull(result);
        assertEquals(user.getLogin(), result.getLogin());
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

        Mockito.verify(userRepository, times(1)).save(any(User.class));
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

        assertThrows(UserAlreadyExistsException.class, () -> userService.register(login, password));
    }

    @Test
    void testLogin_Success() {
        String login = "validLogin";
        String password = "validPassword";
        User mockUser = User.builder()
                .login(login)
                .password(BCrypt.hashpw(password, BCrypt.gensalt()))
                .build();

        Mockito.doReturn(Optional.of(mockUser)).when(userRepository).findByLogin(login);

        UserDTO result = userService.login(login, password);

        assertNotNull(result);
        assertEquals(login, result.getLogin());
    }

    @Test
    void testLogin_WrongPassword() {
        String login = "validLogin";
        String validPassword = "validPassword";
        String invalidPassword = "otherPassword";

        User mockUser = User.builder()
                .login(login)
                .password(BCrypt.hashpw(validPassword, BCrypt.gensalt()))
                .build();

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(mockUser));

        assertThrows(UserInputException.class, () -> userService.login(login, invalidPassword));
    }

    @Test
    void testLogin_UserNotFound() {
        String login = "nonExistentLogin";
        String password = "validPassword";

        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.login(login, password));
    }

}
