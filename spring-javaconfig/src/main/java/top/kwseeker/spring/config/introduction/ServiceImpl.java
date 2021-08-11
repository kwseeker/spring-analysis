package top.kwseeker.spring.config.introduction;

public class ServiceImpl implements Service {

    private Repository repository;

    public ServiceImpl(Repository repository) {
        this.repository = repository;
    }

    public void doSomething() {
        System.out.println("do something ...");
        System.out.println(repository.toString());
    }
}
