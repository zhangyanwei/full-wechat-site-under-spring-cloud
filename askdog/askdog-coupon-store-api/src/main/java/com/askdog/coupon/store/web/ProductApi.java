package com.askdog.coupon.store.web;

import com.askdog.oauth.client.vo.UserInfo;
import com.askdog.service.ProductService;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.product.ProductCreation;
import com.askdog.service.bo.product.ProductDetail;
import com.askdog.service.bo.product.ProductUpdate;
import com.askdog.service.bo.product.productdetail.ProductPageDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/api/products")
public class ProductApi {

    private static final String PRE_AUTHORIZE = "hasAnyRole('ROLE_ADMIN', 'ROLE_AGENT_ADMIN', 'ROLE_AGENT_EMPLOYEE', 'ROLE_STORE_ADMIN')";

    @Autowired private ProductService productService;

    @PreAuthorize(PRE_AUTHORIZE)
    @RequestMapping(value = "/management", params = {"store_id"})
    public PagedData<ProductDetail>
    getManageStoreProducts(@AuthenticationPrincipal UserInfo user,
                           @RequestParam(value = "store_id") Long storeId,
                           @PageableDefault(sort = "creationTime", direction = DESC) Pageable pageable) {
        return productService.getManageStoreProduct(user != null ? user.getId() : null, storeId, pageable);
    }

    @PreAuthorize(PRE_AUTHORIZE)
    @RequestMapping(value = "/{productId}")
    public ProductPageDetail get(@AuthenticationPrincipal UserInfo user, @PathVariable Long productId) {
        return productService.getPageDetail(user != null ? user.getId() : null, productId);
    }

    @PreAuthorize(PRE_AUTHORIZE)
    @RequestMapping(method = POST)
    public ProductPageDetail create(@AuthenticationPrincipal UserInfo user,
                                    @RequestBody @Valid ProductCreation productCreation) {
        return productService.create(user.getId(), productCreation);
    }

    @PreAuthorize(PRE_AUTHORIZE)
    @RequestMapping(value = "/{productId}", method = PUT)
    public ProductPageDetail update(@AuthenticationPrincipal UserInfo user, @PathVariable Long productId,
                                    @RequestBody @Valid ProductUpdate productUpdate) {
        return productService.update(user.getId(), productUpdate.setId(productId));
    }

    @PreAuthorize(PRE_AUTHORIZE)
    @RequestMapping(value = "/{productId}", method = DELETE)
    public void delete(@AuthenticationPrincipal UserInfo user, @PathVariable Long productId) {
        productService.delete(user.getId(), productId);
    }

}
