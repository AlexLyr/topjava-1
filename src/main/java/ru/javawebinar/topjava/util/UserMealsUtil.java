package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        System.out.println(getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(13, 0), 2000));
        System.out.println(getFilteredWithExceededOptional(mealList, LocalTime.of(7, 0), LocalTime.of(13, 0), 2000));
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dateWithExceed = new HashMap<>();
        for (UserMeal userMeal : mealList) {
            dateWithExceed.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
        }
        List<UserMealWithExceed> userMealWithExceedList = new ArrayList<>();

        for (UserMeal userMeal : mealList) {
            LocalDateTime date = userMeal.getDateTime();
            if (TimeUtil.isBetween(date.toLocalTime(), startTime, endTime)) {

                String description = userMeal.getDescription();
                Integer calories = userMeal.getCalories();
                boolean exceed = false;
                if (dateWithExceed.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay)
                    exceed = true;
                userMealWithExceedList.add(new UserMealWithExceed(date, description, calories, exceed));
            }
        }

        return userMealWithExceedList;
    }


    public static List<UserMealWithExceed> getFilteredWithExceededOptional(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dateWithExceed = mealList.stream()
                .collect(Collectors.groupingBy((u -> u.getDateTime().toLocalDate()), Collectors.summingInt(UserMeal::getCalories)));
        return mealList.stream()
                .filter(userMeal -> TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> {
                    UserMealWithExceed u;
                    LocalDateTime date = userMeal.getDateTime();
                    String description = userMeal.getDescription();
                    Integer calories = userMeal.getCalories();
                    boolean exceed = false;
                    if (dateWithExceed.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay)
                        exceed = true;
                    u = new UserMealWithExceed(date, description, calories, exceed);
                    return u;
                })
                .collect(Collectors.toList());
    }
}
