package com.renga.aws.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class DocumentDetails {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
