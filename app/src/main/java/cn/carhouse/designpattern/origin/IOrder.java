package cn.carhouse.designpattern.origin;

/**
 * 订单抽象
 */
public interface IOrder {
    /**
     * 订单数量
     */
    int getOrderNumber();

    /**
     * 订单一次生产数量
     */
    int produceOrderNumber();


    void setOrderNumber(int number);


    Object cloneOrder();

}
