package com.jl.openapi.game.service;

import com.jl.openapi.exception.ThirdGameException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ThirdGameFactory  implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private List<IThirdGameService> thirdGameList;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    private void init() {
        Map<String, IThirdGameService> matchBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(
                applicationContext, IThirdGameService.class, true, false);
        this.thirdGameList = new ArrayList<>(matchBeans.values());
    }

    public IThirdGameService get(String code) {
        List<IThirdGameService> list = thirdGameList.stream().filter(e -> e.match(code)).collect(Collectors.toList());
        if(list.isEmpty()){
            throw new ThirdGameException("no match IThirdGameService impl with code [" + code + "]");
        }
        if(list.size() > 1){
            throw new ThirdGameException("there are one more IThirdGameService impl with code [" + code + "]");
        }
        return list.get(0);
    }

}
