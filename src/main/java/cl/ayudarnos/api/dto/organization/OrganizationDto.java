package cl.ayudarnos.api.dto.organization;

import cl.ayudarnos.api.entity.enums.OrganizationType;
import java.util.UUID;

public class OrganizationDto {

    private UUID id;
    private String name;
    private OrganizationType type;
    private String rutTaxId;
    private String contactPerson;
    private String phone;
    private String region;
    private String commune;
    private String address;
    private String description;
    private boolean verified;

    public OrganizationDto() {
    }

    public OrganizationDto(UUID id, String name, OrganizationType type, String rutTaxId,
                           String contactPerson, String phone, String region, String commune,
                           String address, String description, boolean verified) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.rutTaxId = rutTaxId;
        this.contactPerson = contactPerson;
        this.phone = phone;
        this.region = region;
        this.commune = commune;
        this.address = address;
        this.description = description;
        this.verified = verified;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrganizationType getType() {
        return type;
    }

    public void setType(OrganizationType type) {
        this.type = type;
    }

    public String getRutTaxId() {
        return rutTaxId;
    }

    public void setRutTaxId(String rutTaxId) {
        this.rutTaxId = rutTaxId;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
