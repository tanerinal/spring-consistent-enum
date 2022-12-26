package com.tanerinal.springconsistentenum.constant;

import com.tanerinal.springconsistentenum.model.Enum;

import java.util.List;

public interface IEnum<T> {
    String getValue();
    T getByValue(String value);
    void validateEnum(List<Enum> enumList);
}
