package cl.ayudarnos.api.entity;

import cl.ayudarnos.api.entity.enums.DeliveryMethod;
import cl.ayudarnos.api.entity.enums.DonationStatus;
import cl.ayudarnos.api.entity.enums.GroupType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "group_donations")
public class GroupDonation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 32)
    private String trackingCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "need_id", nullable = false)
    private Need need;

    @Column(nullable = false, length = 150)
    private String groupName; // ej: "Junta de Vecinos Los Aromos", "3° Medio B Colegio San Agustín"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private GroupType groupType = GroupType.NEIGHBORHOOD_ASSOCIATION;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coordinator_user_id")
    private User coordinatorUser;

    @Column(nullable = false, length = 150)
    private String coordinatorName;

    @Column(nullable = false, length = 150)
    private String coordinatorEmail;

    @Column(nullable = false, length = 30)
    private String coordinatorPhone;

    @Column(length = 255)
    private String targetDescription; // ej: "Meta: Juntar 50 paquetes de fideos y 30 de arroz entre los vecinos"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DeliveryMethod deliveryMethod;

    @Column(nullable = false, length = 100)
    private String donorCommune;

    @Column(length = 255)
    private String availability;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DonationStatus status = DonationStatus.PLEDGED;

    @Column(columnDefinition = "TEXT")
    private String statusNotes;

    @OneToMany(mappedBy = "groupDonation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<GroupContributionItem> contributions = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    public GroupDonation() {
    }

    public GroupDonation(String trackingCode, Need need, String groupName, GroupType groupType,
                         User coordinatorUser, String coordinatorName, String coordinatorEmail,
                         String coordinatorPhone, String targetDescription, DeliveryMethod deliveryMethod,
                         String donorCommune, String availability) {
        this.trackingCode = trackingCode;
        this.need = need;
        this.groupName = groupName;
        this.groupType = groupType != null ? groupType : GroupType.NEIGHBORHOOD_ASSOCIATION;
        this.coordinatorUser = coordinatorUser;
        this.coordinatorName = coordinatorName;
        this.coordinatorEmail = coordinatorEmail;
        this.coordinatorPhone = coordinatorPhone;
        this.targetDescription = targetDescription;
        this.deliveryMethod = deliveryMethod;
        this.donorCommune = donorCommune;
        this.availability = availability;
        this.status = DonationStatus.PLEDGED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addContribution(GroupContributionItem item) {
        contributions.add(item);
        item.setGroupDonation(this);
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

    public User getCoordinatorUser() {
        return coordinatorUser;
    }

    public void setCoordinatorUser(User coordinatorUser) {
        this.coordinatorUser = coordinatorUser;
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

    public List<GroupContributionItem> getContributions() {
        return contributions;
    }

    public void setContributions(List<GroupContributionItem> contributions) {
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
