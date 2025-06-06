package com.pokedex.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponse<T> {
    private List<T> data;
    private int offset;
    private int limit;
    private int total;
}
