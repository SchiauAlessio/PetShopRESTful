package client.ui.consoles;

import client.services.OrderProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.StreamSupport;

@Component
@ComponentScan("PetShop.client")
public class OrderProductConsole {
    private final OrderProductService orderProductService;

    @Autowired
    public OrderProductConsole(OrderProductService orderProductService) {
        this.orderProductService = orderProductService;
    }

    private void printMenu() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("1. Get all OrderProducts");
        System.out.println("2. Add an OrderProduct");
        System.out.println("3. Remove an OrderProduct");
        System.out.println("4. Exit");
    }

    private String readChoice() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String choice = "0";
        try {
            choice = bufferRead.readLine();
            if (!"1234".contains(choice))
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
                    getOrderProducts();
                    break;
                case "2":
                    addOrderProduct();
                    break;
                case "3":
                    removeOrderProduct();
                    break;
                default:
                    System.out.println("Exiting orderProduct console");
                    running = false;
                    break;
            }
        }
    }

    private void getOrderProducts() {
        orderProductService.getAllOrderProducts().whenComplete((result, exception) -> {
            if (exception == null)
                StreamSupport.stream(result.spliterator(), false).forEach(System.out::println);
            else
                System.out.println(exception.getMessage());
        });
    }

    private void addOrderProduct() {
        try {
            Integer orderId = readId("Order");
            Integer productId = readId("Product");
            orderProductService.addOrderProduct(orderId, productId).whenComplete((result, exception) -> {
                if (exception == null)
                    System.out.println(result);
                else
                    System.out.println(exception.getMessage());
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeOrderProduct() {
        try {
            Integer orderId = readId("Order");
            Integer productId = readId("Product");
            orderProductService.deleteOrderProduct(orderId, productId).whenComplete((result, exception) -> {
                if (exception == null)
                    System.out.println(result);
                else
                    System.out.println(exception.getMessage());
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private Integer readId(String entityName) throws IOException {
        System.out.println(entityName + " id: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        return Integer.parseInt(bufferRead.readLine());
    }

}
