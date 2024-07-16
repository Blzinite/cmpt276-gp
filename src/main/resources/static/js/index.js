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

function fetchLocalData() {
    var testdata = '{"userExams":[{"name":"CMPT 301","date":"2024-07-12","start":"12:00","duration":3},{"name":"CMPT 301","date":"2024-07-12","start":"12:00","duration":3},{"name":"CMPT 301","date":"2024-07-12","start":"12:00","duration":3}],"OtherExams":[{"name":"CMPT 101","date":"2024-07-14","start":"12:00","duration":3},{"name":"CMPT 101","date":"2024-07-14","start":"12:00","duration":3},{"name":"CMPT 201","date":"2024-07-14","start":"12:00","duration":3},{"name":"CMPT 201","date":"2024-07-14","start":"12:00","duration":3}]}';
    localData = JSON.parse(testdata);
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