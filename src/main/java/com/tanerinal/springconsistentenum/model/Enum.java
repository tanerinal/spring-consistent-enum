package com.tanerinal.springconsistentenum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Enum {
    private String group;
    private String name;
    private String value;
}
