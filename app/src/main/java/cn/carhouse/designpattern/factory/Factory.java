package cn.carhouse.designpattern.factory;

/**
 * 工厂设计模式：
 * 1. 任何创建复杂对象的地方都可以用工厂设计模式.
 * 2. 直接就可以new出来的就不需要了。
 */
public abstract class Factory {

    /**
     * 创建产品
     *
     * @return
     */
    public abstract <P extends Product> P createProduct(Class<P> clz);
}
