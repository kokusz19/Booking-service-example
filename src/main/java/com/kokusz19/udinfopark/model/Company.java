package com.kokusz19.udinfopark.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int companyId;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Column(name = "address", nullable = false)
    private String address;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "hour", column = @Column(name = "open_at_hour")),
            @AttributeOverride(name = "minute", column = @Column(name = "open_at_minute"))
    })
    private Time openAt;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "hour", column = @Column(name = "close_at_hour")),
            @AttributeOverride(name = "minute", column = @Column(name = "close_at_minute"))
    })
    private Time closeAt;
}
