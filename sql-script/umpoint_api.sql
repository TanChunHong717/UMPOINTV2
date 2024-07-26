CREATE TABLE tb_user (
  id bigint NOT NULL COMMENT 'ID',
  username varchar(50) NOT NULL COMMENT 'Username',
  mobile varchar(20) NOT NULL COMMENT 'Mobile',
  password varchar(64) COMMENT 'Password',
  email varchar(64) COMMENT 'Email',
  create_date datetime COMMENT 'Create date',
  PRIMARY KEY (id),
  UNIQUE INDEX (username),
  UNIQUE INDEX (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='User';

CREATE TABLE tb_token (
  id bigint NOT NULL COMMENT 'ID',
  user_id bigint NOT NULL COMMENT 'User ID',
  token varchar(100) NOT NULL COMMENT 'token',
  expire_date datetime COMMENT 'Expired date',
  update_date datetime COMMENT 'Update date',
  PRIMARY KEY (id),
  UNIQUE INDEX (user_id),
  UNIQUE INDEX (token)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='UserToken';

-- 账号：1110687471  密码：admin
INSERT INTO tb_user (id, username, mobile, password, create_date) VALUES (1067246875900000001, 'Chun Hong', '1110687471', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', now());
