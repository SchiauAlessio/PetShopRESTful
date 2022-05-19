package client.ui.consoles;

import client.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.StreamSupport;


@Component
@ComponentScan("PetShop.client")
public class ProductConsole {
    private final ProductService productService;

    @Autowired
    public ProductConsole(ProductService productService) {
        this.productService = productService;
    }

    private void printMenu() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("1. Get all products");
        System.out.println("2. Get products by order");
        System.out.println("3. Add a product");
        System.out.println("4. Update a product");
        System.out.println("5. Remove a product");
        System.out.println("6. Filter products");
        System.out.println("7. Sort products");
        System.out.println("8. Exit");
    }

    private String readChoice() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String choice = "0";
        try {
            choice = bufferRead.readLine();
            if (!"12345678".contains(choice))
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
                    getProducts();
                    break;
                case "2":
                    getProductsByOrder();
                    break;
                case "3":
                    addProduct();
                    break;
                case "4":
                    updateProduct();
                    break;
                case "5":
                    removeProduct();
                    break;
                case "6":
                    filterProducts();
                    break;
                case "7":
                    sortProducts();
                    break;
                default:
                    System.out.println("Exiting product console");
                    running = false;
                    break;
            }
        }
    }

    private void getProducts() {
        productService.getAllProducts().whenComplete((result, exception) -> {
            if (exception == null)
                StreamSupport.stream(result.spliterator(), false).forEach(System.out::println);
            else
                System.out.println(exception.getMessage());
        });
    }

    private void sortProducts() {
        System.out.println("Products sorted in desc order by price:");
        productService.getSortedProducts().whenComplete((result, exception) -> {
            if (exception == null)
                result.forEach(System.out::println);
            else
                System.out.println(exception.getMessage());
        });
    }

    private void filterProducts() {
        try {
            double price = readPrice();
            System.out.println("Products which have their price <= " + price + " :");
            productService.getFilteredProducts(price).whenComplete((result, exception) -> {
                if (exception == null)
                    StreamSupport.stream(result.spliterator(), false).forEach(System.out::println);
                else
                    System.out.println(exception.getMessage());
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getProductsByOrder() {
        try {
            Integer orderId = readId("Order");
            productService.getProductByOrder(orderId).whenComplete((result, exception) -> {
                if (exception == null)
                    StreamSupport.stream(result.spliterator(), false).forEach(System.out::println);
                else
                    System.out.println(exception.getMessage());
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addProduct() {
        try {
            String name = readName();
            double price = readPrice();
            productService.addProduct(name, price).whenComplete((result, exception) -> {
                if (exception == null)
                    System.out.println(result);
                else
                    System.out.println(exception.getMessage());
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateProduct() {
        try {
            Integer id = readId("Product");
            String name = readName();
            double price = readPrice();
            productService.updateProduct(id, name, price).whenComplete((result, exception) -> {
                if (exception == null)
                    System.out.println(result);
                else
                    System.out.println(exception.getMessage());
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeProduct() {
        try {
            Integer id = readId("Product");
            productService.deleteProduct(id).whenComplete((result, exception) -> {
                if (exception == null)
                    System.out.println(result);
                else
                    System.out.println(exception.getMessage());
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String readName() throws IOException {
        System.out.println("Product name: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        return bufferRead.readLine();
    }

    private double readPrice() throws IOException {
        System.out.println("Product price: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        return Double.parseDouble(bufferRead.readLine());
    }

    private Integer readId(String entityName) throws IOException {
        System.out.println(entityName + " id: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        return Integer.parseInt(bufferRead.readLine());
    }

}
