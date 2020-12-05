package onlineShop.models.products.peripherals;

import onlineShop.models.products.BaseProduct;

public class BasePeripheral extends BaseProduct implements Peripheral {
    private String connectionType;

    protected BasePeripheral(int id, String manufacturer, String model, double price, double overallPerformance, String connectionType) {
        super(id, manufacturer, model, price, overallPerformance);
        this.connectionType = connectionType;
    }

    @Override
    public String getConnectionType() {
        return this.connectionType;
    }

    @Override
    public String toString() {
        return String.format("Overall Performance: %.2f. Price: %.2f - %s: %s %s (Id: %d) Connection Type: %s",
                overallPerformance,
                price,
                this.getClass().getSimpleName(),
                manufacturer,
                model,
                id,
                connectionType
        );
    }
}
