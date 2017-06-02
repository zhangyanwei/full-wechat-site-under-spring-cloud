package com.askdog.operation.web.api;

import com.askdog.model.data.video.Music;
import com.askdog.service.VideoToolService;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.video.PureMusic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/api/video")
public class VideoToolApi {

    @Autowired
    private VideoToolService videoToolService;

    @PreAuthorize("hasRole('ROLE_OPERATION')")
    @RequestMapping(path = "/musics", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    public PagedData<Music> backgroundMusics(@PageableDefault Pageable pageable) {
        return videoToolService.backgroundMusics(pageable.getPageNumber(), pageable.getPageSize());
    }

    @PreAuthorize("hasRole('ROLE_OPERATION')")
    @RequestMapping(path = "/music", method = POST, produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE)
    public Music addMusic(@RequestBody PureMusic pureMusic) {
        return videoToolService.saveMusic(pureMusic);
    }

    @PreAuthorize("hasRole('ROLE_OPERATION')")
    @RequestMapping(path = "/music", method = DELETE)
    public void deleteMusic(@RequestParam("id") String musicId) {
        videoToolService.deleteMusic(musicId);
    }

}
