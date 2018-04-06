create table user
(
    username varchar(50) primary key,
    password varchar(60) not null,
    address varchar(255) not null,
    phone varchar(10) not null,
    salary double not null,
    reference_code varchar(12),
    member_type varchar(20),
);


