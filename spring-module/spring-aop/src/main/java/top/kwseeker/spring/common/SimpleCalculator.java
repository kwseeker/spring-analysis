package top.kwseeker.spring.common;

public class SimpleCalculator implements Calculate {

    public int add(int numA, int numB) {
        System.out.println("执行加法:add");
        return numA + numB;
    }

    public int sub(int numA, int numB) {
        System.out.println("执行减法:reduce");
        return numA - numB;
    }

    public int div(int numA, int numB) {
        System.out.println("执行除法:div");
        return numA / numB;
    }

    public int multi(int numA, int numB) {
        System.out.println("执行乘法:multi");
        return numA * numB;
    }

    public int mod(int numA, int numB) {
        System.out.println("执行求模:mod");
        return numA%numB;
    }

}
