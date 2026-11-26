package cl.ayudarnos.api.dto.need;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public class NeedCreateRequest {

    @NotBlank(message = "El título de la necesidad es obligatorio")
    private String title;

    private String description;

    @NotBlank(message = "La categoría es obligatoria (ej: Higiene, Alimentos, etc.)")
    private String category;

    @NotBlank(message = "La región es obligatoria")
    private String region;

    @NotBlank(message = "La comuna es obligatoria")
    private String commune;

    @NotNull(message = "La cantidad meta es obligatoria")
    @Min(value = 1, message = "La meta debe ser mayor a 0")
    private Integer targetQuantity;

    private String instructions;

    private List<String> suggestedProducts;

    private boolean urgent = false;

    private UUID catastropheId;

    public NeedCreateRequest() {
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
