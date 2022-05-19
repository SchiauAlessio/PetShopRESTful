package client.ui.consoles;

import client.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.StreamSupport;

@Component
@ComponentScan("PetShop.client")
public class OrderConsole {
    private final OrderService orderService;

    @Autowired
    public OrderConsole(OrderService orderService) {
        this.orderService = orderService;
    }

    private void printMenu() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("1. Get all orders");
        System.out.println("2. Get all orders by client");
        System.out.println("3. Get all orders by product");
        System.out.println("4. Add an order");
        System.out.println("5. Update an order");
        System.out.println("6. Remove an order");
        System.out.println("7. Filter orders");
        System.out.println("8. Sort orders");
        System.out.println("9. Exit");
    }

    private String readChoice() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String choice = "0";
        try {
            choice = bufferRead.readLine();
            if (!"123456789".contains(choice))
                choice = "0";
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return choice;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String option = readChoice();
            switch (option) {
                case "1":
                    getOrders();
                    break;
                case "2":
                    getOrdersByClient();
                    break;
                case "3":
                    getOrdersByProduct();
                    break;
                case "4":
                    addOrder();
                    break;
                case "5":
                    updateOrder();
                    break;
                case "6":
                    removeOrder();
                    break;
                case "7":
                    filterOrders();
                    break;
                case "8":
                    sortOrders();
                    break;
                default:
                    System.out.println("Exiting order console");
                    running = false;
                    break;
            }
        }
    }

    private void getOrders() {
        orderService.getAllOrders().whenComplete((result, exception) -> {
            if (exception == null)
                StreamSupport.stream(result.spliterator(), false).forEach(System.out::println);
            else
                System.out.println(exception.getMessage());
        });
    }

    private void sortOrders() {
        System.out.println("Orders sorted in asc order by details:");
        orderService.getSortedOrders().whenComplete((result, exception) -> {
            if (exception == null)
                result.forEach(System.out::println);
            else
                System.out.println(exception.getMessage());
        });
    }

    private void filterOrders() {
        try {
            String details = readDetails();
            System.out.println("Orders which have details containing " + details + " :");
            orderService.getFilteredOrders(details).whenComplete((result, exception) -> {
                if (exception == null)
                    StreamSupport.stream(result.spliterator(), false).forEach(System.out::println);
                else
                    System.out.println(exception.getMessage());
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getOrdersByClient() {
        try {
            Integer clientId = readId("Client");
            orderService.getOrdersByClient(clientId).whenComplete((result, exception) -> {
                if (exception == null)
                    StreamSupport.stream(result.spliterator(), false).forEach(System.out::println);
                else
                    System.out.println(exception.getMessage());
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getOrdersByProduct() {
        try {
            Integer productId = readId("Product");
            orderService.getOrdersByProduct(productId).whenComplete((result, exception) -> {
                if (exception == null)
                    StreamSupport.stream(result.spliterator(), false).forEach(System.out::println);
                else
                    System.out.println(exception.getMessage());
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addOrder() {
        try {
            String details = readDetails();
            Integer clientId = readId("Client");
            orderService.addOrder(details, clientId).whenComplete((result, exception) -> {
                if (exception == null)
                    System.out.println(result);
                else
                    System.out.println(exception.getMessage());
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateOrder() {
        try {
            Integer id = readId("Order");
            String details = readDetails();
            Integer clientId = readId("Client");
            orderService.updateOrder(id, details, clientId).whenComplete((result, exception) -> {
                if (exception == null)
                    System.out.println(result);
                else
                    System.out.println(exception.getMessage());
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeOrder() {
        try {
            Integer id = readId("Order");
            orderService.deleteOrder(id).whenComplete((result, exception) -> {
                if (exception == null)
                    System.out.println(result);
                else
                    System.out.println(exception.getMessage());
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String readDetails() throws IOException {
        System.out.println("Order details: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        return bufferRead.readLine();
    }

    private Integer readId(String entityName) throws IOException {
        System.out.println(entityName + " id: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        return Integer.parseInt(bufferRead.readLine());
    }

}
