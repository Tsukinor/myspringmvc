package com.jeffrey.service;

import com.jeffrey.entity.Monster;

import java.util.List;

/**
 * @program: myspringmvc
 * @author: Jeffrey
 * @create: 2024-04-25 18:48
 * @description:
 **/
public interface MonsterService {
    //返回monsterList列表
    List<Monster> listMonster();

    //通过传入的name，返回monster列表
    List<Monster> findMonsterByName(String name);

    //处理登录
     boolean login(String name);
}
