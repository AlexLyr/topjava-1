package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.BeanMatcher;
import ru.javawebinar.topjava.model.Meal;
import static ru.javawebinar.topjava.model.BaseEntity.MEAL_START_SEQ;

public class MealTestData {

    public static final BeanMatcher<Meal> MATCHER = new BeanMatcher<>();

}
