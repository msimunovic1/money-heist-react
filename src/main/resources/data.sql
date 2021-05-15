INSERT INTO MEMBER(id, name, sex, email, status) VALUES
    (100, 'Tokyo', 'F', 'tokyo@ag04.com', 'AVAILABLE'),
    (101, 'Berlin', 'M', 'berlin@ag04.com', 'AVAILABLE'),
    (102, 'Moscow', 'M', 'moscow@ag04.com', 'INCARCERATED');

INSERT INTO SKILL(id, name, level) VALUES
    (100, 'driving', '****'),
    (101, 'combat', '******'),
    (102, 'combat', '********');

INSERT INTO MEMBER_SKILL(member_id, skill_id, main_skill) VALUES
    (100, 100, 'N'),
    (100, 101, 'Y'),
    (101, 102, 'Y'),
    (102, 100, 'Y');


INSERT INTO HEIST(id, name, location, start_time, end_time, status, outcome) VALUES
    (100, 'European Central Bank - ECB', 'Frankfurt, Germany', '2021-01-01T20:00:00.000Z', '2021-02-01T20:00:00.000Z', 'FINISHED', 'SUCCEEDED');

INSERT INTO HEIST_SKILL(heist_id, skill_id, members) VALUES
    (100, 100, 3),
    (100, 101, 1);

INSERT INTO HEIST_MEMBER(heist_id, member_id) VALUES
    (100, 100),
    (100, 102);