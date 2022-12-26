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
public enum PaymentType implements IEnum<PaymentType> {
    CASH("1"),
    MOBILE_PAYMENT("2"),
    CREDIT_CARD ("3");

    private final String value;

    @Override
    public PaymentType getByValue(String value) {
        return Arrays.stream(PaymentType.values())
                .filter(status -> status.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }

    @MyEnumValidation
    @Override
    public void validateEnum(List<Enum> enumDefinitionList) {
        List<Enum> enumAsList = Arrays.stream(PaymentType.values())
                .map(paymentType -> Enum.builder()
                        .name(paymentType.name())
                        .value(paymentType.getValue()).build()).toList();

        EnumValidation.validateEnum(PaymentType.this.getClass(), enumDefinitionList, enumAsList);
    }
}
