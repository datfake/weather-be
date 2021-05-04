package com.datngo.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    String city;
    LocalDateTime date;
    Float minTemp;
    Float maxTemp;
    String main;

    @Column(name = "created_on", length = 50)
    LocalDateTime createOn;

    @Column(name = "updated_on", length = 50)
    LocalDateTime updateOn;
}
