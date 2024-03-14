package com.artemn.treatment.entity;

import com.artemn.treatment.enums.TreatmentAction;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Data
@Accessors(chain = true)
public class TreatmentPlan {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TreatmentAction action;
    private String patient;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String recurrencePattern;
    private LocalDateTime scheduledUntil;
}
