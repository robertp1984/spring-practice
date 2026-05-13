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
