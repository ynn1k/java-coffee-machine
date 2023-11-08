package machine;

import static machine.Variant.*;

public class Machine {
    public enum MachineState {
        OFF, MAIN, BUYING, FILLING_WATER, FILLING_MILK, FILLING_COFFEE, FILLING_CUPS
    }

    private MachineState state;
    private int water;
    private int milk;
    private int coffee;
    private int cups;
    private int money;

    public Machine(int water, int milk, int coffee, int cups, int money) {
        this.water = water;
        this.milk = milk;
        this.coffee = coffee;
        this.cups = cups;
        this.money = money;

        setMainState();
    }

    public void start(String input) {
        switch (state) {
            case MAIN:
                setState(input);
                break;
            case BUYING:
                buy(input);
                setMainState();
                break;
            case FILLING_WATER:
                System.out.println("Write how many ml of milk do you want to add:");
                water += Integer.parseInt(input);
                state = MachineState.FILLING_MILK;
                break;
            case FILLING_MILK:
                System.out.println("Write how many grams of coffee beans do you want to add:");
                milk += Integer.parseInt(input);
                state = MachineState.FILLING_COFFEE;
                break;
            case FILLING_COFFEE:
                System.out.println("Write how many disposable cups of coffee do you want to add:");
                coffee += Integer.parseInt(input);
                state = MachineState.FILLING_CUPS;
                break;
            case FILLING_CUPS:
                cups += Integer.parseInt(input);
                setMainState();
                break;
            default:
                break;
        }
    }

    public boolean power() {
        return state != MachineState.OFF;
    }

    public void setState(String action) {
        switch (action) {
            case "buy":
                System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
                state = MachineState.BUYING;
                break;
            case "fill":
                System.out.println("Write how many ml of water do you want to add:");
                state = MachineState.FILLING_WATER;
                break;
            case "take":
                takeMoney();
                setMainState();
                break;
            case "exit":
                state = MachineState.OFF;
                break;
            case "remaining":
                status();
                setMainState();
                break;
            default:
                System.out.println("Unexpected action.");
                setMainState();
                break;
        }
    }

    public void buy(String input) {
        Variant type;

        switch (input) {
            case "back":
                state = MachineState.MAIN;
                return;
            case "1":
                type = ESPRESSO;
                break;
            case "2":
                type = LATTE;
                break;
            case "3":
                type = CAPPUCCINO;
                break;
            default:
                System.out.println("unexpected input");
                return;
        }

        makeCoffee(type);
    }

    public void makeCoffee(Variant recipe) {
        if (water < recipe.getWater()) {
            System.out.println("Sorry, not enough water!");
            return;
        }

        if (milk < recipe.getMilk()) {
            System.out.println("Sorry, not enough milk!");
            return;
        }

        if (coffee < recipe.getCoffee()) {
            System.out.println("Sorry, not enough coffee bean!");
            return;
        }

        if (cups < 1) {
            System.out.println("Sorry, not enough disposable cups!");
            return;
        }

        water -= recipe.getWater();
        milk -= recipe.getMilk();
        coffee -= recipe.getCoffee();
        cups--;
        money += recipe.getPrice();

        System.out.println("I have enough resources, making you a coffee!");
    }

    public void takeMoney() {
        System.out.println("I gave you $" + money);
        money = 0;
    }

    public void status() {
        System.out.println("The coffee machine has:");
        System.out.println(water + " ml of water");
        System.out.println(milk + " ml of milk");
        System.out.println(coffee + " g of coffee beans");
        System.out.println(cups + " disposable cups");
        System.out.println("$" + money + " of money");
    }

    public void setMainState() {
        System.out.println("Write action (buy, fill, take, remaining, exit):");
        state = MachineState.MAIN;
    }
}
