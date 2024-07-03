var pwd1 = document.getElementById("password1");
var pwd2 = document.getElementById("password2")
var letter = document.getElementById("letter");
var capital = document.getElementById("capital");
var number = document.getElementById("number");
var length = document.getElementById("length");
var match = document.getElementById("match")
var submit = document.getElementById("submit")

// When the user clicks on the password field, show the message box
pwd1.onfocus = function() {
    document.getElementById("message1").style.display = "block";
}

// When the user clicks outside of the password field, hide the message box
pwd1.onblur = function() {
    document.getElementById("message1").style.display = "none";
}

// When the user starts to type something inside the password field
pwd1.onkeyup = function() {
    // Validate lowercase letters
    var lowerCaseLetters = /[a-z]/g;
    if(pwd1.value.match(lowerCaseLetters)) {
        letter.classList.remove("invalid");
        letter.classList.add("valid");
    } else {
        letter.classList.remove("valid");
        letter.classList.add("invalid");
    }

    // Validate capital letters
    var upperCaseLetters = /[A-Z]/g;
    if(pwd1.value.match(upperCaseLetters)) {
        capital.classList.remove("invalid");
        capital.classList.add("valid");
    } else {
        capital.classList.remove("valid");
        capital.classList.add("invalid");
    }

    // Validate numbers
    var numbers = /[0-9]/g;
    if(pwd1.value.match(numbers)) {
        number.classList.remove("invalid");
        number.classList.add("valid");
    } else {
        number.classList.remove("valid");
        number.classList.add("invalid");
    }

    // Validate length
    if(pwd1.value.length >= 8) {
        length.classList.remove("invalid");
        length.classList.add("valid");
    } else {
        length.classList.remove("valid");
        length.classList.add("invalid");
    }
}

pwd2.onfocus = function() {
    document.getElementById("message2").style.display = "block";
}

pwd2.onblur = function() {
    document.getElementById("message2").style.display = "none";
}

pwd2.onkeyup = function() {
    if(pwd2.value === pwd1.value){
        match.classList.remove("invalid");
        match.classList.add("valid");
        submit.removeAttribute("disabled")
    }else {
        match.classList.remove("valid");
        match.classList.add("invalid");
        submit.disabled = "disabled";
    }
}