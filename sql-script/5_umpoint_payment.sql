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
    amount decimal(10,2) NOT NULL COMMENT 'Payment Amount',
    method_id bigint NULL COMMENT 'Payment Method ID',
    date datetime NULL COMMENT 'Payment date',
    PRIMARY KEY (id),
    FOREIGN KEY (method_id) REFERENCES payment_method(id),
    FOREIGN KEY (booking_id) REFERENCES spc_booking(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Space Payment';

CREATE TABLE spc_payment_item (
    `id` BIGINT NOT NULL COMMENT 'ID',
    `payment_id` BIGINT NOT NULL COMMENT 'Payment ID',
    `item_name` VARCHAR(255) NOT NULL COMMENT 'Payment Item Name',
    `item_amount` INT NOT NULL COMMENT 'Payment Item Count',
    `item_price` DECIMAL(10,2) NOT NULL COMMENT 'Price per item (total = amount * price)' ,
    PRIMARY KEY (`id`),
    FOREIGN KEY (payment_id) REFERENCES spc_payment(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Space Payment Itemized';

CREATE TABLE svc_payment (
    id bigint NOT NULL COMMENT 'ID',
    status tinyint NOT NULL COMMENT 'Status: 0:Pending, 1:Success, 2:Failed, 3:Refund',
    booking_id bigint NOT NULL COMMENT 'Space/Service/Accommodation Booking ID',
    amount decimal(10,2) NOT NULL COMMENT 'Payment Amount',
    method_id bigint NULL COMMENT 'Payment Method ID',
    date datetime NULL COMMENT 'Payment date',
    PRIMARY KEY (id),
    FOREIGN KEY (method_id) REFERENCES payment_method(id),
    FOREIGN KEY (booking_id) REFERENCES svc_booking(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Service Payment';

CREATE TABLE svc_payment_item (
    `id` BIGINT NOT NULL COMMENT 'ID',
    `payment_id` BIGINT NOT NULL COMMENT 'Payment ID',
    `item_name` VARCHAR(255) NOT NULL COMMENT 'Payment Item Name',
    `item_amount` INT NOT NULL COMMENT 'Payment Item Count',
    `item_price` DECIMAL(10,2) NOT NULL COMMENT 'Price per item (total = amount * price)' ,
    PRIMARY KEY (`id`),
    FOREIGN KEY (payment_id) REFERENCES svc_payment(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Service Payment Itemized';

CREATE TABLE acc_payment (
    id bigint NOT NULL COMMENT 'ID',
    status tinyint NOT NULL COMMENT 'Status: 0:Pending, 1:Success, 2:Failed, 3:Refund',
    booking_id bigint NOT NULL COMMENT 'Space/Service/Accommodation Booking ID',
    amount decimal(10,2) NOT NULL COMMENT 'Payment Amount',
    method_id bigint NULL COMMENT 'Payment Method ID',
    date datetime NULL COMMENT 'Payment date',
    PRIMARY KEY (id),
    FOREIGN KEY (method_id) REFERENCES payment_method(id),
    FOREIGN KEY (booking_id) REFERENCES acc_booking(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Accommodation Payment';

CREATE TABLE acc_payment_item (
    `id` BIGINT NOT NULL COMMENT 'ID',
    `payment_id` BIGINT NOT NULL COMMENT 'Payment ID',
    `item_name` VARCHAR(255) NOT NULL COMMENT 'Payment Item Name',
    `item_amount` INT NOT NULL 'Payment Item Count',
    `item_price` DECIMAL(10,2) NOT NULL COMMENT 'Price per item (total = amount * price)' ,
    PRIMARY KEY (`id`),
    FOREIGN KEY (payment_id) REFERENCES acc_payment(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Accommodation Payment Itemized';