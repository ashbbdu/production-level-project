
CREATE TABLE permissions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT uk_permissions_name UNIQUE (name)
);

CREATE TABLE user_permissions (
    user_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, permission_id),

    CONSTRAINT fk_user_permissions_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_permissions_permission
        FOREIGN KEY (permission_id)
        REFERENCES permissions(id)
        ON DELETE CASCADE
);

INSERT INTO permissions (name, description) VALUES
('CREATE_PROJECT', 'Create projects'),
('READ_PROJECT', 'View projects'),
('UPDATE_PROJECT', 'Update projects'),
('DELETE_PROJECT', 'Delete projects'),
('CREATE_TASK', 'Create tasks'),
('READ_TASK', 'View tasks'),
('UPDATE_TASK', 'Update tasks'),
('DELETE_TASK', 'Delete tasks'),
('CREATE_COMMENT', 'Create comments'),
('READ_COMMENT', 'View comments'),
('UPDATE_COMMENT', 'Update comments'),
('DELETE_COMMENT', 'Delete comments');
