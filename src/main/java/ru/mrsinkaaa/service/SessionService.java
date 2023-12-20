package ru.mrsinkaaa.service;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import ru.mrsinkaaa.config.AppConfig;
import ru.mrsinkaaa.dto.SessionDTO;
import ru.mrsinkaaa.dto.UserDTO;
import ru.mrsinkaaa.entity.Session;
import ru.mrsinkaaa.repository.SessionRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionService {

    private static final SessionService INSTANCE = new SessionService();
    private final SessionRepository sessionRepository = SessionRepository.getInstance();
    private final ModelMapper mapper = new ModelMapper();

    public UUID createSession(UserDTO userDTO) {
        long expiresAt = Long.parseLong(AppConfig.getProperty("session.expiresAt"));

        Session session = Session.builder()
                .id(UUID.randomUUID().toString())
                .userId(userDTO.getId())
                .expiresAt(LocalDateTime.now().plusMinutes(expiresAt))
                .build();

        sessionRepository.update(session);
        return UUID.fromString(session.getId());
    }

    public boolean checkIfSessionExists(HttpServletRequest request) {
        Cookie cookie = Arrays.stream(request.getCookies())
                .filter(cookie1 -> cookie1.getName().equals("session"))
                .findFirst()
                .orElse(null);

        if (cookie != null) {
            SessionDTO session = getSession(UUID.fromString(cookie.getValue()));
            return session != null && session.getExpiresAt().isAfter(LocalDateTime.now());
        }
        return false;
    }

    public SessionDTO getSession(UUID id) {
        Optional<Session> session = sessionRepository.findById(id.toString());

        return session.map(this::toSessionDTO)
                .orElse(null);
    }

    public void deleteSession(UUID id) {
        Optional<Session> session = sessionRepository.findById(id.toString());
        session.ifPresent(sessionRepository::delete);
    }

    private SessionDTO toSessionDTO(Session session) {
        return mapper.map(session, SessionDTO.class);
    }

    public static SessionService getInstance() {
        return INSTANCE;
    }
}