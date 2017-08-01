package ru.javawebinar.topjava.service;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
})
@RunWith(SpringRunner.class)
public class MealServiceTest {
    private  ConfigurableApplicationContext appCtx=new ClassPathXmlApplicationContext("classpath:spring/spring-app.xml");

    @Autowired
    private  MealService service;


    static {
        SLF4JBridgeHandler.install();
    }


    private DbPopulator dbPopulator=appCtx.getBean(DbPopulator.class);

    @Before
    public void setUp() throws Exception{
        dbPopulator.execute();
    }
 @After
 public void tearDown(){
     appCtx.close();
 }

    @Test
    public void testGet() throws Exception {
        int id=1;
        Meal meal=service.get(id,USER_ID);
        Meal meal2=null;
        Iterator<Meal> iter=USER_MEAL.iterator();
        service.delete(id,USER_ID);
        while(iter.hasNext()){
            Meal mealCycle=iter.next();
            if(mealCycle.getId()==id)
                meal2=mealCycle;
        }
        MealTestData.MATCHER.assertEquals(meal,meal2);
    }

    @Test
    public void testDelete() throws Exception {
        int id=1;
        Iterator<Meal> iter=USER_MEAL.iterator();
        service.delete(id,USER_ID);
        while(iter.hasNext()){
            if(iter.next().getId()==id)
                iter.remove();
        }
        MealTestData.MATCHER.assertCollectionEquals(USER_MEAL,service.getAll(USER_ID));
    }

    @Test
    public void testGetBetweenDateTimes() throws Exception {
        LocalDateTime start=LocalDateTime.of(2017, Month.JULY,6,8,0);
        LocalDateTime end=LocalDateTime.of(2017, Month.JULY,6,15,0);
        Collection<Meal> meals=service.getBetweenDateTimes(start,end,USER_ID);
        MealTestData.MATCHER.assertCollectionEquals(USER_MEAL.stream()
        .filter(m->DateTimeUtil.isBetween(m.getDateTime(),start,end))
                .collect(Collectors.toList()), meals);
    }

    @Test
    public void testGetAll() throws Exception {
        Collection<Meal> meals=service.getAll(USER_ID);
        MealTestData.MATCHER.assertCollectionEquals(meals,USER_MEAL);
    }

    @Test
    public void testUpdate() throws Exception {
        Meal meal=new Meal(1, LocalDateTime.of(2017, Month.JULY,6,9,0),"ПолуЗавтрак",1000);
        Meal created=service.update(meal, USER_ID);

        MealTestData.MATCHER.assertEquals(meal,service.get(meal.getId(),USER_ID));

    }

    @Test
    public void testSave() throws Exception {
        Meal meal=new Meal(null, LocalDateTime.of(2017, Month.JULY,6,9,0),"ПолуЗавтрак",1000);
        Meal created=service.save(meal, USER_ID);
        meal.setId(created.getId());
        List<Meal> list=new ArrayList<>(USER_MEAL);
        list.add(meal);
        list=list.stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
        MealTestData.MATCHER.assertCollectionEquals(list,service.getAll(USER_ID));
    }
    @Test(expected = NotFoundException.class)
    public void testNotFoundDelete() throws Exception {
        service.delete(1,ADMIN_ID);
    }
    @Test(expected =NotFoundException.class)
    public void testNotFoundUpdate() throws Exception {
     int id=1;
        Meal meal=new Meal(id, LocalDateTime.of(2017, Month.JULY,6,9,0),"ПолуЗавтрак",1000);
        service.update(meal,ADMIN_ID);
    }
    @Test(expected = NotFoundException.class)
    public void testNotFoundGet() throws Exception {
        service.get(1,ADMIN_ID);
    }
}