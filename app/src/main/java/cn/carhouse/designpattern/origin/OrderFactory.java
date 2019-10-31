package cn.carhouse.designpattern.origin;

public class OrderFactory {
    /**
     * 生产订单
     * 1. 按实际情况，生产
     * 2. 私人不超过500一次，公司最多一次生产1000
     */
    public static void productOrder(IOrder order) {
        int orderNumber = order.getOrderNumber();
        int produceOrderNumber = order.produceOrderNumber();
        while (orderNumber > 0) {
            IOrder newOrder = (IOrder) order.cloneOrder();
            newOrder.setOrderNumber(orderNumber >= produceOrderNumber ? produceOrderNumber : orderNumber);
            orderNumber -= produceOrderNumber;
        }
    }

    public void test() {
        PersonalOrder order = new PersonalOrder();
        order.setOrderName("私人订单");
        order.setOrderNumber(1700);
        productOrder(order);

        CompanyOrder companyOrder=new CompanyOrder();
        companyOrder.setOrderName("私人订单");
        companyOrder.setOrderNumber(2500);
        productOrder(companyOrder);
    }
}
