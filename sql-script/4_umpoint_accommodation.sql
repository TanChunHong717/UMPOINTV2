CREATE TABLE acc_category (
    id bigint NOT NULL COMMENT 'ID',
    name varchar(50) NOT NULL COMMENT 'Name',
    PRIMARY KEY (ID),
    UNIQUE INDEX (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Accommodation Category';

CREATE TABLE acc_tag (
    id bigint NOT NULL COMMENT 'ID',
    tag_name varchar(50) NOT NULL COMMENT 'Tag name',
    PRIMARY KEY (id),
    UNIQUE INDEX (tag_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Accommodation Tag';

CREATE TABLE acc_booking_rule (
    id bigint NOT NULL COMMENT 'ID',
    open_for_staff tinyint NOT NULL COMMENT '0:Staff not allow to book 1:Staff allow to book',
    open_for_student tinyint NOT NULL COMMENT '0:Student not allow to book 1:Student allow to book',
    open_for_public tinyint NOT NULL COMMENT '0:Public not allow to book 1:Public allow to book',
    holiday_available tinyint NOT NULL COMMENT 'Availability in public holiday, 1: Available, 0: Close',
    approval_required tinyint NOT NULL COMMENT '0:Automatic approve 1:Require admin approve',
    max_booking_advance_day decimal(3,0) NOT NULL COMMENT 'Max booking advance day',
    min_booking_advance_day decimal(3,0) NOT NULL COMMENT 'Min booking advance day',
    max_reservation_day decimal(3, 0) NOT NULL COMMENT 'Maximum reservation day',
    min_reservation_day decimal(3, 0) NOT NULL COMMENT 'Minimum reservation day',
    max_technician_number decimal(3, 0) NOT NULL COMMENT 'Maximum number of technician',
    technician_price decimal(10, 2) NOT NULL COMMENT 'Price per technician',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Accommodation Booking Rule';

CREATE TABLE acc_accommodation (
    id bigint NOT NULL COMMENT 'ID',
    status tinyint NOT NULL COMMENT 'Status 0:Suspend 1:Normal',
    name varchar(250) NOT NULL COMMENT 'Name',
    cat_id bigint NOT NULL COMMENT 'Category ID',
    dept_id bigint NOT NULL COMMENT 'Department ID',
    address varchar(250) NOT NULL COMMENT 'Address',
    description varchar(2500) NULL COMMENT 'Description',
    facilities varchar(250) NULL COMMENT 'Facilities',
    capacity decimal(3,0) NOT NULL COMMENT 'Max capacity',
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

CREATE TABLE acc_booking (
    id bigint NOT NULL COMMENT 'ID',
    status tinyint NOT NULL COMMENT 'Status: 0:Pending, 1:Reject, 2:Approve(Pending Payment), 3:Approve(Payment Complete), 4:Cancel',
    accommodation_id bigint NOT NULL COMMENT 'Accommodation ID',
    admin_id bigint NULL COMMENT 'Admin that approve/reject, user will contact this admin rather manager if umpoint.booking.accommodation.find-approve-admin-first=true',
    user_id bigint NOT NULL COMMENT 'User ID',
    event varchar(250) NOT NULL COMMENT 'Description of the event or purpose for the booking',
    payment_amount decimal(10,2) NOT NULL COMMENT 'Amount need to be pay',
    start_day date NOT NULL COMMENT 'Start day of booking',
    end_day date NOT NULL COMMENT 'End day of booking',
    technician_number decimal(3,0) NOT NULL COMMENT 'Number of technician',
    create_date datetime NOT NULL COMMENT 'Create date',
    update_date datetime NOT NULL COMMENT 'Update date',
    PRIMARY KEY (id),
    FOREIGN KEY (accommodation_id) REFERENCES acc_accommodation(id),
    FOREIGN KEY (admin_id) REFERENCES sys_user(id),
    FOREIGN KEY (user_id) REFERENCES cli_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Accommodation Booking';

CREATE TABLE acc_booking_technician (
    id bigint NOT NULL COMMENT 'ID',
    booking_id bigint NOT NULL COMMENT 'Booking ID',
    technician_id bigint NOT NULL COMMENT 'Technician ID',
    PRIMARY KEY (id),
    FOREIGN KEY (booking_id) REFERENCES acc_booking(id),
    FOREIGN KEY (technician_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Accommodation Booking Technician';

CREATE TABLE acc_closure (
    id bigint NOT NULL COMMENT 'ID',
    accommodation_id bigint NOT NULL COMMENT 'Accommodation ID',
    start_day date NOT NULL COMMENT 'Start day of booking',
    end_day date NOT NULL COMMENT 'End day of booking',
    PRIMARY KEY (id),
    FOREIGN KEY (accommodation_id) REFERENCES acc_accommodation(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Accommodation Closure Period';

CREATE TABLE acc_event (
    id bigint NOT NULL COMMENT 'ID',
    accommodation_id bigint NOT NULL COMMENT 'Accommodation ID',
    booking_id bigint NULL COMMENT 'Booking ID',
    closure_id bigint NULL COMMENT 'Closure ID',
    type tinyint NOT NULL COMMENT 'Type: 0:Booking, 1:Closure period',
    start_time DATETIME NOT NULL COMMENT 'Event start time',
    end_time DATETIME NOT NULL COMMENT 'Event end time',
    PRIMARY KEY (id),
    FOREIGN KEY (accommodation_id) REFERENCES acc_accommodation(id),
    FOREIGN KEY (booking_id) REFERENCES acc_booking(id),
    FOREIGN KEY (closure_id) REFERENCES acc_closure(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Accommodation Occupied Event';

CREATE TABLE acc_upcoming_event AS
SELECT *
FROM acc_event
WHERE start_time > NOW();

TRUNCATE TABLE acc_upcoming_event;

DELIMITER |

CREATE TRIGGER trg_acc_event_insert
    AFTER INSERT ON acc_event
    FOR EACH ROW
BEGIN
    IF NEW.start_time > NOW() THEN
        INSERT INTO acc_upcoming_event (id, accommodation_id, booking_id, closure_id, type, start_time, end_time)
        VALUES (NEW.id, NEW.accommodation_id, NEW.booking_id, NEW.closure_id, NEW.type, NEW.start_time, NEW.end_time);
    END IF;
END;

|

CREATE TRIGGER trg_acc_event_delete
    AFTER DELETE ON acc_event
    FOR EACH ROW
BEGIN
    DELETE FROM acc_upcoming_event WHERE id = OLD.id;
END;

|

SET GLOBAL event_scheduler = ON;

|

CREATE EVENT delete_past_acc_upcoming_events
ON SCHEDULE EVERY 1 DAY
DO
BEGIN
    DELETE FROM acc_upcoming_event
    WHERE end_time < NOW();
END;

|