package com.askdog.operation.web.api;

import com.askdog.operation.web.vo.OperationSearchParam;
import com.askdog.service.admin.OperationService;
import com.askdog.service.bo.admin.operation.OperationState;
import com.askdog.service.bo.admin.operation.OpsUserRequestParam;
import com.askdog.service.search.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RestController
@RequestMapping("/api/score")
public class OperationApi {

    @Autowired
    private OperationService operationService;

    @PreAuthorize("hasRole('ROLE_OP_RANK')")
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public OperationState operationUser(@RequestBody @Validated OpsUserRequestParam opsScoreRequestParam) throws UnknownHostException {
        return operationService.updateUser(opsScoreRequestParam);
    }

    @PreAuthorize("hasRole('ROLE_OP_RANK')")
    @RequestMapping(value = "/{type}/{id}", method = RequestMethod.PUT)
    public OperationState updateScore(@PathVariable(value = "type") String type,
                                      @PathVariable(value = "id") String id,
                                      @RequestParam(value = "score") Double score) throws UnknownHostException {
        return operationService.updateScore(type, id, score);
    }

    @PreAuthorize("hasRole('ROLE_OP_RANK')")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public SearchResult searchOperationResult(@Validated OperationSearchParam operationSearchParam) {
        return operationService.searchOperationResult(operationSearchParam.convert());
    }

    @PreAuthorize("hasRole('ROLE_OP_RANK')")
    @RequestMapping(value = "/search/all", method = RequestMethod.GET)
    public SearchResult getAllOperationResult(@Validated OperationSearchParam operationSearchParam) {
        return operationService.getAllOperationResult(operationSearchParam.convert());
    }

}
