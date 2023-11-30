package ru.mrsinkaaa.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.mrsinkaaa.dto.UserDTO;
import ru.mrsinkaaa.entity.User;
import ru.mrsinkaaa.repository.UserRepository;

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


    public static UserService getInstance() {
        return INSTANCE;
    }
}
