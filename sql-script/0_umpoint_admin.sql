CREATE TABLE sys_user (
  id bigint NOT NULL COMMENT 'ID',
  username varchar(50) NOT NULL COMMENT 'Username',
  password varchar(100) COMMENT 'Password',
  real_name varchar(50) COMMENT 'Name',
  head_url varchar(200) COMMENT 'Profile picture',
  gender tinyint unsigned COMMENT 'Gender 0:Male 1:Female',
  email varchar(100) COMMENT 'Email',
  mobile varchar(100) COMMENT 'Mobile',
  dept_id bigint COMMENT 'Department ID',
  super_admin tinyint unsigned COMMENT 'Super admin 0:Not 1:Yes',
  status tinyint COMMENT 'Status 0:Suspend 1:Normal',
  creator bigint COMMENT 'Creator',
  create_date datetime COMMENT 'Create date',
  updater bigint COMMENT 'Updater',
  update_date datetime COMMENT 'Update date',
  primary key (id),
  unique key uk_username (username),
  key idx_create_date (create_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System user';

CREATE TABLE sys_dept (
  id bigint NOT NULL COMMENT 'ID',
  pid bigint COMMENT 'Parent ID',
  pids varchar(500) COMMENT 'All parent ID，separate by comma',
  name varchar(50) COMMENT 'Department name',
  sort int unsigned COMMENT 'Sort',
  creator bigint COMMENT 'Creator',
  create_date datetime COMMENT 'Create date',
  updater bigint COMMENT 'Updater',
  update_date datetime COMMENT 'Update date',
  primary key (id),
  key idx_pid (pid),
  key idx_sort (sort)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Department';

CREATE TABLE sys_role (
  id                   bigint NOT NULL COMMENT 'ID',
  name                 varchar(50) COMMENT 'Name',
  remark               varchar(100) COMMENT 'Remark',
  dept_id              bigint COMMENT 'Department ID',
  creator              bigint COMMENT 'Creator',
  create_date          datetime COMMENT 'Create date',
  updater              bigint COMMENT 'Updater',
  update_date          datetime COMMENT 'Update date',
  primary key (id),
  key idx_dept_id (dept_id)
)ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='Role';

CREATE TABLE sys_menu (
  id                   bigint NOT NULL COMMENT 'ID',
  pid                  bigint COMMENT 'Parent ID, 0 for first class menu',
  name                 varchar(200) COMMENT 'name',
  url                  varchar(200) COMMENT 'URL',
  permissions          varchar(500) COMMENT 'permission(separate by ":", example -> sys:user:list,sys:user:save)',
  menu_type            tinyint unsigned COMMENT 'Type 0:Menu 1:Button',
  icon                 varchar(50) COMMENT 'Icon',
  sort                 int COMMENT 'Sort',
  creator              bigint COMMENT 'Creator',
  create_date          datetime COMMENT 'Create date',
  updater              bigint COMMENT 'Updater',
  update_date          datetime COMMENT 'Update date',
  primary key (id),
  key idx_pid (pid),
  key idx_sort (sort)
)ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='Menu';

CREATE TABLE sys_role_user (
  id                   bigint NOT NULL COMMENT 'ID',
  role_id              bigint COMMENT 'Role ID',
  user_id              bigint COMMENT 'User ID',
  creator              bigint COMMENT 'Creator',
  create_date          datetime COMMENT 'Create date',
  primary key (id),
  key idx_role_id (role_id),
  key idx_user_id (user_id)
)ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='User role relationship';

CREATE TABLE sys_role_menu (
  id                   bigint NOT NULL COMMENT 'ID',
  role_id              bigint COMMENT 'Role ID',
  menu_id              bigint COMMENT 'Menu ID',
  creator              bigint COMMENT 'Creator',
  create_date          datetime COMMENT 'Create date',
  primary key (id),
  key idx_role_id (role_id),
  key idx_menu_id (menu_id)
)ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='Role menu relationship';

CREATE TABLE sys_role_data_scope (
  id                   bigint NOT NULL COMMENT 'ID',
  role_id              bigint COMMENT 'Role ID',
  dept_id              bigint COMMENT 'Department ID',
  creator              bigint COMMENT 'Creator',
  create_date          datetime COMMENT 'Create date',
  primary key (id),
  key idx_role_id (role_id)
)ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='Role data scope';

CREATE TABLE sys_params (
  id                   bigint NOT NULL COMMENT 'ID',
  param_code           varchar(32) COMMENT 'Param code',
  param_value          varchar(2000) COMMENT 'Param value',
  param_type           tinyint unsigned default 1 COMMENT 'Type 0:System param 1:Non-system param',
  remark               varchar(200) COMMENT 'Remark',
  creator              bigint COMMENT 'Creator',
  create_date          datetime COMMENT 'Create date',
  updater              bigint COMMENT 'Updater',
  update_date          datetime COMMENT 'Update date',
  primary key (id),
  unique key uk_param_code (param_code),
  key idx_create_date (create_date)
)ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='Params';

CREATE TABLE sys_dict_type (
    id                   bigint NOT NULL COMMENT 'ID',
    dict_type            varchar(100) NOT NULL COMMENT 'Dictionary type',
    dict_name            varchar(255) NOT NULL COMMENT 'Dictionary name',
    remark               varchar(255) COMMENT 'Remark',
    sort                 int unsigned COMMENT 'Sort',
    creator              bigint COMMENT 'Creator',
    create_date          datetime COMMENT 'Create date',
    updater              bigint COMMENT 'Updater',
    update_date          datetime COMMENT 'Update date',
    primary key (id),
    UNIQUE KEY(dict_type)
)ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='Dictionary type';

CREATE TABLE sys_dict_data (
    id                   bigint NOT NULL COMMENT 'ID',
    dict_type_id         bigint NOT NULL COMMENT 'Dictionary type ID',
    dict_label           varchar(255) NOT NULL COMMENT 'Dictionary label',
    dict_value           varchar(255) COMMENT 'Dictionary value',
    remark               varchar(255) COMMENT 'Remark',
    sort                 int unsigned COMMENT 'Sort',
    creator              bigint COMMENT 'Creator',
    create_date          datetime COMMENT 'Create date',
    updater              bigint COMMENT 'Updater',
    update_date          datetime COMMENT 'Update date',
    primary key (id),
    unique key uk_dict_type_value (dict_type_id, dict_value),
    key idx_sort (sort)
)ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='Dictionary data';

CREATE TABLE sys_log_login (
  id                   bigint NOT NULL COMMENT 'ID',
  operation            tinyint unsigned COMMENT 'Operation 0:Login 1:Logout',
  status               tinyint unsigned NOT NULL COMMENT 'Status 0:Failed 1:Success 2:Account lock',
  user_agent           varchar(500) COMMENT 'User agent',
  ip                   varchar(32) COMMENT 'IP',
  creator_name         varchar(50) COMMENT 'Username',
  creator              bigint COMMENT 'Creator',
  create_date          datetime COMMENT 'Create date',
  primary key (id),
  key idx_status (status),
  key idx_create_date (create_date)
)ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='Login log';

CREATE TABLE sys_log_operation (
  id                   bigint NOT NULL COMMENT 'ID',
  operation            varchar(50) COMMENT 'Operation',
  request_uri          varchar(200) COMMENT 'Request URI',
  request_method       varchar(20) COMMENT 'Request method',
  request_params       text COMMENT 'Request params',
  request_time         int unsigned NOT NULL COMMENT 'Request time(ms)',
  user_agent           varchar(500) COMMENT 'User agent',
  ip                   varchar(32) COMMENT 'IP',
  status               tinyint unsigned NOT NULL COMMENT 'Status 0:Failed 1:Success',
  creator_name         varchar(50) COMMENT 'Username',
  creator              bigint COMMENT 'Creator',
  create_date          datetime COMMENT 'Create date',
  primary key (id),
  key idx_create_date (create_date)
)ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='Operation log';

CREATE TABLE sys_log_error (
  id                   bigint NOT NULL COMMENT 'ID',
  request_uri          varchar(200) COMMENT 'Request URI',
  request_method       varchar(20) COMMENT 'Request method',
  request_params       text COMMENT 'Request params',
  user_agent           varchar(500) COMMENT 'Useragent',
  ip                   varchar(32) COMMENT 'IP',
  error_info           text COMMENT 'Error Info',
  creator              bigint COMMENT 'Creator',
  create_date          datetime COMMENT 'Create date',
  primary key (id),
  key idx_create_date (create_date)
)ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='Error log';

CREATE TABLE sys_oss (
  id bigint NOT NULL COMMENT 'ID',
  url varchar(200) COMMENT 'URL',
  creator bigint COMMENT 'Creator',
  create_date datetime COMMENT 'Create date',
  PRIMARY KEY (id),
  key idx_create_date (create_date)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COMMENT='File upload';

CREATE TABLE schedule_job (
  id bigint NOT NULL COMMENT 'ID',
  bean_name varchar(200) DEFAULT NULL COMMENT 'Spring bean name',
  params varchar(2000) DEFAULT NULL COMMENT 'Params',
  cron_expression varchar(100) DEFAULT NULL COMMENT 'cron expression',
  status tinyint unsigned COMMENT 'Status 0:Stop 1:Normal',
  remark varchar(255) DEFAULT NULL COMMENT 'Remark',
  creator bigint COMMENT 'Creator',
  create_date datetime COMMENT 'Create date',
  updater bigint COMMENT 'Updater',
  update_date datetime COMMENT 'Update date',
  PRIMARY KEY (id),
  key idx_create_date (create_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Schedule job';

CREATE TABLE schedule_job_log (
  id bigint NOT NULL COMMENT 'ID',
  job_id bigint NOT NULL COMMENT 'Job ID',
  bean_name varchar(200) DEFAULT NULL COMMENT 'spring bean name',
  params varchar(2000) DEFAULT NULL COMMENT 'Params',
  status tinyint unsigned NOT NULL COMMENT 'Status 0:Failed 1:Success',
  error varchar(2000) DEFAULT NULL COMMENT 'Error',
  times int NOT NULL COMMENT 'Times(ms)',
  create_date datetime COMMENT 'Create date',
  PRIMARY KEY (id),
  key idx_job_id (job_id),
  key idx_create_date (create_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Schedule job log';

CREATE TABLE sys_user_token (
  id bigint NOT NULL COMMENT 'ID',
  user_id bigint NOT NULL COMMENT 'User ID',
  token varchar(100) NOT NULL COMMENT 'User token',
  expire_date datetime COMMENT 'Expire date',
  update_date datetime COMMENT 'Update date',
  create_date datetime COMMENT 'Create date',
  PRIMARY KEY (id),
  UNIQUE KEY user_id (user_id),
  UNIQUE KEY token (token)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System User Token';

INSERT INTO sys_user(id, username, password, real_name, gender, email, mobile, status, dept_id, super_admin, creator, create_date, updater, update_date) VALUES (1067246875800000001, 'admin', '$2a$10$012Kx2ba5jzqr9gLlG4MX.bnQJTD9UWqF57XDo2N3.fPtLne02u/m', 'Admin', 0, '', '', 1, null, 1, 1067246875800000001, now(), 1067246875800000001, now());

INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000002,0,'User',NULL,NULL,0,'icon-team',4,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-09-20 18:52:55');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000003,1067246875800000055,'Add',NULL,'sys:user:save,sys:dept:list,sys:role:list',1,NULL,1,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000004,1067246875800000055,'Update',NULL,'sys:user:update,sys:dept:list,sys:role:list',1,NULL,2,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000005,1067246875800000055,'Delete',NULL,'sys:user:delete',1,NULL,3,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000006,1067246875800000055,'Export',NULL,'sys:user:export',1,NULL,4,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000007,1067246875800000002,'Role','sys/role',NULL,0,'icon-team',2,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-27 08:04:03');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000008,1067246875800000007,'View',NULL,'sys:role:page,sys:role:info',1,NULL,0,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000009,1067246875800000007,'Add',NULL,'sys:role:save,sys:menu:select,sys:dept:list',1,NULL,1,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000010,1067246875800000007,'Update',NULL,'sys:role:update,sys:menu:select,sys:dept:list',1,NULL,2,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000011,1067246875800000007,'Delete',NULL,'sys:role:delete',1,NULL,3,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000012,1067246875800000002,'Department','sys/dept',NULL,0,'icon-apartment',3,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-10-01 15:40:51');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000014,1067246875800000012,'View',NULL,'sys:dept:list,sys:dept:info',1,NULL,0,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000015,1067246875800000012,'Add',NULL,'sys:dept:save',1,NULL,1,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000016,1067246875800000012,'Update',NULL,'sys:dept:update',1,NULL,2,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000017,1067246875800000012,'Delete',NULL,'sys:dept:delete',1,NULL,3,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000025,1067246875800000035,'Menu','sys/menu',NULL,0,'icon-unorderedlist',0,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-27 08:01:01');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000026,1067246875800000025,'View',NULL,'sys:menu:list,sys:menu:info',1,NULL,0,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000027,1067246875800000025,'Add',NULL,'sys:menu:save',1,NULL,1,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000028,1067246875800000025,'Update',NULL,'sys:menu:update',1,NULL,2,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000029,1067246875800000025,'Delete',NULL,'sys:menu:delete',1,NULL,3,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000030,1067246875800000035,'Schedule Job','job/schedule',NULL,0,'icon-dashboard',3,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000031,1067246875800000030,'View',NULL,'sys:schedule:page,sys:schedule:info',1,NULL,0,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000032,1067246875800000030,'Add',NULL,'sys:schedule:save',1,NULL,1,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000033,1067246875800000030,'Update',NULL,'sys:schedule:update',1,NULL,2,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000034,1067246875800000030,'Delete',NULL,'sys:schedule:delete',1,NULL,3,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000035,0,'System',NULL,NULL,0,'icon-setting',5,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-08-23 20:51:26');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000036,1067246875800000030,'Pause',NULL,'sys:schedule:pause',1,NULL,4,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000037,1067246875800000030,'Resume',NULL,'sys:schedule:resume',1,NULL,5,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000038,1067246875800000030,'Run',NULL,'sys:schedule:run',1,NULL,6,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000039,1067246875800000030,'Log',NULL,'sys:schedule:log',1,NULL,7,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000040,1067246875800000035,'Param','sys/params','',0,'icon-fileprotect',1,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-27 08:01:06');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000041,1067246875800000035,'Dictionary','sys/dict-type',NULL,0,'icon-golden-fill',2,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-27 08:01:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000042,1067246875800000041,'View',NULL,'sys:dict:page,sys:dict:info',1,NULL,0,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000043,1067246875800000041,'Add',NULL,'sys:dict:save',1,NULL,1,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000044,1067246875800000041,'Update',NULL,'sys:dict:update',1,NULL,2,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000045,1067246875800000041,'Delete',NULL,'sys:dict:delete',1,NULL,3,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000046,0,'Log',NULL,NULL,0,'icon-container',6,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-09-08 22:37:07');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000048,1067246875800000046,'Login','sys/log-login','sys:log:login',0,'icon-filedone',0,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-27 08:04:19');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000049,1067246875800000046,'Operation','sys/log-operation','sys:log:operation',0,'icon-solution',1,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-27 08:04:23');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000050,1067246875800000046,'Error','sys/log-error','sys:log:error',0,'icon-file-exception',2,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-27 08:04:27');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000053,0,'System Monitoring',NULL,NULL,0,'icon-desktop',7,1067246875800000001,'2024-08-03 07:47:18',1067246875800000001,'2024-09-08 22:37:15');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000055,1067246875800000002,'Admin','sys/user',NULL,0,'icon-user',1,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-10-01 15:40:41');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000056,1067246875800000055,'View',NULL,'sys:user:page,sys:user:list,sys:user:info',1,NULL,0,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-09-04 18:44:03');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000057,1067246875800000040,'Add',NULL,'sys:params:save',1,NULL,1,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000058,1067246875800000040,'Export',NULL,'sys:params:export',1,NULL,4,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000059,1067246875800000040,'View','','sys:params:page,sys:params:info',1,NULL,0,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000060,1067246875800000040,'Update',NULL,'sys:params:update',1,NULL,2,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1067246875800000061,1067246875800000040,'Delete','','sys:params:delete',1,'',3,1067246875800000001,'2024-07-26 23:17:12',1067246875800000001,'2024-07-26 23:17:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1156748733921165314,1067246875800000053,'API Documentation','{{ApiUrl}}/doc.html','',0,'icon-file-word',1,1067246875800000001,'2024-08-03 07:48:19',1067246875800000001,'2024-08-03 07:48:19');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1819637394136117250,1819639044389208066,'Space','space/space',NULL,0,'icon-bank-fill',0,1067246875800000001,'2024-08-03 07:38:09',1067246875800000001,'2024-08-23 21:04:50');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1819637394136117251,1819637394136117250,'View',NULL,'space:space:page,space:space:info,space:closure:info,space:event:info',1,NULL,0,1067246875800000001,'2024-08-03 07:38:09',1067246875800000001,'2024-10-08 19:12:55');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1819637394136117252,1819637394136117250,'Add',NULL,'space:space:save',1,NULL,1,1067246875800000001,'2024-08-03 07:38:09',1067246875800000001,'2024-08-03 07:38:09');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1819637394136117253,1819637394136117250,'Update',NULL,'space:space:update',1,NULL,2,1067246875800000001,'2024-08-03 07:38:09',1067246875800000001,'2024-08-14 16:53:25');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1819637394136117254,1819637394136117250,'Delete',NULL,'space:space:delete',1,NULL,3,1067246875800000001,'2024-08-03 07:38:09',1067246875800000001,'2024-09-06 18:11:55');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1819637394136117255,1819637394136117250,'Export',NULL,'space:space:export',1,NULL,4,1067246875800000001,'2024-08-03 07:38:09',1067246875800000001,'2024-09-06 18:11:58');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1819637394333249538,1819639044389208066,'Tag','space/tag',NULL,0,'icon-tags',3,1067246875800000001,'2024-08-03 07:35:01',1067246875800000001,'2024-09-06 18:02:58');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1819637394333249539,1819637394333249538,'View',NULL,'space:tag:page,space:tag:info,space:tag:list',1,NULL,0,1067246875800000001,'2024-08-03 07:35:01',1067246875800000001,'2024-08-09 19:43:58');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1819637394333249540,1819637394333249538,'Add',NULL,'space:tag:save',1,NULL,1,1067246875800000001,'2024-08-03 07:35:01',1067246875800000001,'2024-08-03 07:35:01');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1819637394333249541,1819637394333249538,'Update',NULL,'space:tag:update',1,NULL,2,1067246875800000001,'2024-08-03 07:35:01',1067246875800000001,'2024-08-03 07:35:01');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1819637394333249542,1819637394333249538,'Delete',NULL,'space:tag:delete',1,NULL,3,1067246875800000001,'2024-08-03 07:35:01',1067246875800000001,'2024-08-03 07:35:01');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1819639044389208066,0,'Space','','',0,'icon-bank',1,1067246875800000001,'2024-08-03 15:38:50',1067246875800000001,'2024-09-08 22:36:48');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1821875193665142785,1819639044389208066,'Category','space/category','',0,'icon-appstore',2,1067246875800000001,'2024-08-09 19:44:30',1067246875800000001,'2024-09-06 18:02:49');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1821875582749753345,1821875193665142785,'View',NULL,'space:category:page,space:category:info,space:category:list',1,NULL,0,1067246875800000001,'2024-08-09 19:46:02',1067246875800000001,'2024-08-09 19:46:02');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1821875668586184706,1821875193665142785,'Add',NULL,'space:category:save',1,NULL,1,1067246875800000001,'2024-08-09 19:46:23',1067246875800000001,'2024-08-09 19:46:23');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1821875731907592193,1821875193665142785,'Update',NULL,'space:category:update',1,NULL,2,1067246875800000001,'2024-08-09 19:46:38',1067246875800000001,'2024-08-09 19:46:38');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1821875782528647169,1821875193665142785,'Delete',NULL,'space:category:delete',1,NULL,3,1067246875800000001,'2024-08-09 19:46:50',1067246875800000001,'2024-08-09 19:46:50');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1825851693246533633,0,'Service','','',0,'icon-carryout',2,1067246875800000001,'2024-08-20 19:05:41',1067246875800000001,'2024-09-08 22:36:52');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1825852007919996929,1825851693246533633,'Service','service/service','',0,'icon-carryout-fill',0,1067246875800000001,'2024-08-20 19:06:56',1067246875800000001,'2024-09-06 18:08:31');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1825852345188175874,1825851693246533633,'Category','service/category','',0,'icon-appstore',2,1067246875800000001,'2024-08-20 19:08:16',1067246875800000001,'2024-09-06 18:12:58');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1825852813926813698,1825851693246533633,'Tag','service/tag','',0,'icon-tags',3,1067246875800000001,'2024-08-20 19:10:08',1067246875800000001,'2024-09-06 18:13:02');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1825857041843965954,1825852007919996929,'View','','service:service:page,service:service:info',1,'',0,1067246875800000001,'2024-08-20 19:26:56',1067246875800000001,'2024-08-20 19:27:07');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1825857157057302530,1825852007919996929,'Add','','service:service:save',1,'',1,1067246875800000001,'2024-08-20 19:27:24',1067246875800000001,'2024-08-20 19:38:49');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1825857225508343809,1825852007919996929,'Update','','service:service:update',1,'',2,1067246875800000001,'2024-08-20 19:27:40',1067246875800000001,'2024-08-20 19:27:40');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1825857480110985217,1825852007919996929,'Delete','','service:service:delete',1,'',3,1067246875800000001,'2024-08-20 19:28:41',1067246875800000001,'2024-09-06 18:14:24');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1825857540458631169,1825852007919996929,'Export','','service:service:export',1,'',4,1067246875800000001,'2024-08-20 19:28:55',1067246875800000001,'2024-09-06 18:14:27');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1825857698453868545,1825852345188175874,'View','','service:category:page,service:category:info,service:category:list',1,'',0,1067246875800000001,'2024-08-20 19:29:33',1067246875800000001,'2024-08-20 19:31:03');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1825857748504498178,1825852345188175874,'Add','','service:category:save',1,'',1,1067246875800000001,'2024-08-20 19:29:45',1067246875800000001,'2024-08-20 19:29:45');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1825857813147111426,1825852345188175874,'Update','','service:category:update',1,'',2,1067246875800000001,'2024-08-20 19:30:00',1067246875800000001,'2024-08-20 19:30:00');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1825857905576988673,1825852345188175874,'Delete','','service:category:delete',1,'',3,1067246875800000001,'2024-08-20 19:30:22',1067246875800000001,'2024-08-20 19:30:22');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1825858254429835266,1825852813926813698,'View','','service:tag:page,service:tag:info,service:tag:list',1,'',0,1067246875800000001,'2024-08-20 19:31:45',1067246875800000001,'2024-08-20 19:31:45');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1825858305185107970,1825852813926813698,'Add','','service:tag:save',1,'',1,1067246875800000001,'2024-08-20 19:31:57',1067246875800000001,'2024-08-20 19:31:57');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1825858365276901378,1825852813926813698,'Update','','service:tag:update',1,'',2,1067246875800000001,'2024-08-20 19:32:12',1067246875800000001,'2024-08-20 19:32:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1825858425314168834,1825852813926813698,'Delete','','service:tag:delete',1,'',3,1067246875800000001,'2024-08-20 19:32:26',1067246875800000001,'2024-08-20 19:32:47');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1826965415125848065,0,'Accommodation','','',0,'icon-home',3,1067246875800000001,'2024-08-23 20:51:13',1067246875800000001,'2024-09-08 22:36:56');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1826968650481930242,1826965415125848065,'Accommodation','accommodation/accommodation','',0,'icon-home-fill',0,1067246875800000001,'2024-08-23 21:04:04',1067246875800000001,'2024-08-23 21:04:26');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1826969086198812674,1826965415125848065,'Category','accommodation/category','',0,'icon-appstore',2,1067246875800000001,'2024-08-23 21:05:48',1067246875800000001,'2024-09-06 18:15:30');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1826969303254044673,1826965415125848065,'Tag','accommodation/tag','',0,'icon-tags',3,1067246875800000001,'2024-08-23 21:06:40',1067246875800000001,'2024-09-06 18:15:33');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1826969303254044674,1826968650481930242,'View',NULL,'accommodation:accommodation:page,accommodation:accommodation:info,accommodation:closure:info,accommodation:event:info',1,NULL,0,1067246875800000001,'2024-08-20 19:31:45',1067246875800000001,'2024-10-09 17:00:08');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1826969303254044675,1826968650481930242,'Add',NULL,'accommodation:accommodation:save',1,NULL,1,1067246875800000001,'2024-08-20 19:31:57',1067246875800000001,'2024-08-20 19:31:57');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1826969303254044676,1826968650481930242,'Update',NULL,'accommodation:accommodation:update',1,NULL,2,1067246875800000001,'2024-08-20 19:32:12',1067246875800000001,'2024-08-20 19:32:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1826969303254044677,1826968650481930242,'Delete',NULL,'accommodation:accommodation:delete',1,NULL,3,1067246875800000001,'2024-08-20 19:32:26',1067246875800000001,'2024-09-06 18:17:30');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1826969303254044678,1826968650481930242,'Export',NULL,'accommodation:accommodation:export',1,NULL,4,1067246875800000001,'2024-08-20 19:32:35',1067246875800000001,'2024-09-06 18:17:33');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1826969303254044679,1826969086198812674,'View',NULL,'accommodation:category:page,accommodation:category:info,accommodation:category:list',1,NULL,0,1067246875800000001,'2024-08-20 19:31:45',1067246875800000001,'2024-08-20 19:31:45');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1826969303254044680,1826969086198812674,'Add',NULL,'accommodation:category:save',1,NULL,1,1067246875800000001,'2024-08-20 19:31:57',1067246875800000001,'2024-08-20 19:31:57');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1826969303254044681,1826969086198812674,'Update',NULL,'accommodation:category:update',1,NULL,2,1067246875800000001,'2024-08-20 19:32:12',1067246875800000001,'2024-08-20 19:32:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1826969303254044682,1826969086198812674,'Delete',NULL,'accommodation:category:delete',1,NULL,3,1067246875800000001,'2024-08-20 19:32:26',1067246875800000001,'2024-08-20 19:32:47');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1826969303254044684,1826969303254044673,'View',NULL,'accommodation:tag:page,accommodation:tag:info,accommodation:tag:list',1,NULL,0,1067246875800000001,'2024-08-20 19:31:45',1067246875800000001,'2024-08-20 19:31:45');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1826969303254044685,1826969303254044673,'Add',NULL,'accommodation:tag:save',1,NULL,1,1067246875800000001,'2024-08-20 19:31:57',1067246875800000001,'2024-08-20 19:31:57');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1826969303254044686,1826969303254044673,'Update',NULL,'accommodation:tag:update',1,NULL,2,1067246875800000001,'2024-08-20 19:32:12',1067246875800000001,'2024-08-20 19:32:12');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1826969303254044687,1826969303254044673,'Delete',NULL,'accommodation:tag:delete',1,NULL,3,1067246875800000001,'2024-08-20 19:32:26',1067246875800000001,'2024-08-20 19:32:47');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1831997659548782594,1819639044389208066,'Booking Rule','space/booking-rule','',0,'icon-file-text',1,1067246875800000001,'2024-09-06 18:07:34',1067246875800000001,'2024-09-06 18:07:34');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1831998699291578369,1831997659548782594,'Update Default Booking Rule','','space:default-booking-rule:update',1,'',2,1067246875800000001,'2024-09-06 18:11:41',1067246875800000001,'2024-09-20 18:46:54');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1831998899267604482,1831997659548782594,'View',NULL,'space:space:page,space:space:info,space:default-booking-rule:info',1,NULL,0,1067246875800000001,'2024-09-06 18:12:29',1067246875800000001,'2024-10-08 19:12:51');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1831999182806749185,1825851693246533633,'Booking Rule','service/booking-rule','',0,'icon-file-text',1,1067246875800000001,'2024-09-06 18:13:37',1067246875800000001,'2024-09-06 18:13:37');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1831999273957363713,1831999182806749185,'View','','service:service:page,service:service:info,service:default-booking-rule:info',1,'',0,1067246875800000001,'2024-09-06 18:13:58',1067246875800000001,'2024-09-17 20:59:30');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1831999572684083202,1831999182806749185,'Update Default Booking Rule','','service:default-booking-rule:update',1,'',2,1067246875800000001,'2024-09-06 18:15:10',1067246875800000001,'2024-09-20 18:47:03');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1831999760773451777,1826965415125848065,'Booking Rule','accommodation/booking-rule','',0,'icon-file-text',1,1067246875800000001,'2024-09-06 18:15:55',1067246875800000001,'2024-09-06 18:15:55');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1832000027816398849,1831999760773451777,'View',NULL,'accommodation:accommodation:page,accommodation:accommodation:info,accommodation:default-booking-rule:info',1,NULL,0,1067246875800000001,'2024-09-06 18:16:58',1067246875800000001,'2024-09-17 21:05:52');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1832000260424110082,1831999760773451777,'Update Default Booking Rule',NULL,'accommodation:default-booking-rule:update',1,NULL,2,1067246875800000001,'2024-09-06 18:17:54',1067246875800000001,'2024-09-20 18:52:20');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1832790411601633282,0,'Booking','','',0,'icon-book',0,1067246875800000001,'2024-09-08 22:37:40',1067246875800000001,'2024-09-08 22:37:40');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1832790568795758594,1832790411601633282,'Space','booking/spc-booking','',0,'icon-bank',0,1067246875800000001,'2024-09-08 22:38:18',1067246875800000001,'2024-09-08 22:38:53');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1832792281791455233,1832790411601633282,'Service','booking/svc-booking','',0,'icon-carryout',1,1067246875800000001,'2024-09-08 22:45:06',1067246875800000001,'2024-09-10 20:27:49');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1832792573014564865,1832790411601633282,'Accommodation','booking/acc-booking','',0,'icon-home',2,1067246875800000001,'2024-09-08 22:46:16',1067246875800000001,'2024-09-10 20:27:57');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1832793625982001153,1832790568795758594,'View','','space:booking:page,space:booking:info,payment:space:page',1,'',0,1067246875800000001,'2024-09-08 22:50:27',1067246875800000001,'2024-09-17 20:51:41');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1832793720211234818,1832792281791455233,'View','','service:booking:page,service:booking:info,payment:service:page',1,'',0,1067246875800000001,'2024-09-08 22:50:49',1067246875800000001,'2024-09-17 20:52:34');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1832793802176323586,1832792573014564865,'View','','accommodation:booking:page,accommodation:booking:info,payment:accommodation:page',1,'',0,1067246875800000001,'2024-09-08 22:51:09',1067246875800000001,'2024-09-17 20:55:24');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1832794314867073026,1832790568795758594,'Approve & Reject','','space:booking:approve,space:booking:reject',1,'',1,1067246875800000001,'2024-09-08 22:53:11',1067246875800000001,'2024-09-17 20:56:40');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1832794443212775426,1832792281791455233,'Approve & Reject','','service:booking:approve,service:booking:reject',1,'',1,1067246875800000001,'2024-09-08 22:53:42',1067246875800000001,'2024-09-17 20:57:11');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1832794516671815682,1832792573014564865,'Approve & Reject','','accommodation:booking:approve,accommodation:booking:reject',1,'',1,1067246875800000001,'2024-09-08 22:53:59',1067246875800000001,'2024-09-17 20:57:22');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1833482866957295618,1832790411601633282,'Payment Method','booking/payment-method','',0,'icon-Pound',4,1067246875800000001,'2024-09-10 20:29:15',1067246875800000001,'2024-09-10 20:30:23');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1833483257740599298,1833482866957295618,'View','','payment:method:page,payment:method:info',1,'',0,1067246875800000001,'2024-09-10 20:30:48',1067246875800000001,'2024-09-17 20:55:52');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1833483338569031681,1833482866957295618,'Add','','payment:method:save',1,'',1,1067246875800000001,'2024-09-10 20:31:07',1067246875800000001,'2024-09-17 20:56:19');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1833483379245391873,1833482866957295618,'Update','','payment:method:update',1,'',2,1067246875800000001,'2024-09-10 20:31:17',1067246875800000001,'2024-09-17 20:56:11');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1833483425093328898,1833482866957295618,'Delete','','payment:method:delete',1,'',3,1067246875800000001,'2024-09-10 20:31:28',1067246875800000001,'2024-09-17 20:56:04');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1836060935270211585,1819637394136117250,'Create Closure',NULL,'space:closure:save',1,NULL,5,1067246875800000001,'2024-09-17 23:13:34',1067246875800000001,'2024-09-17 23:13:34');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1836061001364054017,1819637394136117250,'Update Closure',NULL,'space:closure:update',1,NULL,6,1067246875800000001,'2024-09-17 23:13:50',1067246875800000001,'2024-09-17 23:13:57');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1836061122441027586,1819637394136117250,'Delete Closure',NULL,'space:closure:delete',1,NULL,7,1067246875800000001,'2024-09-17 23:14:19',1067246875800000001,'2024-09-17 23:14:19');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1837080968620412929,1831997659548782594,'Update','','space:booking-rule:update,sys:user:list',1,'',1,1067246875800000001,'2024-09-20 18:46:49',1067246875800000001,'2024-10-09 18:46:19');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1837081074476257282,1831999182806749185,'Update','','service:booking-rule:update,sys:user:list',1,'',1,1067246875800000001,'2024-09-20 18:47:14',1067246875800000001,'2024-10-09 18:46:37');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1837082445111889922,1831999760773451777,'Update',NULL,'accommodation:booking-rule:update,sys:user:list',1,NULL,1,1067246875800000001,'2024-09-20 18:52:41',1067246875800000001,'2024-10-09 18:46:49');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1841020520263335937,1067246875800000002,'Client','client/user','',0,'icon-user',0,1067246875800000001,'2024-10-01 15:41:11',1067246875800000001,'2024-10-01 15:41:30');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1841020926506844161,1841020520263335937,'View','','client:user:page,client:user:info',1,'',0,1067246875800000001,'2024-10-01 15:42:48',1067246875800000001,'2024-10-01 15:42:48');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1841021000485978113,1841020520263335937,'Export','','client:user:export',1,'',1,1067246875800000001,'2024-10-01 15:43:06',1067246875800000001,'2024-10-01 15:43:06');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1843944307434844161,1826968650481930242,'Create Closure',NULL,'accommodation:closure:save',1,NULL,5,1067246875800000001,'2024-10-09 17:19:16',1067246875800000001,'2024-10-09 17:19:40');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1843944379648176129,1826968650481930242,'Update Closure',NULL,'accommodation:closure:update',1,NULL,6,1067246875800000001,'2024-10-09 17:19:34',1067246875800000001,'2024-10-09 17:19:34');
INSERT INTO sys_menu (`id`,`pid`,`name`,`url`,`permissions`,`menu_type`,`icon`,`sort`,`creator`,`create_date`,`updater`,`update_date`) VALUES (1843944468005384193,1826968650481930242,'Delete Closure',NULL,'accommodation:closure:delete',1,NULL,7,1067246875800000001,'2024-10-09 17:19:55',1067246875800000001,'2024-10-09 17:19:55');

