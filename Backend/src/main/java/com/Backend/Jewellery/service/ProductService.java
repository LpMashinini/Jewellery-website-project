package com.Backend.Jewellery.service;

import com.Backend.Jewellery.dto.PageResponse;
import com.Backend.Jewellery.dto.ProductResponse;
import com.Backend.Jewellery.entity.Product;
import com.Backend.Jewellery.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final int MAX_PAGE_SIZE = 50;
    private static final List<String> SORTABLE_FIELDS = List.of("id", "name", "price");

    private final ProductRepository productRepository;



    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getProducts(
            Long categoryId, int page, int size, String sortBy, String direction
    ){
        Pageable pageable = buildPageable(page, size, sortBy, direction);

        Page<Product> products = (categoryId == null) ? productRepository.findAll(pageable) : productRepository.findByCategoryId(categoryId, pageable);

        return PageResponse.from(products.map(ProductResponse::from));
    }

    private Pageable buildPageable(int page, int size, String sortBy,String direction){


        if(!SORTABLE_FIELDS.contains(sortBy)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cannot sort by '" + sortBy + "'. allowed: " + SORTABLE_FIELDS
            );
        }

        Sort.Direction sortDirection;
        if ("asc".equalsIgnoreCase(direction)){
            sortDirection = Sort.Direction.ASC;
        } else if ("desc".equalsIgnoreCase(direction)){
            sortDirection = Sort.Direction.DESC;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "direction must be 'asc' or 'desc' "
                    );
        }

        Sort sort = Sort.by(sortDirection, sortBy);
        if (!sortBy.equals("id")){
            sort = sort.and(Sort.by(Sort.Direction.ASC, "id")); // tie-breaker
        }

        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);

        return PageRequest.of(safePage, safeSize, sort);
    }
}
