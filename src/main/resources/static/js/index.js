const calendarFrame = window.frames["calendar"];
const actionPanelFrame = window.frames["actionPanel"];

var localData;

async function getData(url) {
  try {
    const response = await fetch(url);
    if (!response.ok) {
      throw new Error(`Response status: ${response.status}`);
    }

    localData = await response.json();
    highlightUserDates();
  } catch (error) {
    console.error(error.message);
  }
}

function clearData() {
    localData = null;
    switchToMonth();
    calendarFrame.removeHighlights();
}

function addTime(time) {
    if (localData != null) {
        if (localData.userExams.includes(time)) {
            localData.userExams.splice(localData.userExams.indexOf(time));
        } else {
            if (localData.userExams.length >= 3) {
               localData.userExams.pop();
            }
            localData.userExams.unshift(time)
        }
    }
}

function monthCalendarCall(date) {
    let dates = [];
    for (let i = 0; i < localData.userExams.length; i++) {
        dates.push(localData.userExams[i].date);
    }
    if (dates.includes(date)) {
        localData.userExams.splice(dates.indexOf(date),1);
    } else {
        if (localData.userExams.length >= 3) {
            let removed = localData.userExams.pop();
            const tmpObj = {
                "name" : removed.name,
                "date" : date,
                "start" : "12:00",
                "duration" : removed.duration
            }
            console.log(tmpObj);
            localData.userExams.unshift(tmpObj);
        } else {
            const tmpObj = {
                "name" : localData.userExamName,
                "date" : date,
                "start" : "12:00",
                "duration" : localData.userExamDur
            }
            localData.userExams.unshift(tmpObj);
        }
    }
    console.log(dates);
    highlightUserDates();
    actionPanelFrame.updateDates(localData.userExams);
}

// Create New Exam
// Function: Called by a calendar when clicked by
function CreateNewExam(dates) {
    actionPanelFrame.setActiveDetail(dates);
}

function highlightUserDates() {
    calendarFrame.removeHighlights();
    for (let i = 0; i < localData.otherExams.length; i++) {
        console.log(localData.otherExams[i]);
        const day = localData.otherExams[i].date.split("-");
        const time = localData.otherExams[i].start.split(":");
        const examinfo = [
            new Date(day[0],day[1]-1,day[2],time[0],time[1]),
            localData.otherExams[i].duration,
            localData.otherExams[i].name,
            0
        ];
        calendarFrame.highlight(examinfo);
    }
    for (let i = 0; i < localData.userExams.length; i++) {
        console.log(localData.userExams[i]);
        const day = localData.userExams[i].date.split("-");
        const time = localData.userExams[i].start.split(":");
        const examinfo = [
            new Date(day[0],day[1]-1,day[2],time[0],time[1]),
            localData.userExams[i].duration,
            localData.userExams[i].name,
            i+1
        ];
        calendarFrame.highlight(examinfo);
    }
}

function getDates() {
    return localData.userExams;
}

var activeDate;

function switchToWeek(date) {
    document.getElementById("calendar-panel").src = "/calendarWeek.html";
    activeDate = date;
}

function switchToMonth() {
    document.getElementById("calendar-panel").src = "/calendarMonth";
}

function getActiveDate() {
    return new Date(activeDate);
}

function unlistWarning(active) {
    actionPanelFrame.showUnlist(active, localData.userExamName);
}