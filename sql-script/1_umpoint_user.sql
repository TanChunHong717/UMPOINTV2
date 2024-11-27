CREATE TABLE cli_user (
    id bigint NOT NULL COMMENT 'ID',
    username varchar(50) NOT NULL COMMENT 'Username',
    real_name varchar(255) NOT NULL COMMENT 'Real name',
    password varchar(64) NOT NULL COMMENT 'Password',
    type varchar(20) NOT NULL COMMENT 'Type name',
    mobile varchar(20) NOT NULL COMMENT 'Mobile',
    email varchar(64) NOT NULL COMMENT 'Email',
    address varchar(250) NOT NULL COMMENT 'Address',
    matric_number varchar(15) NULL COMMENT 'Matric number for student/staff',
    space_permission tinyint NOT NULL COMMENT 'Have permission to book space',
    service_permission tinyint NOT NULL COMMENT 'Have permission to book service',
    accommodation_permission tinyint NOT NULL COMMENT 'Have permission to book accommodation',
    create_date datetime NOT NULL COMMENT 'Create date',
    PRIMARY KEY (id),
    UNIQUE INDEX (username),
    UNIQUE INDEX (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User';

CREATE TABLE cli_token (
    id bigint NOT NULL COMMENT 'ID',
    user_id bigint NOT NULL COMMENT 'User ID',
    token varchar(100) NOT NULL COMMENT 'token',
    expire_date datetime COMMENT 'Expired date',
    update_date datetime COMMENT 'Update date',
    PRIMARY KEY (id),
    UNIQUE INDEX (user_id),
    UNIQUE INDEX (token)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='UserToken';