package com.custempmanag.librarymanagmentsystem.dto;

import com.custempmanag.librarymanagmentsystem.models.Patrons;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PatronsDTO {

    private long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2)
    private String name;

    @NotBlank(message = "Contact information is required")
    private String contactInfo;

    public PatronsDTO() {
    }

    public PatronsDTO(Patrons patrons) {
        this.id = patrons.getId();
        this.name = patrons.getName();
        this.contactInfo = patrons.getContactInfo();
    }

    public PatronsDTO(Long id, String name, String contactInfo) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
    }

    public PatronsDTO(String name, String contactInfo) {
        this.name = name;
        this.contactInfo = contactInfo;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
}
