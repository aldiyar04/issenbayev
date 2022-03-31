INSERT INTO users (role, email, username, password) VALUES
    ('Admin', 'admin@test.com', 'Admin', 'password'),
    ('Manager', 'manager1@test.com', 'Manager1', 'password'),
    ('Manager', 'manager2@test.com', 'Manager2', 'password'),
    ('Lead Dev', 'allaissen@gmail.com', 'LeadDev1', 'password'),
    ('Lead Dev', 'leaddev2@test.com', 'LeadDev2', 'password'),
    ('Lead Dev', 'leaddev3@test.com', 'LeadDev3', 'password'),
    ('Developer', 'developer1@test.com', 'Developer1', 'password'),
    ('Developer', 'developer2@test.com', 'Developer2', 'password'),
    ('Developer', 'developer3@test.com', 'Developer3', 'password'),
    ('Developer', 'developer4@test.com', 'Developer4', 'password');

INSERT INTO projects (name, description, lead_dev_id) VALUES
    ('Text Editor',
        'Notepad style application that can open, edit, and save text documents.',
        4),
    ('RSS Feed Creator',
        'A program which can read in text from other sources and put it in RSS or Atom news format for syndication.',
        5),
    ('Text to HTML Generator',
        'Converts text files into web HTML files and stylizes them. Great for making online documentation of standard text documentation',
        6);

INSERT INTO tickets (title, project_id, submitter_id, type, status, priority, target_res_date) VALUES
    ('Issue 1', 1, 2, 'Bug', 'New', 'High', '2022-03-12'),
    ('Issue 2', 1, 2, 'Refactoring', 'New', 'Medium', '2022-03-15'),
    ('Issue 3', 2, 2, 'Vulnerability', 'New', 'Critical', '2022-03-11'),
    ('Issue 4', 2, 3, 'Feature Request', 'New', 'High', '2022-03-30'),
    ('Issue 5', 3, 3, 'Bug', 'New', 'High', '2022-03-13'),
    ('Issue 6', 3, 3, 'Bug', 'New', 'Medium', '2022-03-20');
