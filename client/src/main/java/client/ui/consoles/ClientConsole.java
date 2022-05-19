package client.ui.consoles;

import client.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.StreamSupport;

@Component
@ComponentScan("client")
public class ClientConsole {
    private final ClientService clientService;

    @Autowired
    public ClientConsole(ClientService clientService) {
        this.clientService = clientService;
    }

    private void printMenu() {
        try {
        Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("1. Get all clients");
        System.out.println("2. Add a client");
        System.out.println("3. Update a client");
        System.out.println("4. Remove a client");
        System.out.println("5. Filter clients by name");
        System.out.println("6. Sort clients by name");
        System.out.println("7. Exit");
    }

    private String readChoice() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String choice = "0";
        try {
            choice = bufferRead.readLine();
            if (!"1234567".contains(choice))
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
                    getClients();
                    break;
                case "2":
                    addClient();
                    break;
                case "3":
                    updateClient();
                    break;
                case "4":
                    removeClient();
                    break;
                case "5":
                    filterClients();
                    break;
                case "6":
                    sortClients();
                    break;
                default:
                    System.out.println("Exiting client console");
                    running = false;
                    break;
            }
        }
    }

    private void getClients() {
        System.out.println("Clients:");
        clientService.getAllClients().whenComplete((result, exception) -> {
            if (exception == null)
                StreamSupport.stream(result.spliterator(), false).forEach(System.out::println);
            else
                System.out.println(exception.getMessage());
        });
    }

    private void sortClients() {
        System.out.println("Clients sorted in asc order by name:");
        clientService.getSortedClients().whenComplete((result, exception) -> {
            if (exception == null)
                result.forEach(System.out::println);
            else
                System.out.println(exception.getMessage());
        });
    }

    private void filterClients() {
        try {
            String name = readName();
            System.out.println("Clients which have names containing " + name + " :");
            clientService.getFilteredClients(name).whenComplete((result, exception) -> {
                if (exception == null)
                    result.forEach(System.out::println);
                else
                    System.out.println(exception.getMessage());
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    private void addClient() {
        try {
            String name = readName();
            clientService.addClient(name).whenComplete((result, exception) -> {
                if (exception == null)
                    System.out.println(result);
                else
                    System.out.println(exception.getMessage());
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateClient() {
        try {
            Integer id = readId();
            String name = readName();
            clientService.updateClient(id, name).whenComplete((result, exception) -> {
                if (exception == null)
                    System.out.println(result);
                else
                    System.out.println(exception.getMessage());
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void removeClient() {
        try {
            Integer id = readId();
            clientService.deleteClient(id).whenComplete((result, exception) -> {
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
        System.out.println("Client name: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        return bufferRead.readLine();
    }

    private Integer readId() throws IOException {
        System.out.println("Client id: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        return Integer.parseInt(bufferRead.readLine());
    }
}
