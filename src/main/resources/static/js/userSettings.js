let pwd = document.getElementById("password")
let pwdmatch = document.getElementById("passmatch")
let mlabel = document.getElementById("matchlabel")
let plabel = document.getElementById("passlabel")
let update = document.getElementById("updateBtn")
let change = document.getElementById("passchange")
let letter = document.getElementById("letter");
let capital = document.getElementById("capital");
let number = document.getElementById("number");
let length = document.getElementById("length");
let match = document.getElementById("match")

change.onclick = function() {
    if(pwd.type === "password" && pwd.value === "" && pwdmatch.value === ""){
        plabel.hidden = "hidden"
        pwd.type = "hidden"
    }else{
        plabel.removeAttribute("hidden")
        pwd.type = "password"
    }
}

pwd.onfocus = function() {
    document.getElementById("message1").style.display = "block"
    mlabel.removeAttribute("hidden")
    pwdmatch.type = "password"
}

pwd.onblur = function() {
    if(pwd.value === "" && pwdmatch.value === ""){
        mlabel.hidden = "hidden"
        pwdmatch.type = "hidden"
        document.getElementById("message1").style.display = "none"
    }
}

pwd.onkeyup = function() {
    if(pwd.value !== pwdmatch.value){
        update.disabled = "disabled"
    }
    // Validate lowercase letters
    var lowerCaseLetters = /[a-z]/g;
    if(pwd.value.match(lowerCaseLetters)) {
        letter.classList.remove("invalid");
        letter.classList.add("valid");
    } else {
        letter.classList.remove("valid");
        letter.classList.add("invalid");
    }

    // Validate capital letters
    var upperCaseLetters = /[A-Z]/g;
    if(pwd.value.match(upperCaseLetters)) {
        capital.classList.remove("invalid");
        capital.classList.add("valid");
    } else {
        capital.classList.remove("valid");
        capital.classList.add("invalid");
    }

    // Validate numbers
    var numbers = /[0-9]/g;
    if(pwd.value.match(numbers)) {
        number.classList.remove("invalid");
        number.classList.add("valid");
    } else {
        number.classList.remove("valid");
        number.classList.add("invalid");
    }

    // Validate length
    if(pwd.value.length >= 8) {
        length.classList.remove("invalid");
        length.classList.add("valid");
    } else {
        length.classList.remove("valid");
        length.classList.add("invalid");
    }
}

pwdmatch.onfocus = function() {
    document.getElementById("message1").style.display = "block"
}

pwdmatch.onblur = function() {
    if(pwd.value === "" && pwdmatch.value === ""){
        document.getElementById("message1").style.display = "none"
    }
}

pwdmatch.onkeyup = function() {
    if(pwd.value !== pwdmatch.value){
        update.disabled = "disabled"
        match.classList.remove("valid")
        match.classList.add("invalid")
    }
    if(pwd.value === pwdmatch.value){
        update.removeAttribute("disabled")
        match.classList.remove("invalid");
        match.classList.add("valid");
    }
    if(pwdmatch.value === "" && pwd.value === ""){
        mlabel.hidden = "hidden"
        pwdmatch.type = "hidden"
    }
}