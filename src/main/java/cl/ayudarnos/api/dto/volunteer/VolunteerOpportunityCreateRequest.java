package cl.ayudarnos.api.dto.volunteer;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class VolunteerOpportunityCreateRequest {

    @NotBlank(message = "El título de la convocatoria es obligatorio")
    private String title;

    @NotBlank(message = "La descripción de las tareas es obligatoria")
    private String description;

    @NotBlank(message = "El nombre del centro de acopio o lugar es obligatorio")
    private String locationName;

    @NotBlank(message = "La región es obligatoria")
    private String region;

    @NotBlank(message = "La comuna es obligatoria")
    private String commune;

    private String address;

    @NotBlank(message = "El horario o turnos es obligatorio")
    private String schedule;

    private String requirements;

    @NotNull(message = "El número de voluntarios requeridos es obligatorio")
    @Min(value = 1, message = "Debe solicitar al menos 1 voluntario")
    private Integer requiredVolunteers;

    private boolean urgent = false;

    private UUID catastropheId;

    public VolunteerOpportunityCreateRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public Integer getRequiredVolunteers() {
        return requiredVolunteers;
    }

    public void setRequiredVolunteers(Integer requiredVolunteers) {
        this.requiredVolunteers = requiredVolunteers;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public UUID getCatastropheId() {
        return catastropheId;
    }

    public void setCatastropheId(UUID catastropheId) {
        this.catastropheId = catastropheId;
    }
}
