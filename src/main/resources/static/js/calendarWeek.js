let exam1 = creatExam();
exam1[0].setFullYear(2024);
exam1[0].setMonth(8 - 1);
exam1[0].setDate(28);
exam1[0].setHours(10);
exam1[0].setMinutes(30);
exam1[1] = 3.5;
exam1[2] = "CMPT 276";
exam1[3] = 1;

let exam2 = creatExam();
exam2[0].setFullYear(2024);
exam2[0].setMonth(8 - 1);
exam2[0].setDate(28);
exam2[0].setHours(8);
exam2[0].setMinutes(0);
exam2[1] = 3;
exam2[2] = "CMPT 276";
exam2[3] = 2;

let exam3 = creatExam();
exam3[0].setFullYear(2024);
exam3[0].setMonth(7 - 1);
exam3[0].setDate(28);
exam3[0].setHours(16);
exam3[0].setMinutes(0);
exam3[1] = 3;
exam3[2] = "CMPT 276";
exam3[3] = 3;

let exam4 = creatExam();
exam4[0].setFullYear(2024);
exam4[0].setMonth(7 - 1);
exam4[0].setDate(26);
exam4[0].setHours(8);
exam4[0].setMinutes(0);
exam4[1] = 4;
exam4[2] = "CMPT 307";
exam4[3] = 1;

let exam5 = creatExam();
exam5[0].setFullYear(2024);
exam5[0].setMonth(7 - 1);
exam5[0].setDate(26);
exam5[0].setHours(13);
exam5[0].setMinutes(30);
exam5[1] = 4;
exam5[2] = "CMPT 110";
exam5[3] = 1;

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

var currentSunday = getSunday(exam1[0]);
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

function creatExam(){
    let examDate = new Date();
    let examLength;
    let examName;
    let examPriority;
    let exam = [examDate, examLength, examName, examPriority];
    return exam;
}

const colors = ["#7AB2B2", "#8aff73", "#fdff73", "#ff7373"];

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

function highlight(e){
    let x = document.getElementsByTagName("td");
    let time = (e[0].getHours() + e[0].getMinutes()/60  - 8) * 16;
    let length = (e[1] * 2);
    let weekOfDay = e[0].getDay();    
    let name = e[2];
    let priority = e[3];
    let color = colors[priority];
    for (let i = 0; i < length; i++){
        let j = 0;
        j += i * 8;
        x[time + weekOfDay + j + 1].style.backgroundColor = color;
        x[time + weekOfDay + j + 1].style.border = "none";
    }
    x[time + ((Math.ceil(length/2) - 1) * 8) + weekOfDay + 1].innerHTML = name + "(" + priority + ")";
}

function removeHighlights() {
    let tableData = document.getElementsByTagName("td");
    for (let i = 0; i < tableData.length; i++) {
        tableData[i].style.backgroundColor = "";
        x[time + weekOfDay + j + 1].style.border = "";
    }
}

function removeHighlight(e){
    let x = document.getElementsByTagName("td");
    let time = (e[0].getHours() + e[0].getMinutes()/60  - 8) * 16;
    let length = (e[1] * 2);
    let weekOfDay = e[0].getDay();    
    let name = e[2];
    let priority = e[3];
    let color = colors[priority];
    for (let i = 0; i < length; i++){
        let j = 0;
        j += i * 8;
        x[time + weekOfDay + j + 1].style.backgroundColor = "";
        x[time + weekOfDay + j + 1].style.border = "";
    }
    x[time + ((Math.ceil(length/2) - 1) * 8) + weekOfDay + 1].innerHTML = "";
}

// function getRandomColor() {
//     let letters = '0123456789ABCDEF';
//     let color = '#';
//     for (var i = 0; i < 6; i++) {
//         color += letters[Math.floor(Math.random() * 16)];
//     }
//     return color;
// }

function highlightExam(e){
    if (e[0] > currentSunday && e[0] < nextSunday) {
        highlight(e);
    }
    else {
        removeHighlight(e);
    }
}

function showExam(){
    highlightExam(exam1);
    highlightExam(exam2);
    highlightExam(exam3);
    highlightExam(exam4);
    highlightExam(exam5);
}

function setCurrentSunday(date) {
    currentSunday = getSunday(date);
}

function update(){
    showDays();
    showMonth();
    // showExam();
}

update();