package com.askdog.service.search;

import java.util.List;

public interface SearchAgent {
    SearchResult search(SearchRequest searchRequest);
    List<String> searchIds(SearchRequest searchRequest);
}
