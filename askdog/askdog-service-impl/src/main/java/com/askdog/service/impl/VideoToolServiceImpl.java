package com.askdog.service.impl;

import com.askdog.dao.repository.mongo.VideoMusicRepository;
import com.askdog.model.data.video.Music;
import com.askdog.service.VideoToolService;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.video.PureMusic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.askdog.service.bo.common.PagedDataUtils.rePage;

@RestController
public class VideoToolServiceImpl implements VideoToolService {

    @Autowired
    private VideoMusicRepository videoMusicRepository;

    @Override
    public PagedData<Music> backgroundMusics(@RequestParam("page") int page, @RequestParam("size") int size) {
        PageRequest request = new PageRequest(page, size, new Sort(new Sort.Order(Sort.Direction.DESC, "addTime")));
        return rePage(
                videoMusicRepository.findAll(request),
                request, music -> music
        );
    }

    @Override
    public Music saveMusic(@RequestBody PureMusic pureMusic) {
        com.askdog.model.data.video.Music music = new com.askdog.model.data.video.Music();
        music.setName(pureMusic.getName());
        music.setUrl(pureMusic.getUrl());
        music.setDuration(pureMusic.getDuration());
        music.setAddTime(new Date());
        return videoMusicRepository.save(music);
    }

    @Override
    public void deleteMusic(@RequestParam("id") String musicId) {
        videoMusicRepository.delete(musicId);
    }
}
