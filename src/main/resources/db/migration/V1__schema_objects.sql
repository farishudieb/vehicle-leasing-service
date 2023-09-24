create table vehicles(
id bigint primary key,
brand varchar(100) not null,
model varchar(100) not null,
model_year int not null,
vin varchar(200) unique,
price decimal(10,2) not null,
created_at timestamp not null,
last_modified_at timestamp
);

create table customers(
id bigint primary key,
first_name varchar(200) not null,
last_name varchar(200) not null,
birth_date date not null,
created_at timestamp not null,
last_modified_at timestamp
);

create table leasing_contracts(
id bigint primary key,
contract_number varchar(200) not null unique,
monthly_rate decimal(8,2),
customer_id bigint,
vehicle_id bigint,
created_at timestamp not null,
last_modified_at timestamp,
constraint customer_id_fk foreign key(customer_id) references customers(id),
constraint vehicle_id_fk foreign key(vehicle_id) references vehicles(id)
);