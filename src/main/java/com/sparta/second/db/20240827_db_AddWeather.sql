CREATE TABLE task (
    task_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100),
    contents VARCHAR(200),
    user_id BIGINT,
    weather VARCHAR(100),
    delete_status TINYINT(1) DEFAULT 0,
    reg_date TIMESTAMP,
    mod_date TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE SET NULL
);