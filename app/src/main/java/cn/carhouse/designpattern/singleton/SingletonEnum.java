package cn.carhouse.designpattern.singleton;

/**
 * 枚举单例
 */
public class SingletonEnum {
    private SingletonEnum() {
    }

    enum SingletonEnumHolder {
        INSTANCE;
        private SingletonEnum singletonEnum;

        SingletonEnumHolder() {
            singletonEnum = new SingletonEnum();
        }

        public SingletonEnum getSingletonEnum() {
            return singletonEnum;
        }
    }

    public static SingletonEnum getInstance() {
        return SingletonEnumHolder.INSTANCE.getSingletonEnum();
    }
}
