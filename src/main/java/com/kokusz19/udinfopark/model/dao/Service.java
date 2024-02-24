package com.kokusz19.udinfopark.model.dao;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int serviceId;
    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @NotNull @Valid
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    @Column(name = "description")
    private String description;
    @NotNull @Min(0)
    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;
}
