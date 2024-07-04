// Initial setup
// currentMonthNum is 0 indexed, not 1 indexed
var currentMonthNum = new Date().getMonth();
var currentMonthDisplay = document.getElementById("currentMonth");
var currentYearNum = new Date().getFullYear();
var currentYearDisplay = document.getElementById("currentYear");

var months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
var monthLengths = [31,28,31,30,31,30,31,31,30,31,30,31];

document.addEventListener("DOMContentLoaded", () => {Initialise();});

// Initialise Calendar
// Function: Called by DOMContentLoaded listener to set the initial values when the page is loaded
function Initialise()
{
    currentMonthDisplay.textContent = months[currentMonthNum];
    currentYearDisplay.textContent = currentYearNum;
    UpdateCalendar();
}

// Next Month
// Function: Called by the next month button in calendarMonth.html (id = nextMonth).
//           Determines next month, then updates the calendar to display it.
function NextMonth()
{
    // Gets the number of the next month (0 indexed)
    currentMonthNum = (currentMonthNum + 1) % 12;

    // If the new current month has rolled over to january of the next year, update the year as well.
    if(currentMonthNum == 0)
    {
        currentYearNum++;
        currentYearDisplay.textContent = currentYearNum;
    }

    // Update the month display, and the calendar grid numbers/size.
    currentMonthDisplay.textContent = months[currentMonthNum];
    UpdateCalendar();
}

// Previous Month
// Function: Called by the previous month button in calendarMonth.html (id = prevMonth).
//           Determines previous month, then updates the calendar to display it.
function PrevMonth()
{
    // Gets the number of the previous month (0 indexed)
    currentMonthNum = mod((currentMonthNum - 1), 12);

    // If the new current month has rolled over to december of the previous year, update the year as well.
    if(currentMonthNum == 11)
    {
        currentYearNum--;
        currentYearDisplay.textContent = currentYearNum;
    }

    // Update the month display, and the calendar grid numbers/size.
    currentMonthDisplay.textContent = months[currentMonthNum];
    UpdateCalendar();
}

// Custom Modulo
// Function: Modulo operator (%) has undesirable behavior when the left operand is negative. This avoids that.
function mod(n, m) 
{
    return ((n % m) + m) % m;
}

// Get day of the week
// Function: Given the day number, the month number (0 indexed), and the year number, return the day name as a number where
/*           0 = Sunday
             1 = Monday
             ...
             5 = Friday
             6 = Saturday
 */
function DayOfTheWeek(day, month, year) 
{
    // Convert month input to be 1 indexed
    // Necessary because NextMonth and PrevMonth modulo behavior is more simple when month is 0 indexed.
    // Additionally, easier to index months array when month number is stored with 0 index.
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

    // The algorithm returns 0 = saturday
    // Convert from 0 = saturday to 0 = sunday
    dayOfWeek = mod(dayOfWeek - 1, 7);

    return dayOfWeek;
}

// Update the calendar display
// Function: Determines which day of the week the current month starts on and how long the month is.
//           Displays the correct number of weeks and correct number on each day.
function UpdateCalendar()
{
    // Determine which day of the week the month starts on
    const firstDay = DayOfTheWeek(1, currentMonthNum, currentYearNum);
    let currentDayNum = 0 - firstDay;

    // Determine how many days are in the month
    let daysInMonth = monthLengths[currentMonthNum];
    if (currentMonthNum === 1 && LeapYear(currentYearNum))
    {
        daysInMonth++;
    }

    // How many weeks will need to be displayed? i.e. height of calendar in rows
    // Create a new row for each
    const rows = Math.ceil((daysInMonth - (7 - firstDay)) / 7) + 1;
    const tableBody = document.getElementById("monthTableBody");
    tableBody.innerHTML = "";
    // Create row
    for(let row = 0; row < rows; row++)
    {
        const rowObj = document.createElement('tr');
        // Create day
        for(let day = 0; day < 7; day++)
        {
            const dayObj = document.createElement('td');
            rowObj.appendChild(dayObj);
        }
        tableBody.appendChild(rowObj);
    }

    // For each day, set the day number
    const days = document.querySelectorAll('tbody td');
    for (let day of days)
    {
        currentDayNum++;
        if(currentDayNum < 1 || currentDayNum > daysInMonth)
        {
            day.textContent = "";
        }
        else
        {
            day.textContent = currentDayNum.toString();
            day.setAttribute("td:text", "${currentUser.getEmail()}");
            day.addEventListener("click", function() {CreateExamRequest(day.textContent)});
        }
    }
}

// Is it a leap year?
// Function: Returns true if input is a leap year, false otherwise.
function LeapYear(year)
{
    if (year % 4 === 0)
    {
        if (year % 100 === 0)
        {
            if (year % 400 === 0)
            {
                return true;
            }
            return false;
        }
        return true;
    }
    return false;
}

// Create Exam Request Form
// Function: Send date info to parent window when clicked
function CreateExamRequest(dayNumInput)
{
    // let test = [[${currentUser.getEmail()}]];
    // console.log(test);
    window.parent.postMessage(dayNumInput + " " + currentMonthNum + " " + currentYearNum);
}

function Test()
{
    console.log("test");
}