// Initial setup
// currentMonthNum is 0 indexed, not 1 indexed
var currentMonthNum = new Date().getMonth();
var currentMonthDisplay = document.getElementById("currentMonth");
var months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
var monthLengths = [31,0,31,30,31,30,31,31,30,31,30,31];

var currentYearNum = new Date().getFullYear();
var currentYearDisplay = document.getElementById("currentYear");

var tableBody = document.getElementById("tableBody");

document.addEventListener("DOMContentLoaded", () => {Initialise();});

// Fill with current month/year
function Initialise()
{
    currentMonthDisplay.textContent = months[currentMonthNum];
    currentYearDisplay.textContent = currentYearNum;
    UpdateCalendar();
}

// Go to next month
function NextMonth()
{
    currentMonthNum = (currentMonthNum + 1) % 12;
    if(currentMonthNum == 0)
    {
        currentYearNum++;
        currentYearDisplay.textContent = currentYearNum;
    }
    currentMonthDisplay.textContent = months[currentMonthNum];
    UpdateCalendar();
}

// Go to previous month
function PrevMonth()
{
    currentMonthNum = mod((currentMonthNum - 1), 12);
    if(currentMonthNum == 11)
    {
        currentYearNum--;
        currentYearDisplay.textContent = currentYearNum;
    }
    currentMonthDisplay.textContent = months[currentMonthNum];
    UpdateCalendar();
}

// Simple function to fix negative modulo behavior in PrevMonth()
function mod(n, m) 
{
    return ((n % m) + m) % m;
}

// Given month, day, year, calculate day of the week
function DayOfTheWeek(day, month, year) 
{
    // Convert months to be 1 indexed
    month++;

    // If month is jan or feb, must set month num to 13/14 respectively
    if (month < 3) 
    {
        month += 12;
        year -= 1;
    }

    // https://en.wikipedia.org/wiki/Zeller%27s_congruence#:~:text=Zeller's%20congruence%20is%20an%20algorithm,day%20and%20the%20calendar%20date.
    let q = day;
    let m = month;
    let Y = year;

    let dayOfWeek = (q + Math.floor(13 * (m + 1) / 5) + Y + Math.floor(Y / 4) - Math.floor(Y / 100) + Math.floor(Y / 400)) % 7;
    // days = ["sat", "sun", "mon", "tues", "wed", "thurs", "fri"];
    // console.log(months[currentMonthNum] + " " + days[dayOfWeek]);

    // Convert from 0 = saturday to 0 = sunday
    dayOfWeek = mod(dayOfWeek - 1, 7);

    return dayOfWeek;
}

// Update calendar numbers for the correct month and year
function UpdateCalendar()
{
    // Determine which day of the week the month starts on
    var firstDay = DayOfTheWeek(1, currentMonthNum, currentYearNum);
    var currentDayNum = 0 - firstDay;

    var daysInMonth = DaysInMonth(currentMonthNum);

    // How many weeks will need to be displayed? i.e. height of calendar in rows
    var rows = Math.ceil((daysInMonth - (7 - firstDay)) / 7) + 1;
    var tableBody = document.getElementById("monthTableBody");
    tableBody.innerHTML = "";
    for(let row = 0; row < rows; row++)
    {
        var rowObj = document.createElement('tr');
        for(let day = 0; day < 7; day++)
        {
            var dayObj = document.createElement('td');
            rowObj.appendChild(dayObj);
        }
        tableBody.appendChild(rowObj);
    }

    // Set number of each day of the month
    var days = document.querySelectorAll('tbody td');
    for (var day of days)
    {
        currentDayNum++;
        if(currentDayNum < 1 || currentDayNum > daysInMonth)
        {
            day.textContent = "";
        }
        else
        {
            day.textContent = currentDayNum;
        }
    }
}

// Is it a leap year
function LeapYear(year)
{
    if (year % 4 == 0)
    {
        if (year % 100 == 0)
        {
            if (year % 400 == 0)
            {
                return true;
            }
            return false;
        }
        return true;
    }
    return false;
}

// Days in month
function DaysInMonth(month)
{
    if(month == 1)
    {
        if (LeapYear(currentYearNum) == true)
        {
            return 29;
        }
        else
        {
            return 28;
        }
    }
    else
    {
        return monthLengths[month];
    }
}