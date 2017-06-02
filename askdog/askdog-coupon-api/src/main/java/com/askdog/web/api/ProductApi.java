package com.askdog.web.api;

import com.askdog.model.data.inner.VoteDirection;
import com.askdog.service.ProductService;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.product.productdetail.ProductPageDetail;
import com.askdog.service.bo.storedetail.StorePageDetail_Product;
import com.askdog.web.configuration.userdetails.AdUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping(value = "/api/products")
public class ProductApi {

    @Autowired private ProductService productService;

    @RequestMapping(value = "/{productId}")
    public ProductPageDetail get(@AuthenticationPrincipal AdUserDetails user, @PathVariable Long productId) {
        return productService.getPageDetail(user != null ? user.getId() : null, productId);
    }

    @RequestMapping(params = {"store_id"})
    public PagedData<StorePageDetail_Product>
    getStoreProducts(@AuthenticationPrincipal AdUserDetails user, @RequestParam(value = "store_id") Long storeId,
                     @PageableDefault(sort = "creationTime", direction = DESC) Pageable pageable) {
        return productService.getStoreProduct(user != null ? user.getId() : null, storeId, pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{productId}/vote", method = PUT)
    public void vote(@AuthenticationPrincipal AdUserDetails user, @PathVariable("productId") Long productId,
                     @RequestParam(value = "direction") VoteDirection direction) {
        productService.vote(user.getId(), productId, direction);
    }

}
