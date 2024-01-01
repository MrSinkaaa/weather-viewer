package ru.mrsinkaaa.repository;

import jakarta.persistence.NoResultException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.mrsinkaaa.config.HibernateConfig;
import ru.mrsinkaaa.entity.Location;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationRepository implements CrudRepository<Integer, Location> {

    private static final LocationRepository INSTANCE = new LocationRepository();
    private final SessionFactory sessionFactory = HibernateConfig.getSessionFactory();

    @Override
    public Optional<Location> findById(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(Location.class, id));
        }
    }

    public List<Location> findByUserId(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Location l where l.userId = :id", Location.class)
                    .setParameter("id", id)
                    .getResultList();
        }
    }

    public List<Location> findByLocationName(String name) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Location l where l.name = :name", Location.class)
                    .setParameter("name", name)
                    .getResultList();
        }
    }

    public Optional<Location> findByLocationIdAndUserId(Integer id, Integer userId) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.createQuery("from Location l where l.id = :locationId and l.userId = :userId", Location.class)
                    .setParameter("locationId", id)
                    .setParameter("userId", userId)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Location> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Location", Location.class).getResultList();
        }
    }

    @Override
    public void update(Location entity) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            session.merge(entity);
            session.flush();

            tx.commit();

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error updating location: " + e.getMessage());
        }
    }

    @Override
    public void save(Location entity) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {

            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();

            }
            throw new RuntimeException("Error saving location: " + e.getMessage());
        }
    }

    @Override
    public void delete(Location entity) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {

            tx = session.beginTransaction();
            session.remove(entity);
            tx.commit();

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error deleting location: " + e.getMessage());
        }

    }

    public static LocationRepository getInstance() {
        return INSTANCE;
    }
}
