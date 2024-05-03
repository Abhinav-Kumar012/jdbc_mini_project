insert into employee VALUES
('John B Smith','123456789','731 Fondren, Houston, TX','M',30000,'333445555',5),
('Franklin T Wong','333445555','638 Fondren, Houston, TX','M',40000,'888665555',5),
('Alicia J Zelaya','999887777','3321 Fondren, Houston, TX','F',25000,'987654321',4),
('Jennifer S Wallace','987654321','21 Fondren, Houston, TX','F',43000,'888665555',4),
('Ramesh K Narayan','666884444','975 Fondren, Houston, TX','M',38000,'333445555',5),
('Joyce A English','453453453','5631 Fondren, Houston, TX','F',25000,'333445555',5),
('Ahmad V Jabbar','987987987','980 Fondren, Houston, TX','M',25000,'987654321',4),
('James E Borg','888665555','450 Fondren, Houston, TX','M',55000,NULL,1);

insert into department(dname,dnumber,mgr_ssn,no_of_employee) VALUES
('Research',5,'333445555',4),
('Administration',4,'987654321',3),
('Headquarters',1,'888665555',1);

insert into dept_locations(dnumber,dlocation) values
(1, 'Houston'),
(4, 'Stafford'),
(5, 'Bellaire'),
(5, 'Sugarland'),
(5, 'Houston');

insert into works_on (essn, pno, hours) values
('123456789', 1, 32.5),
('123456789', 2, 7.5),
('666884444', 3, 40.0),
('453453453', 1, 20.0),
('453453453', 2, 20.0),
('333445555', 2, 10.0),
('333445555', 3, 10.0),
('333445555', 10, 10.0),
('333445555', 20, 10.0),
('999887777', 30, 30.0),
('999887777', 10, 10.0),
('987987987', 10, 35.0),
('987987987', 30, 5.0),
('987654321', 30, 20.0),
('987654321', 20, 15.0);

insert into project(pname,pnumber,plocation,dnum) values
('ProductX', 1, 'Bellaire', 5),
('ProductY', 2, 'Sugarland', 5),
('ProductZ', 3, 'Houston', 5),
('Computerization', 10, 'Stafford', 4),
('Reorganization', 20, 'Houston', 1),
('Newbenefits', 30, 'Stafford', 4);