drop table if exists draw_number_uri;

create table if not exists draw_number_uri
(
    draw_date date not null,
    detail_uri text not null,
    archive_url text not null,
    primary key (draw_date)
)