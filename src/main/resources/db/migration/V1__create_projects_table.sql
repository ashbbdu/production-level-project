CREATE TABLE projects (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    status VARCHAR(20) NOT NULL,
    start_date DATETIME(6) NOT NULL,
    end_date DATETIME(6),
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,

    CONSTRAINT pk_projects PRIMARY KEY (id),
    CONSTRAINT uk_projects_name UNIQUE (name)
);