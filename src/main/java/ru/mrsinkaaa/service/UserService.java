package ru.mrsinkaaa.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import ru.mrsinkaaa.dto.UserDTO;
import ru.mrsinkaaa.entity.User;
import ru.mrsinkaaa.repository.UserRepository;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserService {

    private static final UserService INSTANCE = new UserService();
    private final UserRepository userRepository = UserRepository.getInstance();

    public UserDTO findById(Integer id) {
        return userRepository.findById(id).map(user ->
                        UserDTO.builder()
                                .id(user.getId())
                                .login(user.getLogin())
                                .build())
                .orElseThrow();
    }

    public UserDTO findByLogin(String login) {
        return userRepository.findByLogin(login).map(user ->
                        UserDTO.builder()
                                .id(user.getId())
                                .login(user.getLogin())
                                .build())
                .orElseThrow();
    }

    public void save(UserDTO userDTO) {
        User user = User.builder()
                .login(userDTO.getLogin())
                .password(userDTO.getPassword())
                .build();

        userRepository.save(user);
    }

    public void register(String login, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        UserDTO userDTO = UserDTO.builder()
                .login(login)
                .password(hashedPassword)
                .build();

        save(userDTO);
    }

    public Optional<UserDTO> login(String login, String password) {
        return userRepository.findByLogin(login)
                .map(user -> {
                     if (BCrypt.checkpw(password, user.getPassword())) {
                         return UserDTO.builder()
                               .id(user.getId())
                               .login(user.getLogin())
                               .build();
                     } else {
                         return null;
                     }
                });
    }

    public static UserService getInstance() {
        return INSTANCE;
    }
}
