package com.askdog.service.impl.search;


import com.askdog.model.redis.SerializableValueRedisTemplate;
import com.askdog.service.impl.search.component.SearchRequestBuilderAdapter;
import com.askdog.service.search.SearchRequest;
import com.google.common.collect.Maps;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.Scroll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static com.askdog.common.utils.Json.writeValueAsString;

@Service
public class SearchBuilderProducer {

    @Autowired
    private Client client;

    @Autowired
    private SerializableValueRedisTemplate<String> serializableRedisTemplate;

    private static Scroll scroll = new Scroll(TimeValue.parseTimeValue("1d", null, ""));

    public SearchRequestBuilder getSearchRequestBuilder(SearchRequest searchRequest) {
        SearchRequestBuilder searchRequestBuilder;

        String scrollId = searchRequest.getSearchId() == null ? null : serializableRedisTemplate.opsForValue().get(searchRequest.getSearchId());

        if (!StringUtils.isEmpty(scrollId)) {
            searchRequestBuilder = new SearchRequestBuilderAdapter(client, client.prepareSearchScroll(scrollId));

        } else {
            Map<String, Object> queryDslTemplate = Maps.newHashMap();
            queryDslTemplate.put("id", searchRequest.getType());
            queryDslTemplate.put("params", searchRequest.getParams());
            searchRequestBuilder = client.prepareSearch().setTemplateSource(writeValueAsString(queryDslTemplate));
        }

        if (!StringUtils.isEmpty(searchRequest.getSearchId())) {
            searchRequestBuilder.setScroll(scroll);
        }

        return searchRequestBuilder;
    }

    public MultiGetRequestBuilder getMultiGetBuilder(String index, String type, List<String> ids) {
        return client.prepareMultiGet().add(index, type, ids);
    }

}
