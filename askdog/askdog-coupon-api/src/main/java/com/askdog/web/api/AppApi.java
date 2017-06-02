package com.askdog.web.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@RestController
@RequestMapping("/api/app")
public class AppApi {

    @RequestMapping("/status")
    public Map<String, String> status(@RequestParam String version) {
        Map<String, String> map = newHashMap();
        map.put("status", "OK");
        map.put("version", version);
        return map;
    }

}
