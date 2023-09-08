CREATE SCHEMA IF NOT EXISTS filmfocus;

CREATE TABLE IF NOT EXISTS filmfocus.categories
(
    id SERIAL,
    name character varying(255) UNIQUE,
    CONSTRAINT categories_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS filmfocus.cinemas
(
    id SERIAL,
    address character varying(255),
    city character varying(255),
    average_rating double precision NOT NULL DEFAULT 0.0,
    CONSTRAINT cinemas_pkey PRIMARY KEY (id),
    CONSTRAINT unique_address_city UNIQUE (address, city)
);

CREATE TABLE IF NOT EXISTS filmfocus.discounts
(
    id SERIAL,
    type character varying(255),
    code character varying(255),
    percentage int,
	CONSTRAINT discounts_pkey PRIMARY KEY (id),
    CONSTRAINT unique_type UNIQUE (type),
	CONSTRAINT unique_code UNIQUE (code)
);

CREATE TABLE IF NOT EXISTS filmfocus.halls
(
    id SERIAL,
    capacity integer NOT NULL,
    cinema_id integer,
    CONSTRAINT halls_pkey PRIMARY KEY (id),
    CONSTRAINT fk_halls_cinema_id FOREIGN KEY (cinema_id)
        REFERENCES filmfocus.cinemas (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS filmfocus.items
(
    id SERIAL,
    name character varying(255) UNIQUE,
    price double precision,
    quantity integer NOT NULL,
    CONSTRAINT items_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS filmfocus.movies
(
    id SERIAL,
    title character varying(255) UNIQUE,
    average_rating double precision NOT NULL DEFAULT 0.0,
    description character varying(255),
    release_date date,
    runtime bigint,
    category_id integer,
    CONSTRAINT movies_pkey PRIMARY KEY (id),
    CONSTRAINT fk_movies_category_id FOREIGN KEY (category_id)
        REFERENCES filmfocus.categories (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS filmfocus.users
(
    id SERIAL,
    username character varying(255) UNIQUE,
    password character varying(255),
    email character varying(255) UNIQUE,
    first_name character varying(255),
    last_name character varying(255),
    join_date date,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS filmfocus.orders
(
    id SERIAL,
    date_of_purchase date,
    total_price double precision,
    user_id integer,
    CONSTRAINT orders_pkey PRIMARY KEY (id),
    CONSTRAINT fk_orders_user_id FOREIGN KEY (user_id)
        REFERENCES filmfocus.users (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS filmfocus.orders_items
(
    order_id integer NOT NULL,
    item_id integer NOT NULL,
    CONSTRAINT fk_orders_items_item_id FOREIGN KEY (item_id)
        REFERENCES filmfocus.items (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_orders_items_order_id FOREIGN KEY (order_id)
        REFERENCES filmfocus.orders (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS filmfocus.programs
(
    id SERIAL,
    program_date date,
    cinema_id integer,
    CONSTRAINT programs_pkey PRIMARY KEY (id),
    CONSTRAINT fk_programs_cinema_id FOREIGN KEY (cinema_id)
        REFERENCES filmfocus.cinemas (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS filmfocus.projections
(
    id SERIAL,
    price double precision,
    hall_id integer,
    program_id integer,
    movie_id integer,
    start_time time without time zone,
 CONSTRAINT projections_pkey PRIMARY KEY (id),
    CONSTRAINT fk_projections_program_id FOREIGN KEY (program_id)
        REFERENCES filmfocus.programs (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_projections_hall_id FOREIGN KEY (hall_id)
        REFERENCES filmfocus.halls (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_projections_movie_id FOREIGN KEY (movie_id)
        REFERENCES filmfocus.movies (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS filmfocus.reviews
(
    id serial,
    date_modified date,
    rating double precision,
    review_text character varying(255),
    cinema_id integer,
    movie_id integer,
    user_id integer NOT NULL,
    CONSTRAINT reviews_pkey PRIMARY KEY (id),
    CONSTRAINT fk_reviews_cinema_id FOREIGN KEY (cinema_id)
        REFERENCES filmfocus.cinemas (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_reviews_movie_id FOREIGN KEY (movie_id)
        REFERENCES filmfocus.movies (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_reviews_user_id FOREIGN KEY (user_id)
        REFERENCES filmfocus.users (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS filmfocus.tickets
(
    id SERIAL,
    date_of_purchase date,
    projection_id integer,
    CONSTRAINT tickets_pkey PRIMARY KEY (id),
    CONSTRAINT fk_tickets_projection_id FOREIGN KEY (projection_id)
        REFERENCES filmfocus.projections (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS filmfocus.orders_tickets
(
    order_id integer NOT NULL,
    tickets_id integer NOT NULL,
    CONSTRAINT uk_tickets_id UNIQUE (tickets_id),
    CONSTRAINT fk_orders_tickets_order_id FOREIGN KEY (order_id)
        REFERENCES filmfocus.orders (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_orders_tickets_tickets_id FOREIGN KEY (tickets_id)
        REFERENCES filmfocus.tickets (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS filmfocus.roles
(
    id SERIAL,
    name character varying(255) UNIQUE,
    CONSTRAINT roles_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS filmfocus.users_roles
(
    user_id integer NOT NULL,
    role_id integer NOT NULL,
    CONSTRAINT fk_users_roles_user_id FOREIGN KEY (user_id)
        REFERENCES filmfocus.users (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_users_roles_role_id FOREIGN KEY (role_id)
        REFERENCES filmfocus.roles (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);