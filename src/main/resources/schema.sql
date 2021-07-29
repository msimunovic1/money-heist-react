/* MEMBER */
-- sequence --
DROP SEQUENCE IF EXISTS member_seq;

CREATE SEQUENCE member_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1;

-- table --
DROP TABLE IF EXISTS member cascade;

CREATE TABLE member(
    id INT PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    sex VARCHAR(1) NOT NULL, -- F (female), M (male) --
    email VARCHAR(100) NOT NULL UNIQUE,
    status VARCHAR(50) NOT NULL
);

/* SKILL */
-- sequence --
DROP SEQUENCE IF EXISTS skill_seq;

CREATE SEQUENCE skill_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1;

-- table --
DROP TABLE IF EXISTS skill cascade;

CREATE TABLE skill(
    id INT PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    level VARCHAR(10)
);

/* MEMBER_SKILL */
-- table --
DROP TABLE IF EXISTS member_skill;

CREATE TABLE member_skill(
    member_id INT NOT NULL,
    skill_id INT NOT NULL,
    main_skill VARCHAR(1), -- Y (yes), N (no)
    FOREIGN KEY(member_id) REFERENCES member(id),
    FOREIGN KEY(skill_id) REFERENCES skill(id)
);

/* HEIST */
-- sequence --
DROP SEQUENCE IF EXISTS heist_seq;

CREATE SEQUENCE heist_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1;

-- table --
DROP TABLE IF EXISTS heist cascade;

CREATE TABLE heist(
    id INT PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL UNIQUE,
    location VARCHAR(100) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    outcome VARCHAR(50) DEFAULT NULL
);


/* HEIST_SKILL */
-- table --
DROP TABLE IF EXISTS heist_skill;

CREATE TABLE heist_skill(
    heist_id INT NOT NULL,
    skill_id INT NOT NULL,
    members INT NOT NULL,
    FOREIGN KEY(heist_id) REFERENCES heist(id),
    FOREIGN KEY(skill_id) REFERENCES skill(id)
);


/* HEIST_MEMBER */
-- table --
DROP TABLE IF EXISTS heist_member;

CREATE TABLE heist_member(
    heist_id INT NOT NULL,
    member_id INT NOT NULL,
    FOREIGN KEY(heist_id) REFERENCES heist(id),
    FOREIGN KEY(member_id) REFERENCES member(id)
);
