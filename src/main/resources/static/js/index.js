// Index js needs to handle passing information between the calendar and the action panel

window.addEventListener("message", function(event){ReceieveMessage(event)});

function ReceieveMessage(event)
{
    let sourceWindow = event.source;

    if (sourceWindow.name === "calendarMonth")
    {
        // console.log("yep thats the calendar");
        // sourceWindow.Test();
    }

    console.log(event.data);
}