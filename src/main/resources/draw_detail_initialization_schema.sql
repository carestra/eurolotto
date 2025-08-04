drop table if exists draw_detail;

create table if not exists draw_detail
(
    draw_date                   date    not null UNIQUE,
    selected_balls_draw_order   integer ARRAY[5],
    euro_balls_draw_order       integer ARRAY[2],
    jackpot_amount              bigint,
    currency_symbol             text,
    jackpot_rollover_count      integer,
    number_of_jackpot_winners   integer,
    resource_uri                text      not null,
    archive_url                 text,
    version                     bigint    not null,
    created_date                timestamp not null,
    modified_date               timestamp,
    primary key (resource_uri)
);