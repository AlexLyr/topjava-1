package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import ru.javawebinar.topjava.model.Meal;

import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.MealServiceImpl;



import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


public class MealServlet extends HttpServlet {
    private static final Logger LOG= LoggerFactory.getLogger(MealServlet.class);
    private MealService mealService;

    @Override
    public void init() throws ServletException {
        super.init();
        mealService=new MealServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String forward;
        String action=req.getParameter("action");

        if (action.equalsIgnoreCase("delete")){
            mealService.removeMeal(Integer.parseInt(req.getParameter("mealId")));
            forward="/meals.jsp";
            req.setAttribute("meals",mealService.mealList());
        }
        else if(action.equalsIgnoreCase("edit")){
            forward="/meal.jsp";
            Meal meal=mealService.getMealById(Integer.parseInt(req.getParameter("mealId")));
            req.setAttribute("meal",meal);
        }
        else if(action.equalsIgnoreCase("mealList")){
            LOG.debug("redirect to meals");
            forward = "/meals.jsp";
            req.setAttribute("meals",mealService.mealList());
        }
        else {
            forward="/meal.jsp";
        }
        RequestDispatcher rd=getServletContext().getRequestDispatcher(forward);
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        LocalDateTime dateTime=LocalDateTime.now();
        String description=req.getParameter("description");
        int calories=Integer.parseInt(req.getParameter("calories"));
        Meal meal=new Meal(dateTime,description,calories);
        String mealId=req.getParameter("mealId");
        if(mealId==null||mealId.isEmpty()){
            mealService.addMeal(meal);
        }
        else {
            meal.setId(Integer.parseInt(mealId));
            mealService.updateMeal(meal);
        }
        RequestDispatcher rd=req.getRequestDispatcher("/meals.jsp");
        req.setAttribute("meals",mealService.mealList());
        rd.forward(req,resp);
    }
}
