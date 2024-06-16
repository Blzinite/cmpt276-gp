SchedulEx
Rex Feng, Canyon Gudaitis, Daniel Wang, Nolan Nadasdy, Brian Park
Project Group 1

GitHub
https://github.com/Blzinite/cmpt276-gp
Abstract
SchedulEx is a web application designed to streamline and automate the process of exam scheduling for educational institutions. By utilizing user-friendly interfaces, SchedulEx aims to enhance the efficiency of scheduling exams, reducing the manual workload on school staff and ensuring a more organized and timely arrangement of exam timetables. The application hopes to improve current scheduling methods by allowing instructors to select and prioritize their preferred exam dates, enabling admins to create, edit, and finalize exam schedules, and notifying invigilators of their assignments. Additionally, SchedulEx integrates with Gmail API to notify via email. The target audience includes school staff, instructors, and exam invigilators, with distinct interfaces and functionalities tailored to their specific needs. The project scope encompasses multiple key features, including account management, calendar visualization, and a prioritization algorithm to balance scheduling preferences. SchedulEx is poised to significantly improve the exam scheduling process, making it more efficient and less cumbersome for all involved parties.

Legend
[ ] Current Direction

What is the name of your web application?
SchedulEx

Do we have a clear understanding of the problem?

- How is this problem solved currently (if at all)?
  - The exact details of how the problem is currently solved still needs to be clarified with the instructor. Currently, we have listed all the features needed for the app. Once the details are cleared we can make adjustments.
  - There are numerous scheduling software out there, but we are looking for one more tailored to our customer’s needs. Features should include:
    - User Permission Levels
      Ability for instructors to select multiple dates, and prioritize their choices
      Allow admins to create, edit and finalize exam
      Notify Invigilators of finalized schedule and mark dates as unavailable
    - Use of APIs
      Use [Gmail(Free)] or SMTP(Paid) APIs to send email updates to users
      ❌Use of School’s API to expedite the management process(auto-fill, integration, etc.) (SFU has this but unsure if we are doing it for SFU)❌
- How will this project make life better? Is it educational or just for entertainment?
  - The system should significantly enhance the efficiency of exam scheduling by automating the majority of the scheduling tasks, thereby streamlining the entire process. This automation will expedite the scheduling process, ensuring a more organized and timely arrangement of exams.
- Who is the target audience? Who will use your app or play your game?
  - School Staff
  - Instructors
  - Exam Invigilators

What is the scope of your project?

- Does this project have many individual features or one main feature (a possibility with many subproblems)? These are the ‘epics’ of your project.
  - Epic: Login and Account Levels, eg (only admin level accounts can create new accounts for other people or allow sign up, and admin can approve sign ups)
  - Epic: Account level designates features, instructor to post requests, admin to manage and invigilators to accept. All three should have the ability to see the schedule
    ■ Instructor Interface
      View Calendar
      Submit Preferred Times for exam
      Check Assigned Exam Time
    ■ Admin Interface
      Modify calendar(create, read, update and delete)
      Approve new accounts, create new accounts
      Review schedule (possibly edit schedule?)
      Finalize Exam schedule
    ■ Invigilator Interface
      View Calendar
      Mark days(or hours??) as unavailable
      Accept/reject exam assignments
  - Epic: Prioritize choices (Have a few very unhappy instructors by prioritizing first choices or [have everyone equally unhappy by prioritizing later choices])
    ■ R&D of priority Algorithm
  - Epic: Calendar Visual(Monthly/Weekly/?Yearly? Probably not needed)
    ■ Monthly to show all exams on which days
    ■ Weekly shows a detailed timetable
What are some sample stories/scenarios? For example, as a regular user of your site, what types of things can I do?  These are the ‘stories’ of your project.
  See ■’s above ^^
You may choose to start creating some UI mockups on paper.
  “On it” -Blz
Be honest, is the amount of work required in this proposal enough for five group members
  -Yes.
