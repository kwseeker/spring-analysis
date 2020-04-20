package top.kwseeker.spring.transaction.service;

public interface UserService {

    void createUser(String firstName, String lastName);

    void addMoney(String id, int count);
}
