package ru.javawebinar.topjava.web.meal;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.of;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.UserTestData.USER_ID;


public class MealRestControllerTest extends AbstractControllerTest{
    private static final String REST_URL0="/rest/meals";
    private static final String REST_URL="/rest/meals/";

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(REST_URL+MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentMatcher(MEAL1));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL+MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print());
                MATCHER.assertListEquals(Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2),mealService.getAll(USER_ID));
    }

    @Test
    public void testGetAll() throws Exception {
       String jsonString= mockMvc.perform(get(REST_URL0))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
       List<MealWithExceed> list=JsonUtil.readValues(jsonString,MealWithExceed.class);
       LIST_MATCHER.assertListEquals(list, MealsUtil.getWithExceeded(MEALS, USER.getCaloriesPerDay()));

    }

    @Test
    public void testCreate() throws Exception {
        Meal expected=new Meal(null,of(2015, Month.MAY, 28, 14, 0), "Обед1111", 1000);
        ResultActions action = mockMvc.perform(post(REST_URL0)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected))).andExpect(status().isCreated());
        Meal returned=MATCHER.fromJsonAction(action);
        expected.setId(returned.getId());
       MATCHER.assertEquals(expected, returned);
        List<MealWithExceed> listMeals=new ArrayList<>();
        listMeals.addAll(MealsUtil.getWithExceeded(MEALS,USER.getCaloriesPerDay()));
        listMeals.add(MealsUtil.createWithExceed(expected,expected.getCalories()>USER.getCaloriesPerDay()));
        listMeals=listMeals.stream()
                .sorted(Comparator.comparing(MealWithExceed::getDateTime).reversed())
                .collect(Collectors.toList());
        System.out.println(listMeals);
        System.out.println(MealsUtil.getWithExceeded(mealService.getAll(USER_ID),USER.getCaloriesPerDay()));

        LIST_MATCHER.assertListEquals(listMeals, MealsUtil.getWithExceeded(mealService.getAll(USER_ID),USER.getCaloriesPerDay()));
    }

    @Test
    public void testUpdate() throws Exception {
        Meal updated=MealTestData.getUpdated();
        mockMvc.perform(put(REST_URL+MEAL1_ID)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.writeValue(updated)));
        MATCHER.assertEquals(updated,mealService.get(MEAL1_ID,USER_ID));

    }

    @Test
    public void testGetBetween() throws Exception {
        String url=REST_URL+"filter?startDateTime=2015-05-29T09:15:30&endDateTime=2015-05-30T17:15:30";
        LocalDateTime start=LocalDateTime.of(2015,5,29,9,15,30);
        LocalDateTime end=LocalDateTime.of(2015,5,30,17,15,30);
       String jsonString= mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andReturn()
               .getResponse()
               .getContentAsString();
        List<MealWithExceed> list=JsonUtil.readValues(jsonString,MealWithExceed.class);
        List<MealWithExceed> list2=MealsUtil.getWithExceeded(mealService.getBetweenDateTimes(start,end,USER_ID),USER.getCaloriesPerDay());
        LIST_MATCHER.assertListEquals(list2,list);
    }

}