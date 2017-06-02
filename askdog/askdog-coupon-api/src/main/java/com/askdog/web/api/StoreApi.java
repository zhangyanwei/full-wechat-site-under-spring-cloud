package com.askdog.web.api;

import com.askdog.service.StoreService;
import com.askdog.service.bo.addresscode.AddressCodePageDetail;
import com.askdog.service.bo.addresscode.PureAddressCode;
import com.askdog.service.bo.common.ListedWrapper;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.store.StoreDetail;
import com.askdog.service.bo.store.StoreHome;
import com.askdog.service.bo.user.StoreEmployeeDetail;
import com.askdog.web.configuration.userdetails.AdUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.askdog.web.utils.HeaderUtils.getRequestRealIp;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@RestController
@RequestMapping("/api/stores")
public class StoreApi {

    @Autowired
    private StoreService storeService;

    @Nonnull
    @RequestMapping(method = RequestMethod.GET)
    public PagedData<StoreHome> getStores(HttpServletRequest request,
                                          @RequestParam(name = "lat", required = false) Double lat,
                                          @RequestParam(name = "lng", required = false) Double lng,
                                          @RequestParam(name = "ad_code", required = false) String adCode,
                                          @PageableDefault(sort = "creationTime", direction = DESC) Pageable pageable) {
        return storeService.getStores(getRequestRealIp(request), lat, lng, adCode, pageable);
    }

    @Nonnull
    @RequestMapping(value = "/{storeId}", method = RequestMethod.GET)
    public StoreDetail getStoreDetail(@AuthenticationPrincipal AdUserDetails user, @PathVariable("storeId") long storeId) {
        return storeService.findPageDetail(user != null ? user.getId() : null, storeId);
    }

    @Nonnull
    @RequestMapping(value = "/cities", method = RequestMethod.GET)
    public ListedWrapper<AddressCodePageDetail> getCites(@AuthenticationPrincipal AdUserDetails user) {
        return storeService.getCities();
    }

    @RequestMapping(value = "/cities", method = RequestMethod.POST)
    public void createCites(@AuthenticationPrincipal AdUserDetails user,
                            @Valid @RequestBody PureAddressCode pureAddressCode) {
        storeService.createCites(pureAddressCode);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{storeId}/employees", method = POST)
    public StoreEmployeeDetail addEmployee(@AuthenticationPrincipal AdUserDetails user, @PathVariable("storeId") Long storeId,
                                           @RequestParam String token) {
        return storeService.addEmployee(token, user.getId(), storeId);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{storeId}/employees/{employeeId}", method = DELETE)
    public void removeEmployee(@AuthenticationPrincipal AdUserDetails user, @PathVariable("storeId") Long storeId, @PathVariable("employeeId") Long employeeId) {
        storeService.removeEmployee(user.getId(), storeId, employeeId);
    }

}
