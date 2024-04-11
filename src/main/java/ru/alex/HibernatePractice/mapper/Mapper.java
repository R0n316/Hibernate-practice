package ru.alex.HibernatePractice.mapper;


public interface Mapper <F,T>{
    T mapFrom(F object);
}
