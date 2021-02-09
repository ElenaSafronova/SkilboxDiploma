
--Description: Initial database structure

create table hibernate_sequence (next_val bigint);
insert into hibernate_sequence values ( 1 );

create table captcha_codes(
    id integer not null auto_increment,
<<<<<<< HEAD
    code tinyint not null,
    secret_code tinyint not null,
=======
    code tinytext not null,
    secret_code tinytext not null,
>>>>>>> dev
    time datetime not null,
    primary key (id)
);

create table global_settings (
    id integer not null auto_increment,
    code varchar(255) not null,
    name varchar(255) not null,
    value varchar(255) not null,
    primary key (id)
);

create table post_comments (
    id integer not null auto_increment,
    parent_id integer,
    post_id integer not null,
    text text not null,
    time datetime not null,
    user_id integer not null,
    primary key (id)
);

create table post_votes (
    id integer not null auto_increment,
    post_id integer not null,
    time datetime not null,
    user_id integer not null,
    value tinyint not null,
    primary key (id)
);

create table posts (
    id integer not null auto_increment,
    is_active tinyint not null,
    moderator_id integer,
    moderation_status varchar(8) default 'NEW' not null,
    text text not null,
    time datetime not null,
    title varchar(255) not null,
    user_id integer not null,
    view_count integer not null,
    primary key (id)
);

create table tag2post (
    id integer not null auto_increment,
    post_id integer not null,
    tag_id integer not null,
    primary key (id)
);

create table tags (
    id integer not null auto_increment,
    name varchar(255) not null,
    primary key (id)
);

create table users (
    id integer not null auto_increment,
    code varchar(255),
<<<<<<< HEAD
    email varchar(255),
    is_moderator tinyint,
    name varchar(255),
    password varchar(255),
    photo Text,
    reg_time datetime,
=======
    email varchar(255) not null,
    is_moderator tinyint not null,
    name varchar(255) not null,
    password varchar(255) not null,
    photo Text,
    reg_time datetime not null,
>>>>>>> dev
    primary key (id)
);