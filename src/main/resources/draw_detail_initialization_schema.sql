drop table if exists draw_detail;

create table if not exists draw_detail
(
    draw_date                   date      not null,
    selected_balls_draw_order   smallint,
    euro_balls_draw_order       smallint,
    jackpot_amount              bigint,
    currency_symbol             text,
    jackpot_rollover_count      smallint,
    number_of_jackpot_winners   smallint,
    resource_uri                text,
    archive_url                 text,
    version                     bigint    not null,
    created_date                timestamp not null,
    modified_date               timestamp,
    primary key (draw_date)
);