package com.sf.saas.bps.core.service.event.spring;

import org.springframework.context.ApplicationEvent;

/**
 * @author 01407460
 * @date 2022/9/14 21:05
 */
public class StrategyUpdateEvent extends ApplicationEvent {


    private Long id;


    public StrategyUpdateEvent(Object source,Long id) {
        super(source);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
