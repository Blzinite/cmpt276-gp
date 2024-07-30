const index = window.parent;

function leapYear(y){
    return (
    (y % 4 == 0 && y % 100 != 0 && y % 400 != 0) ||
    (y % 100 == 0 && y % 400 == 0)
    );
}

const daysOfMonth = [31,28,31,30,31,30,31,31,30,31,30,31];

function getDaysOfMonth(m, y){
    if (leapYear(y) && m == 1){
        return 29;
    }
    return daysOfMonth[m];
}

function getSunday(e) {
    let cSunday = new Date();
    cSunday.setFullYear(e.getFullYear());
    cSunday.setMonth(e.getMonth());
    cSunday.setDate(e.getDate() - e.getDay());
    cSunday.setHours(1);
    cSunday.setMinutes(0);
    return cSunday;
}

function getNextSunday() {
    const d = new Date(currentSunday);
    const nSunday = new Date(d.setDate(d.getDate() + 7));
    return nSunday;
}

var currentSunday = getSunday(index.getActiveDate());
var nextSunday = getNextSunday();

function showDays(){
    for (let i = 0; i < 7; i++){
        let day = currentSunday.getDate() + i;
        let daysOfcurrentMonth = getDaysOfMonth(currentSunday.getMonth(), currentSunday.getFullYear());
        if (day > daysOfcurrentMonth){
            day -= daysOfcurrentMonth;
        }
        document.getElementById("day"+ i).innerHTML = day;
    }
}

const month = ["DEC.","JAN.","FEB.","MAR.","APR.","MAY","JUN.","JUL.","AUG.","SEP.","OCT.","NOV."];

function showMonth(){
    document.getElementById("currentMonth").innerHTML = month[(currentSunday.getMonth() + 1) % 12];
}

function lastWeek(){
    const i = currentSunday.getDate() - 7;
    currentSunday.setDate(i);
    nextSunday = getNextSunday();
    update();
}

function nextWeek(){
    const i = currentSunday.getDate() + 7;
    currentSunday.setDate(i);
    nextSunday = getNextSunday();
    update();
}

const colors = ["#7AB2B2", "#8aff73", "#fdff73", "#ff7373"];

function getRandomColor() {
    let letters = '0123456789ABCDEF';
    let color = '#';
    for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

function highlight(e){
    if (e[0] > currentSunday && e[0] < nextSunday) {
        let x = document.getElementsByTagName("td");
        let time = (e[0].getHours() + e[0].getMinutes()/60  - 8) * 16;
        let length = (e[1] * 2);
        let weekOfDay = e[0].getDay();    
        let name = e[2];
        let priority = e[3];
        let color = colors[priority];
        let success = false;
        for (let i = 0; i < length; i++){
            let j = 0;
            j += i * 8;
            try {
                x[time + weekOfDay + j + 1].style.backgroundColor = color;
                x[time + weekOfDay + j + 1].style.border = "none";
            } catch (e) {
                console.log("Unmapped Time: "+e[0]);
                success = true;
            }
        }
        try {
            x[time + ((Math.ceil(length/2) - 1) * 8) + weekOfDay + 1].innerHTML = name + "(" + priority + ")";
        } catch (e) {
            console.log("Unable to place name")
            success = true;
        }
        index.unlistWarning(success);
    }
}

function examBorder(e){
    if (e[0] > currentSunday && e[0] < nextSunday) {
        let x = document.getElementsByTagName("td");
        let time = (e[0].getHours() + e[0].getMinutes()/60  - 8) * 16;
        let length = (e[1] * 2);
        let weekOfDay = e[0].getDay();
        let priority = e[3];
        let color = colors[priority];
        let success = false;
        try {
            x[time + weekOfDay + 1].style.borderTop = "5px solid";
            x[time + weekOfDay + 1].style.borderTopColor = color;
        } catch (e) {
            console.log("TOP BORDER BREACHING LIMITS")
            success = true;
        }
        try {
            x[time + weekOfDay + ((length - 1)*8) + 1].style.borderBottom = "5px solid";
            x[time + weekOfDay + ((length - 1)*8) + 1].style.borderBottomColor = color;
        } catch (e) {
            console.log("BOT BORDER BREACHING LIMITS")
            success = true;
        }
        index.unlistWarning(success);
    }
}

function removeHighlights(){
    let x = document.getElementsByTagName("td");
    for (let i = 0; i < 22*8; i += 8){
        for (let j = 1; j < 8; j++){
            let k = i + j;
            x[k].style.backgroundColor = "";
            x[k].style.border = "";
            x[k].innerHTML = "";
        }
    }
}

function showExam(){
    let dates = index.getDates();
    for (let i = 0; i < dates.length; i++) {
        console.log(dates[i]);
        const day = dates[i].date.split("-");
        const time = dates[i].start.split(":");
        const examinfo = [
            new Date(day[0],day[1]-1,day[2],time[0],time[1]),
            dates[i].duration,
            dates[i].name,
            i+1
        ];
        highlight(examinfo);
    }
}

function showExamBorder(){
    let dates = index.getDates();
    for (let i = 0; i < dates.length; i++) {
        console.log(dates[i]);
        const day = dates[i].date.split("-");
        const time = dates[i].start.split(":");
        const examinfo = [
            new Date(day[0],day[1]-1,day[2],time[0],time[1]),
            dates[i].duration,
            dates[i].name,
            i+1
        ];
        examBorder(examinfo);
    }
}

function setCurrentSunday(date) {
    currentSunday = getSunday(date);
}

function update(){
    showDays();
    showMonth();
    // removeHighlights();
    showExam();
    showExamBorder();
}

update();

// function highlight(e){
//     let x = document.getElementsByTagName("td");
//     let time = (e[0].getHours() + e[0].getMinutes()/60  - 8) * 16;
//     let length = (e[1] * 2);
//     let weekOfDay = e[0].getDay();    
//     let name = e[2];
//     let priority = e[3];
//     let color = colors[priority];
//     for (let i = 0; i < length; i++){
//         let j = 0;
//         j += i * 8;
//         if (e[0] > currentSunday && e[0] < nextSunday) {
//             x[time + weekOfDay + j + 1].style.backgroundColor = color;
//             x[time + weekOfDay + j + 1].style.border = "none";
//         }
//         else{
//             x[time + weekOfDay + j + 1].style.backgroundColor = "";
//             x[time + weekOfDay + j + 1].style.border = "";
//         }
//     }

//     if (e[0] > currentSunday && e[0] < nextSunday) {
//         x[time + ((Math.ceil(length/2) - 1) * 8) + weekOfDay + 1].innerHTML = name + "(" + priority + ")";
//     }
//     else{
//         x[time + ((Math.ceil(length/2) - 1) * 8) + weekOfDay + 1].innerHTML = "";
//     }
// }

// function removeHighlight(e){
//     let x = document.getElementsByTagName("td");
//     let time = (e[0].getHours() + e[0].getMinutes()/60  - 8) * 16;
//     let length = (e[1] * 2);
//     let weekOfDay = e[0].getDay();    
//     let name = e[2];
//     let priority = e[3];
//     let color = colors[priority];
//     for (let i = 0; i < length; i++){
//         let j = 0;
//         j += i * 8;
//         x[time + weekOfDay + j + 1].style.backgroundColor = "";
//         x[time + weekOfDay + j + 1].style.border = "";
//     }
//     x[time + ((Math.ceil(length/2) - 1) * 8) + weekOfDay + 1].innerHTML = "";
// }