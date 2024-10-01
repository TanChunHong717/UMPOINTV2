CREATE TABLE cli_user (
    id bigint NOT NULL COMMENT 'ID',
    username varchar(50) NOT NULL COMMENT 'Username',
    password varchar(64) COMMENT 'Password',
    type_id bigint NOT NULL COMMENT 'User type id',
    mobile varchar(20) NOT NULL COMMENT 'Mobile',
    email varchar(64) COMMENT 'Email',
    type varchar(20) NOT NULL COMMENT 'Type name',
    space_permission tinyint NOT NULL COMMENT 'Have permission to book space',
    service_permission tinyint NOT NULL COMMENT 'Have permission to book service',
    accommodation_permission tinyint NOT NULL COMMENT 'Have permission to book accommodation',
    create_date datetime COMMENT 'Create date',
    PRIMARY KEY (id),
    UNIQUE INDEX (username),
    UNIQUE INDEX (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='User';

CREATE TABLE cli_token (
    id bigint NOT NULL COMMENT 'ID',
    user_id bigint NOT NULL COMMENT 'User ID',
    token varchar(100) NOT NULL COMMENT 'token',
    expire_date datetime COMMENT 'Expired date',
    update_date datetime COMMENT 'Update date',
    PRIMARY KEY (id),
    UNIQUE INDEX (user_id),
    UNIQUE INDEX (token)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='UserToken';