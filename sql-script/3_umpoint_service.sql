CREATE TABLE svc_category (
    id bigint NOT NULL COMMENT 'ID',
    name varchar(50) NOT NULL COMMENT 'Name',
    PRIMARY KEY (ID),
    UNIQUE INDEX (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Service Category';

CREATE TABLE svc_tag (
    id bigint NOT NULL COMMENT 'ID',
    tag_name varchar(20) NOT NULL COMMENT 'Tag name',
    PRIMARY KEY (id),
    UNIQUE INDEX (tag_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Service Tag';

CREATE TABLE svc_booking_rule (
    id bigint NOT NULL COMMENT 'ID',
    price decimal(10, 2) NOT NULL COMMENT 'Price',
    approval_required BOOLEAN NOT NULL COMMENT 'Is booking require approve by manager',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Service Booking Rule';

CREATE TABLE svc_service (
    id bigint NOT NULL COMMENT 'ID',
    name varchar(50) NOT NULL COMMENT 'Name',
    cat_id bigint NOT NULL COMMENT 'Category ID',
    dept_id bigint NOT NULL COMMENT 'Department ID',
    address varchar(250) NOT NULL COMMENT 'Address',
    description varchar(2500) COMMENT 'Description',
    manager bigint NULL COMMENT 'Manager ID',
    booking_rule_id bigint NOT NULL COMMENT 'Booking Rule ID',
    status tinyint COMMENT 'Status 0:Suspend 1:Normal',
    creator bigint NOT NULL COMMENT 'Creator',
    create_date datetime NOT NULL COMMENT 'Create date',
    updater bigint NOT NULL COMMENT 'Updater',
    update_date datetime NOT NULL COMMENT 'Update date',
    PRIMARY KEY (id),
    FOREIGN KEY (cat_id) REFERENCES svc_category(id),
    FOREIGN KEY (dept_id) REFERENCES sys_dept(id),
    FOREIGN KEY (manager) REFERENCES sys_user(id),
    FOREIGN KEY (booking_rule_id) REFERENCES svc_booking_rule(id),
    FOREIGN KEY (creator) REFERENCES sys_user(id),
    FOREIGN KEY (updater) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Service';

CREATE TABLE svc_service_tag (
    service_id bigint NOT NULL COMMENT 'Service ID',
    tag_id bigint NOT NULL COMMENT 'Tag ID',
    PRIMARY KEY (service_id, tag_id),
    FOREIGN KEY (service_id) REFERENCES svc_service(id),
    FOREIGN KEY (tag_id) REFERENCES svc_tag(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Service tag relationship';

CREATE TABLE svc_image (
    id bigint NOT NULL COMMENT 'ID',
    service_id bigint NOT NULL COMMENT 'Service ID',
    image_url varchar(250) NOT NULL COMMENT 'Image url',
    PRIMARY KEY (id),
    FOREIGN KEY (service_id) REFERENCES svc_service(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Service Image';