package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.time.LocalTime;
import java.util.List;

/**
 * Created by SPIDER on 17.07.2017.
 */
public interface MealDao {
    void addMeal(Meal meal);
    void removeMeal(int id);
    void updateMeal(Meal meal);
    Meal getMealById(int id);
    List<MealWithExceed> mealList(LocalTime startTime, LocalTime endTime, int caloriesPerDay);
}
