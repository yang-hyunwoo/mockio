create table interview_compare_summary
(
    id bigint generated always as identity primary key,
    current_interview_id bigint not null,
    prev_interview_id bigint not null,
    total_count bigint not null,
    better_count bigint not null,
    not_count bigint not null,
    headline varchar(255) not null,
    summary text not null,
    strengths jsonb  null default '[]'::jsonb,
    improvements jsonb  null default '[]'::jsonb,
    improvement_tags jsonb  null default '[]'::jsonb,
    provider varchar(50) not null,
    model varchar(100) not null,
    prompt_version varchar(50) not null,
    temperature DOUBLE PRECISION NULL,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),

    constraint fk_interview_compare_summary_current_interview
        foreign key (current_interview_id) references interviews(id) on delete cascade,

    constraint fk_interview_compare_summary_prev_interview
        foreign key (prev_interview_id) references interviews(id) on delete cascade,

    constraint uk_interview_compare_summary_current_interview
        unique (current_interview_id)
);

create index idx_interview_compare_summary_prev_interview_id
    on interview_compare_summary(prev_interview_id);

create index idx_interview_compare_summary_created_at
    on interview_compare_summary(created_at);