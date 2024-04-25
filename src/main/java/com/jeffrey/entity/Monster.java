package com.jeffrey.entity;

/**
 * @program: myspringmvc
 * @author: Jeffrey
 * @create: 2024-04-25 22:40
 * @description:
 **/
public class Monster {
    private Integer id;
    private String name;
    private String kill;
    private Integer age;

    public Monster(Integer id, String name, String kill, Integer age) {
        this.id = id;
        this.name = name;
        this.kill = kill;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKill() {
        return kill;
    }

    public void setKill(String kill) {
        this.kill = kill;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Monster{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", kill='" + kill + '\'' +
                ", age=" + age +
                '}';
    }
}
