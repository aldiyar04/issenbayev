DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS project_assignees;
DROP TABLE IF EXISTS projects;

CREATE TABLE projects (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    version BIGINT DEFAULT 1 NOT NULL,
    name VARCHAR(60) UNIQUE NOT NULL,
    description TEXT NOT NULL,
    lead_dev VARCHAR(30),
    created_on DATE DEFAULT CURRENT_DATE NOT NULL,
    updated_on DATE DEFAULT CURRENT_DATE NOT NULL
);

CREATE TABLE project_assignees (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    version BIGINT DEFAULT 1 NOT NULL,
    project_id BIGINT NOT NULL REFERENCES projects(id),
    assignee VARCHAR(30) NOT NULL
);

CREATE TABLE tickets (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    version BIGINT DEFAULT 1 NOT NULL,
    title VARCHAR(40) NOT NULL,
    description TEXT,
    project_id BIGINT NOT NULL REFERENCES projects(id),
    assignee VARCHAR(30),
    submitter VARCHAR(30),
    type VARCHAR(30) CHECK (type IN ('Bug', 'Vulnerability', 'Feature Request', 'Refactoring', 'Other')) NOT NULL,
    status VARCHAR(30) CHECK(status IN ('New', 'Assigned', 'In Progress', 'Submitted', 'Extra Work Required', 'Resolved')) NOT NULL,
    priority VARCHAR(30) CHECK(priority IN ('Critical', 'High', 'Medium', 'Low', 'None')) NOT NULL,
    target_res_date DATE,
    actual_res_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
