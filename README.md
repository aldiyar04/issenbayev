**Project idea**

Bug tracker is an application that lets developers track issues in software projects they build.


**Functionality**

**Basic functionality**

- Authentication – registration and login
- Authorization – different user roles and permissions
- Login as a demo user – anyone can demo the project without registering. It is possible to log in under any user role.

**Note:** Changes made by a demo user don’t get saved to the database to prevent corruption of existing data

**Search and pagination**

- General search for any type of info
- Every list (e.g., a list of users or tickets) can be:
  - Searched (filtered by a search query)
  - Paginated
    - Number of items shown on one page can be changed

**Notifications**

- Notify developers when a ticket is assigned to them
- Notify project leads when any ticket of their project becomes overdue

**Another type of JMS messages**

The bug tracker can consume messages sent by web-projects that it manages. Users of those web-projects can send feedback (e.g., feature requests or bugs) via messages.

`		`**How it will be implemented**

Along with the description of an issue, the web-project sending it must send its name, which should be unique. The bug tracker then identifies the corresponding project within itself and creates a ticket, with the “submitter id” column being null (to signify that the ticket is created by a customer, not by an admin, manager, project lead, or developer)


**Prerequisites To Understand User Permissions Completely**

**Ticket history** – the history of *updates* of a ticket. Needed to avoid losing important ticket data when users *update* tickets.

**Details on CRUD of different tables:**

- **History** is special in that it is *created* automatically by the application, not by any users. It cannot be *updated*. A history can be *deleted* by Admin only when he *deletes* the project or ticket containing the history (i.e., Admin cannot directly *delete* ticket history).
- **Comments** and **attachments** of tickets cannot be *updated* or *deleted* to avoid losing important data.
- **Ticket** properties: 
  - “submitter” property is *created* automatically and cannot be *updated*
  - “assignee” property can be *updated* only by Admin or Project lead

**User Roles**

- **Admin** – a most powerful authority that basically can do almost anything (see what he cannot do above)
- **Manager** – manages projects, assigns project leads to them
- **Project lead** – the leading programmer of a project that assigns tickets to developers and can work on tickets himself

**Note**: Project lead is called Lead dev in the DB tables for shortness

- **Developer** – a programmer that works on tickets (fixes bugs, adds new features, refactors specific parts of a project)

**Note:** project leads and developers can work only on one project at a time.

**User Permissions**

- **Admin:** *CRUD* any table, except for *updating* passwords of other users and directly *deleting* ticket histories (see the above section “Details on CRUD of different tables”)
- **Manager:** *create, read, update* projects; *create, read* tickets for all projects

**Note:** 

Manager can *update* his tickets and *read* their attachments and history only while the type of a ticket is “new”. When the type becomes “open”, he can only *read* his tickets and their attachments.

- **Project lead:** *read* and *update* the project he is assigned to; interact with everything inside the project: 
  - *Create, read, update* its tickets

**Details:**

He can *update* the status of a ticket of [whom] only as follows:

- Himself: “new” -> “open” -> “in progress” -> “resolved”
- Developer: “submitted” -> “under revision” -> “resolved” or “cancelled”
  - *Create* and *read* comments, attachments
  - *Read* the history of a ticket
- **Developer:** everything Project lead can do, except for:
  - He cannot *update* the project
  - He can *update* ticket status only as follows:

“new” -> “open” -> “in progress” -> “submitted”; “cancelled” -> “in progress”

- He can *create* tickets, assigning them only to nobody or himself

`	`**Note:** everyone can update their own passwords.
