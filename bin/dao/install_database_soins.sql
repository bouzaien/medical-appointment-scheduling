-- Titre :             install_database_soins.sql
-- Version :           0.2
-- Date création :     10 octobre 2018
-- Date modification : 6 mars 2019
-- Auteur :            Philippe Tanguy & Laurent Brisson
-- Description :

-- +----------------------------------------------------------------------------------------------+
-- | Tables drop                                                                                  |
-- +----------------------------------------------------------------------------------------------+

drop table if exists prescription;
drop table if exists visite;
drop table if exists patient;
drop table if exists medecin;


-- +----------------------------------------------------------------------------------------------+
-- | Tables creation                                                                              |
-- +----------------------------------------------------------------------------------------------+

create table medecin
(
  medecin_id	 serial primary key,
  rpps         varchar(12) not null,
  nom          varchar(20) not null,
  prenom       varchar(20),
  adresse      varchar(256),
  telephone    varchar(15),
  specialite   varchar(20)
);

create table patient
(
  patient_id              serial primary key,
  numsecu                 varchar(15),
  genre                   varchar(10),
  date_naissance          varchar(20),
  nom                     varchar(20) not null,
  prenom                  varchar(20),
  rattachement            integer references patient,
  medecin_referent        integer references medecin
);

create table visite
(
	medecin		  integer,
	patient		  integer,
	date_visite	date,
	prix			  real,
	constraint pk_visite primary key(medecin, patient, date_visite),
	constraint fk_medecin foreign key(medecin) references medecin,
	constraint fk_patient foreign key(patient) references patient
);

create table prescription
(
	prescription_id	 serial,
	medicament		   varchar(30),
	medecin		       integer,
	patient		       integer,
	date_visite	     date,
	duree			       integer,
	posologie		     real,
	modalites		     varchar(20),
	constraint pk_prescription primary key(prescription_id, medicament),
	constraint fk_visite foreign key(medecin, patient, date_visite) references visite
);

-- +----------------------------------------------------------------------------------------------+
-- | Some data for testing purpose                                                                |
-- +----------------------------------------------------------------------------------------------+

-- Doctors

insert into medecin values (DEFAULT, '100000000001', 'Grey', 'Meredith', 'Centre Hospitalier Régional et Universitaire de Brest', '02 98 22 33 33', 'Généraliste');
insert into medecin values (DEFAULT, '100000000002', 'Shepherd', 'Derek', 'Centre Hospitalier Régional et Universitaire de Brest', '02 98 22 33 33', 'Généraliste');
insert into medecin values (DEFAULT, '100000000003', 'Karev', 'Alex', 'Centre Hospitalier Régional et Universitaire de Brest', '02 98 22 33 33', 'Généraliste');
insert into medecin values (DEFAULT, '100000000004', 'Stevens', 'Izzie', 'Centre Hospitalier Régional et Universitaire de Brest', '02 98 22 33 33', 'Généraliste');
insert into medecin values (DEFAULT, '100000000005', 'Bailey', 'Miranda', 'Centre Hospitalier Régional et Universitaire de Brest', '02 98 22 33 33', 'Généraliste');
insert into medecin values (DEFAULT, '100000000006', 'Webber', 'Richard', 'Centre Hospitalier Régional et Universitaire de Brest', '02 98 22 33 33', 'Généraliste');
insert into medecin values (DEFAULT, '100000000007', 'Yang', 'Cristina', 'Centre Hospitalier Régional et Universitaire de Brest', '02 98 22 33 33', 'Dermatologue');
insert into medecin values (DEFAULT, '100000000008', 'Omalley', 'George', 'Centre Hospitalier Régional et Universitaire de Brest', '02 98 22 33 33', 'Chirurgien');
insert into medecin values (DEFAULT, '100000000009', 'Burke', 'Preston', 'Centre Hospitalier Régional et Universitaire de Brest', '02 98 22 33 33', 'Chirurgien');
insert into medecin values (DEFAULT, '100000000010', 'Montgomery-Shepherd', 'Addison', 'Centre Hospitalier Régional et Universitaire de Brest', '02 98 22 33 33', 'Cardiologue');
insert into medecin values (DEFAULT, '100000000011', 'Torres', 'Callie', 'Centre Hospitalier Régional et Universitaire de Brest', '02 98 22 33 33', 'Dermatologue');
insert into medecin values (DEFAULT, '100000000012', 'Sloan', 'Mark', 'Centre Hospitalier Régional et Universitaire de Brest', '02 98 22 33 33', 'Allergologue');


-- Patients

insert into patient values (DEFAULT, '1040529012561', 'Male', '22/05/2004','Dempsey', 'Justin', null, 1);
insert into patient values (DEFAULT, '2080229931002', 'Female', '01/02/2008', 'Dempsey', 'Ellen', 1, 1);
insert into patient values (DEFAULT, '1610329028069',  'Male', '02/03/1961', 'Dempsey', 'Patrick',  1, 1);

