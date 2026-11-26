package cl.ayudarnos.api.entity;

import cl.ayudarnos.api.entity.enums.DeliveryMethod;
import cl.ayudarnos.api.entity.enums.DonationStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "donations")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 32)
    private String trackingCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "need_id", nullable = false)
    private Need need;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_user_id")
    private User donorUser;

    @OneToMany(mappedBy = "donation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DonationItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DeliveryMethod deliveryMethod;

    @Column(nullable = false, length = 100)
    private String donorCommune;

    @Column(length = 255)
    private String availability;

    @Column(nullable = false, length = 150)
    private String donorFullName;

    @Column(nullable = false, length = 150)
    private String donorEmail;

    @Column(nullable = false, length = 30)
    private String donorPhone;

    @Column(nullable = false)
    private boolean consentContactSharing;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DonationStatus status = DonationStatus.PLEDGED;

    @Column(columnDefinition = "TEXT")
    private String statusNotes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    public Donation() {
    }

    public Donation(String trackingCode, Need need, User donorUser, DeliveryMethod deliveryMethod,
                    String donorCommune, String availability, String donorFullName,
                    String donorEmail, String donorPhone, boolean consentContactSharing) {
        this.trackingCode = trackingCode;
        this.need = need;
        this.donorUser = donorUser;
        this.deliveryMethod = deliveryMethod;
        this.donorCommune = donorCommune;
        this.availability = availability;
        this.donorFullName = donorFullName;
        this.donorEmail = donorEmail;
        this.donorPhone = donorPhone;
        this.consentContactSharing = consentContactSharing;
        this.status = DonationStatus.PLEDGED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addItem(DonationItem item) {
        items.add(item);
        item.setDonation(this);
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

    public Need getNeed() {
        return need;
    }

    public void setNeed(Need need) {
        this.need = need;
    }

    public User getDonorUser() {
        return donorUser;
    }

    public void setDonorUser(User donorUser) {
        this.donorUser = donorUser;
    }

    public List<DonationItem> getItems() {
        return items;
    }

    public void setItems(List<DonationItem> items) {
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
