CREATE TABLE user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(10),
    email VARCHAR(100),
    delete_status TINYINT(1) DEFAULT 0,
    reg_date TIMESTAMP,
    mod_date TIMESTAMP
);

CREATE TABLE task (
    task_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    contents VARCHAR(200),
    user_id BIGINT,
    delete_status TINYINT(1) DEFAULT 0,
    reg_date TIMESTAMP,
    mod_date TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE SET NULL
);

CREATE TABLE reply (
    reply_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id BIGINT,
    contents VARCHAR(200),
    user_id BIGINT,
    delete_status TINYINT(1) DEFAULT 0,
    reg_date TIMESTAMP,
    mod_date TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES task(task_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE SET NULL
);

CREATE TABLE manager (
    manager_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id BIGINT,
    user_id BIGINT,
    delete_status TINYINT(1) DEFAULT 0,
    FOREIGN KEY (task_id) REFERENCES task(task_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE SET NULL
);



