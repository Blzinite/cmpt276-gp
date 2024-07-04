package com.example.SchedulEx.controllers;

import com.example.SchedulEx.models.ExamRepository;
import com.example.SchedulEx.models.User;
import com.example.SchedulEx.models.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

@Controller
public class CalendarController
{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExamRepository examRepository;

    Calendar calendar = new GregorianCalendar();
    DateFormatSymbols dateSymbols = new DateFormatSymbols();

    @GetMapping("calendarMonth")
    public String GetCalendarMonth(Model model, HttpSession session)
    {
        // User curr = (User) session.getAttribute("user");
        // model.addAttribute("currentUser", curr);

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

        System.out.println("month: " + dateSymbols.getMonths()[calendar.get(Calendar.MONTH)]);
        System.out.println("days in month: +" + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return "calendarMonth";
    }

    private int[][] GetDayMatrix()
    {
        Calendar temp = (Calendar) calendar.clone();
        temp.set(Calendar.DAY_OF_MONTH, 0);
        int firstDay = temp.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = temp.getMaximum(Calendar.DAY_OF_MONTH);
        int weeks = (int) Math.ceil((firstDay + daysInMonth) / 7f);
        int[][] days = new int[weeks][7];

        int currentDayNum = -firstDay + 1;
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
