package com.askdog.service;

import com.askdog.model.data.inner.VoteDirection;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.product.ProductCreation;
import com.askdog.service.bo.product.ProductDetail;
import com.askdog.service.bo.product.ProductUpdate;
import com.askdog.service.bo.product.productdetail.ProductPageDetail;
import com.askdog.service.bo.storedetail.StorePageDetail_Product;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@FeignClient("service")
@RequestMapping("/service/products")
public interface ProductService {

    @RequestMapping(value = "/{productId}/detail")
    ProductDetail getDetail(@RequestParam(value = "userId", required = false) Long userId,
                            @PathVariable(value = "productId") Long productId,
                            @RequestParam(value = "allow_deleted") boolean allowDeleted);

    @RequestMapping(value = "/{productId}/page_detail")
    ProductPageDetail getPageDetail(@RequestParam(value = "userId", required = false) Long userId,
                                    @PathVariable(value = "productId") Long productId);

    @RequestMapping(params = {"store_id"})
    PagedData<StorePageDetail_Product> getStoreProduct(@RequestParam(value = "userId", required = false) Long userId,
                                                       @RequestParam(value = "store_id") Long storeId,
                                                       @RequestBody Pageable pageable);

    @RequestMapping(value = "/management", params = {"store_id"})
    PagedData<ProductDetail> getManageStoreProduct(@RequestParam(value = "userId", required = false) Long userId,
                                                   @RequestParam(value = "store_id") Long storeId,
                                                   @RequestBody Pageable pageable);

    @Nonnull
    @RequestMapping(method = POST)
    ProductPageDetail create(@RequestParam(value = "user_id") Long userId,
                             @RequestBody @Valid ProductCreation productCreation);

    @Nonnull
    @RequestMapping(method = PUT)
    ProductPageDetail update(@RequestParam(value = "user_id") Long userId,
                             @RequestBody @Valid ProductUpdate productUpdate);

    @RequestMapping(method = DELETE)
    void delete(@RequestParam(value = "user_id") Long userId,
                @RequestParam(value = "product_id") Long productId);

    @RequestMapping(value = "/{productId}/vote", method = PUT)
    void vote(@RequestParam("userId") Long userId,
                   @PathVariable("productId") Long productId,
                   @RequestParam(value = "direction") VoteDirection direction);

}
