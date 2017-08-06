package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User ref = entityManager.getReference(User.class, userId);
        meal.setUser(ref);
        if (meal.isNew()) {
            entityManager.persist(meal);
            return meal;
        } else {
            int count = entityManager.createQuery("UPDATE Meal m set m.dateTime=:dt,m.description=:dc,m.calories=:c WHERE m.id=:id and m.user.id=:user_id")
                    .setParameter("dt", meal.getDateTime())
                    .setParameter("dc", meal.getDescription())
                    .setParameter("c", meal.getCalories())
                    .setParameter("id", meal.getId())
                    .setParameter("user_id", userId)
                    .executeUpdate();
            if (count != 0)
                return meal;
            else return null;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return entityManager.createQuery("DELETE from Meal m where m.id=:id AND m.user.id=:user_id")
                .setParameter("id", id)
                .setParameter("user_id", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> list = entityManager.createQuery("SELECT m from Meal m where m.id=:id AND m.user.id=:user_id", Meal.class)
                .setParameter("id", id)
                .setParameter("user_id", userId)
                .getResultList();
        return DataAccessUtils.singleResult(list);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return entityManager.createQuery("SELECT m FROM Meal m where  m.user.id=:user_id", Meal.class)
                .setParameter("user_id", userId)
                .getResultList().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return entityManager.createQuery("SELECT m FROM Meal m where  m.user.id=:user_id", Meal.class)
                .setParameter("user_id", userId)
                .getResultList().stream()
                .filter(m -> (DateTimeUtil.isBetween(m.getDateTime(), startDate, endDate)))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}