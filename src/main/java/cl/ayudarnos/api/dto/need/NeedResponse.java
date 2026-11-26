package cl.ayudarnos.api.dto.need;

import cl.ayudarnos.api.dto.organization.OrganizationDto;
import cl.ayudarnos.api.entity.enums.NeedStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class NeedResponse {

    private UUID id;
    private String title;
    private String description;
    private String category;
    private String region;
    private String commune;
    private Integer targetQuantity;
    private Integer currentQuantity;
    private Integer progressPercentage;
    private String instructions;
    private List<String> suggestedProducts;
    private NeedStatus status;
    private boolean urgent;
    private OrganizationDto organization;
    private UUID catastropheId;
    private String catastropheName;
    private LocalDateTime createdAt;

    public NeedResponse() {
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
