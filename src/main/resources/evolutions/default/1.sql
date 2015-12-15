# Do NOT forget this line as your script will NOT be applied
# --- !Ups
create table location
( id BIGSERIAL PRIMARY KEY
, name VARCHAR NOT NULL
);

# --- !Downs

DROP TABLE location;
