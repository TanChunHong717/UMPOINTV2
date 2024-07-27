CREATE TABLE spc_space (
    id bigint NOT NULL COMMENT 'ID',
    name varchar(50) NOT NULL COMMENT 'Name',
    cat_id bigint NOT NULL COMMENT 'Category ID',
    dept_id bigint NOT NULL COMMENT 'Department ID',
    address varchar(250) NOT NULL COMMENT 'Address',
    `desc` varchar(250) COMMENT 'Description',
    facilities varchar(250) COMMENT 'Facilities',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Space';

CREATE TABLE spc_category (
    id bigint NOT NULL COMMENT 'ID',
    name varchar(50) NOT NULL COMMENT 'Name',
    PRIMARY KEY (ID),
    UNIQUE INDEX (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Space Category';

CREATE TABLE spc_tag (
    id bigint NOT NULL COMMENT 'ID',
    tag_name varchar(20) NOT NULL COMMENT 'Tag name',
    PRIMARY KEY (id),
    UNIQUE INDEX (tag_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Space Tag';

CREATE TABLE spc_space_tag (
    space_id bigint NOT NULL COMMENT 'Space ID',
    tag_id bigint NOT NULL COMMENT 'Tag ID',
    PRIMARY KEY (space_id, tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Space tag relationship';

CREATE TABLE spc_image (
    id bigint NOT NULL COMMENT 'ID',
    space_id bigint NOT NULL COMMENT 'Space ID',
    space_url varchar(250) NOT NULL COMMENT 'Space url',
    space_path varchar(250) NOT NULL COMMENT 'Space path',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Space Image';