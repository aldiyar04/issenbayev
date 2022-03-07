INSERT INTO users (role, email, username) VALUES
    ('Admin', 'admin@test.com', 'Admin'),
    ('Manager', 'manager1@test.com', 'Manager1'),
    ('Manager', 'manager2@test.com', 'Manager2'),
    ('Lead Dev', 'leaddev1@test.com', 'LeadDev1'),
    ('Lead Dev', 'leaddev2@test.com', 'LeadDev2'),
    ('Lead Dev', 'leaddev3@test.com', 'LeadDev3'),
    ('Developer', 'developer1@test.com', 'Developer1'),
    ('Developer', 'developer2@test.com', 'Developer2'),
    ('Developer', 'developer3@test.com', 'Developer3'),
    ('Developer', 'developer4@test.com', 'Developer4');

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
