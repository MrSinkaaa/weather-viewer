package ru.mrsinkaaa.repository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.mrsinkaaa.config.HibernateConfig;
import ru.mrsinkaaa.entity.Session;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionRepository implements CrudRepository<String, Session> {

    private static final SessionRepository INSTANCE = new SessionRepository();
    private final SessionFactory sessionFactory = HibernateConfig.getSessionFactory();

    @Override
    public Optional<Session> findById(String id) {
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(Session.class, id));
        }
    }

    @Override
    public List<Session> findAll() {
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            return session.createQuery("from Session", Session.class).getResultList();
        }
    }

    @Override
    public void update(Session entity) {
        Transaction tx = null;

        try (org.hibernate.Session session = sessionFactory.openSession()) {

            tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            throw new RuntimeException("Error updating session: " + e.getMessage());
        }
    }

    @Override
    public void save(Session entity) {
        Transaction tx = null;

        try (org.hibernate.Session session = sessionFactory.openSession()) {

            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            throw new RuntimeException("Error saving session: " + e.getMessage());
        }
    }

    @Override
    public void delete(Session entity) {
        Transaction tx = null;

        try (org.hibernate.Session session = sessionFactory.openSession()) {

            tx = session.beginTransaction();
            session.remove(entity);
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            throw new RuntimeException("Error deleting session: " + e.getMessage());
        }
    }

    public static SessionRepository getInstance() {
        return INSTANCE;
    }
}
