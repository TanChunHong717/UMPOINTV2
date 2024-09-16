CREATE TABLE payment_method (
    id bigint NOT NULL COMMENT 'ID',
    method varchar(20) NOT NULL COMMENT 'Method',
    PRIMARY KEY (id),
    UNIQUE INDEX (method)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Payment Method';

CREATE TABLE spc_payment (
    id bigint NOT NULL COMMENT 'ID',
    status tinyint NOT NULL COMMENT 'Status: 0:Pending, 1:Success, 2:Failed, 3:Refund',
    booking_id bigint NOT NULL COMMENT 'Space/Service/Accommodation Booking ID',
    method_id bigint NOT NULL COMMENT 'Payment Method ID',
    amount decimal(10,2) NOT NULL COMMENT 'Payment Amount',
    date datetime NOT NULL COMMENT 'Payment date',
    PRIMARY KEY (id),
    FOREIGN KEY (method_id) REFERENCES payment_method(id),
    FOREIGN KEY (booking_id) REFERENCES spc_booking(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Space Payment';

CREATE TABLE svc_payment (
    id bigint NOT NULL COMMENT 'ID',
    status tinyint NOT NULL COMMENT 'Status: 0:Pending, 1:Success, 2:Failed, 3:Refund',
    booking_id bigint NOT NULL COMMENT 'Space/Service/Accommodation Booking ID',
    method_id bigint NOT NULL COMMENT 'Payment Method ID',
    amount decimal(10,2) NOT NULL COMMENT 'Payment Amount',
    date datetime NOT NULL COMMENT 'Payment date',
    PRIMARY KEY (id),
    FOREIGN KEY (method_id) REFERENCES payment_method(id),
    FOREIGN KEY (booking_id) REFERENCES svc_booking(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Service Payment';

CREATE TABLE acc_payment (
    id bigint NOT NULL COMMENT 'ID',
    status tinyint NOT NULL COMMENT 'Status: 0:Pending, 1:Success, 2:Failed, 3:Refund',
    booking_id bigint NOT NULL COMMENT 'Space/Service/Accommodation Booking ID',
    method_id bigint NOT NULL COMMENT 'Payment Method ID',
    amount decimal(10,2) NOT NULL COMMENT 'Payment Amount',
    date datetime NOT NULL COMMENT 'Payment date',
    PRIMARY KEY (id),
    FOREIGN KEY (method_id) REFERENCES payment_method(id),
    FOREIGN KEY (booking_id) REFERENCES acc_booking(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Accommodation Payment';