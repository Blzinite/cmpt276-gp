
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