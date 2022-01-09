package com.optimagrowth.license.model;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "licenses")
public class License extends RepresentationModel<License> {

    @Id
    @Column(name = "licence_id", nullable = false)
    private String licenseId;

    private String description;
    @Column(name = "organization_id", nullable = false)
    private String organizationId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "license_type", nullable = false)
    private String licenseType;

    @Column(name="comment")
    private String comment;

    public License withComment(String comment){
        this.setComment(comment);
        return this;
    }

}


