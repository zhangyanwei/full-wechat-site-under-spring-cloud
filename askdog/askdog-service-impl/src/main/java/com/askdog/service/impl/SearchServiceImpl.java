package com.askdog.service.impl;

import com.askdog.service.SearchService;
import com.askdog.service.search.SearchAgent;
import com.askdog.service.search.SearchRequest;
import com.askdog.service.search.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;

@RestController
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired private SearchAgent searchAgent;

    private final static Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Override
    public SearchResult similar(@RequestBody @Nonnull SearchRequest searchRequest) {
        return searchAgent.search(searchRequest);
    }
}
