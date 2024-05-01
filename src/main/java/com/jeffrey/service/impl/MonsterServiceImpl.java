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
        //这里就模拟数据->从DB来
        List<Monster> monsters =
                new ArrayList<>();
        monsters.add(new Monster(100, "牛魔王", "芭蕉扇", 400));
        monsters.add(new Monster(200, "老猫妖怪", "抓老鼠", 200));
        return monsters;
    }

    @Override
    public List<Monster> findMonsterByName(String name) {
        //这里就模拟数据->从DB来
        List<Monster> monsters =
                new ArrayList<>();
        monsters.add(new Monster(100, "牛魔王", "芭蕉扇", 400));
        monsters.add(new Monster(200, "老猫妖怪", "抓老鼠", 200));
        monsters.add(new Monster(300, "大象精", "运木头", 100));
        monsters.add(new Monster(400, "黄袍怪", "吐烟雾", 300));
        monsters.add(new Monster(500, "白骨精", "美人计", 800));

        //创建集合返回查询到的monster集合
        List<Monster> findMonsters = new ArrayList<>();
        //遍历monsters ，返回满足条件
        for (Monster monster :monsters) {
            if (monster.getName().contains(name)){
                findMonsters.add(monster);
            }
        }
        return findMonsters;
    }

    @Override
    public boolean login(String name){
        if ("monster".equals(name)){
            return true;
        }else {
            return false;
        }
    }
}
