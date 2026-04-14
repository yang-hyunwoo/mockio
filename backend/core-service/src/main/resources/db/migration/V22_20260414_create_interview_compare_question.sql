create table interview_compare_question
(
    id bigint generated always as identity primary key,
    interview_id bigint not null,
    current_question_id bigint not null,
    prev_question_id bigint not null,

    status varchar(20) not null,

    headline varchar(255) null,
    summary text null,
    strengths jsonb not null default '[]'::jsonb,
    improvements jsonb not null default '[]'::jsonb,
    improvement_tags jsonb not null default '[]'::jsonb,

    provider varchar(50) null,
    model varchar(100) null,
    prompt_version varchar(50) null,
    temperature double precision null,
    error_message text null,

    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),

    constraint fk_interview_compare_question_interview
        foreign key (interview_id) references interviews(id) on delete cascade,

    constraint uk_interview_compare_question
        unique (interview_id, current_question_id, prev_question_id)
);