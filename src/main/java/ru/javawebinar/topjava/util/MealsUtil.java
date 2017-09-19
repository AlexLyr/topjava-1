package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class MealsUtil {

    private MealsUtil() {
    }

    public static List<MealWithExceed> getWithExceeded(Collection<Meal> meals, int caloriesPerDay) {
        return getFilteredWithExceeded(meals, LocalTime.MIN, LocalTime.MAX, caloriesPerDay);
    }

    public static List<MealWithExceed> getFilteredWithExceeded(Collection<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getTime(), startTime, endTime))
                .map(meal -> createWithExceed(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<MealWithExceed> getFilteredWithExceededByCycle(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        final Map<LocalDate, Integer> caloriesSumByDate = new HashMap<>();
        meals.forEach(meal -> caloriesSumByDate.merge(meal.getDate(), meal.getCalories(), Integer::sum));

        final List<MealWithExceed> mealsWithExceeded = new ArrayList<>();
        meals.forEach(meal -> {
            if (DateTimeUtil.isBetween(meal.getTime(), startTime, endTime)) {
                mealsWithExceeded.add(createWithExceed(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay));
            }
        });
        return mealsWithExceeded;
    }

    /*
     *  Advanced solution in one return (listDayMeals can be inline).
     *  Streams are not multiplied, so complexity is still O(N)
     *  Execution time is increased as for every day we create 2 additional streams
     */
    public static List<MealWithExceed> getFilteredWithExceededInOneReturn(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Collection<List<Meal>> listDayMeals = meals.stream()
                .collect(Collectors.groupingBy(Meal::getDate)).values();

        return listDayMeals
                .stream().flatMap(dayMeals -> {
                    boolean exceed = dayMeals.stream().mapToInt(Meal::getCalories).sum() > caloriesPerDay;
                    return dayMeals.stream().filter(meal ->
                            DateTimeUtil.isBetween(meal.getTime(), startTime, endTime))
                            .map(meal -> createWithExceed(meal, exceed));
                })
                .collect(Collectors.toList());
    }

    public static MealWithExceed createWithExceed(Meal meal, boolean exceeded) {
        return new MealWithExceed(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceeded);
    }

    public static Meal createFromExceed(MealWithExceed mealWithExceed) {
        return new Meal(mealWithExceed.getId(), mealWithExceed.getDateTime(), mealWithExceed.getDescription(), mealWithExceed.getCalories());
    }

    public static Meal updateFromTo(Meal meal, MealWithExceed mealWithExceed) {
        meal.setDateTime(mealWithExceed.getDateTime());
        meal.setDescription(mealWithExceed.getDescription());
        meal.setCalories(mealWithExceed.getCalories());
        return meal;
    }
}