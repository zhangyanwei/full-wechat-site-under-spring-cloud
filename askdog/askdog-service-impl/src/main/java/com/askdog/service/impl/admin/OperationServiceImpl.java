package com.askdog.service.impl.admin;

import com.askdog.service.UserService;
import com.askdog.service.admin.OperationService;
import com.askdog.service.bo.BasicInnerUser;
import com.askdog.service.bo.admin.operation.OperationState;
import com.askdog.service.bo.admin.operation.OpsUserRequestParam;
import com.askdog.service.exception.NotFoundException;
import com.askdog.service.search.SearchAgent;
import com.askdog.service.search.SearchRequest;
import com.askdog.service.search.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import java.net.UnknownHostException;

import static com.askdog.service.exception.NotFoundException.Error.USER;

@Service
@RestController
public class OperationServiceImpl implements OperationService {

    public static final String INDEX_ASKDOG = "askdog";

    @Autowired
    private ScoreOperationExecutor scoreOperationExecutor;

    @Autowired
    private UserService userService;

    @Autowired
    private SearchAgent searchAgent;

    @Override
    public OperationState updateUser(@RequestBody @Nonnull OpsUserRequestParam opsParam) throws UnknownHostException {
        BasicInnerUser innerUser = userService.findByEmail(opsParam.getEmail()).orElseThrow(() -> new NotFoundException(USER));
        return scoreOperationExecutor.updateScore(opsParam.getIndex(), opsParam.getType(), String.valueOf(innerUser.getId()), opsParam.getScore());
    }

    @Nonnull
    @Override
    public OperationState updateScore(@PathVariable(value = "type") String type,
                                      @PathVariable(value = "id") String id,
                                      @RequestParam(value = "score") Double score) throws UnknownHostException {
        return scoreOperationExecutor.updateScore(INDEX_ASKDOG, type, id, score);
    }

    @Override
    public SearchResult searchOperationResult(@RequestBody SearchRequest searchRequest) {
        return searchAgent.search(searchRequest);
    }

    @Override
    public SearchResult getAllOperationResult(@RequestBody SearchRequest searchRequest) {
        return searchAgent.search(searchRequest);
    }

}
