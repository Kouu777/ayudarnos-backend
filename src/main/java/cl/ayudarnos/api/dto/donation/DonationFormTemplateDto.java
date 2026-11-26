package cl.ayudarnos.api.dto.donation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DonationFormTemplateDto {

    private UUID needId;
    private String needTitle;
    private String organizationName;
    private String needRegion;
    private String needCommune;
    private String instructions;
    private String category;
    private List<String> referenceProducts = new ArrayList<>();

    // Contact info: Pre-filled ONLY if the user is authenticated in the current session
    private boolean authenticated;
    private String donorFullName;
    private String donorEmail;
    private String donorPhone;

    // Delivery info (empty for user to specify)
    private String donorCommune;
    private String availability;

    // Product items: ALWAYS EMPTY for the donor to input what they want to donate
    private List<DonationItemDto> items = new ArrayList<>();

    public DonationFormTemplateDto() {
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

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getReferenceProducts() {
        return referenceProducts;
    }

    public void setReferenceProducts(List<String> referenceProducts) {
        this.referenceProducts = referenceProducts;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
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

    public List<DonationItemDto> getItems() {
        return items;
    }

    public void setItems(List<DonationItemDto> items) {
        this.items = items;
    }
}
