package cl.ayudarnos.api.dto.alert;

import jakarta.validation.constraints.NotBlank;

public class AlertSubscriptionRequest {

    // Autofilled from session if logged in
    private String email;

    private String phone;

    @NotBlank(message = "La región a la que desea suscribirse es obligatoria")
    private String region; // ej: "Región de Valparaíso" o "Todas"

    private String commune;

    private String category; // "ALIMENTOS", "HIGIENE", "VOLUNTARIADO", "TODAS"

    public AlertSubscriptionRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
