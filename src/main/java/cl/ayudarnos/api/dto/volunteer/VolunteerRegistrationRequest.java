package cl.ayudarnos.api.dto.volunteer;

public class VolunteerRegistrationRequest {

    // Can be empty if the user is authenticated in session (backend will autofill)
    private String fullName;

    private String email;

    private String phone;

    private String availabilityNotes;

    public VolunteerRegistrationRequest() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvailabilityNotes() {
        return availabilityNotes;
    }

    public void setAvailabilityNotes(String availabilityNotes) {
        this.availabilityNotes = availabilityNotes;
    }
}
