package com.trs.tickets.service;

import org.springframework.stereotype.Component;

@Component
public class PageSizeChecker {

    public Integer checkPage(Integer page){
        if(page == null || page < 0){
            page = 0;
            return page;
        }

        return page;
    }

    public Integer checkSize(Integer size){
        if(size == null || size < 0){
            size = 6;
            return size;
        }

        return size;
    }

}
