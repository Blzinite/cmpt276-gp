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
        let colors = ["#7AB2B2", "#8aff73", "#fdff73", "#ff7373"]
        calendarEntries[examinfo[0].getDate()-1].style.backgroundColor = colors[examinfo[3]];
        // let ex_info = document.createElement("p");
        // ex_info.innerText = examinfo[2];
        // let ex_info = document.createElement("details");
        // let ex_summary = document.createElement("summary");
        // ex_summary.innerText = examinfo[2];
        // ex_info.appendChild(ex_summary);
        //
        // let ex_item = document.createElement("p");
        // ex_item.innerText = examinfo[0].getHours() + ":" + examinfo[0].getMinutes();
        // ex_info.appendChild(ex_item);
        //
        // ex_item = document.createElement("p");
        // ex_item.innerText = "Duration: " + examinfo[1];
        // ex_info.appendChild(ex_item);
        //
        // calendarEntries[examinfo[0].getDate()-1].appendChild(ex_info);
    }
}

function removeHighlights() {
    for (let i = 0; i < calendarEntries.length; i++) {
        calendarEntries[i].style.backgroundColor = "";
    }
}
