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

function getSunday() {
    const today = new Date();
    const i = today.getDate() - today.getDay();
    const sunday = new Date(today.setDate(i));
    return sunday;
}

let currentSunday = getSunday();
let currentWeek = 0;

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
    currentWeek--;
    update();
}

function nextWeek(){
    const i = currentSunday.getDate() + 7;
    currentSunday.setDate(i);
    currentWeek++;
    update();
}


let examDate = new Date();
examDate.setFullYear(2024);
examDate.setMonth(7 - 1);
examDate.setDate(4);
examDate.setHours(15);
examDate.setMinutes(30);
let examLength = 3;
let examName = "CMPT 276";
let examPriority = 2;

let exam = [examDate, examLength, examName, examPriority];

function highlight(e){
    let x = document.getElementsByTagName("td");
    let color = getRandomColor();
    let time = (e[0].getHours() + e[0].getMinutes()/60  - 8) * 16;
    let length = e[1];
    let weekOfDay = e[0].getDay();
    for (let i = 0; i < length; i++){
        let j = 0;
        j += i * 8;
        x[time + weekOfDay + j + 1].style.backgroundColor = color;
        x[time + weekOfDay + j + 1].style.border = "none";
    }
    let name = e[2];
    let priority = e[3];
    x[time + ((Math.ceil(length/2) - 1) * 8) + weekOfDay + 1].innerHTML = name + "(" + priority + ")";
}

function getRandomColor() {
    let letters = '0123456789ABCDEF';
    let color = '#';
    for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

function update(){
    showDays();
    showMonth();
    highlight(exam);
}

update();