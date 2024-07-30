const month = document.getElementById("currentMonth").textContent;
const year = document.getElementById("currentYear").textContent;
const index = window.parent;

const monthDict = ["January", "February", "March", "April", "May", "June", "July",
    "August", "September", "October", "November", "December"]

// Add event listeners to each td element
const calendarEntries = document.getElementsByClassName("calendarEntry");

for (let entry of calendarEntries) {
    entry.addEventListener('click', function()
    {
        let day = entry.textContent;
        let date = year+"-"+String(monthDict.indexOf(month)+1).padStart(2, "0")+"-"+String(day).padStart(2, "0");
        index.monthCalendarCall(date);
    })
}

function highlight(examinfo) {
    if (examinfo[0].getMonth()===monthDict.indexOf(month)) {
        let colors = ["#7AB2B222", "#8aff73", "#fdff73", "#ff7373"];
        let c = calendarEntries[examinfo[0].getDate()-1].style.backgroundColor;
        c = c.substring(5,c.length-1).split(', ');
        if (c.length===4 && examinfo[3]===0) {
            c[3] = parseFloat(c[3])*2;
            calendarEntries[examinfo[0].getDate()-1].style.backgroundColor = "rgba("+c.join(', ')+")";
        } else {
            calendarEntries[examinfo[0].getDate()-1].style.backgroundColor = colors[examinfo[3]];
        }
    }
}

function removeHighlights() {
    for (let i = 0; i < calendarEntries.length; i++) {
        calendarEntries[i].style.backgroundColor = "";
    }
}

index.highlightUserDates()