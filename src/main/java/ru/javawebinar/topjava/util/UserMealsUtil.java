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
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
       System.out.println(getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000));
        System.out.println(getFilteredWithExceededOptional(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000));
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate,Integer> map=new HashMap<>();
        List<LocalDate> datesWithExceed=new ArrayList<>();
        List<UserMealWithExceed> userMealWithExceedList=new ArrayList<>();

        for(UserMeal userMeal:mealList){
            map.merge(userMeal.getDateTime().toLocalDate(),userMeal.getCalories(),(Integer::sum));
        }

        for(Map.Entry<LocalDate,Integer> pair:map.entrySet()){
            LocalDate date=pair.getKey();
            Integer calories=pair.getValue();
            if(calories>caloriesPerDay)
                datesWithExceed.add(date);
        }

        for(UserMeal userMeal:mealList){
            if(TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(),startTime,endTime)) {
                if (datesWithExceed.contains(userMeal.getDateTime().toLocalDate())) {
                    userMealWithExceedList.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), true));
                }
                else
                    userMealWithExceedList.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), false));
            }
        }

        return userMealWithExceedList;
    }


    public static List<UserMealWithExceed>  getFilteredWithExceededOptional(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay){
     Map<LocalDate,List<UserMeal>> gropingByDateMap=mealList.stream()
             .collect(Collectors.groupingBy(u->u.getDateTime().toLocalDate()));
     Map<LocalDate,Integer>dateWithExceed=new HashMap<>();
        List<UserMeal> listWithExceed=new ArrayList<>();

     for(UserMeal userMeal:mealList){
         dateWithExceed.merge(userMeal.getDateTime().toLocalDate(),userMeal.getCalories(),Integer::sum);
         if(dateWithExceed.get(userMeal.getDateTime().toLocalDate())>caloriesPerDay){
           listWithExceed.addAll(gropingByDateMap.get(userMeal.getDateTime().toLocalDate()));
         }
     }

        return mealList.stream()
                 .filter(userMeal -> TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(),startTime,endTime))
                 .map(userMeal -> {
                     UserMealWithExceed u;
                     if(listWithExceed.contains(userMeal))
                    u= new UserMealWithExceed(userMeal.getDateTime(),userMeal.getDescription(),userMeal.getCalories(),true);
                     else  u=new UserMealWithExceed(userMeal.getDateTime(),userMeal.getDescription(),userMeal.getCalories(),false);
                return u; })
                 .collect(Collectors.toList());
    }
}
