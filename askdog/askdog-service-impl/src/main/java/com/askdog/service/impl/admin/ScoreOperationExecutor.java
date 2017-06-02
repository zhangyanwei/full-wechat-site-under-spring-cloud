package com.askdog.service.impl.admin;

import com.askdog.service.bo.admin.operation.OperationState;
import com.google.common.collect.Maps;
import freemarker.ext.beans.HashAdapter;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;

@Service
public class ScoreOperationExecutor {

    @Autowired
    private Client client;

    public OperationState updateScore(String index, String type, String id, Double score) throws UnknownHostException {

        OperationState operationState = new OperationState();
        Map<String,Object> params = Maps.newHashMap();
        params.put("operation_score",score);
        params.put("modified", score > 0);
        client.prepareUpdate(index, type, id)
                .setDoc(params)
                .get();
        operationState.setId(id);
        operationState.setOperationScore(score);
        operationState.setOperationTime(new Date());
        operationState.setIndex(index);
        operationState.setType(type);
        return operationState;
    }

}
