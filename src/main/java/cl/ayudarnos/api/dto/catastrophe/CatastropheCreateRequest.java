package cl.ayudarnos.api.dto.catastrophe;

import jakarta.validation.constraints.NotBlank;

public class CatastropheCreateRequest {

    @NotBlank(message = "El nombre de la catástrofe es obligatorio")
    private String name;

    private String description;

    @NotBlank(message = "La región es obligatoria")
    private String region;

    private boolean active = true;

    public CatastropheCreateRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
