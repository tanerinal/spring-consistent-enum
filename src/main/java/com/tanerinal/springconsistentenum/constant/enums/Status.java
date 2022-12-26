package com.tanerinal.springconsistentenum.constant.enums;

import com.tanerinal.springconsistentenum.configuration.EnumValidation;
import com.tanerinal.springconsistentenum.constant.IEnum;
import com.tanerinal.springconsistentenum.model.Enum;
import com.tanerinal.springconsistentenum.annotation.MyEnum;
import com.tanerinal.springconsistentenum.annotation.MyEnumValidation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
@MyEnum
@AutoConfigureBefore(EnumValidation.class)
public enum Status implements IEnum<Status> {
    ACTIVE("1"),
    PASSIVE ("0");

    private final String value;

    @Override
    public Status getByValue(String value) {
        return Arrays.stream(Status.values())
                .filter(status -> status.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }

    @MyEnumValidation
    @Override
    public void validateEnum(List<Enum> enumDefinitionList) {
        List<Enum> enumAsList = Arrays.stream(Status.values())
                .map(paymentType -> Enum.builder()
                        .name(paymentType.name())
                        .value(paymentType.getValue()).build()).toList();

        EnumValidation.validateEnum(Status.this.getClass(), enumDefinitionList, enumAsList);
    }
}
