package com.example.iam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleEntity {

    @Id
    private Integer idRole;

    @Column(nullable = false,length = 50)
    private String name;

    @Column(nullable = false, length = 150)
    private String description;


}
