package cl.ayudarnos.api.dto.volunteer;

import cl.ayudarnos.api.dto.organization.OrganizationDto;
import cl.ayudarnos.api.entity.enums.VolunteerStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public class VolunteerOpportunityResponse {

    private UUID id;
    private String title;
    private String description;
    private String locationName;
    private String region;
    private String commune;
    private String address;
    private String schedule;
    private String requirements;
    private Integer requiredVolunteers;
    private Integer currentVolunteers;
    private VolunteerStatus status;
    private boolean urgent;
    private OrganizationDto organization;
    private UUID catastropheId;
    private String catastropheName;
    private LocalDateTime createdAt;

    public VolunteerOpportunityResponse() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Integer getCurrentVolunteers() {
        return currentVolunteers;
    }

    public void setCurrentVolunteers(Integer currentVolunteers) {
        this.currentVolunteers = currentVolunteers;
    }

    public VolunteerStatus getStatus() {
        return status;
    }

    public void setStatus(VolunteerStatus status) {
        this.status = status;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public OrganizationDto getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationDto organization) {
        this.organization = organization;
    }

    public UUID getCatastropheId() {
        return catastropheId;
    }

    public void setCatastropheId(UUID catastropheId) {
        this.catastropheId = catastropheId;
    }

    public String getCatastropheName() {
        return catastropheName;
    }

    public void setCatastropheName(String catastropheName) {
        this.catastropheName = catastropheName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
