package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.time.LocalTime;
import java.util.List;

/**
 * Created by SPIDER on 17.07.2017.
 */
public interface MealDao {
    public void addMeal(Meal meal);
    public void removeMeal(int id);
    public void updateMeal(Meal meal);
    public Meal getMealById(int id);
    public List<MealWithExceed> mealList(LocalTime startTime,LocalTime endTime,int caloriesPerDay);
}
