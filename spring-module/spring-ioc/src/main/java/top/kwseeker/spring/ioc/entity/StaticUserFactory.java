package top.kwseeker.spring.ioc.entity;

public class StaticUserFactory {

    public static User createUser() {
        return new User();
    }
}
