package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;


public class MealServlet extends HttpServlet {
    private static final Logger LOG= LoggerFactory.getLogger(MealServlet.class);
    private static LocalTime startTime=LocalTime.of(0,0);
    private static LocalTime endTime=LocalTime.of(23,0);
    private static int caloriesPerDay=2000;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<MealWithExceed> list = MealsUtil.getFilteredWithExceeded(MealsUtil.getMeals(),startTime,endTime,caloriesPerDay);
        LOG.debug("redirect to meals");
        req.setAttribute("meals",list);
        RequestDispatcher rd=getServletContext().getRequestDispatcher("/meals.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
