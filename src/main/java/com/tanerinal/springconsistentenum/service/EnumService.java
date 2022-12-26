package com.tanerinal.springconsistentenum.service;

import com.tanerinal.springconsistentenum.model.Enum;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class EnumService {

    public List<Enum> getEnumList() {
        return Arrays.asList(Enum.builder().group("STATUS").name("ACTIVE").value("12").build(),
                Enum.builder().group("STATUS").name("PASSIVE").value("0").build(),
                Enum.builder().group("PAYMENT_TYPE").name("CASH").value("1").build(),
                Enum.builder().group("PAYMENT_TYPE").name("MOBILE_PAYMENT").value("2").build(),
                Enum.builder().group("PAYMENT_TYPE").name("CREDIT_CARD").value("3").build());
    }
}
