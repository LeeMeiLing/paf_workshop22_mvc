package sg.edu.nus.iss.paf_workshop22_mvc.models;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Rsvp {

    private Integer id;

    @NotNull
    @Size(min = 3, max = 150, message = "Fullname must be between 3 and 150 characters")
    private String fullname;

    @Email(message = "Invalid email format")
    @Size(min = 10, max = 150, message = "Email only accepts maximum 150 characters")
    private String email;

    private String phone;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date confirmationDate;

    private String comments;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public Date getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(Date confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Rsvp [id=" + id + ", fullname=" + fullname + ", email=" + email + ", phone=" + phone
                + ", confirmationDate=" + confirmationDate + ", comments=" + comments + "]";
    }

    public MultiValueMap<String,String> toMultiValueMap(){
        
        MultiValueMap<String,String> rsvpForm = new LinkedMultiValueMap<>();
        // rsvpForm.add("id", this.getId().toString());   // !! cannot parse null to string value
        if(null != this.id){
            rsvpForm.add("id", String.valueOf(this.id));
        }
        rsvpForm.add("fullname", this.fullname);
        rsvpForm.add("email", this.email);
        rsvpForm.add("phone", this.phone);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        rsvpForm.add("confirmationDate",formatter.format(this.confirmationDate)); // need to format the date so REST endpoint can parse it into java.sql.date
        rsvpForm.add("comments", this.comments);

        return rsvpForm;

    }
        
       
}
