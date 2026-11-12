
-- using if not exists so that we can run safely on every launch
CREATE TABLE IF NOT EXISTS COURSES{
    id      TEXT    PRIMARY KEY,
    code    TEXT    NOT NULL,
    name    TEXT    NOT NULL
};
