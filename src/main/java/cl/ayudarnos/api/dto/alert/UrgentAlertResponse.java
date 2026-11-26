package cl.ayudarnos.api.dto.alert;

import cl.ayudarnos.api.dto.organization.OrganizationDto;
import cl.ayudarnos.api.entity.enums.AlertSeverity;
import java.time.LocalDateTime;
import java.util.UUID;

public class UrgentAlertResponse {

    private UUID id;
    private String title;
    private String message;
    private AlertSeverity severity;
    private String region;
    private String commune;
    private boolean active;
    private OrganizationDto organization;
    private UUID catastropheId;
    private String catastropheName;
    private LocalDateTime createdAt;

    public UrgentAlertResponse() {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AlertSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(AlertSeverity severity) {
        this.severity = severity;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
