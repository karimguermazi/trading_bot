package com.currency.exchanger.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int permissionId;

    private String description;
    private String httpMethod;
    private String apiPath;
    private boolean status;
}
