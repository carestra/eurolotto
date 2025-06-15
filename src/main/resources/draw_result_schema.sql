drop table if exists draw_result;

create table if not exists draw_result
(
    draw_date                   date      not null,
    balls_draw_order            smallint[],
    euroballs_draw_order        smallint[],
    rollover                    smallint,
    jackpot_amount              bigint,
    currency_symbol             text,
    number_of_jackpot_winners   smallint,
    archive_url                 text,
    version                     bigint    not null,
    created_date                timestamp not null,
    modified_date               timestamp,
    primary key (draw_date)
)