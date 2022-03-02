drop database if exists `smart_led`;
create database `smart_led`;
use `smart_led`;

create table `user`(
`id` int unsigned auto_increment primary key,
`login_id` varchar(32) unique not null, 
`login_pwd` varchar(16) not null
);
create index `idx_user_login` on `user`(`login_id`, `login_pwd`);

create table `mcu`(
`id` int unsigned auto_increment primary key,
`mac` char(17) unique not null,
`token` char(32) not null,
`create_time` timestamp default now(),
`last_time` timestamp default now(),
`user_id` int unsigned,
constraint `fk_mcu_user` foreign key(`user_id`) references `user`(`id`)
);
create index `idx_mcu_mac_token` on `mcu`(`mac`, `token`);

create table `data`(
`id` int unsigned auto_increment primary key,
`value` varchar(128),
`ip` varchar(50) not null,
`create_time` timestamp default now(),
`mcu_id` int unsigned,
constraint `fk_data_mcu` foreign key(`mcu_id`) references `mcu`(`id`)
);
create index `idx_data_mcu_time` on `data`(`mcu_id`, `create_time`);