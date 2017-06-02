package com.askdog.service.impl.search;

import com.askdog.common.utils.MapUtils;
import com.askdog.service.search.SearchResult;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHitField;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EsResponseResolver {

    public SearchResult resolve(SearchResponse searchResponse) {
        SearchResult searchResult = new SearchResult();

        searchResult.setScrollId(searchResponse.getScrollId());
        searchResult.setTotal(searchResponse.getHits().getTotalHits());

        searchResult.setResult(Stream.of(searchResponse.getHits().getHits()).map(searchHitField -> {
            Map<String, Object> resultMap = searchHitField.getSource();
            for (String key : searchHitField.fields().keySet()) {
                Object value = getValue(searchHitField.fields().get(key));
                if (value != null) {
                    MapUtils.setValue(resultMap, key.split("\\."), value);
                }
            }
            for (String key : searchHitField.getHighlightFields().keySet()) {
                MapUtils.setValue(resultMap, key.split("\\."), searchHitField.getHighlightFields().get(key).fragments()[0].string());
            }

            return resultMap;
        }).collect(Collectors.toList()));
        return searchResult;
    }

    @SuppressWarnings("unchecked")
    private Object getValue(SearchHitField obj) {
        Object value = obj.getValue();
        if (value instanceof Map) {
            Map<String, Object> mapValue = (Map<String, Object>) value;
            if (mapValue.containsKey("_list")) {
                return mapValue.get("_list");
            }
        }
        return value;
    }

}
