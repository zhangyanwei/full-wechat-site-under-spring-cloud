package com.askdog.service.impl.search;


import com.askdog.model.redis.SerializableValueRedisTemplate;
import com.askdog.service.search.SearchAgent;
import com.askdog.service.search.SearchRequest;
import com.askdog.service.search.SearchResult;
import com.google.common.collect.Lists;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Component
public class EsSearchAgent implements SearchAgent {

    private static EsResponseResolver esResponseResolver = new EsResponseResolver();

    private static Scroll scroll = new Scroll(TimeValue.parseTimeValue("1d", null, ""));

    @Autowired
    private SerializableValueRedisTemplate<String> serializableRedisTemplate;

    @Autowired
    private SearchBuilderProducer searchBuilderProducer;

    @Override
    public SearchResult search(SearchRequest searchRequest) {
        Assert.notNull(searchRequest);

        // disable scroll pagination
        searchRequest.setSearchId(null);

        SearchRequestBuilder searchRequestBuilder = searchBuilderProducer.getSearchRequestBuilder(searchRequest);
        SearchResult searchResult = esResponseResolver.resolve(searchRequestBuilder.get());

        initLastFlag(searchRequest, searchResult);

        if (!StringUtils.isEmpty(searchResult.getScrollId())) {
            serializableRedisTemplate.opsForValue().set(searchRequest.getSearchId(), searchResult.getScrollId());
            serializableRedisTemplate.expire(searchRequest.getSearchId(), scroll.keepAlive().getMillis(), MILLISECONDS);
        }
        return searchResult;
    }

    @Override
    public List<String> searchIds(SearchRequest searchRequest) {
        List<String> ids = Lists.newArrayList();
        SearchResponse searchResponse = searchBuilderProducer.getSearchRequestBuilder(searchRequest).get();
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            ids.add(searchHit.getId());
        }
        return ids;
    }

    private void initLastFlag(SearchRequest searchRequest, SearchResult searchResult) {
        int from = Integer.valueOf(String.valueOf(searchRequest.getParams().get("from")));
        int size = Integer.valueOf(String.valueOf(searchRequest.getParams().get("size")));
        searchResult.setLast(from + size >= searchResult.getTotal());
    }

}