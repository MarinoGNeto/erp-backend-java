package com.backend.adapter.inbound.web.v1;

import com.backend.adapter.inbound.web.dto.FilterProductRequest;
import com.backend.adapter.inbound.web.dto.Pagination;
import com.backend.adapter.inbound.web.dto.ProductRequest;
import com.backend.adapter.inbound.web.dto.ProductResponse;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

public interface ProductWebController {

    String PATH = "/api/products";
    String ID_PATH = "/{id}";
    String ID_PARAMETER_MESSAGE = "Product unique ID";

    @PostMapping(path = PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest productRequest);

    @PutMapping(path = PATH + ID_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ProductResponse> update(@Parameter(description = ID_PARAMETER_MESSAGE, required = true) @PathVariable("id") UUID id,
                                           @Valid @RequestBody ProductRequest productRequest);

    @GetMapping(path = PATH + ID_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ProductResponse> findById(@Parameter(description = ID_PARAMETER_MESSAGE, required = true) @PathVariable("id") UUID id);

    @GetMapping(path = PATH + ":paginated", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Pagination<ProductResponse>> findAll(
            @Min(value = 10, message = "Page size must be at least 10.") @Max(value = 100, message = "Page size must limited to 100.") @RequestParam(name = "_size", value = "_size", defaultValue = "10", required = false) Integer pageSize,
            @Min(value = 0, message = "Page number must be 0 or greater.") @RequestParam(name = "_page", value = "_page", defaultValue = "0", required = false) Integer page,
            @Valid FilterProductRequest filterProductRequest);

    @DeleteMapping(path = PATH + ID_PATH)
    ResponseEntity<Void> delete(@Parameter(description = ID_PARAMETER_MESSAGE, required = true) @PathVariable("id") UUID id);
}
