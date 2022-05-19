package client.ui;

import client.ui.consoles.ClientConsole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import client.ui.consoles.OrderConsole;
import client.ui.consoles.OrderProductConsole;
import client.ui.consoles.ProductConsole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class MainConsole {
    private final ClientConsole clientConsole;
    private final OrderConsole orderConsole;
    private final ProductConsole productConsole;
    private final OrderProductConsole orderProductConsole;

    @Autowired
    public MainConsole(ClientConsole clientConsole, OrderConsole orderConsole, ProductConsole productConsole, OrderProductConsole orderProductConsole) {
        this.clientConsole = clientConsole;
        this.orderConsole = orderConsole;
        this.productConsole = productConsole;
        this.orderProductConsole = orderProductConsole;
    }

    private void printMenu() {
        System.out.println("1. Run Client console");
        System.out.println("2. Run Order console");
        System.out.println("3. Run Product console");
        System.out.println("4. Run OrderProduct console");
        System.out.println("5. Exit");
    }

    private String readChoice() {
        var bufferRead = new BufferedReader(new InputStreamReader(System.in));
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
                    clientConsole.run();
                    break;
                case "2":
                    orderConsole.run();
                    break;
                case "3":
                    productConsole.run();
                    break;
                case "4":
                    orderProductConsole.run();
                    break;
                default:
                    System.out.println("Exiting main console");
                    running = false;
                    break;
            }
        }
    }
}
