package com.quang.template.utils;

import com.quang.template.dto.response.PageResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public class PageUtils {

    public static <T, R> PageResponse<R> createPageResponse(Page<T> page, List<R> content, HttpServletRequest request) {
        String baseUrl = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString())
                .replaceQuery(request.getQueryString() != null ? request.getQueryString() : "")
                .replaceQueryParam("page")
                .build()
                .toUriString();

        String nextPage = page.hasNext()
                ? baseUrl + (baseUrl.contains("?") ? "&" : "?") + "page=" + (page.getNumber() + 1)
                : null;

        String prevPage = page.hasPrevious()
                ? baseUrl + (baseUrl.contains("?") ? "&" : "?") + "page=" + (page.getNumber() - 1)
                : null;

        return PageResponse.<R>builder()
                .content(content)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .first(page.isFirst())
                .nextPage(nextPage)
                .prevPage(prevPage)
                .build();
    }
}