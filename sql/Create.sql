create table employee(
	name varchar(50),
	ssn char(9),
	address varchar(30),
	sex char(1),
	salary decimal(10,2),
	super_ssn char(9),
	dno smallint,
	constraint pk_employee PRIMARY KEY (ssn)
);

create table department(
	dname varchar(30),
	dnumber smallint,
	mgr_ssn char(9),
	no_of_employee int,
	constraint pk_department PRIMARY KEY (dnumber)
);

create table dept_locations(
	dnumber smallint,
	dlocation varchar(20),
	constraint pk_dept_loc PRIMARY KEY (dnumber,dlocation)	
);

create table project(
	pname varchar(30),
	pnumber smallint,
	plocation varchar(30),
	dnum smallint,
	constraint pk_project PRIMARY KEY (pnumber)
);

create table works_on(
	essn char(9),
	pno smallint,
	hours decimal(4,2),
	constraint pk_works_on PRIMARY KEY (essn, pno)
);
