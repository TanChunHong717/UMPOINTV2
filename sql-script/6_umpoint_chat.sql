CREATE TABLE `chat_room` (
    `id` bigint(20) NOT NULL COMMENT 'Room ID',
    `name` varchar(255) NOT NULL COMMENT 'Room name',
    `status` tinyint(4) NOT NULL COMMENT 'Status: 0:Created(No admin), 1:Open(Admin assigned), 2:Closed, 3:Resolved, 4:Reported',
    `facility_type` tinyint(4) NOT NULL COMMENT 'Type: 0:Space, 1:Service, 2:Accommodation',
    `facility_id` bigint(20) NOT NULL COMMENT 'Facility ID',
    `initiate_user_id` bigint(20) NOT NULL COMMENT 'User ID that created room',
    `assigned_admin_id` bigint(20) DEFAULT NULL COMMENT 'Admin user ID',
    `created_at` datetime NOT NULL COMMENT 'Creation Date',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`initiate_user_id`) REFERENCES `cli_user` (`id`),
    FOREIGN KEY (`assigned_admin_id`) REFERENCES `sys_user` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Chat room';

CREATE TABLE `chat_message` (
    `id` bigint(20) NOT NULL COMMENT 'Message ID',
    `chat_room_id` bigint(20) NOT NULL COMMENT 'Chat room ID',
    `sender_type` tinyint NOT NULL COMMENT 'Sender user type: 0:System, 1:Bot, 2:User, 3:Admin',
    `user_id` bigint(20) NULL COMMENT 'Sender user ID',
    `admin_id` bigint(20) NULL COMMENT 'Sender admin ID',
    `message` text NOT NULL COMMENT 'Message body',
    `reply_message_id` bigint(20) NULL COMMENT 'Message ID that is responding to',
    `created_at` datetime NOT NULL COMMENT 'Creation Date',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`chat_room_id`) REFERENCES `chat_room` (`id`),
    FOREIGN KEY (`reply_message_id`) REFERENCES `chat_message` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Chat message';

CREATE TABLE `chat_message_attachment` (
    `id` bigint(20) NOT NULL COMMENT 'Attachment ID',
    `message_id` bigint(20) NOT NULL COMMENT 'Message ID',
    `type` varchar(20) NOT NULL COMMENT 'Attachment type',
    `url` varchar(250) NOT NULL COMMENT 'Image url',
    PRIMARY KEY (id),
    FOREIGN KEY (`message_id`) REFERENCES `chat_message` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Chat message attachment';

CREATE TABLE `chat_reported_user` (
    `id` bigint(20) NOT NULL COMMENT 'ID',
    `reported_user_id` bigint(20) NOT NULL COMMENT 'User ID that is reported',
    `chat_room_id` bigint(20) NOT NULL COMMENT 'Chat room ID',
    `reason` text NOT NULL COMMENT 'Reason',
    `created_at` datetime NOT NULL COMMENT 'Creation Date',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`chat_room_id`) REFERENCES `chat_room` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Report user in chat';