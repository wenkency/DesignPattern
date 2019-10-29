package cn.carhouse.designpattern.factory;

/**
 * 具体的工厂类
 */
public class ConcreteFactory extends Factory {

    @Override
    public <P extends Product> P createProduct(Class<P> clz) {
        Product product = null;
        try {
            product = clz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (P) product;
    }
}
