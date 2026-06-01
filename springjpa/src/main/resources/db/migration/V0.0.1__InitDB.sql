-- Asset class
CREATE TABLE asset_class (
    id VARCHAR PRIMARY KEY,
    name VARCHAR UNIQUE NOT NULL,
    description VARCHAR NOT NULL
);
CREATE UNIQUE INDEX asset_class_name_idx ON asset_class(name);


-- Asset
CREATE TABLE asset (
    id VARCHAR PRIMARY KEY,
    name VARCHAR UNIQUE NOT NULL,
    description VARCHAR NOT NULL,
    asset_class_id VARCHAR NOT NULL,
    CONSTRAINT asset_asset_class_id_fk FOREIGN KEY(asset_class_id) REFERENCES asset_class(id)
);
CREATE UNIQUE INDEX asset_name_idx ON asset(name);
CREATE INDEX asset_asset_class_id_idx ON asset(asset_class_id);


--- Client
CREATE TABLE client (
    id VARCHAR PRIMARY KEY,
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL
);
CREATE INDEX client_first_name_idx ON client(first_name);
CREATE INDEX client_last_name_idx ON client(last_name);

--- Reference
CREATE TABLE reference (
    id VARCHAR PRIMARY KEY,
    name VARCHAR NOT NULL,
    value_string VARCHAR,

    dtype VARCHAR NOT NULL, -- for inheritance

    asset_id VARCHAR REFERENCES asset(id),
    CONSTRAINT reference_value_present CHECK(value_string IS NOT NULL)
);
CREATE INDEX reference_name_idx ON reference(name);
CREATE INDEX reference_dtype_idx ON reference USING HASH(dtype);
CREATE INDEX reference_asset_id_idx ON reference(asset_id);
CREATE UNIQUE INDEX reference_lk_idx ON reference(asset_id, name);

---
CREATE TABLE exactly_once_delivery_entry (
    id VARCHAR PRIMARY KEY,
    message_id VARCHAR NOT NULL,
    type VARCHAR NOT NULL
);
CREATE UNIQUE INDEX exactly_once_delivery_entry_lk_idx ON exactly_once_delivery_entry USING HASH(message_id, type);
