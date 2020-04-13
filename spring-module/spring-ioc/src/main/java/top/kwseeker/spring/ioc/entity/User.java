package top.kwseeker.spring.ioc.entity;

import java.util.Set;


public class User {

    private String username;
    private int age;
    private Set<String> roles;
    private Job job;

    public User() {}

    public User(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public void init() {
        System.out.println("测试init-method方法");
    }

    public void close() {
        System.out.println("测试destroy-method方法");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
