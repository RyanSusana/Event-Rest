package com.isa.arnhem.isarest.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResultPage<T extends Identifiable> {
    private List<T> values;
    private int pageNumber;
    private int numberOfPages;
    private int pageSize;
}
