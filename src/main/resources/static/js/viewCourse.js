const index = window.parent;
const overlap1 = document.getElementById("overlap1");
const overlap2 = document.getElementById("overlap2");
const overlap3 = document.getElementById("overlap3");
let btn1 = document.getElementById("btn1");
let btn2 = document.getElementById("btn2");
let btn3 = document.getElementById("btn3");

if(overlap1 != null){
    btn1.onclick = function() {
        return confirm("Are you sure?");
    }
}
if(overlap2 != null){
    btn2.onclick = function() {
        return confirm("Are you sure?");
    }
}
if(overlap3 != null){
    btn3.onclick = function() {
        return confirm("Are you sure?");
    }
}

let active = document.getElementsByClassName("data-name")[0].innerText;
index.getData("/course-info/"+active.replace(" ", "-"));

document.getElementById("backBtn").addEventListener("click", () => {
    index.clearData();
});