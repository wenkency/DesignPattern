package cn.carhouse.designpattern.singleton;

/**
 * 懒汉式
 */
public class IOLazy {
    private volatile static IOLazy instance;

    private IOLazy() {
    }

    public static IOLazy getInstance() {
        if (instance == null) {
            synchronized (IOLazy.class) {
                if (instance == null) {
                    instance = new IOLazy();
                }
            }
        }
        return instance;
    }
}
