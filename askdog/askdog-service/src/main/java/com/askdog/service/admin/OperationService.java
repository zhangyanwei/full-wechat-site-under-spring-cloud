package com.askdog.service.admin;

import com.askdog.service.bo.admin.operation.OperationState;
import com.askdog.service.bo.admin.operation.OpsUserRequestParam;
import com.askdog.service.search.SearchRequest;
import com.askdog.service.search.SearchResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import java.net.UnknownHostException;

@FeignClient("service")
@RequestMapping("/service/operation")
public interface OperationService {

    @RequestMapping(value = "/score/user", method = RequestMethod.PUT)
    OperationState updateUser(@RequestBody @Nonnull OpsUserRequestParam opsUserRequestParam) throws UnknownHostException;

    @Nonnull
    @RequestMapping(value = "/{type}/{id}", method = RequestMethod.PUT)
    OperationState updateScore(@PathVariable(value = "type") String type,
                               @PathVariable(value = "id") String id,
                               @RequestParam(value = "score") Double score) throws UnknownHostException;

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    SearchResult searchOperationResult(@RequestBody SearchRequest searchRequest);

    @RequestMapping(value = "/search/all",method = RequestMethod.POST)
    SearchResult getAllOperationResult(@RequestBody SearchRequest searchRequest);

}
