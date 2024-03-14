package com.artemn.treatment.api;

import com.artemn.treatment.entity.TreatmentPlan;
import com.artemn.treatment.entity.TreatmentTask;
import com.artemn.treatment.service.TreatmentService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Path("/api/treatment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class TreatmentResource {

    private final TreatmentService treatmentService;

    @GET
    @Path("tasks")
    public List<TreatmentTask> getTasks() {
        return treatmentService.findTasks();
    }

    @GET
    @Path("plans")
    public List<TreatmentPlan> getPlans() {
        return treatmentService.findPlans();
    }
}
