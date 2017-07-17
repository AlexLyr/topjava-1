package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.time.LocalTime;
import java.util.List;

/**
 * Created by SPIDER on 17.07.2017.
 */
public interface MealService {
    public void addMeal(Meal meal);
    public void removeMeal(int id);
    public void updateMeal(Meal meal);
    public Meal getMealById(int id);
    public List<MealWithExceed> mealList();
}
