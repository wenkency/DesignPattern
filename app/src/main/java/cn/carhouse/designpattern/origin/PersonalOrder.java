package cn.carhouse.designpattern.origin;

/**
 * 私人订单
 */
public class PersonalOrder implements IOrder {
    private int orderNumber;
    private String orderName;

    @Override
    public int getOrderNumber() {
        return orderNumber;
    }

    @Override
    public int produceOrderNumber() {
        return 500;
    }

    @Override
    public void setOrderNumber(int number) {
        this.orderNumber = number;
    }

    @Override
    public Object cloneOrder() {
        PersonalOrder order = new PersonalOrder();
        order.setOrderName(this.orderName);
        order.setOrderNumber(this.orderNumber);
        return order;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }


}
