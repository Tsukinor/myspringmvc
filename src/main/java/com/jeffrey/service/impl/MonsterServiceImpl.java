package com.jeffrey.service.impl;

import com.jeffrey.entity.Monster;
import com.jeffrey.service.MonsterService;
import com.jeffrey.springmvc.annotation.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: myspringmvc
 * @author: Jeffrey
 * @create: 2024-04-25 18:48
 * @description:
 *
 **/
@Service
public class MonsterServiceImpl implements MonsterService {
    @Override
    public List<Monster> listMonster() {
        //这里老师就模拟数据->DB
        List<Monster> monsters =
                new ArrayList<>();
        monsters.add(new Monster(100, "牛魔王", "芭蕉扇", 400));
        monsters.add(new Monster(200, "老猫妖怪", "抓老鼠", 200));
        return monsters;
    }
}
