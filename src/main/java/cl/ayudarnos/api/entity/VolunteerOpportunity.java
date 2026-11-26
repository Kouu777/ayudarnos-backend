package cl.ayudarnos.api.entity;

import cl.ayudarnos.api.entity.enums.VolunteerStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "volunteer_opportunities")
public class VolunteerOpportunity {

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
    private String description;

    @Column(nullable = false, length = 150)
    private String locationName; // ej: Centro de Acopio DIDECO, Gimnasio Municipal

    @Column(nullable = false, length = 100)
    private String region;

    @Column(nullable = false, length = 100)
    private String commune;

    @Column(length = 255)
    private String address;

    @Column(nullable = false, length = 150)
    private String schedule; // ej: "Sábado y Domingo de 09:00 a 14:00"

    @Column(length = 255)
    private String requirements; // ej: "Mayor de 18 años, calzado cerrado"

    @Column(nullable = false)
    private Integer requiredVolunteers = 10;

    @Column(nullable = false)
    private Integer currentVolunteers = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private VolunteerStatus status = VolunteerStatus.OPEN;

    private boolean urgent = false;

    @OneToMany(mappedBy = "opportunity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<VolunteerRegistration> registrations = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    public VolunteerOpportunity() {
    }

    public VolunteerOpportunity(Organization organization, Catastrophe catastrophe, String title,
                                String description, String locationName, String region,
                                String commune, String address, String schedule, String requirements,
                                Integer requiredVolunteers, boolean urgent) {
        this.organization = organization;
        this.catastrophe = catastrophe;
        this.title = title;
        this.description = description;
        this.locationName = locationName;
        this.region = region;
        this.commune = commune;
        this.address = address;
        this.schedule = schedule;
        this.requirements = requirements;
        this.requiredVolunteers = requiredVolunteers;
        this.currentVolunteers = 0;
        this.status = VolunteerStatus.OPEN;
        this.urgent = urgent;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (requiredVolunteers != null && currentVolunteers >= requiredVolunteers && status == VolunteerStatus.OPEN) {
            this.status = VolunteerStatus.FULL;
        }
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

    public List<VolunteerRegistration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<VolunteerRegistration> registrations) {
        this.registrations = registrations;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
