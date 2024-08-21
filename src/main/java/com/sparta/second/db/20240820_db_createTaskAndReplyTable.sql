CREATE TABLE Task1 (
    task_id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(100),
    contents VARCHAR(200),
    reg_date TIMESTAMP,
    mod_date TIMESTAMP,
    name VARCHAR(10),
    delete_status TINYINT(1) DEFAULT 0,
    PRIMARY KEY (task_id)
);

CREATE TABLE Reply1 (
    reply_id BIGINT NOT NULL AUTO_INCREMENT,
    task_id BIGINT,
    contents VARCHAR(200),
    reg_date TIMESTAMP,
    mod_date TIMESTAMP,
    name VARCHAR(10),
    delete_status TINYINT(1) DEFAULT 0,
    PRIMARY KEY (reply_id),
    FOREIGN KEY (task_id) REFERENCES Task1(task_id) ON DELETE CASCADE
);


