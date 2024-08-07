
// Add informational validity messages to "add course" inputs
document.getElementById("department").addEventListener("input", function()
{
    if (this.validity.patternMismatch)
    {
        this.setCustomValidity("Please enter a valid department code (2 to 4 uppercase letters).");
    }
    else
    {
        this.setCustomValidity("");
    }

    this.reportValidity();
})

// Add information to add course confirmation message
document.getElementById("newCourseButton").addEventListener("click", ConfirmAddCourse);

function ConfirmAddCourse(event)
{
    let department = document.getElementById("department").value;
    let number = document.getElementById("courseNumber").value;
    let enrollment = document.getElementById("enrollment").value;

    let confirmationMessage = "Are you sure you want to add a new course with the following details?" +
        "\nCourse Name: " + department + " " + number + "\nStudents Enrolled: " + enrollment;

    if(!confirm(confirmationMessage))
    {
        event.preventDefault();
        event.stopPropagation();
        return false;
    }
}

// Fetch all the details element.
const details = document.querySelectorAll("details");
const index = window.parent;

for (let dur of document.getElementsByTagName("input")) {
    dur.addEventListener('input', function() {
        updateLocalData();
    })
}

// Add the onclick listeners.
details.forEach((detail) => {
    if (detail.className === "list-element") {
        detail.addEventListener("toggle", () => {
            if (detail.open) {
                closeUnrelatedDetails(detail);
                let active = detail.getElementsByTagName("summary")[0].id;
                index.getData("/course-info/"+active.replace(" ", "-"));
            } else {
                index.clearData();
            }
        });
    } else if (detail.className === "date-dropdown") {
        detail.addEventListener("toggle", () => {
            if (detail.open) {
                let active = detail.getElementsByTagName("summary")[0];
                active = active.getElementsByTagName("input")[0];
                index.switchToWeek(active.value);
                console.log(active.value)
            } else {
                index.switchToMonth();
            }
        });
    }
});

// Close all the details that are not targetDetail.
function closeUnrelatedDetails(targetDetail) {
  details.forEach((detail) => {
    if (detail !== targetDetail) {
      detail.open = false;
    }
  });
}

function updateDates(dates) {
    for (let i = 0; i < dates.length; i++) {
        document.getElementById(dates[i].name+"-date"+i).value = dates[i].date;
        document.getElementById(dates[i].name+"-time"+i).value = dates[i].start;
    }
}

function updateLocalData() {
    let dates = index.localData.userExams;
    for (let i = 0; i < dates.length; i++) {
        dates[i].date = document.getElementById(dates[i].name+"-date"+i).value;
        let start = document.getElementById(dates[i].name+"-time"+i);
        console.log(start)
        if (start != null) {
            dates[i].start = start.value;
        }
    }
    showUnsave(true, index.localData.userExamName)
    index.highlightUserDates();
}

function showUnlist(show, id) {
    let wrn = document.getElementById(id+"-unlist")
    if (show) {
        wrn.style.display = "block";
    } else {
        wrn.style.display = "none"
    }
}

function showUnsave(show, id) {
    let wrn = document.getElementById(id+"-unsave")
    if (show) {
        wrn.style.display = "block";
    } else {
        wrn.style.display = "none"
    }
}
index.removeHighlights()