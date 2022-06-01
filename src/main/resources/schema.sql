-- public.vehicle_brand definition

-- Drop table

-- DROP TABLE public.vehicle_brand;

CREATE TABLE public.vehicle_brand (
	id uuid PRIMARY KEY NOT NULL,
	name varchar(255) NULL
);

-- public.yard_card definition

-- Drop table

-- DROP TABLE public.yard_card;

CREATE TABLE public.yard_card (
	id_car uuid PRIMARY KEY NOT NULL,
	address varchar(255) NOT NULL,
	"name" varchar(255) NOT NULL,
	number_pdv varchar(255) NOT NULL,
	phone varchar(255) NOT NULL
);


-- public.executive definition

-- Drop table

-- DROP TABLE public.executive;

CREATE TABLE public.executive (
	id uuid PRIMARY KEY NOT NULL,
	address varchar(255) NULL,
	age int4 NULL,
	conventional_telephone varchar(255) NULL,
	first_name varchar(255) NULL,
	identification varchar(255) NOT NULL,
	last_name varchar(255)  NULL,
	mobile varchar(255) NULL,
	number_yard_cars varchar(255) NULL
);



-- public.client definition

-- Drop table

-- DROP TABLE public.client;

CREATE TABLE public.client (
	id uuid PRIMARY KEY NOT NULL,
	address varchar(255) NULL,
	age int4 NULL,
	birth_date date NULL,
	civil_status varchar(255) NULL,
	first_name varchar(255) NULL,
	identification varchar(255) NOT NULL,
	identification_spouse varchar(255) NULL,
	last_name varchar(255) NULL,
	phone varchar(255) NULL,
	spouse_name varchar(255) NULL,
	sujeto_credit_type varchar(255) NULL
);


-- public.vehicle definition

-- Drop table

-- DROP TABLE public.vehicle;

CREATE TABLE public.vehicle (
	id_vehicle uuid PRIMARY KEY NOT NULL,
	anio int4 NULL,
	appraise varchar(255) NULL,
	chassis_number varchar(255) NULL,
	cylinder_capacity varchar(255) NULL,
	license_plate varchar(255) NULL UNIQUE,
	model varchar(255) NULL,
	"type" varchar(255) NULL,
	vehicle_id uuid NULL,
	yard_car_id uuid NULL,
	CONSTRAINT yard_car_vehicle_fky FOREIGN KEY (vehicle_id) REFERENCES public.yard_card(id_car),
	CONSTRAINT vehicle_brand_vehicle_fky  FOREIGN KEY (yard_car_id) REFERENCES public.vehicle_brand(id)
);


-- public.vehicle foreign keys

--ALTER TABLE public.vehicle ADD CONSTRAINT fk8c9na9lt41eqjr4exvuv8rygt FOREIGN KEY (yard_car_id_car) REFERENCES public.yard_card(id_car);
--ALTER TABLE public.vehicle ADD CONSTRAINT fkaaw6dn6g4yqa9kjqo2f8pr2y5 FOREIGN KEY (vehicle_brand_id) REFERENCES public.vehicle_brand(id);




-- public.asignation_client_patio definition

-- Drop table

-- DROP TABLE public.asignation_client_patio;

CREATE TABLE public.asignation_client_patio (
	id_asignation uuid PRIMARY KEY NOT NULL,
	created_asignation date NULL,
	client_id uuid NOT NULL,
	yard_car_id uuid NOT NULL,
 CONSTRAINT yard_car_asignation_client_patio_fk FOREIGN KEY (yard_car_id) REFERENCES public.yard_card(id_car),
 CONSTRAINT client_asignation_client_patio FOREIGN KEY (client_id) REFERENCES public.client(id)
);


-- public.asignation_client_patio foreign keys

--ALTER TABLE public.asignation_client_patio ADD CONSTRAINT fk1j7j18mf5dtcybstitdm41rpm FOREIGN KEY (yard_car_id_car) REFERENCES public.yard_card(id_car);
--ALTER TABLE public.asignation_client_patio ADD CONSTRAINT fk8nm9si6dd1n47e2aaryai0chm FOREIGN KEY (client_id) REFERENCES public.client(id);


----------------------------
-- public.request_credit definition

-- Drop table

-- DROP TABLE public.request_credit;

CREATE TABLE public.request_credit (
	id_request_credit uuid PRIMARY KEY  NOT NULL,
	date_elaboration date NULL,
	entry numeric(19, 2) NULL,
	months_term int4 NULL,
	observation varchar(255) NULL,
	quotas int4 NULL,
	status varchar(255) NULL,
	client_id uuid NULL,
	executive_id uuid NULL,
	id_vehicle uuid NULL,
	yard_car_id uuid NULL,
	CONSTRAINT request_credit_yard_card_fk FOREIGN KEY (yard_car_id) REFERENCES public.yard_card(id_car),
	CONSTRAINT request_credit_executive_fk FOREIGN KEY (executive_id) REFERENCES public.executive(id),
	CONSTRAINT request_credit_client_fk FOREIGN KEY (client_id) REFERENCES public.client(id),
	CONSTRAINT request_credit_vehicle_fk FOREIGN KEY (id_vehicle) REFERENCES public.vehicle(id_vehicle)
);


-- public.request_credit foreign keys

--ALTER TABLE public.request_credit ADD CONSTRAINT fkbwvl73loo6c8wwifvpewd1j23 FOREIGN KEY (yard_car_id_car) REFERENCES public.yard_card(id_car);
--ALTER TABLE public.request_credit ADD CONSTRAINT fkep1u31soc17bu5n4sm7cs3wx2 FOREIGN KEY (executive_id) REFERENCES public.executive(id);
--ALTER TABLE public.request_credit ADD CONSTRAINT fksb29921uc3kb5xustsdrs7k8i FOREIGN KEY (client_id) REFERENCES public.client(id);
--ALTER TABLE public.request_credit ADD CONSTRAINT fksk7s624juyweetgev6xpaxm5d FOREIGN KEY (vehicle_id_vehicle) REFERENCES public.vehicle(id_vehicle);
