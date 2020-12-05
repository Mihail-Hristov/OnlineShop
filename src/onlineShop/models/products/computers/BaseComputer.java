package onlineShop.models.products.computers;


import onlineShop.common.constants.ExceptionMessages;
import onlineShop.models.products.BaseProduct;
import onlineShop.models.products.Product;
import onlineShop.models.products.components.Component;
import onlineShop.models.products.peripherals.Peripheral;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BaseComputer extends BaseProduct implements Computer {
    private List<Component> components;
    private List<Peripheral> peripherals;

    protected BaseComputer(int id, String manufacturer, String model, double price, double overallPerformance) {
        super(id, manufacturer, model, price, overallPerformance);
        this.components = new ArrayList<>();
        this.peripherals = new ArrayList<>();
    }

    @Override
    public List<Component> getComponents() {
        return this.components;
    }

    @Override
    public List<Peripheral> getPeripherals() {
        return this.peripherals;
    }

    @Override
    public double getOverallPerformance() {
        if (components.isEmpty()) {
            return this.overallPerformance;
        }

        double averageOverallComponentsPerformance = components.stream().mapToDouble(p -> p.getOverallPerformance()).average().orElse(0);

        return overallPerformance + averageOverallComponentsPerformance;
    }

    @Override
    public double getPrice() {
        double componentsPrice = components.stream().mapToDouble(Product::getPrice).sum();
        double peripheralsPrice = peripherals.stream().mapToDouble((Product::getPrice)).sum();

        return componentsPrice + peripheralsPrice + this.price;
    }

    @Override
    public void addComponent(Component component) {
        if (components.contains(component)){
            throw new IllegalArgumentException(String.format(ExceptionMessages.EXISTING_COMPONENT,
                    component.getClass().getSimpleName(),
                    this.getClass().getSimpleName(),
                    this.getId()));
        }

        components.add(component);
    }

    @Override
    public Component removeComponent(String componentType) {
        boolean componentExist = false;
        Component currentComponent = null;

        if (components.isEmpty()) {
            componentExist = false;
        }else {
            for (Component component : components) {
                if (component.getClass().getSimpleName().equals(componentType)) {
                    currentComponent = component;
                    componentExist = components.remove(currentComponent);
                }
                if (componentExist) {
                    break;
                }
            }
        }

        if (!componentExist) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.NOT_EXISTING_COMPONENT,
                    componentType,
                    this.getClass().getSimpleName(),
                    this.getId()
                    ));
        }

        return currentComponent;
    }

    @Override
    public void addPeripheral(Peripheral peripheral) {
        if (peripherals.contains(peripheral)){
            throw new IllegalArgumentException(String.format(ExceptionMessages.EXISTING_PERIPHERAL,
                    peripheral.getClass().getSimpleName(),
                    this.getClass().getSimpleName(),
                    this.getId()));
        }

        peripherals.add(peripheral);
    }

    @Override
    public Peripheral removePeripheral(String peripheralType) {
        boolean peripheralExist = false;
        Peripheral currentPeripheral = null;

        if (peripherals.isEmpty()) {
            peripheralExist = false;
        }else {
            for (Peripheral peripheral : peripherals) {
                if (peripheral.getClass().getSimpleName().equals(peripheralType)) {
                    currentPeripheral = peripheral;
                    peripheralExist = peripherals.remove(currentPeripheral);
                }
                if (peripheralExist) {
                    break;
                }
            }
        }

        if (!peripheralExist) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.NOT_EXISTING_PERIPHERAL,
                    peripheralType,
                    this.getClass().getSimpleName(),
                    this.getId()
            ));
        }

        return currentPeripheral;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("Overall Performance: %.2f. Price: %.2f - %s: %s %s (Id: %d)",
                this.getOverallPerformance(),
                this.getPrice(),
                this.getClass().getSimpleName(),
                manufacturer,
                model,
                id));
        result.append(System.lineSeparator());
        result.append(String.format(" Components (%d):", components.size()));
        result.append(System.lineSeparator());
        for (Component component : components) {
            result.append("  ").append(component.toString());
            result.append(System.lineSeparator());
        }
        result.append(String.format(" Peripherals (%d); Average Overall Performance (%.2f):",
                peripherals.size(),
                peripherals.stream().mapToDouble(p -> p.getOverallPerformance()).average().orElse(0.0)));
        result.append(System.lineSeparator());
        for (Peripheral peripheral : peripherals) {
            result.append("  ").append(peripheral.toString());
            result.append(System.lineSeparator());
        }

        return result.toString().trim();
    }
}
