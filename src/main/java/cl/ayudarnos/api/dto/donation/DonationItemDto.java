package cl.ayudarnos.api.dto.donation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DonationItemDto {

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String productName;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer quantity;

    @NotBlank(message = "La presentación es obligatoria (ej: Bolsas de 1 kg, Pack de 25 unidades)")
    private String presentation;

    public DonationItemDto() {
    }

    public DonationItemDto(String productName, Integer quantity, String presentation) {
        this.productName = productName;
        this.quantity = quantity;
        this.presentation = presentation;
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
