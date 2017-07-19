package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MockMealDaoImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.time.LocalTime;
import java.util.List;

/**
 * Created by SPIDER on 17.07.2017.
 */
public class MealServiceImpl implements MealService {
     private MealDao mealDao;

    public MealServiceImpl() {
        this.mealDao = new MockMealDaoImpl();
    }

    @Override
    public void addMeal(Meal meal) {
      this.mealDao.addMeal(meal);
    }

    @Override
    public void removeMeal(int id) {
        this.mealDao.removeMeal(id);
    }

    @Override
    public void updateMeal(Meal meal) {
         this.mealDao.updateMeal(meal);
    }

    @Override
    public Meal getMealById(int id) {
        return this.mealDao.getMealById(id);
    }

    @Override
    public List<MealWithExceed> mealList() {
        return this.mealDao.mealList(LocalTime.MIN,LocalTime.MAX,2000);
    }
}
