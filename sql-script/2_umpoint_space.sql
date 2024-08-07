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

CREATE TABLE spc_space (
    id bigint NOT NULL COMMENT 'ID',
    name varchar(50) NOT NULL COMMENT 'Name',
    cat_id bigint NOT NULL COMMENT 'Category ID',
    dept_id bigint NOT NULL COMMENT 'Department ID',u
    description varchar(250) COMMENT 'Description',
    facilities varchar(250) COMMENT 'Facilities',
    creator bigint NOT NULL COMMENT 'Creator',
    create_date NOT NULL datetime COMMENT 'Create date',
    updater NOT NULL bigint COMMENT 'Updater',
    update_date NOT NULL datetime COMMENT 'Update date',
    PRIMARY KEY (id),
    FOREIGN KEY (cat_id) REFERENCES spc_category(id),
    FOREIGN KEY (dept_id) REFERENCES sys_dept(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Space';

CREATE TABLE spc_space_tag (
    space_id bigint NOT NULL COMMENT 'Space ID',
    tag_id bigint NOT NULL COMMENT 'Tag ID',
    PRIMARY KEY (space_id, tag_id),
    FOREIGN KEY (space_id) REFERENCES spc_space(id),
    FOREIGN KEY (tag_id) REFERENCES spc_tag(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Space tag relationship';

CREATE TABLE spc_image (
    id bigint NOT NULL COMMENT 'ID',
    space_id bigint NOT NULL COMMENT 'Space ID',
    image_url varchar(250) NOT NULL COMMENT 'Image url',
    PRIMARY KEY (id),
    FOREIGN KEY (space_id) REFERENCES spc_space(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Space Image';

CREATE TABLE spc_availability (
    id bigint NOT NULL COMMENT 'ID',
    space_id bigint NOT NULL COMMENT 'Space ID',
    year decimal(4,0) NOT NULL COMMENT 'Year',
    availability BLOB NOT NULL COMMENT 'Availability of space, consist of 366*24*2 bit, 1 represent available in specific hour in one year'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Space Availability';