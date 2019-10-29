package cn.carhouse.designpattern.factory;

/**
 * 具体的产品A
 */
public class ConcreteProductA extends Product {
    @Override
    public void method() {
        System.out.println("我是具体产品A");
    }
}
