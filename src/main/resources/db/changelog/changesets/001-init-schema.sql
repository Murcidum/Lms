CREATE TABLE teacher (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL
);

CREATE TABLE groups (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE course (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    teacher_id UUID NOT NULL REFERENCES teacher(id)
);

CREATE TABLE student (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    group_id UUID NOT NULL REFERENCES groups(id)
);

CREATE TABLE schedule (
    id UUID PRIMARY KEY,
    group_id UUID NOT NULL REFERENCES groups(id),
    teacher_id UUID NOT NULL REFERENCES teacher(id),
    course_id UUID NOT NULL REFERENCES course(id),
    date_time TIMESTAMP NOT NULL,
    end_date_time TIMESTAMP NOT NULL
);