insert into patient values (DEFAULT, '1560935006999', 'Male', '12/09/1956', 'Knight', 'T.R.', null, 2);
insert into patient values (DEFAULT, '2170335112001', 'Female', '02/03/2017', 'Knight', 'Katherine', 4, 1);
insert into patient values (DEFAULT, '2991135006001', 'Female', '05/11/1999', 'Knight', 'Sandra', 4, 4);
insert into patient values (DEFAULT, '2060835112059', 'Female', '25/08/2006', 'Knight', 'Kate',  4, null);
insert into patient values (DEFAULT, '1100135112456', 'Male', '17/01/2010', 'Knight', 'Isaiah',  4, null);

insert into patient values (DEFAULT, '1690672111035', 'Male', '24/06/1969', 'Dane', 'Eric',  null, null);
insert into patient values (DEFAULT, '1800899112023', 'Male', '24/08/1980', 'Dane', 'James Jr.',  9, 2);

insert into patient values (DEFAULT, '2410606006006', 'Female', '21/06/1941', 'Ramirez', 'Sara',  null, 2);
insert into patient values (DEFAULT, '2010106006956', 'Female', '12/01/2001', 'Ramirez', 'Chandra',  11, null);

-- Visites

insert into visite values (1, 1,  to_date('02/01/2018','dd/mm/yyyy'), 23);
insert into visite values (1, 2,  to_date('15/01/2018','dd/mm/yyyy'), 23);
insert into visite values (1, 2,  to_date('22/01/2018','dd/mm/yyyy'), 23);
insert into visite values (1, 3,  to_date('02/01/2018','dd/mm/yyyy'), 23);
insert into visite values (1, 3,  to_date('12/01/2018','dd/mm/yyyy'), 23);
insert into visite values (1, 3,  to_date('22/01/2018','dd/mm/yyyy'), 23);
insert into visite values (11,4,  to_date('01/01/2018','dd/mm/yyyy'), 23);
insert into visite values (11,4,  to_date('15/01/2018','dd/mm/yyyy'), 23);
insert into visite values (11,4,  to_date('21/01/2018','dd/mm/yyyy'), 23);
insert into visite values (11,4,  to_date('31/01/2018','dd/mm/yyyy'), 23);
insert into visite values ( 3,10, to_date('01/01/2018','dd/mm/yyyy'), 23);
insert into visite values ( 4,10, to_date('11/01/2018','dd/mm/yyyy'), 23);
insert into visite values ( 5,10, to_date('21/01/2018','dd/mm/yyyy'), 23);
insert into visite values ( 6,10, to_date('31/01/2018','dd/mm/yyyy'), 23);
insert into visite values (4, 6,  to_date('02/01/2018','dd/mm/yyyy'), 23);
insert into visite values (5, 6,  to_date('02/01/2018','dd/mm/yyyy'), 30);
insert into visite values (1, 6,  to_date('02/01/2018','dd/mm/yyyy'), 23);
insert into visite values (10,6,  to_date('05/01/2018','dd/mm/yyyy'), 70);
insert into visite values (4, 6,  to_date('05/01/2018','dd/mm/yyyy'), 23);
insert into visite values (11,6,  to_date('07/01/2018','dd/mm/yyyy'), 40);
insert into visite values (12,6,  to_date('07/01/2018','dd/mm/yyyy'), 23);
insert into visite values (12,6,  to_date('09/01/2018','dd/mm/yyyy'), 40);
insert into visite values (6, 8,  to_date('09/01/2018','dd/mm/yyyy'), 23);
insert into visite values (6, 8,  to_date('19/01/2018','dd/mm/yyyy'), 23);
insert into visite values (6, 8,  to_date('23/01/2018','dd/mm/yyyy'), 23);
insert into visite values (6, 8,  to_date('27/01/2018','dd/mm/yyyy'), 23);
insert into visite values (5, 9,  to_date('27/01/2018','dd/mm/yyyy'), 30);
insert into visite values (7, 9,  to_date('27/01/2018','dd/mm/yyyy'), 40);
insert into visite values (8, 9,  to_date('27/01/2018','dd/mm/yyyy'), 23);
insert into visite values (9, 9,  to_date('27/01/2018','dd/mm/yyyy'), 23);
insert into visite values (10,9,  to_date('27/01/2018','dd/mm/yyyy'), 70);
insert into visite values (11,9,  to_date('27/01/2018','dd/mm/yyyy'), 40);
insert into visite values (12,9,  to_date('27/01/2018','dd/mm/yyyy'), 40);

-- Prescriptions

insert into prescription values(DEFAULT, 'ventoline', 1, 3,      to_date('02/01/2018', 'dd/mm/yyyy'), 90, 8, 'selon besoins');
insert into prescription values(DEFAULT, 'becotide', 1, 3,       to_date('02/01/2018', 'dd/mm/yyyy'), 180, 6, '2 bouffees');
insert into prescription values(DEFAULT, 'doliprane 500', 1, 3,  to_date('22/01/2018', 'dd/mm/yyyy'), 180, 6, '');
