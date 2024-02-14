package ru.mrsinkaaa.service;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class SessionService {

    private final SessionRepository sessionRepository;
    private final ModelMapper mapper = new ModelMapper();

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public UUID createSession(UserDTO userDTO) {
        long expiresAt = Long.parseLong(AppConfig.getProperty("session.expiresAt"));

        Session session = Session.builder()
                .id(UUID.randomUUID().toString())
                .userId(userDTO.getId())
                .expiresAt(LocalDateTime.now().plusMinutes(expiresAt))
                .build();

        log.debug("Session created for user {}: {}", session.getUserId() ,session.getId());
        sessionRepository.update(session);
        return UUID.fromString(session.getId());
    }

    public Optional<SessionDTO> getSession(HttpServletRequest request) {
        Cookie cookie = Arrays.stream(request.getCookies())
                .filter(cookie1 -> cookie1.getName().equals("session"))
                .findFirst()
                .orElse(null);

        if(cookie != null) {
            Optional<Session> session = sessionRepository.findById(cookie.getValue());

            if(session.isPresent()) {
                log.debug("Session found: {}", session.get().getId());
                return getValidSession(session.get());
            }
        }
        log.debug("Session or cookie not found");

        return Optional.empty();
    }

    public void deleteAllExpiredSessions() {
        sessionRepository.deleteAllExpiredSessions();
    }

    private Optional<SessionDTO> getValidSession(Session session) {
        if(session.getExpiresAt().isAfter(LocalDateTime.now())) {
            log.debug("Session valid: {}", session.getExpiresAt());
            return Optional.ofNullable(toSessionDTO(session));
        }
        return Optional.empty();
    }

    public void deleteSession(UUID id) {
        Optional<Session> session = sessionRepository.findById(id.toString());
        session.ifPresent(sessionRepository::delete);
        log.debug("Session for user {} is deleted {}", session.get().getUserId(), session.get().getId());
    }

    private SessionDTO toSessionDTO(Session session) {
        return mapper.map(session, SessionDTO.class);
    }

}
