package com.askdog.coupon.store.web;

import com.askdog.model.data.StoreSetting;
import com.askdog.oauth.client.vo.UserInfo;
import com.askdog.service.StoreService;
import com.askdog.service.UserCouponService;
import com.askdog.service.bo.BasicUser;
import com.askdog.service.bo.TokenDetail;
import com.askdog.service.bo.admin.dashboard.CouponStatistics;
import com.askdog.service.bo.admin.dashboard.StoreStatistics;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.store.AmendedStore;
import com.askdog.service.bo.store.PureStore;
import com.askdog.service.bo.store.StoreDetail;
import com.askdog.service.bo.user.EmployeeUpdate;
import com.askdog.service.bo.user.StoreEmployeeDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/api/stores")
public class StoreApi {

    private static final String STORE_MANAGER = "hasAnyRole('ROLE_ADMIN', 'ROLE_AGENT_ADMIN', 'ROLE_AGENT_EMPLOYEE', 'ROLE_STORE_ADMIN')";

    @Autowired private StoreService storeService;
    @Autowired private UserCouponService userCouponService;

    @PreAuthorize(STORE_MANAGER)
    @RequestMapping(value = "/list", method = GET)
    public PagedData<StoreDetail> getStoreList(@AuthenticationPrincipal UserInfo userInfo,
                                               @PageableDefault(sort = "creationTime", direction = DESC) Pageable pageable) {
        return storeService.getStoreByRole(userInfo.getId(), pageable);
    }

    @PreAuthorize(STORE_MANAGER)
    @RequestMapping(value = "/{storeId}", method = GET)
    public StoreDetail getStoreDetail(@AuthenticationPrincipal UserInfo userInfo, @PathVariable("storeId") long storeId) {
        return storeService.findPageDetail(userInfo.getId(), storeId);
    }

    @PreAuthorize(STORE_MANAGER)
    @RequestMapping(method = POST)
    public StoreDetail create(@AuthenticationPrincipal UserInfo userInfo, @Valid @RequestBody PureStore pureStore) {
        return storeService.create(userInfo.getId(), pureStore);
    }

    @PreAuthorize(STORE_MANAGER)
    @RequestMapping(value = "/{storeId}", method = PUT)
    public StoreDetail update(@AuthenticationPrincipal UserInfo userInfo, @PathVariable("storeId") Long storeId,
                              @Valid @RequestBody AmendedStore amendedStore) {
        return storeService.update(userInfo.getId(), storeId, amendedStore);
    }

    @PreAuthorize(STORE_MANAGER)
    @RequestMapping(value = "/{storeId}", method = DELETE)
    public void delete(@AuthenticationPrincipal UserInfo userInfo, @PathVariable("storeId") Long storeId) {
        storeService.delete(userInfo.getId(), storeId);
    }

    @PreAuthorize(STORE_MANAGER)
    @RequestMapping(value = "/{storeId}/setting", method = GET)
    public StoreSetting getStoreSetting(@PathVariable("storeId") long storeId) {
        return storeService.getSetting(storeId);
    }

    @PreAuthorize(STORE_MANAGER)
    @RequestMapping(value = "/{storeId}/setting", method = PUT)
    public void updateStoreSetting(@PathVariable("storeId") long storeId, @RequestBody StoreSetting setting) {
        storeService.updateSetting(storeId, setting);
    }

    @PreAuthorize(STORE_MANAGER)
    @RequestMapping(value = "/{storeId}/employee-bind-token")
    public TokenDetail employeeBindToken(@AuthenticationPrincipal UserInfo userInfo, @PathVariable("storeId") Long storeId) {
        return storeService.employeeBindToken(userInfo.getId(), storeId);
    }

    @PreAuthorize(STORE_MANAGER)
    @RequestMapping(value = "/{storeId}/employees")
    public PagedData<StoreEmployeeDetail> employees(@AuthenticationPrincipal UserInfo userInfo,
                                                    @PathVariable("storeId") Long storeId,
                                                    @PageableDefault(sort = "creationTime", direction = DESC) Pageable pageable) {
        return storeService.getEmployees(userInfo.getId(), storeId, pageable);
    }

    @PreAuthorize(STORE_MANAGER)
    @RequestMapping(value = "/{storeId}/employees", method = PUT)
    public StoreEmployeeDetail updateEmployee(@AuthenticationPrincipal UserInfo userInfo,
                                              @PathVariable("storeId") Long storeId,
                                              @RequestParam Long employeeId,
                                              @RequestBody EmployeeUpdate employeeUpdate) {
        return storeService.updateEmployee(userInfo.getId(), storeId, employeeId, employeeUpdate);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{storeId}/employees", method = POST)
    public StoreEmployeeDetail addEmployee(@AuthenticationPrincipal UserInfo userInfo, @PathVariable("storeId") Long storeId,
                                           @RequestParam String token) {
        return storeService.addEmployee(token, userInfo.getId(), storeId);
    }

    @PreAuthorize(STORE_MANAGER)
    @RequestMapping(value = "/{storeId}/employees/{employeeId}", method = DELETE)
    public void removeEmployee(@AuthenticationPrincipal UserInfo userInfo, @PathVariable("storeId") Long storeId, @PathVariable("employeeId") Long employeeId) {
        storeService.removeEmployee(userInfo.getId(), storeId, employeeId);
    }

    @PreAuthorize(STORE_MANAGER)
    @RequestMapping(value = "/search/{key}", produces = APPLICATION_JSON_UTF8_VALUE)
    public List<BasicUser> search(@PathVariable("key") String key) {
        return storeService.search(key);
    }

    @PreAuthorize(STORE_MANAGER)
    @RequestMapping(value = "/storedata", produces = APPLICATION_JSON_UTF8_VALUE)
    public StoreStatistics store() {
        return storeService.storeStatistic();
    }

    @PreAuthorize(STORE_MANAGER)
    @RequestMapping(value = "/coupondata", produces = APPLICATION_JSON_UTF8_VALUE)
    public CouponStatistics coupon() {
        return userCouponService.couponStatistic();
    }
}
