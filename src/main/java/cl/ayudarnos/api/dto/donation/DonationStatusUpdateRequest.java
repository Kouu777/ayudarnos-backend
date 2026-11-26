package cl.ayudarnos.api.dto.donation;

import cl.ayudarnos.api.entity.enums.DonationStatus;
import jakarta.validation.constraints.NotNull;

public class DonationStatusUpdateRequest {

    @NotNull(message = "El nuevo estado es obligatorio")
    private DonationStatus status;

    private String statusNotes;

    public DonationStatusUpdateRequest() {
    }

    public DonationStatusUpdateRequest(DonationStatus status, String statusNotes) {
        this.status = status;
        this.statusNotes = statusNotes;
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
}
