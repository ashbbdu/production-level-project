package com.api_task_management.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

//This class is created for pagination
@Data
@AllArgsConstructor
public class PageResponse <T> {
    private List<T> data;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
}
