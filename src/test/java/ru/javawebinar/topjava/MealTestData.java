package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.BeanMatcher;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MealTestData {



    public static final List<Meal> USER_MEAL= Stream.of(
            new Meal(1, LocalDateTime.of(2017, Month.JULY,6,8,0),"Завтрак",1000),
            new Meal(2, LocalDateTime.of(2017, Month.JULY,6,13,0),"Обед",902),
            new Meal(3, LocalDateTime.of(2017, Month.JULY,6,22,0),"Ужин",100),
            new Meal(7, LocalDateTime.of(2017, Month.JULY,7,8,0),"Завтрак",600),
            new Meal(8, LocalDateTime.of(2017, Month.JULY,7,14,0),"Обед",310),
            new Meal(9, LocalDateTime.of(2017, Month.JULY,7,21,0),"Ужин",202)
    )
            .sorted(Comparator.comparing(Meal::getDateTime).reversed())
            .collect(Collectors.toList());

    public static final List<Meal> ADMIN_MEAL= Stream.of(
            new Meal(4, LocalDateTime.of(2017, Month.JULY,6,8,0),"Завтрак",500),
            new Meal(5, LocalDateTime.of(2017, Month.JULY,6,13,0),"Обед",510),
            new Meal(6, LocalDateTime.of(2017, Month.JULY,6,22,0),"Ужин",502)
            )
            .sorted(Comparator.comparing(Meal::getDateTime).reversed())
            .collect(Collectors.toList());

    public static final BeanMatcher<Meal> MATCHER = new BeanMatcher<Meal>((expected, actual) -> expected==actual||
            (Objects.equals(expected.getId(),actual.getId())
                    &&Objects.equals(expected.getCalories(),actual.getCalories())
                    &&Objects.equals(expected.getDateTime(),actual.getDateTime())
                    &&Objects.equals(expected.getDescription(),actual.getDescription())
                    )
            );

}
