package cl.ayudarnos.api.dto.donation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class GroupContributionItemDto {

    private UUID id;

    @NotBlank(message = "El nombre del vecino, compañero o familia aportante es obligatorio")
    private String contributorName; // ej: "Familia Gómez", "Depto 204", "Equipo de Ventas"

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String productName;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer quantity;

    @NotBlank(message = "La presentación es obligatoria (ej: Bolsas de 1 kg)")
    private String presentation;

    public GroupContributionItemDto() {
    }

    public GroupContributionItemDto(String contributorName, String productName, Integer quantity, String presentation) {
        this.contributorName = contributorName;
        this.productName = productName;
        this.quantity = quantity;
        this.presentation = presentation;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContributorName() {
        return contributorName;
    }

    public void setContributorName(String contributorName) {
        this.contributorName = contributorName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }
}
