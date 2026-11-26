package cl.ayudarnos.api.entity;

import cl.ayudarnos.api.entity.enums.NeedStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "needs")
public class Need {

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

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 80)
    private String category; // e.g. Higiene, Alimentos, Abrigo, Agua, etc.

    @Column(nullable = false, length = 100)
    private String region;

    @Column(nullable = false, length = 100)
    private String commune;

    @Column(nullable = false)
    private Integer targetQuantity = 100;

    @Column(nullable = false)
    private Integer currentQuantity = 0;

    // Direct progress percentage from 0 to 100
    @Column(nullable = false)
    private Integer progressPercentage = 0;

    @Column(length = 255)
    private String instructions; // e.g. "Dona productos sellados, sin abrir y en buen estado."

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "need_suggested_products", joinColumns = @JoinColumn(name = "need_id"))
    @Column(name = "product_name")
    private List<String> suggestedProducts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NeedStatus status = NeedStatus.ACTIVE;

    private boolean urgent = false;

    @OneToMany(mappedBy = "need", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Donation> donations = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    public Need() {
    }

    public Need(Organization organization, Catastrophe catastrophe, String title, String description,
                String category, String region, String commune, Integer targetQuantity,
                Integer currentQuantity, Integer progressPercentage, String instructions, boolean urgent) {
        this.organization = organization;
        this.catastrophe = catastrophe;
        this.title = title;
        this.description = description;
        this.category = category;
        this.region = region;
        this.commune = commune;
        this.targetQuantity = targetQuantity;
        this.currentQuantity = currentQuantity;
        this.progressPercentage = progressPercentage;
        this.instructions = instructions;
        this.urgent = urgent;
        this.status = NeedStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (targetQuantity != null && targetQuantity > 0 && currentQuantity != null) {
            this.progressPercentage = Math.min(100, (int) Math.round(((double) currentQuantity / targetQuantity) * 100));
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public Integer getTargetQuantity() {
        return targetQuantity;
    }

    public void setTargetQuantity(Integer targetQuantity) {
        this.targetQuantity = targetQuantity;
    }

    public Integer getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(Integer currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public Integer getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Integer progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public List<String> getSuggestedProducts() {
        return suggestedProducts;
    }

    public void setSuggestedProducts(List<String> suggestedProducts) {
        this.suggestedProducts = suggestedProducts;
    }

    public NeedStatus getStatus() {
        return status;
    }

    public void setStatus(NeedStatus status) {
        this.status = status;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public List<Donation> getDonations() {
        return donations;
    }

    public void setDonations(List<Donation> donations) {
        this.donations = donations;
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
