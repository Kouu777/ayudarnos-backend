package cl.ayudarnos.api.dto.donation;

import cl.ayudarnos.api.entity.enums.DeliveryMethod;
import cl.ayudarnos.api.entity.enums.GroupType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public class GroupDonationCreateRequest {

    @NotNull(message = "El identificador de la necesidad es obligatorio")
    private UUID needId;

    @NotBlank(message = "El nombre del grupo o iniciativa colectiva es obligatorio (ej: Junta de Vecinos Los Aromos)")
    private String groupName;

    private GroupType groupType = GroupType.NEIGHBORHOOD_ASSOCIATION;

    // Contact info for the coordinator: autofilled from session if authenticated!
    private String coordinatorName;

    private String coordinatorEmail;

    private String coordinatorPhone;

    private String targetDescription; // ej: "Meta: Juntar 100 kg de alimentos entre los vecinos"

    @NotNull(message = "El método de entrega es obligatorio")
    private DeliveryMethod deliveryMethod;

    @NotBlank(message = "La comuna de origen del grupo es obligatoria")
    private String donorCommune;

    private String availability;

    @Valid
    private List<GroupContributionItemDto> initialContributions;

    public GroupDonationCreateRequest() {
    }

    public UUID getNeedId() {
        return needId;
    }

    public void setNeedId(UUID needId) {
        this.needId = needId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }

    public String getCoordinatorName() {
        return coordinatorName;
    }

    public void setCoordinatorName(String coordinatorName) {
        this.coordinatorName = coordinatorName;
    }

    public String getCoordinatorEmail() {
        return coordinatorEmail;
    }

    public void setCoordinatorEmail(String coordinatorEmail) {
        this.coordinatorEmail = coordinatorEmail;
    }

    public String getCoordinatorPhone() {
        return coordinatorPhone;
    }

    public void setCoordinatorPhone(String coordinatorPhone) {
        this.coordinatorPhone = coordinatorPhone;
    }

    public String getTargetDescription() {
        return targetDescription;
    }

    public void setTargetDescription(String targetDescription) {
        this.targetDescription = targetDescription;
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

    public List<GroupContributionItemDto> getInitialContributions() {
        return initialContributions;
    }

    public void setInitialContributions(List<GroupContributionItemDto> initialContributions) {
        this.initialContributions = initialContributions;
    }
}
