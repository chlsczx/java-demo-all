drop table if exists `user`;
create table user
(
    id   bigint unsigned auto_increment primary key,
    name varchar(255) not null,
    age  int unsigned not null
) engine = InnoDB