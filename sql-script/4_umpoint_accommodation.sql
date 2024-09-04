CREATE TABLE acc_category (
    id bigint NOT NULL COMMENT 'ID',
    name varchar(50) NOT NULL COMMENT 'Name',
    PRIMARY KEY (ID),
    UNIQUE INDEX (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Accommodation Category';

CREATE TABLE acc_tag (
    id bigint NOT NULL COMMENT 'ID',
    tag_name varchar(20) NOT NULL COMMENT 'Tag name',
    PRIMARY KEY (id),
    UNIQUE INDEX (tag_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Accommodation Tag';

CREATE TABLE acc_booking_rule (
    id bigint NOT NULL COMMENT 'ID',
    open_for_staff tinyint NOT NULL COMMENT '0:Staff not allow to book 1:Staff allow to book',
    open_for_student tinyint NOT NULL COMMENT '0:Student not allow to book 1:Student allow to book',
    open_for_public tinyint NOT NULL COMMENT '0:Public not allow to book 1:Public allow to book',
    approval_required tinyint NOT NULL COMMENT '0:Automatic approve 1:Require admin approve',
    close_days_before_event decimal(5,0) NOT NULL COMMENT 'Days close for booking before event',
    close_days_after_event decimal(5,0) NOT NULL COMMENT 'Days close for booking after event',
    max_reservation_days decimal(5, 0) NOT NULL COMMENT 'Maximum reservation days',
    min_booking_days decimal(5, 0) NOT NULL COMMENT 'Minimum booking days',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Accommodation Booking Rule';

CREATE TABLE acc_accommodation (
   id bigint NOT NULL COMMENT 'ID',
   status tinyint COMMENT 'Status 0:Suspend 1:Normal',
   name varchar(50) NOT NULL COMMENT 'Name',
   cat_id bigint NOT NULL COMMENT 'Category ID',
   dept_id bigint NOT NULL COMMENT 'Department ID',
   address varchar(250) NOT NULL COMMENT 'Address',
   description varchar(2500) COMMENT 'Description',
   facilities varchar(250) COMMENT 'Facilities',
   capacity decimal(5,0) COMMENT 'Max capacity',
   manager bigint NULL COMMENT 'Manager ID',
   day_price decimal(10, 2) NULL COMMENT 'Price for book a day',
   week_price decimal(10, 2) NULL COMMENT 'Price for book a week',
   booking_rule_id bigint NULL COMMENT 'Booking Rule ID',
   creator bigint NOT NULL COMMENT 'Creator',
   create_date datetime NOT NULL COMMENT 'Create date',
   updater bigint NOT NULL COMMENT 'Updater',
   update_date datetime NOT NULL COMMENT 'Update date',
   PRIMARY KEY (id),
   FOREIGN KEY (cat_id) REFERENCES acc_category(id),
   FOREIGN KEY (dept_id) REFERENCES sys_dept(id),
   FOREIGN KEY (manager) REFERENCES sys_user(id),
   FOREIGN KEY (booking_rule_id) REFERENCES acc_booking_rule(id),
   FOREIGN KEY (creator) REFERENCES sys_user(id),
   FOREIGN KEY (updater) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Accommodation';

CREATE TABLE acc_accommodation_tag (
   accommodation_id bigint NOT NULL COMMENT 'Accommodation ID',
   tag_id bigint NOT NULL COMMENT 'Tag ID',
   PRIMARY KEY (accommodation_id, tag_id),
   FOREIGN KEY (accommodation_id) REFERENCES acc_accommodation(id),
   FOREIGN KEY (tag_id) REFERENCES acc_tag(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Accommodation tag relationship';

CREATE TABLE acc_image (
   id bigint NOT NULL COMMENT 'ID',
   accommodation_id bigint NOT NULL COMMENT 'Accommodation ID',
   image_url varchar(250) NOT NULL COMMENT 'Image url',
   PRIMARY KEY (id),
   FOREIGN KEY (accommodation_id) REFERENCES acc_accommodation(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Accommodation Image';

CREATE TABLE acc_availability (
  id bigint NOT NULL COMMENT 'ID',
  accommodation_id bigint NOT NULL COMMENT 'Accommodation ID',
  year decimal(4,0) NOT NULL COMMENT 'Year',
  availability BLOB NOT NULL COMMENT 'Availability of accommodation, consist of 366*24 bit, 1 represent available in specific day in one year',
  PRIMARY KEY (id),
  FOREIGN KEY (accommodation_id) REFERENCES acc_accommodation(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Accommodation Availability';