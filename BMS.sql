create database BMS;
use BMS;
drop table managers;
create table admins(
username varchar(10),
password varchar(10));
insert into admins (username,password) values(123,123);
