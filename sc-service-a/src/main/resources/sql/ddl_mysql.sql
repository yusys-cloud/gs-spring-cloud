CREATE TABLE seata_state_inst (
	id varchar(255) NOT NULL,
	machine_inst_id varchar(255) NOT NULL,
	name varchar(255) NOT NULL,
	type varchar(255),
	service_name varchar(255),
	service_method varchar(255),
	service_type varchar(255),
	business_key varchar(255),
	state_id_compensated_for varchar(255),
	state_id_retried_for varchar(255),
	gmt_started timestamp NOT NULL,
	is_for_update tinyint(1),
	input_params longtext,
	output_params longtext,
	status varchar(255) NOT NULL,
	excep longblob,
	gmt_updated timestamp,
	gmt_end timestamp,
	PRIMARY KEY (id)
) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE seata_state_machine_def (
	id varchar(255) NOT NULL,
	name varchar(255) NOT NULL,
	tenant_id varchar(255) NOT NULL,
	app_name varchar(255) NOT NULL,
	type varchar(255),
	comment_ varchar(255),
	ver varchar(255) NOT NULL,
	gmt_create timestamp NOT NULL,
	status varchar(255) NOT NULL,
	content longtext,
	recover_strategy varchar(255),
	PRIMARY KEY (id)
) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE utf8mb4_General_ci;

CREATE TABLE seata_state_machine_inst (
	id varchar(255) NOT NULL,
	machine_id varchar(255) NOT NULL,
	tenant_id varchar(255) NOT NULL,
	parent_id varchar(255),
	gmt_started timestamp NOT NULL,
	business_key varchar(255),
	start_params longtext,
	gmt_end timestamp,
	excep longblob,
	end_params longtext,
	status varchar(255),
	compensation_status varchar(255),
	is_running tinyint(1),
	gmt_updated timestamp NOT NULL,
	PRIMARY KEY (id, machine_id),
	UNIQUE unikey_buz_tenant (business_key, tenant_id)
) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE utf8mb4_General_ci;