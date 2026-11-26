package cl.ayudarnos.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "group_contribution_items")
public class GroupContributionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_donation_id", nullable = false)
    @JsonIgnore
    private GroupDonation groupDonation;

    @Column(nullable = false, length = 150)
    private String contributorName; // ej: "Familia González", "Vecino Dpto 302", "Equipo TI"

    @Column(nullable = false, length = 150)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, length = 100)
    private String presentation;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public GroupContributionItem() {
    }

    public GroupContributionItem(String contributorName, String productName, Integer quantity, String presentation) {
        this.contributorName = contributorName;
        this.productName = productName;
        this.quantity = quantity;
        this.presentation = presentation;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public GroupDonation getGroupDonation() {
        return groupDonation;
    }

    public void setGroupDonation(GroupDonation groupDonation) {
        this.groupDonation = groupDonation;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
