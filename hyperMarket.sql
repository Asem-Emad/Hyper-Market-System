create database hyperMarket;
use hyperMarket;
--we have 6 tables in the project--

/*1st table identifies the rule of every user
there are 4 rules.. Admin..Inventory employee..Market employee..Sales employee*/
create table rules(
rule_id int PRIMARY KEY not null,
rule_name varchar(50) not null
);
/*adding the 4 rules in the table*/
insert into rules values(1,'Admin');
insert into rules values(2,'Inventory');
insert into rules values(3,'Market');
insert into rules values(4,'Sales');

/*2nd table identifies the attributes for each user..>> id,name,passwaord and role_id*/
create table users(
user_id int PRIMARY KEY not null,
user_name varchar(100) not null,
user_password varchar(5) not null,
roleID int foreign key references rules(rule_id) on delete no action on  update no action
);

/*3rd table is related to inventory employees, he get a notification where quan< minimum quan OR the product is expired*/
create table notification(
inventory_id int foreign key references users(user_id) on delete no action on  update no action,
notf varchar(1000)
);

/*4th table identifies the attributes for each product
..>>id, name, price, quantity, expire date and inventory employee who add this product */
create table product(
prod_id int PRIMARY KEY not null,
prod_name varchar(50) not null,
prod_price float,
prod_quant int,
prod_ex_date varchar(10),
inv_emp_id int foreign key references users(user_id) on delete cascade on  update cascade
);

/*5th table contains the information about the order/ billing, it contains the id, date, total cost, sales employee who made the order*/
create table orders(
order_id int PRIMARY KEY not null,
order_date varchar(10),
total_cost float, --make a query--
seller_id int foreign key references users(user_id) on delete cascade on  update cascade
);

/*6th table contains the order details*/
create table order_details(
item_id int PRIMARY KEY not null,
item_quant int,
orderID int foreign key references orders(order_id) on delete no action on  update no action,
productID int foreign key references product(prod_id) on delete no action on  update no action
);

/* 7th table related to the offers(special discounts) made in a special date*/
create table offers(
--make a query to relate the offers with the order date. Make the discount then update the price --
offer_id int PRIMARY KEY not null,
offer_date varchar(10),
offer_discount float
);
