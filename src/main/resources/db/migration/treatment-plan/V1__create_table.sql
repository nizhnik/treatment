CREATE TABLE treatment_plan (
    id INT PRIMARY KEY NOT NULL,
    action VARCHAR(10) NOT NULL,
    patient VARCHAR(50) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NULL,
    recurrence_pattern VARCHAR(100) NOT NULL,
    scheduled_until TIMESTAMP NULL
);

CREATE SEQUENCE TREATMENT_PLAN_SEQ INCREMENT BY 50;
CREATE INDEX idx_end_scheduled ON treatment_plan(end_time, scheduled_until)

