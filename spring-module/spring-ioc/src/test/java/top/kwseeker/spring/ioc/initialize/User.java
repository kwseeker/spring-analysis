package top.kwseeker.spring.ioc.initialize;

public class User {

    private String name;
    private int age;

    public User(){
        System.out.println("User Constructor no param");
    }

    public User(String name, int age) {
        System.out.println("User Constructor with param");
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
