package cl.ayudarnos.api.dto.donation;

import cl.ayudarnos.api.entity.enums.DeliveryMethod;
import cl.ayudarnos.api.entity.enums.DonationStatus;
import cl.ayudarnos.api.entity.enums.GroupType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GroupDonationResponse {

    private UUID id;
    private String trackingCode;
    private UUID needId;
    private String needTitle;
    private String organizationName;
    private String groupName;
    private GroupType groupType;
    private String coordinatorName;
    private String coordinatorEmail;
    private String coordinatorPhone;
    private String targetDescription;
    private DeliveryMethod deliveryMethod;
    private String donorCommune;
    private String availability;
    private DonationStatus status;
    private String statusNotes;
    private Integer totalItemsCount;
    private List<GroupContributionItemDto> contributions = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GroupDonationResponse() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public UUID getNeedId() {
        return needId;
    }

    public void setNeedId(UUID needId) {
        this.needId = needId;
    }

    public String getNeedTitle() {
        return needTitle;
    }

    public void setNeedTitle(String needTitle) {
        this.needTitle = needTitle;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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

    public DonationStatus getStatus() {
        return status;
    }

    public void setStatus(DonationStatus status) {
        this.status = status;
    }

    public String getStatusNotes() {
        return statusNotes;
    }

    public void setStatusNotes(String statusNotes) {
        this.statusNotes = statusNotes;
    }

    public Integer getTotalItemsCount() {
        return totalItemsCount;
    }

    public void setTotalItemsCount(Integer totalItemsCount) {
        this.totalItemsCount = totalItemsCount;
    }

    public List<GroupContributionItemDto> getContributions() {
        return contributions;
    }

    public void setContributions(List<GroupContributionItemDto> contributions) {
        this.contributions = contributions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
