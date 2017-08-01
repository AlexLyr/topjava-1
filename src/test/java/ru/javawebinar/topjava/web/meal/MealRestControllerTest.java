package ru.javawebinar.topjava.web.meal;

import org.junit.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by SPIDER on 31.07.2017.
 */
public class MealRestControllerTest {
    private static MealRestController controller;
    private static ConfigurableApplicationContext appCtx;
    private static MealRepository repository;

    @BeforeClass
    public static void beforeClass(){
        appCtx=new ClassPathXmlApplicationContext("spring/spring-app.xml");
        System.out.println("\n" + Arrays.toString(appCtx.getBeanDefinitionNames()) + "\n");
        controller=appCtx.getBean(MealRestController.class);
        repository=appCtx.getBean(MealRepository.class);
    }


    @AfterClass
    public static void afterClass() throws Exception {
        appCtx.close();
    }

    @Test
    public void get() throws Exception {
        System.out.println(controller.get(1));
    }

    @Test
    public void delete() throws Exception {
        controller.delete(1);
        Assert.assertEquals(null,repository.get(1,100000));
    }

    @Test
    public void getAll() throws Exception {
        System.out.println(controller.getAll());
    }

    @Test
    public void create() throws Exception {
     controller.create(new Meal(LocalDateTime.of(2016, Month.MAY, 30, 10, 0), "Лотх", 500));
    }

    @Test
    public void update() throws Exception {
        controller.update(new Meal(LocalDateTime.of(2018, Month.MAY, 30, 10, 0), "Лотх", 500),10);
    }

    @Test
    public void getBetween() throws Exception {
    }

}