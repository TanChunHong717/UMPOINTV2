CREATE TABLE spc_category (
    id bigint NOT NULL COMMENT 'ID',
    name varchar(50) NOT NULL COMMENT 'Name',
    PRIMARY KEY (ID),
    UNIQUE INDEX (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Space Category';

CREATE TABLE spc_tag (
    id bigint NOT NULL COMMENT 'ID',
    tag_name varchar(20) NOT NULL COMMENT 'Tag name',
    PRIMARY KEY (id),
    UNIQUE INDEX (tag_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Space Tag';

CREATE TABLE spc_booking_rule (
    id bigint NOT NULL COMMENT 'ID',
    approval_required tinyint NOT NULL COMMENT '0:Automatic approve 1:Require admin approve',
    open_for_staff tinyint NOT NULL COMMENT '0:Staff not allow to book 1:Staff allow to book',
    open_for_student tinyint NOT NULL COMMENT '0:Student not allow to book 1:Student allow to book',
    open_for_public tinyint NOT NULL COMMENT '0:Public not allow to book 1:Public allow to book',
    holiday_available tinyint NOT NULL COMMENT 'Availability in public holiday, 1: Available, 0: Close',
    booking_mode tinyint NOT NULL COMMENT '0: Free time selection, 1: Limited to preset slots',
    start_time time NOT NULL COMMENT 'Start time in a day when booking is allow',
    end_time time NOT NULL COMMENT 'End time in a day when booking is allow',
    booking_unit DECIMAL(3, 2) NOT NULL COMMENT 'Booking unit in hour (e.g., 0.5 for half-hour increments)',
    max_booking_advance_day decimal(5,0) NOT NULL COMMENT 'Max booking advance day',
    min_booking_advance_day decimal(5,0) NOT NULL COMMENT 'Min booking advance day',
    max_reservation_day decimal(5, 0) NOT NULL COMMENT 'Maximum reservation day',
    min_reservation_day decimal(5, 0) NOT NULL COMMENT 'Minimum reservation day',
    max_booking_hour decimal(3, 2) NOT NULL COMMENT 'Maximum booking hour per day',
    min_booking_hour decimal(3, 2) NOT NULL COMMENT 'Minimum booking hour per day',
    max_technician_number decimal(5, 0) NOT NULL COMMENT 'Maximum number of technician',
    technician_price decimal(10, 2) NOT NULL COMMENT 'Price per technician',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Space Booking Rule';

INSERT INTO spc_booking_rule VALUE (0,1,1,1,1,1,0,('00:00:00'),('23:59:59'),1,60,0,5,1,8,1,0,0);

CREATE TABLE spc_space (
    id bigint NOT NULL COMMENT 'ID',
    status tinyint COMMENT 'Status 0:Suspend 1:Normal',
    name varchar(250) NOT NULL COMMENT 'Name',
    cat_id bigint NOT NULL COMMENT 'Category ID',
    dept_id bigint NOT NULL COMMENT 'Department ID',
    address varchar(250) NOT NULL COMMENT 'Address',
    description varchar(2500) COMMENT 'Description',
    facilities varchar(250) COMMENT 'Facilities',
    capacity decimal(5,0) COMMENT 'Max capacity',
    manager bigint NULL COMMENT 'Manager ID',
    hour_price decimal(10, 2) NULL COMMENT 'Price for book an hour',
    four_hours_price decimal(10, 2) NULL COMMENT 'Price for book four hours',
    day_price decimal(10, 2) NULL COMMENT 'Price for book a day',
    booking_rule_id bigint NULL COMMENT 'Booking Rule ID',
    creator bigint NOT NULL COMMENT 'Creator',
    create_date datetime NOT NULL COMMENT 'Create date',
    updater bigint NOT NULL COMMENT 'Updater',
    update_date datetime NOT NULL COMMENT 'Update date',
    PRIMARY KEY (id),
    FOREIGN KEY (cat_id) REFERENCES spc_category(id),
    FOREIGN KEY (dept_id) REFERENCES sys_dept(id),
    FOREIGN KEY (manager) REFERENCES sys_user(id),
    FOREIGN KEY (booking_rule_id) REFERENCES spc_booking_rule(id),
    FOREIGN KEY (creator) REFERENCES sys_user(id),
    FOREIGN KEY (updater) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Space';

CREATE TABLE spc_space_tag (
    space_id bigint NOT NULL COMMENT 'Space ID',
    tag_id bigint NOT NULL COMMENT 'Tag ID',
    PRIMARY KEY (space_id, tag_id),
    FOREIGN KEY (space_id) REFERENCES spc_space(id),
    FOREIGN KEY (tag_id) REFERENCES spc_tag(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Space tag relationship';

CREATE TABLE spc_image (
    id bigint NOT NULL COMMENT 'ID',
    space_id bigint NOT NULL COMMENT 'Space ID',
    image_url varchar(250) NOT NULL COMMENT 'Image url',
    PRIMARY KEY (id),
    FOREIGN KEY (space_id) REFERENCES spc_space(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Space Image';

CREATE TABLE spc_booking (
    id bigint NOT NULL COMMENT 'ID',
    status tinyint NOT NULL COMMENT 'Status: 0:Pending, 1:Reject, 2:Approve(Pending Payment), 3:Approve(Payment Complete)/Complete, 4:Cancel',
    space_id bigint NOT NULL COMMENT 'Space ID',
    admin_id bigint NULL COMMENT 'Admin that approve/reject, user will contact this admin rather manager if umpoint.booking.space.find-approve-admin-first=true',
    user_id bigint NOT NULL COMMENT 'User ID',
    event varchar(250) NOT NULL COMMENT 'Description of the event or purpose for the booking',
    payment_amount decimal(10,2) NOT NULL COMMENT 'Amount need to be pay',
    start_day date NOT NULL COMMENT 'Start day of booking',
    end_day date NOT NULL COMMENT 'End day of booking',
    start_time time NOT NULL COMMENT 'Start time of booking in a day',
    end_time time NOT NULL COMMENT 'End time of booking in a day',
    technician_number decimal(5,0) NOT NULL COMMENT 'Number of technician',
    create_date datetime NOT NULL COMMENT 'Create date',
    update_date datetime NOT NULL COMMENT 'Update date',
    PRIMARY KEY (id),
    FOREIGN KEY (space_id) REFERENCES spc_space(id),
    FOREIGN KEY (admin_id) REFERENCES sys_user(id),
    FOREIGN KEY (user_id) REFERENCES cli_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Space Booking';

CREATE TABLE spc_booking_technician (
    id bigint NOT NULL COMMENT 'ID',
    booking_id bigint NOT NULL COMMENT 'Booking ID',
    technician_id bigint NOT NULL COMMENT 'Technician ID',
    PRIMARY KEY (id),
    FOREIGN KEY (booking_id) REFERENCES spc_booking(id),
    FOREIGN KEY (technician_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Space Booking Technician';

CREATE TABLE spc_closure (
    id bigint NOT NULL COMMENT 'ID',
    space_id bigint NOT NULL COMMENT 'Space ID',
    start_day date NOT NULL COMMENT 'Start day of booking',
    end_day date NOT NULL COMMENT 'End day of booking',
    start_time time NOT NULL COMMENT 'Start time of booking in a day',
    end_time time NOT NULL COMMENT 'End time of booking in a day',
    recur_monday tinyint NOT NULL COMMENT 'Recur on Monday, 0:No, 1:Yes',
    recur_tuesday tinyint NOT NULL COMMENT 'Recur on Tuesday, 0:No, 1:Yes',
    recur_wednesday tinyint NOT NULL COMMENT 'Recur on Wednesday, 0:No, 1:Yes',
    recur_thursday tinyint NOT NULL COMMENT 'Recur on Thursday, 0:No, 1:Yes',
    recur_friday tinyint NOT NULL COMMENT 'Recur on Friday, 0:No, 1:Yes',
    recur_saturday tinyint NOT NULL COMMENT 'Recur on Saturday, 0:No, 1:Yes',
    recur_sunday tinyint NOT NULL COMMENT 'Recur on Sunday, 0:No, 1:Yes',
    PRIMARY KEY (id),
    FOREIGN KEY (space_id) REFERENCES spc_space(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Space Closure Period';

CREATE TABLE spc_event (
    id bigint NOT NULL COMMENT 'ID',
    space_id bigint NOT NULL COMMENT 'Space ID',
    booking_id bigint NULL COMMENT 'Booking ID',
    closure_id bigint NULL COMMENT 'Closure ID',
    type tinyint NOT NULL COMMENT 'Type: 0:Booking, 1:Closure period',
    start_time DATETIME NOT NULL COMMENT 'Event start time',
    end_time DATETIME NOT NULL COMMENT 'Event end time',
    PRIMARY KEY (id),
    FOREIGN KEY (space_id) REFERENCES spc_space(id),
    FOREIGN KEY (booking_id) REFERENCES spc_booking(id),
    FOREIGN KEY (closure_id) REFERENCES spc_closure(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Space Occupied Event';

CREATE TABLE spc_upcoming_event AS
SELECT *
FROM spc_event
WHERE start_time > NOW();

TRUNCATE TABLE spc_upcoming_event;

DELIMITER |

CREATE TRIGGER trg_spc_event_insert
    AFTER INSERT ON spc_event
    FOR EACH ROW
BEGIN
    IF NEW.start_time > NOW() THEN
        INSERT INTO spc_upcoming_event (id, space_id, booking_id, closure_id, type, start_time, end_time)
        VALUES (NEW.id, NEW.space_id, NEW.booking_id, NEW.closure_id, NEW.type, NEW.start_time, NEW.end_time);
END IF;
END;

|

CREATE TRIGGER trg_spc_event_delete
    AFTER DELETE ON spc_event
    FOR EACH ROW
BEGIN
    DELETE FROM spc_upcoming_event WHERE id = OLD.id;
END;

|

SET GLOBAL event_scheduler = ON;

|

CREATE EVENT delete_past_upcoming_events
ON SCHEDULE EVERY 1 DAY
DO
BEGIN
    DELETE FROM spc_upcoming_event
    WHERE end_time < NOW();
END;

|