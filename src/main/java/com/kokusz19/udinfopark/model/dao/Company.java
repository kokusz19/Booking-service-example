package com.kokusz19.udinfopark.model.dao;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @NotNull
    @Column(name = "address", nullable = false)
    private String address;
    @NotNull @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "hour", column = @Column(name = "open_at_hour")),
            @AttributeOverride(name = "minute", column = @Column(name = "open_at_minute"))
    })
    private Time openAt;
    @NotNull @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "hour", column = @Column(name = "close_at_hour")),
            @AttributeOverride(name = "minute", column = @Column(name = "close_at_minute"))
    })
    private Time closeAt;
}
