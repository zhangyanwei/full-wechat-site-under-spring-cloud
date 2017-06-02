package com.askdog.web.api;

import com.askdog.model.data.video.Music;
import com.askdog.service.VideoToolService;
import com.askdog.service.bo.common.PagedData;
import com.askdog.web.configuration.userdetails.AdUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/video")
public class VideoToolApi {

    @Autowired
    private VideoToolService videoToolService;

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/musics", method = GET, produces = APPLICATION_JSON_UTF8_VALUE)
    public PagedData<Music> backgroundMusics(@AuthenticationPrincipal AdUserDetails user,
                                             @PageableDefault Pageable pageable) {
        return videoToolService.backgroundMusics(pageable.getPageNumber(), pageable.getPageSize());
    }

}
