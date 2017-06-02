package com.askdog.service;

import com.askdog.model.data.StoreSetting;
import com.askdog.service.bo.BasicUser;
import com.askdog.service.bo.TokenDetail;
import com.askdog.service.bo.addresscode.AddressCodePageDetail;
import com.askdog.service.bo.addresscode.PureAddressCode;
import com.askdog.service.bo.admin.dashboard.StoreStatistics;
import com.askdog.service.bo.common.ListedWrapper;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.store.AmendedStore;
import com.askdog.service.bo.store.PureStore;
import com.askdog.service.bo.store.StoreDetail;
import com.askdog.service.bo.store.StoreHome;
import com.askdog.service.bo.user.EmployeeUpdate;
import com.askdog.service.bo.user.StoreEmployeeDetail;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import java.util.List;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@FeignClient("service")
@RequestMapping("/service/store")
public interface StoreService {

    @Nonnull
    @RequestMapping("/_internal/{storeId}")
    com.askdog.service.bo.StoreDetail findDetail(@PathVariable("storeId") Long storeId,
                                                 @RequestParam(name = "enableDeleted", defaultValue = "false") boolean enableDeleted);

    @Nonnull
    @RequestMapping("/_internal/{storeId}/state")
    com.askdog.service.bo.StoreDetail findDetailWithState(@RequestParam(value = "userId", required = false) Long userId,
                                                          @PathVariable("storeId") long storeId,
                                                          @RequestParam(name = "enableDeleted", defaultValue = "false") boolean enableDeleted);

    @Nonnull
    @RequestMapping("/{storeId}")
    StoreDetail findPageDetail(@RequestParam(value = "userId", required = false) Long userId, @PathVariable("storeId") long storeId);

    @Nonnull
    @RequestMapping("/{storeId}/state")
    StoreDetail findPageDetailWithState(@RequestParam("userId") long userId,
                                        @PathVariable("storeId") long storeId);

    @Nonnull
    @RequestMapping("/all")
    PagedData<StoreHome> getStores(@RequestParam("ip") @Nonnull String ip,
                                   @RequestParam(name = "lat", required = false) Double lat,
                                   @RequestParam(name = "lng", required = false) Double lng,
                                   @RequestParam(name = "ad_code", required = false) String adCode,
                                   @RequestBody Pageable pageable);

    @Nonnull
    @RequestMapping("/list")
    PagedData<StoreDetail> getStoreByRole(@RequestParam("userId") long userId,
                                          @RequestBody Pageable pageable);

    @Nonnull
    @RequestMapping("/user/{userId}")
    List<Long> findOwnedStores(@PathVariable("userId") long userId);

    @Nonnull
    @RequestMapping(value = "/user/{userId}", method = POST, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(CREATED)
    StoreDetail create(@PathVariable("userId") long userId,
                       @Nonnull @Valid @RequestBody PureStore pureStore);

    @Nonnull
    @RequestMapping(value = "/user/{userId}/{storeId}", method = PUT, produces = APPLICATION_JSON_UTF8_VALUE)
    StoreDetail update(@PathVariable("userId") long userId,
                       @PathVariable("storeId") long storeId,
                       @Nonnull @Valid @RequestBody AmendedStore amendedStore);

    @RequestMapping(value = "/user/{userId}/{storeId}", method = DELETE)
    void delete(@PathVariable("userId") long userId,
                @PathVariable("storeId") long storeId);

    @RequestMapping(value = "/{storeId}/setting", method = GET)
    StoreSetting getSetting(@PathVariable("storeId") long storeId);

    @RequestMapping(value = "/{storeId}/setting", method = PUT)
    void updateSetting(@PathVariable("storeId") long storeId, @RequestBody StoreSetting setting);

    @RequestMapping(value = "/{storeId}/token")
    TokenDetail employeeBindToken(@Nonnull @RequestParam("userId") Long userId, @Nonnull @PathVariable("storeId") Long storeId);

    @RequestMapping(value = "/{storeId}/employees/list", method = POST)
    PagedData<StoreEmployeeDetail> getEmployees(@Nonnull @RequestParam("userId") Long userId,
                                                @Nonnull @PathVariable("storeId") Long storeId,
                                                @RequestBody Pageable pageable);

    @RequestMapping(value = "/{storeId}/employees", method = POST)
    StoreEmployeeDetail addEmployee(@Nonnull @RequestParam("token") String token,
                                    @Nonnull @RequestParam("userId") Long userId,
                                    @Nonnull @PathVariable("storeId") Long storeId);

    @RequestMapping(value = "/{storeId}/employees", method = PUT)
    StoreEmployeeDetail updateEmployee(@Nonnull @RequestParam("userId") Long userId,
                                       @Nonnull @PathVariable("storeId") Long storeId,
                                       @Nonnull @RequestParam("employeeId") Long employeeId,
                                       @RequestBody EmployeeUpdate employeeUpdate);

    @RequestMapping(value = "/{storeId}/employees/{employeeId}", method = DELETE)
    void removeEmployee(@Nonnull @RequestParam("userId") Long userId,
                        @Nonnull @PathVariable("storeId") Long storeId,
                        @Nonnull @PathVariable("employeeId") Long employeeId);

    @RequestMapping(value = "/statistic", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    StoreStatistics storeStatistic();

    @RequestMapping(value = "/search/{key}", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    List<BasicUser> search(@PathVariable("key") String key);

    @RequestMapping(value = "/cities", method = GET)
    ListedWrapper<AddressCodePageDetail> getCities();

    @RequestMapping(value = "/cities/create", method = POST)
    void createCites(@Valid @RequestBody PureAddressCode pureAddressCode);
}
