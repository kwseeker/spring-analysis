package top.kwseeker.java.proxy;

public class SomeService implements ISomeService {

    @Override
    public void doService() {
        System.out.println("service 1 ...");
    }

    @Override
    public void doService2() {
        System.out.println("service 2 ...");
    }

    @Override
    public User getUserInfo() {
        return new User("Arvin", 18);
    }
}
