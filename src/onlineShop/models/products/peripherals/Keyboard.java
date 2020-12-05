package onlineShop.models.products.peripherals;

import onlineShop.models.products.BaseProduct;

public class Keyboard extends BasePeripheral {


    public Keyboard(int id, String manufacturer, String model, double price, double overallPerformance, String connectionType) {
        super(id, manufacturer, model, price, overallPerformance, connectionType);
    }
}
