package ru.mrsinkaaa.repository;

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
        try(Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Location.class, id));
        }
    }

    public List<Location> findByUserId(Integer id) {
        try(Session session = sessionFactory.openSession()) {
            return session.createQuery("from Location l where l.userId = :id", Location.class)
                  .setParameter("id", id)
                  .getResultList();
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
            tx.commit();

        } catch (RuntimeException e) {
            if(tx!= null && tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Error updating location: " + e.getMessage());
        }
    }

    @Override
    public Location save(Location entity) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {

            tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();

        } catch (RuntimeException e) {
            if(tx!= null && tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Error saving location: " + e.getMessage());
        }
        return entity;
    }

    @Override
    public boolean delete(Location entity) {
        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {

            tx = session.beginTransaction();
            session.remove(entity);
            tx.commit();

            return true;
        } catch (RuntimeException e) {
            if(tx!= null && tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Error deleting location: " + e.getMessage());
        }
        return false;
    }

    public static LocationRepository getInstance() {
        return INSTANCE;
    }
}
