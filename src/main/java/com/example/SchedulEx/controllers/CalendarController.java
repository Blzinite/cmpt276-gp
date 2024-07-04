package com.example.SchedulEx.controllers;

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
    Calendar calendar = new GregorianCalendar();
    DateFormatSymbols dateSymbols = new DateFormatSymbols();

    @GetMapping("calendarMonth")
    public String GetCalendarMonth(Model model, HttpSession session)
    {
        // User curr = (User) session.getAttribute("user");
        // model.addAttribute("currentUser", curr);
        calendar.setTime(new Date());

        model.addAttribute("month", dateSymbols.getMonths()[calendar.get(Calendar.MONTH)]);
        model.addAttribute("year", calendar.get(Calendar.YEAR));

        return "calendarMonth";
    }

    @GetMapping("calendarMonth_prevMonth")
    public String PrevMonth(@RequestParam Map<String, String> params, Model model, HttpSession session)
    {
        calendar.add(Calendar.MONTH, -1);

        model.addAttribute("month", dateSymbols.getMonths()[calendar.get(Calendar.MONTH)]);
        model.addAttribute("year", calendar.get(Calendar.YEAR));

        return "calendarMonth";
    }

    @GetMapping("calendarMonth_nextMonth")
    public String UpdateCalendar(@RequestParam Map<String, String> params, Model model, HttpSession session)
    {
        calendar.add(Calendar.MONTH, 1);

        model.addAttribute("month", dateSymbols.getMonths()[calendar.get(Calendar.MONTH)]);
        model.addAttribute("year", calendar.get(Calendar.YEAR));

        return "calendarMonth";
    }
}
