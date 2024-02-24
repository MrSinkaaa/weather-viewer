package ru.mrsinkaaa.servlet;

import lombok.extern.slf4j.Slf4j;
import ru.mrsinkaaa.repository.SessionRepository;

import java.util.TimerTask;

@Slf4j
public class SessionCleanupTask extends TimerTask {

    private final SessionRepository sessionRepository = new SessionRepository();

    @Override
    public void run() {
        sessionRepository.deleteAllExpiredSessions();

    }
}
