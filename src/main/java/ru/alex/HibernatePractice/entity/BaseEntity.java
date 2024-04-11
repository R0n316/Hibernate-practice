package ru.alex.HibernatePractice.entity;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public interface BaseEntity<T> {

    T getId();
    void setId(T id);

//    TODO провалидировать все сущности
}
