package com.example.SchedulEx.controllers;

import com.example.SchedulEx.models.*;
import com.example.SchedulEx.services.CourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormatSymbols;
import java.util.*;

@Controller
public class CalendarController
{
    @Autowired
    CourseService courseService;

    Calendar calendar = new GregorianCalendar();
    DateFormatSymbols dateSymbols = new DateFormatSymbols();

    @GetMapping("calendarMonth")
    public String GetCalendarMonth(Model model, HttpSession session)
    {
        // Get month, year, and day number for each table entry
        calendar.setTime(new Date());
        model.addAttribute("month", dateSymbols.getMonths()[calendar.get(Calendar.MONTH)]);
        model.addAttribute("year", calendar.get(Calendar.YEAR));
        model.addAttribute("daysInMonth", calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        model.addAttribute("dayMatrix", GetDayMatrix());

        return "calendarMonth";
    }

    @GetMapping("calendarMonth_prevMonth")
    public String PrevMonth(@RequestParam Map<String, String> params, Model model, HttpSession session)
    {
        calendar.add(Calendar.MONTH, -1);

        model.addAttribute("month", dateSymbols.getMonths()[calendar.get(Calendar.MONTH)]);
        model.addAttribute("year", calendar.get(Calendar.YEAR));
        model.addAttribute("daysInMonth", calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        model.addAttribute("dayMatrix", GetDayMatrix());

        return "calendarMonth";
    }

    @GetMapping("calendarMonth_nextMonth")
    public String UpdateCalendar(@RequestParam Map<String, String> params, Model model, HttpSession session)
    {
        calendar.add(Calendar.MONTH, 1);

        model.addAttribute("month", dateSymbols.getMonths()[calendar.get(Calendar.MONTH)]);
        model.addAttribute("year", calendar.get(Calendar.YEAR));
        model.addAttribute("daysInMonth", calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        model.addAttribute("dayMatrix", GetDayMatrix());

        User currentUser = (User) session.getAttribute("user");
        model.addAttribute("currentUser", currentUser);

        if(currentUser == null)
        {
            System.out.println("user is NOT logged in");
        }
        else if(currentUser.getAccessLevel() == AccessLevel.PROFESSOR)
        {
            System.out.println("user is logged in as instructor");
        }
        else
        {
            System.out.println("user is NOT instructor");
        }

        return "calendarMonth";
    }

    private int[][] GetDayMatrix()
    {
        Calendar temp = (Calendar) calendar.clone();
        temp.set(Calendar.DAY_OF_MONTH, 1);
        int firstDay = temp.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = temp.getActualMaximum(Calendar.DAY_OF_MONTH);
        int weeks = (int) Math.ceil((firstDay - 1 + daysInMonth) / 7f);
        int[][] days = new int[weeks][7];

        int currentDayNum = -firstDay + 2;
        for (int week = 0; week < weeks; week++)
        {
            for (int day = 0; day < 7; day++)
            {
                days[week][day] = currentDayNum++;
            }
        }

        return days;
    }
}
