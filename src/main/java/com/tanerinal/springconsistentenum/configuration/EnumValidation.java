package com.tanerinal.springconsistentenum.configuration;

import com.tanerinal.springconsistentenum.model.Enum;
import com.tanerinal.springconsistentenum.annotation.MyEnum;
import com.tanerinal.springconsistentenum.annotation.MyEnumValidation;
import com.tanerinal.springconsistentenum.service.EnumService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@AutoConfigureAfter(EnumService.class)
@RequiredArgsConstructor
@Slf4j
public class EnumValidation {
    private final EnumService enumService;

    private static final String PACKAGE_NAME = "com.tanerinal.springconsistentenum.constant.enums";

    @PostConstruct
    private void validateEnums() {
        final List<Enum> enumList = enumService.getEnumList();

        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(PACKAGE_NAME.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        Set<Class> classSet = reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(this::getClass)
                .filter(enumClass -> enumClass.isEnum() && enumClass.isAnnotationPresent(MyEnum.class))
                .collect(Collectors.toSet());

        for (Class enumClass : classSet.stream().toList()) {
            Method validationMethod = Arrays.stream(enumClass.getMethods())
                    .filter(method -> method.isAnnotationPresent(MyEnumValidation.class))
                    .findAny()
                    .orElseThrow(() -> new RuntimeException(enumClass.getSimpleName() + " enum has no validation method!"));
            try {
                if (enumClass.getEnumConstants().length > 0) {
                    validationMethod.invoke(enumClass.getEnumConstants()[0], enumList);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Class getClass(String className) {
        try {
            return Class.forName(PACKAGE_NAME + "." + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void validateEnum(Class enumClass, List<Enum> enumDefinitionList, List<Enum> enumList) {
        final String enumGroup = enumClass.getSimpleName()
                .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
                .replaceAll("([a-z])([A-Z])", "$1_$2")
                .toUpperCase(Locale.ROOT);

        for (Enum enumItem : enumList) {
            boolean missingEnumDefinition = enumDefinitionList.stream()
                    .noneMatch(enumDefiniton ->
                            enumDefiniton.getGroup().equals(enumGroup) &&
                                    enumDefiniton.getName().equals(enumItem.getName()) &&
                                    enumDefiniton.getValue().equals(enumItem.getValue()));

            if (missingEnumDefinition) {
                throw new RuntimeException("Enum definition not found in data source! Enum Group: " + enumGroup + " Enum Item: " + enumItem.getName() + " Enum Value: " + enumItem.getValue());
            }
        }
    }
}
