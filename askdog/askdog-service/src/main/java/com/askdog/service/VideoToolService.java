package com.askdog.service;

import com.askdog.model.data.video.Music;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.video.PureMusic;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@FeignClient("service")
@RequestMapping("/service/video/tool")
public interface VideoToolService {

    @RequestMapping(value = "/musics", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    PagedData<Music> backgroundMusics(@RequestParam("page") int page, @RequestParam("size") int size);

    @RequestMapping(value = "/music", method = POST, produces = APPLICATION_JSON_UTF8_VALUE)
    Music saveMusic(@RequestBody PureMusic pureMusic);

    @RequestMapping(value = "/music", method = DELETE)
    void deleteMusic(@RequestParam("id") String musicId);
}
