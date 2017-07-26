package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(m -> save(m, 0));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal == null) {
            return null;
        }
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
        }
        if (meal.getUserId() == userId)
            repository.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {

        Meal meal = get(id, userId);
        if (meal == null || meal.getUserId() != userId)
            meal = null;
        else meal = repository.remove(id);
        return meal != null;
    }

    @Override
    public Meal get(int id, int userId) {

        Meal meal = repository.get(id);
        if (meal == null || meal.getUserId() != userId)
            return null;
        return meal;

    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted((m1, m2) -> m2.getDateTime().compareTo(m1.getDateTime()))
                .collect(Collectors.toList());
    }
}

