package client;

import client.ui.MainConsole;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class ClientApp {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("client.config")) {
            MainConsole console = context.getBean(MainConsole.class);
            console.run();
            System.out.println("Exited the app");
        }
    }
}
