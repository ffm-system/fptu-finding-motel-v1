package com.example.fptufindingmotelv1.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "RENTER")
public class RenterModel extends UserModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "GENDER")
    private boolean gender;

    @Column(name = "CAREER")
    private String career;

    @Column(name = "DOB")
    private Date dob;

    @ManyToMany(mappedBy = "renters")
    private List<PostModel> posts;
}
