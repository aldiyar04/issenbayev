INSERT INTO projects (name, description, lead_dev) VALUES
    ('Text Editor',
     'Notepad style application that can open, edit, and save text documents.',
     null),
    ('RSS Feed Creator',
     'A program which can read in text from other sources and put it in RSS or Atom news format for syndication.',
     'leaddev2'),
    ('Text to HTML Generator',
     'Converts text files into web HTML files and stylizes them. Great for making online documentation of standard text documentation',
     'leaddev3');

INSERT INTO project_assignees (project_id, assignee) VALUES
    (1, 'developer2'),
    (1, 'developer3'),
    (2, 'developer4');


INSERT INTO tickets (title, project_id, submitter, type, status, priority, target_res_date) VALUES
    ('Issue 1', 1, 'manager1', 'Bug', 'New', 'High', '2022-03-12'),
    ('Issue 2', 1, 'manager1', 'Refactoring', 'New', 'Medium', '2022-03-15'),
    ('Issue 3', 2, 'manager1', 'Vulnerability', 'New', 'Critical', '2022-03-11'),
    ('Issue 4', 2, 'manager2', 'Feature Request', 'New', 'High', '2022-03-30'),
    ('Issue 5', 3, 'manager2', 'Bug', 'New', 'High', '2022-03-13'),
    ('Issue 6', 3, 'manager2', 'Bug', 'New', 'Medium', '2022-03-20');
