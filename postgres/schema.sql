CREATE DATABASE bugtrackerdb;
CREATE USER bugtracker WITH PASSWORD 'bugtracker';
GRANT ALL PRIVILEGES ON DATABASE bugtrackerdb TO bugtracker;
\connect bugtrackerdb

create table project(
    id bigserial not null PRIMARY KEY,
    name text not null,
    description text,
    created_at timestamp without time zone not null,
    last_updated_at timestamp without time zone
);

create table task(
    id bigserial not null PRIMARY KEY,
    project_id bigint not null references project (id),
    name text not null,
    description text,
    status text not null,
    priority int not null,
    created_at timestamp without time zone not null,
    last_updated_at timestamp without time zone
);

create index task_project_id_idx on task(project_id);

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO bugtracker;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO bugtracker;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO bugtracker;