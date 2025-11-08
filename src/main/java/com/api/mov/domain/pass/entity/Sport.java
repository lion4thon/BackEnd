package com.api.mov.domain.pass.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; //종목명

//    @Column(nullable = false)
//    private String tags; //종목 특징? 느낌?

}
