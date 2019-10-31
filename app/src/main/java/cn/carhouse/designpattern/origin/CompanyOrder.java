package cn.carhouse.designpattern.origin;

/**
 * 私人订单
 */
public class CompanyOrder implements IOrder {
    private int orderNumber;
    private String orderName;

    @Override
    public int getOrderNumber() {
        return orderNumber;
    }

    @Override
    public int produceOrderNumber() {
        return 1000;
    }

    @Override
    public void setOrderNumber(int number) {
        this.orderNumber = number;
    }

    @Override
    public Object cloneOrder() {
        CompanyOrder order = new CompanyOrder();
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
