package ru.mrsinkaaa.servlet;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Timer;

@WebListener
@Slf4j
public class AppContextListener implements ServletContextListener {

    private Timer timer;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        timer = new Timer(true);

        long delay = 0;
        long period = 3600 * 1000;

        timer.scheduleAtFixedRate(new SessionCleanupTask(), delay, period);
        log.info("Session cleanup task scheduled");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if(timer != null) {
            timer.cancel();
        }
        log.info("Session cleanup task cancelled");
    }
}
