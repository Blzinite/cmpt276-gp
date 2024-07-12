const month = document.getElementById("currentMonth").textContent;
const year = document.getElementById("currentYear").textContent;
const index = window.parent.frameElement;

// Add event listeners to each td element
const calendarEntries = document.getElementsByClassName("calendarEntry");

for (let entry of calendarEntries)
{
    entry.addEventListener('click', function()
    {
        let day = entry.textContent;
        console.log(month + "/" + day + "/" + year);
        index.CreateNewExam();
    })
}