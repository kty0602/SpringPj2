package com.sparta.second.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@MappedSuperclass
@EntityListeners(value = { AuditingEntityListener.class})
@Getter
public class BaseEntity {
    @CreatedDate
    @Column(name = "reg_date", updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate regDate;

    @LastModifiedDate
    @Column(name ="mod_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate modDate;
}
