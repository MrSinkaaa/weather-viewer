package ru.mrsinkaaa.service;

import org.mindrot.jbcrypt.BCrypt;
import ru.mrsinkaaa.config.AppConfig;
import ru.mrsinkaaa.dto.UserDTO;
import ru.mrsinkaaa.entity.User;
import ru.mrsinkaaa.exception.ErrorMessage;
import ru.mrsinkaaa.exception.user.UserAlreadyExistsException;
import ru.mrsinkaaa.exception.user.UserInputException;
import ru.mrsinkaaa.exception.user.UserNotFoundException;
import ru.mrsinkaaa.repository.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO findById(Integer id) {
        return userRepository.findById(id).map(user ->
                        UserDTO.builder()
                                .id(user.getId())
                                .login(user.getLogin())
                                .build())
                .orElseThrow(UserNotFoundException::new);
    }

    public UserDTO findByLogin(String login) {
        return userRepository.findByLogin(login).map(user ->
                        UserDTO.builder()
                                .id(user.getId())
                                .login(user.getLogin())
                                .build())
                .orElseThrow(UserNotFoundException::new);
    }

    public void save(UserDTO userDTO) {
        User user = User.builder()
                .login(userDTO.getLogin())
                .password(userDTO.getPassword())
                .build();

        if(userRepository.findByLogin(userDTO.getLogin()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        userRepository.save(user);
    }

    public void register(String login, String password) throws UserAlreadyExistsException {
        if (isValidCredentials(login, password)) {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            UserDTO userDTO = UserDTO.builder()
                    .login(login)
                    .password(hashedPassword)
                    .build();

            save(userDTO);
        }
    }

    public UserDTO login(String login, String password) {
        return userRepository.findByLogin(login)
                .map(user -> {
                    if (BCrypt.checkpw(password, user.getPassword())) {
                        return UserDTO.builder()
                                .id(user.getId())
                                .login(user.getLogin())
                                .build();
                    } else {
                        throw new UserInputException(ErrorMessage.USER_WRONG_CREDENTIALS);
                    }
                }).orElseThrow(UserNotFoundException::new);
    }

    private boolean isValidCredentials(String login, String password) {
        if (login.length() < Integer.parseInt(AppConfig.getProperty("login.length"))) {
            throw new UserInputException(ErrorMessage.USER_WRONG_LOGIN);
        } else if (password.length() < Integer.parseInt(AppConfig.getProperty("password.length"))) {
            throw new UserInputException(ErrorMessage.USER_WRONG_PASSWORD);
        }
        return true;
    }

}
