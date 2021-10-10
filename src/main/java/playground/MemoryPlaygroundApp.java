package playground;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MemoryPlaygroundApp {

    LargeObjectsManager objManager = new LargeObjectsManager();

    public String readLine() throws IOException {
        // Enter data using BufferReader
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Reading data using readLine
        return reader.readLine();
    }

    public String inputCommand() throws IOException {
        System.out.printf("Commands:%n");
        System.out.printf("  test%n");
        System.out.printf("  alloc <prefix> <number> <size>%n");
        System.out.printf("  free <prefix>%n");
        System.out.printf("  show [<prefix>]%n");
        System.out.println();
        System.out.printf("  Enter command: ");

        String commandLine = readLine();
        return commandLine;
    }

    public void processCommand(String commandLine) throws InterruptedException {

        String[] args = commandLine.split(" ");
        if (args.length >= 1) {
            String command = args[0];

            if (command.equalsIgnoreCase("test")) {
                objManager.alloc("400m", 1, 400);
                Thread.sleep(2000);
                objManager.free("400m");
                objManager.alloc("40x10m", 400, 1);
                objManager.free("40x10m");
            } else if (command.equalsIgnoreCase("alloc")) {
                if (args.length >= 4) {
                    String prefix = args[1];
                    int number = Integer.parseInt(args[2]);
                    int size = Integer.parseInt(args[3]);
                    objManager.alloc(prefix, number, size);
                } else {
                    printWrongNumberOfArguments();
                }
            } else if (command.equalsIgnoreCase("free")) {
                if (args.length >= 2) {
                    String prefix = args[1];
                    objManager.free(prefix);
                } else {
                    printWrongNumberOfArguments();
                }
            } else if (command.equalsIgnoreCase("show")) {
                if (args.length >= 2) {
                    String prefix = args[1];
                    objManager.show(prefix);
                } else {
                    objManager.showAll();
                }
            } else if (command.equalsIgnoreCase("exit")) {
                System.exit(0);
            }
        }
    }

    private void printWrongNumberOfArguments() {
        System.out.printf("Wrong number of arguments%n");
    }

    final Runnable allocAndFreeInBackground = () -> {
        while (true) {
            objManager.alloc("background-1m", 1, 1);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            objManager.free("background-1m");
        }
    };

    public void run() throws IOException, InterruptedException {

        new Thread(allocAndFreeInBackground, "Background alloc/free").start();

        while (true) {
            String commandLine = inputCommand();
            processCommand(commandLine);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        MemoryPlaygroundApp app = new MemoryPlaygroundApp();
        app.run();
    }
}
