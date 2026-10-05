CREATE TABLE comments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    description VARCHAR(2000) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    task_id BIGINT NOT NULL,

    CONSTRAINT pk_comments PRIMARY KEY (id),

    CONSTRAINT fk_comments_task
        FOREIGN KEY (task_id)
        REFERENCES tasks(id)
);