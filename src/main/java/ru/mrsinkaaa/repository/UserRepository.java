package ru.mrsinkaaa.repository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.mrsinkaaa.entity.User;
import ru.mrsinkaaa.config.HibernateConfig;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class UserRepository implements CrudRepository<Integer, User> {

    private static final UserRepository INSTANCE = new UserRepository();

    @Override
    public Optional<User> findById(Integer id) {
        try(Session session = HibernateConfig.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(User.class, id));
        }
    }

    public Optional<User> findByLogin(String login) {
        try(Session session = HibernateConfig.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.createQuery("from User u where u.login = :login", User.class)
                    .setParameter("login", login)
                    .getSingleResult());
        }
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        try(Session session = HibernateConfig.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.createQuery("from User u where u.login = :login and u.password = :password", User.class)
                   .setParameter("login", login)
                   .setParameter("password", password)
                   .getSingleResult());
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).getResultList();
        }
    }

    @Override
    public void update(User entity) {
        Transaction tx = null;

        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
        } catch (RuntimeException e) {
            if(tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    @Override
    public User save(User entity) {
        Transaction tx = null;

        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
        } catch (RuntimeException e) {
            if(tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Error saving user: " + e.getMessage());
        }
        return entity;
    }

    @Override
    public boolean delete(User entity) {
        Transaction tx = null;

        try (Session session = HibernateConfig.getSessionFactory().openSession()) {

            tx = session.beginTransaction();
            session.remove(entity);
            tx.commit();

            return true;
        } catch (RuntimeException e) {
            if(tx!= null && tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Error deleting player: " + e.getMessage());
        }
        return false;
    }

    public static UserRepository getInstance() {
        return INSTANCE;
    }
}
