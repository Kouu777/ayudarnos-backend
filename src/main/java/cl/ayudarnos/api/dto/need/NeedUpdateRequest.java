package cl.ayudarnos.api.dto.need;

import cl.ayudarnos.api.entity.enums.NeedStatus;
import java.util.List;

public class NeedUpdateRequest {

    private String title;
    private String description;
    private String category;
    private Integer targetQuantity;
    private Integer currentQuantity;
    private Integer progressPercentage;
    private String instructions;
    private List<String> suggestedProducts;
    private NeedStatus status;
    private Boolean urgent;

    public NeedUpdateRequest() {
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

    public Boolean getUrgent() {
        return urgent;
    }

    public void setUrgent(Boolean urgent) {
        this.urgent = urgent;
    }
}