INSERT INTO sys_dept(id, pid, pids, name, sort, creator, create_date, updater, update_date) VALUES (1067246875800000062, 1067246875800000063, '1067246875800000066,1067246875800000063', 'IT Department', 2, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dept(id, pid, pids, name, sort, creator, create_date, updater, update_date) VALUES (1067246875800000063, 1067246875800000066, '1067246875800000066', 'PTM', 1, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dept(id, pid, pids, name, sort, creator, create_date, updater, update_date) VALUES (1067246875800000064, 1067246875800000066, '1067246875800000066', 'FSKTM', 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dept(id, pid, pids, name, sort, creator, create_date, updater, update_date) VALUES (1067246875800000065, 1067246875800000064, '1067246875800000066,1067246875800000064', 'Software Engineering Department', 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dept(id, pid, pids, name, sort, creator, create_date, updater, update_date) VALUES (1067246875800000066, 0, '0', 'University Malaya', 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dept(id, pid, pids, name, sort, creator, create_date, updater, update_date) VALUES (1067246875800000067, 1067246875800000064, '1067246875800000066,1067246875800000064', 'Information System Department', 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dept(id, pid, pids, name, sort, creator, create_date, updater, update_date) VALUES (1067246875800000068, 1067246875800000063, '1067246875800000066,1067246875800000063', 'Marketing Department', 1, 1067246875800000001, now(), 1067246875800000001, now());

INSERT INTO sys_dict_type(id, dict_type, dict_name, remark, sort, creator, create_date, updater, update_date) VALUES (1160061077912858625, 'gender', 'Gender', '', 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1160061112075464705, 1160061077912858625, 'Male', '0', '', 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1160061146967879681, 1160061077912858625, 'Female', '1', '', 1, 1067246875800000001, now(), 1067246875800000001, now());
#INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1160061190127267841, 1160061077912858625, 'Other', '2', '', 2, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dict_type(id, dict_type, dict_name, remark, sort, creator, create_date, updater, update_date) VALUES (1225813644059140097, 'notice_type', 'Notice Type', '', 1, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1225814069634195457, 1225813644059140097, 'Announcement', '0', '', 0, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1225814107559092225, 1225813644059140097, 'Meeting', '1', '', 1, 1067246875800000001, now(), 1067246875800000001, now());
INSERT INTO sys_dict_data(id, dict_type_id, dict_label, dict_value, remark, sort, creator, create_date, updater, update_date) VALUES (1225814271879340034, 1225813644059140097, 'Other', '2', '', 2, 1067246875800000001, now(), 1067246875800000001, now());

INSERT INTO sys_params(id, param_code, param_value, param_type, remark, creator, create_date, updater, update_date) VALUES (1067246875800000073, 'CLOUD_STORAGE_CONFIG_KEY', '{"type":1,"qiniuDomain":"http://test.oss.renren.io","qiniuPrefix":"upload","qiniuAccessKey":"NrgMfABZxWLo5B-YYSjoE8-AZ1EISdi1Z3ubLOeZ","qiniuSecretKey":"uIwJHevMRWU0VLxFvgy0tAcOdGqasdtVlJkdy6vV","qiniuBucketName":"renren-oss","aliyunDomain":"","aliyunPrefix":"","aliyunEndPoint":"","aliyunAccessKeyId":"","aliyunAccessKeySecret":"","aliyunBucketName":"","qcloudDomain":"","qcloudPrefix":"","qcloudSecretId":"","qcloudSecretKey":"","qcloudBucketName":""}', '0', '云存储配置信息', 1067246875800000001, now(), 1067246875800000001, now());

INSERT INTO schedule_job (id, bean_name, params, cron_expression, status, remark, creator, create_date, updater, update_date) VALUES (1067246875800000076, 'testTask', 'renren', '0 0/30 * * * ?', 0, 'Test with param，using JSON for multiple params', 1067246875800000001, now(), 1067246875800000001, now());


CREATE TABLE QRTZ_JOB_DETAILS(
  SCHED_NAME VARCHAR(120) NOT NULL,
  JOB_NAME VARCHAR(200) NOT NULL,
  JOB_GROUP VARCHAR(200) NOT NULL,
  DESCRIPTION VARCHAR(250) NULL,
  JOB_CLASS_NAME VARCHAR(250) NOT NULL,
  IS_DURABLE VARCHAR(1) NOT NULL,
  IS_NONCONCURRENT VARCHAR(1) NOT NULL,
  IS_UPDATE_DATA VARCHAR(1) NOT NULL,
  REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
  JOB_DATA BLOB NULL,
  PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE QRTZ_TRIGGERS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  TRIGGER_NAME VARCHAR(200) NOT NULL,
  TRIGGER_GROUP VARCHAR(200) NOT NULL,
  JOB_NAME VARCHAR(200) NOT NULL,
  JOB_GROUP VARCHAR(200) NOT NULL,
  DESCRIPTION VARCHAR(250) NULL,
  NEXT_FIRE_TIME BIGINT(13) NULL,
  PREV_FIRE_TIME BIGINT(13) NULL,
  PRIORITY INTEGER NULL,
  TRIGGER_STATE VARCHAR(16) NOT NULL,
  TRIGGER_TYPE VARCHAR(8) NOT NULL,
  START_TIME BIGINT(13) NOT NULL,
  END_TIME BIGINT(13) NULL,
  CALENDAR_NAME VARCHAR(200) NULL,
  MISFIRE_INSTR SMALLINT(2) NULL,
  JOB_DATA BLOB NULL,
  PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
  FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
  REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE QRTZ_SIMPLE_TRIGGERS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  TRIGGER_NAME VARCHAR(200) NOT NULL,
  TRIGGER_GROUP VARCHAR(200) NOT NULL,
  REPEAT_COUNT BIGINT(7) NOT NULL,
  REPEAT_INTERVAL BIGINT(12) NOT NULL,
  TIMES_TRIGGERED BIGINT(10) NOT NULL,
  PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
  FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
  REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE QRTZ_CRON_TRIGGERS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  TRIGGER_NAME VARCHAR(200) NOT NULL,
  TRIGGER_GROUP VARCHAR(200) NOT NULL,
  CRON_EXPRESSION VARCHAR(120) NOT NULL,
  TIME_ZONE_ID VARCHAR(80),
  PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
  FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
  REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE QRTZ_SIMPROP_TRIGGERS(
  SCHED_NAME VARCHAR(120) NOT NULL,
  TRIGGER_NAME VARCHAR(200) NOT NULL,
  TRIGGER_GROUP VARCHAR(200) NOT NULL,
  STR_PROP_1 VARCHAR(512) NULL,
  STR_PROP_2 VARCHAR(512) NULL,
  STR_PROP_3 VARCHAR(512) NULL,
  INT_PROP_1 INT NULL,
  INT_PROP_2 INT NULL,
  LONG_PROP_1 BIGINT NULL,
  LONG_PROP_2 BIGINT NULL,
  DEC_PROP_1 NUMERIC(13,4) NULL,
  DEC_PROP_2 NUMERIC(13,4) NULL,
  BOOL_PROP_1 VARCHAR(1) NULL,
  BOOL_PROP_2 VARCHAR(1) NULL,
  PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
  FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
  REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE QRTZ_BLOB_TRIGGERS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  TRIGGER_NAME VARCHAR(200) NOT NULL,
  TRIGGER_GROUP VARCHAR(200) NOT NULL,
  BLOB_DATA BLOB NULL,
  PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
  INDEX (SCHED_NAME,TRIGGER_NAME, TRIGGER_GROUP),
  FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
  REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE QRTZ_CALENDARS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  CALENDAR_NAME VARCHAR(200) NOT NULL,
  CALENDAR BLOB NOT NULL,
  PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  TRIGGER_GROUP VARCHAR(200) NOT NULL,
  PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE QRTZ_FIRED_TRIGGERS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  ENTRY_ID VARCHAR(95) NOT NULL,
  TRIGGER_NAME VARCHAR(200) NOT NULL,
  TRIGGER_GROUP VARCHAR(200) NOT NULL,
  INSTANCE_NAME VARCHAR(200) NOT NULL,
  FIRED_TIME BIGINT(13) NOT NULL,
  SCHED_TIME BIGINT(13) NOT NULL,
  PRIORITY INTEGER NOT NULL,
  STATE VARCHAR(16) NOT NULL,
  JOB_NAME VARCHAR(200) NULL,
  JOB_GROUP VARCHAR(200) NULL,
  IS_NONCONCURRENT VARCHAR(1) NULL,
  REQUESTS_RECOVERY VARCHAR(1) NULL,
  PRIMARY KEY (SCHED_NAME,ENTRY_ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE QRTZ_SCHEDULER_STATE (
  SCHED_NAME VARCHAR(120) NOT NULL,
  INSTANCE_NAME VARCHAR(200) NOT NULL,
  LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
  CHECKIN_INTERVAL BIGINT(13) NOT NULL,
  PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE QRTZ_LOCKS (
  SCHED_NAME VARCHAR(120) NOT NULL,
  LOCK_NAME VARCHAR(40) NOT NULL,
  PRIMARY KEY (SCHED_NAME,LOCK_NAME)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX IDX_QRTZ_J_REQ_RECOVERY ON QRTZ_JOB_DETAILS(SCHED_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_J_GRP ON QRTZ_JOB_DETAILS(SCHED_NAME,JOB_GROUP);

CREATE INDEX IDX_QRTZ_T_J ON QRTZ_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_JG ON QRTZ_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_C ON QRTZ_TRIGGERS(SCHED_NAME,CALENDAR_NAME);
CREATE INDEX IDX_QRTZ_T_G ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_T_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_G_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NEXT_FIRE_TIME ON QRTZ_TRIGGERS(SCHED_NAME,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_MISFIRE ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE_GRP ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE);

CREATE INDEX IDX_QRTZ_FT_TRIG_INST_NAME ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME);
CREATE INDEX IDX_QRTZ_FT_INST_JOB_REQ_RCVRY ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_FT_J_G ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_JG ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_T_G ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_FT_TG ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);