package cn.carhouse.designpattern.singleton;

/**
 * 饿汉式
 */
public class IOHungry {
    private static IOHungry instance = new IOHungry();

    private IOHungry() {
    }

    public static IOHungry getInstance() {
        return instance;
    }
}
