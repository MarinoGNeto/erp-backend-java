package com.backend.adapter.inbound.web.v1;

import com.backend.adapter.inbound.web.dto.FilterProductRequest;
import com.backend.adapter.inbound.web.dto.Pagination;
import com.backend.adapter.inbound.web.dto.ProductRequest;
import com.backend.adapter.inbound.web.dto.ProductResponse;
import com.backend.core.domain.Product;
import com.backend.core.port.ProductInboundPort;
import com.backend.core.usecase.PageContent;
import com.backend.core.usecase.dto.FilterProductCommand;
import com.backend.core.usecase.dto.ProductCommand;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
@Validated
public class ProductWebControllerImpl implements ProductWebController {

    private final ProductInboundPort productInboundPort;
    private final ConversionService conversionService;

    public ProductWebControllerImpl(ProductInboundPort productInboundPort, ConversionService conversionService) {
        this.productInboundPort = productInboundPort;
        this.conversionService = conversionService;
    }

    @Override
    public ResponseEntity<ProductResponse> create(ProductRequest productRequest) {
        ProductCommand productCommand = conversionService.convert(productRequest, ProductCommand.class);

        ProductResponse productResponse = conversionService.convert(productInboundPort.create(productCommand), ProductResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @Override
    public ResponseEntity<ProductResponse> update(UUID id, ProductRequest productRequest) {
        ProductCommand productCommand = conversionService.convert(productRequest, ProductCommand.class);
        Product product = productInboundPort.update(productCommand, id);

        ProductResponse productResponse = conversionService.convert(product, ProductResponse.class);
        return ResponseEntity.ok(productResponse);
    }

    @Override
    public ResponseEntity<ProductResponse> findById(UUID id) {
        return ResponseEntity.ok(conversionService.convert(productInboundPort.findById(id), ProductResponse.class));
    }

    @Override
    public ResponseEntity<Pagination<ProductResponse>> findAll(Integer pageSize, Integer page, FilterProductRequest filterProductRequest) {
        PageContent<Product> pageContent = productInboundPort.findAll(conversionService.convert(filterProductRequest, FilterProductCommand.class), pageSize, page);
        List<ProductResponse> productResponseList = pageContent.content().stream().map(product -> conversionService.convert(product, ProductResponse.class)).toList();
        Pagination<ProductResponse> responsePagination = new Pagination<>(productResponseList, pageContent.totalPages(), pageContent.totalElements(), pageContent.pageNumber());

        return ResponseEntity.ok(responsePagination);
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        productInboundPort.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
