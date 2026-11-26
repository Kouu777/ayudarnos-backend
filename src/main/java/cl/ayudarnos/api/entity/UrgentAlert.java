package cl.ayudarnos.api.entity;

import cl.ayudarnos.api.entity.enums.AlertSeverity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "urgent_alerts")
public class UrgentAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "catastrophe_id")
    private Catastrophe catastrophe;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AlertSeverity severity = AlertSeverity.CRITICAL;

    @Column(nullable = false, length = 100)
    private String region;

    @Column(length = 100)
    private String commune;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public UrgentAlert() {
    }

    public UrgentAlert(Organization organization, Catastrophe catastrophe, String title,
                       String message, AlertSeverity severity, String region, String commune) {
        this.organization = organization;
        this.catastrophe = catastrophe;
        this.title = title;
        this.message = message;
        this.severity = severity != null ? severity : AlertSeverity.CRITICAL;
        this.region = region;
        this.commune = commune;
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Catastrophe getCatastrophe() {
        return catastrophe;
    }

    public void setCatastrophe(Catastrophe catastrophe) {
        this.catastrophe = catastrophe;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
