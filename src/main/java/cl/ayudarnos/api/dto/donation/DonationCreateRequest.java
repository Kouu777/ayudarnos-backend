package cl.ayudarnos.api.dto.donation;

import cl.ayudarnos.api.entity.enums.DeliveryMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import java.util.UUID;

public class DonationCreateRequest {

    @NotNull(message = "El identificador de la necesidad es obligatorio")
    private UUID needId;

    @NotEmpty(message = "Debe ingresar al menos un producto a donar")
    @Valid
    private List<DonationItemDto> items;

    @NotNull(message = "El método de entrega es obligatorio")
    private DeliveryMethod deliveryMethod;

    @NotBlank(message = "La comuna es obligatoria")
    private String donorCommune;

    private String availability;

    private String donorFullName;

    private String donorEmail;

    private String donorPhone;

    @AssertTrue(message = "Debe autorizar compartir sus datos con la organización")
    private boolean consentContactSharing;

    public DonationCreateRequest() {
    }

    public UUID getNeedId() {
        return needId;
    }

    public void setNeedId(UUID needId) {
        this.needId = needId;
    }

    public List<DonationItemDto> getItems() {
        return items;
    }

    public void setItems(List<DonationItemDto> items) {
        this.items = items;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getDonorCommune() {
        return donorCommune;
    }

    public void setDonorCommune(String donorCommune) {
        this.donorCommune = donorCommune;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDonorFullName() {
        return donorFullName;
    }

    public void setDonorFullName(String donorFullName) {
        this.donorFullName = donorFullName;
    }

    public String getDonorEmail() {
        return donorEmail;
    }

    public void setDonorEmail(String donorEmail) {
        this.donorEmail = donorEmail;
    }

    public String getDonorPhone() {
        return donorPhone;
    }

    public void setDonorPhone(String donorPhone) {
        this.donorPhone = donorPhone;
    }

    public boolean isConsentContactSharing() {
        return consentContactSharing;
    }

    public void setConsentContactSharing(boolean consentContactSharing) {
        this.consentContactSharing = consentContactSharing;
    }
}
