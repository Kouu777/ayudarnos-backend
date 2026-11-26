package cl.ayudarnos.api.dto.alert;

import cl.ayudarnos.api.entity.enums.AlertSeverity;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public class UrgentAlertCreateRequest {

    @NotBlank(message = "El título de la alerta es obligatorio")
    private String title;

    @NotBlank(message = "El mensaje o detalle de la alerta es obligatorio")
    private String message;

    private AlertSeverity severity = AlertSeverity.CRITICAL;

    @NotBlank(message = "La región afectada es obligatoria")
    private String region;

    private String commune;

    private UUID catastropheId;

    public UrgentAlertCreateRequest() {
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

    public UUID getCatastropheId() {
        return catastropheId;
    }

    public void setCatastropheId(UUID catastropheId) {
        this.catastropheId = catastropheId;
    }
}
