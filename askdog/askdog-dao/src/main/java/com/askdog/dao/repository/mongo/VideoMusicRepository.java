package com.askdog.dao.repository.mongo;

import com.askdog.model.data.video.Music;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoMusicRepository extends MongoRepository<Music, String> {
}
