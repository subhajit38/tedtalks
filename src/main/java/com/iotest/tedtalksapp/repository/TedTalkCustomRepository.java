package com.iotest.tedtalksapp.repository;

import com.iotest.tedtalksapp.model.TedTalk;
import com.iotest.tedtalksapp.model.TedTalkFilterRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
@RequiredArgsConstructor
public class TedTalkCustomRepository {

    private final R2dbcEntityTemplate template;

    public Flux<TedTalk> findByFilters(TedTalkFilterRequest req) {
        String date = null;
        String author = req.getAuthor();
        String title = req.getTitle();
        if( !ObjectUtils.isEmpty(req) && !ObjectUtils.isEmpty(req.getDate())) {
            date = req.getDate().toString();
        }



        Criteria criteria = Criteria.empty();

        if (author != null && !author.isBlank()) {
            criteria = criteria.and("author").is(author);
        }
        if (title != null && !title.isBlank()) {
            criteria = criteria.and("title").is(title);
        }
        if (date != null) {
            criteria = criteria.and("date").is(date);
        }

        if ((criteria.isEmpty()) ){
            return Flux.error(new RuntimeException("At least one filter must be provided."));
        }

        Query query = Query.query(criteria);

        return template.select(query, TedTalk.class);
    }
}
