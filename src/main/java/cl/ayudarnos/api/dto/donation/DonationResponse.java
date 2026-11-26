package cl.ayudarnos.api.dto.donation;

import cl.ayudarnos.api.entity.enums.DeliveryMethod;
import cl.ayudarnos.api.entity.enums.DonationStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class DonationResponse {

    private UUID id;
    private String trackingCode;
    private UUID needId;
    private String needTitle;
    private String organizationName;
    private String needRegion;
    private String needCommune;
    private List<DonationItemDto> items;
    private DeliveryMethod deliveryMethod;
    private String donorCommune;
    private String availability;
    private String donorFullName;
    private String donorEmail;
    private String donorPhone;
    private boolean consentContactSharing;
    private DonationStatus status;
    private String statusNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DonationResponse() {
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

    public String getNeedRegion() {
        return needRegion;
    }

    public void setNeedRegion(String needRegion) {
        this.needRegion = needRegion;
    }

    public String getNeedCommune() {
        return needCommune;
    }

    public void setNeedCommune(String needCommune) {
        this.needCommune = needCommune;
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
