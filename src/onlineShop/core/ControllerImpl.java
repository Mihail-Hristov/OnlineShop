package onlineShop.core;

import onlineShop.common.constants.ExceptionMessages;
import onlineShop.common.constants.OutputMessages;
import onlineShop.core.interfaces.Controller;
import onlineShop.models.products.components.*;
import onlineShop.models.products.computers.Computer;
import onlineShop.models.products.computers.DesktopComputer;
import onlineShop.models.products.computers.Laptop;
import onlineShop.models.products.peripherals.*;

import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ControllerImpl implements Controller {
    private static final String COMPUTER_PACKAGE_NAME = "onlineShop.models.products.computers.";
    private Map<Integer, Computer> computerMap;
    private Map<Integer, Component> componentMap;
    private Map<Integer, Peripheral> peripheralMap;

    public ControllerImpl() {
        computerMap = new HashMap<>();
        componentMap = new HashMap<>();
        peripheralMap = new HashMap<>();
    }

    @Override
    public String addComputer(String computerType, int id, String manufacturer, String model, double price) throws NoSuchMethodException, ClassNotFoundException {
        Computer computer = null;

        if (computerMap.containsKey(id)) {
            throw new IllegalArgumentException(ExceptionMessages.EXISTING_COMPUTER_ID);
        }

        switch (computerType) {
            case "DesktopComputer":
                computer = new DesktopComputer(id, manufacturer, model, price);
                break;
            case "Laptop":
                computer = new Laptop(id, manufacturer, model, price);
                break;
            default:
                throw new IllegalArgumentException(ExceptionMessages.INVALID_COMPUTER_TYPE);
        }

        computerMap.put(id, computer);

        return String.format(OutputMessages.ADDED_COMPUTER, id);
    }

    @Override
    public String addPeripheral(int computerId, int id, String peripheralType, String manufacturer, String model, double price, double overallPerformance, String connectionType) {
        if (!computerMap.containsKey(computerId)) {
            throw new IllegalArgumentException(ExceptionMessages.NOT_EXISTING_COMPUTER_ID);
        }

        if (peripheralMap.containsKey(id)) {
            throw new IllegalArgumentException(ExceptionMessages.EXISTING_PERIPHERAL_ID);
        }

        Peripheral peripheral = null;

        switch (peripheralType) {
            case "Headset":
                peripheral = new Headset(id, manufacturer, model, price, overallPerformance, connectionType);
                break;
            case "Keyboard":
                peripheral = new Keyboard(id, manufacturer, model, price, overallPerformance, connectionType);
                break;
            case "Monitor":
                peripheral = new Monitor(id, manufacturer, model, price, overallPerformance, connectionType);
                break;
            case "Mouse":
                peripheral = new Mouse(id, manufacturer, model, price, overallPerformance, connectionType);
                break;
            default:
                throw new IllegalArgumentException(ExceptionMessages.INVALID_PERIPHERAL_TYPE);
        }

        computerMap.get(computerId).addPeripheral(peripheral);
        peripheralMap.put(id, peripheral);

        return String.format(OutputMessages.ADDED_PERIPHERAL, peripheralType, id,computerId);
    }

    @Override
    public String removePeripheral(String peripheralType, int computerId) {
        if (!computerMap.containsKey(computerId)) {
            throw new IllegalArgumentException(ExceptionMessages.NOT_EXISTING_COMPUTER_ID);
        }

        Peripheral removedPeripheral = computerMap.get(computerId).removePeripheral(peripheralType);
        peripheralMap.remove(removedPeripheral.getId());

        return String.format(OutputMessages.REMOVED_PERIPHERAL,peripheralType, removedPeripheral.getId());
    }

    @Override
    public String addComponent(int computerId, int id, String componentType, String manufacturer, String model, double price, double overallPerformance, int generation) {
        if (!computerMap.containsKey(computerId)) {
            throw new IllegalArgumentException(ExceptionMessages.NOT_EXISTING_COMPUTER_ID);
        }

        if (componentMap.containsKey(id)) {
            throw new IllegalArgumentException(ExceptionMessages.EXISTING_COMPONENT_ID);
        }

        Component component = null;

        switch (componentType) {
            case "CentralProcessingUnit":
                component = new CentralProcessingUnit(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "Motherboard":
                component = new Motherboard(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "PowerSupply":
                component = new PowerSupply(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "RandomAccessMemory":
                component = new RandomAccessMemory(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "SolidStateDrive":
                component = new SolidStateDrive(id, manufacturer, model, price, overallPerformance, generation);
                break;
            case "VideoCard":
                component = new VideoCard(id, manufacturer, model, price, overallPerformance, generation);
                break;
            default:
                throw new IllegalArgumentException(ExceptionMessages.INVALID_COMPONENT_TYPE);
        }

        computerMap.get(computerId).addComponent(component);
        componentMap.put(id, component);

        return String.format(OutputMessages.ADDED_COMPONENT, componentType,id, computerId);
    }

    @Override
    public String removeComponent(String componentType, int computerId) {
        if (!computerMap.containsKey(computerId)) {
            throw new IllegalArgumentException(ExceptionMessages.NOT_EXISTING_COMPUTER_ID);
        }

        Component removedComponent = computerMap.get(computerId).removeComponent(componentType);
        componentMap.remove(removedComponent.getId());

        return String.format(OutputMessages.REMOVED_COMPONENT, componentType, removedComponent.getId());
    }

    @Override
    public String buyComputer(int id) {
        if (!computerMap.containsKey(id)) {
            throw new IllegalArgumentException(ExceptionMessages.NOT_EXISTING_COMPUTER_ID);
        }

        Computer removedComputer = computerMap.remove(id);

        return removedComputer.toString();
    }

    @Override
    public String BuyBestComputer(double budget) {
        List<Computer> filteredComputers = computerMap.values().stream()
                .filter(c -> c.getPrice() <= budget)
                .sorted(Comparator.comparing(Computer::getOverallPerformance).reversed())
                .collect(Collectors.toList());

        if (filteredComputers.isEmpty()) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.CAN_NOT_BUY_COMPUTER, budget));
        }

        Computer bestComputer = filteredComputers.get(0);

        computerMap.remove(bestComputer.getId());

        return bestComputer.toString();
    }

    @Override
    public String getComputerData(int id) {
        if (!computerMap.containsKey(id)) {
            throw new IllegalArgumentException(ExceptionMessages.NOT_EXISTING_COMPUTER_ID);
        }

        Computer computer = computerMap.get(id);
        return computer.toString();
    }
}
