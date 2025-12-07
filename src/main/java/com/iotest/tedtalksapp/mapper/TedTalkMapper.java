package com.iotest.tedtalksapp.mapper;

import com.iotest.tedtalksapp.model.TedTalk;
import com.iotest.tedtalksapp.model.TedTalkRequest;
import com.iotest.tedtalksapp.util.GenericUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

@Data
@RequiredArgsConstructor
@Component
public class TedTalkMapper {

    public TedTalk toEntity(TedTalkRequest request) {
        if(request== null) {
            return null;
        }
        TedTalk talk = new TedTalk();
        talk.setViews(request.getViews());
        talk.setLink(request.getLink());
        talk.setTitle(request.getTitle());
        talk.setLikes(request.getLikes());
        talk.setAuthor(request.getAuthor());
        talk.setDate(request.getDate());
        talk.setInfluenceScore(GenericUtil.calcScore(request.getViews(),request.getLikes()));
        return talk;
    }

    public void patchUpdate(TedTalkRequest req, TedTalk existing) {
        if (!ObjectUtils.isEmpty(req)) {
            if (req.getTitle() != null) existing.setTitle(req.getTitle());
            if (req.getAuthor() != null) existing.setAuthor(req.getAuthor());
            if (req.getDate() != null) existing.setDate(req.getDate());
            if (req.getViews() != 0) existing.setViews(req.getViews());
            if (req.getLikes() != 0) existing.setLikes(req.getLikes());
            if (req.getLink() != null) existing.setLink(req.getLink());
            existing.setInfluenceScore(GenericUtil.calcScore(req.getViews(), req.getLikes()));
        }
    }
}
