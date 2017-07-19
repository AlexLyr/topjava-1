package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalTime;
import java.util.List;

import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;


public class MockMealDaoImpl implements MealDao {
    private Map<Integer, Meal> map=MealsUtil.getMap();
    private static final Logger LOG= getLogger(MockMealDaoImpl.class);
    @Override
    public void addMeal(Meal meal) {
        LOG.debug("add meal in map");
        map.putIfAbsent(meal.getId(),meal);
    }

    @Override
    public void removeMeal(int id) {
        LOG.debug("remove meal from map");
       map.remove(id);
    }

    @Override
    public void updateMeal(Meal meal) {
        if(map.containsKey(meal.getId())){
            LOG.debug("update meal in map");
            map.put(meal.getId(),meal);
        }
        else
            addMeal(meal);
    }

    @Override
    public Meal getMealById(int id) {
        LOG.debug("get meal by id");
        return map.get(id);
    }

    @Override
    public List<MealWithExceed> mealList(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        LOG.debug("get MealList with exceed");
        return MealsUtil.getFilteredWithExceeded(startTime,endTime,caloriesPerDay);
    }
}
