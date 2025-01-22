package com.example.jiranbulletinboard.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Converter {
    public static <E, D> List<D> EntityListToDtoList(List<E> entityList, Function<E, D> toDtoFunction) {
        if (entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return entityList.stream()
                .map(toDtoFunction)
                .collect(Collectors.toList());
    }
    public static <D, E> List<E> DtoListToEntityList(List<D> dtoList, Function<D, E> toEntityFunction) {
        if (dtoList.isEmpty()) {
            return new ArrayList<>();
        }
        return dtoList.stream()
                .map(toEntityFunction)
                .collect(Collectors.toList());
    }
}