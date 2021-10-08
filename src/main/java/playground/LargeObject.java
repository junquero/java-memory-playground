package playground;

public class LargeObject {

    String name;
    byte[] blob;

    protected LargeObject() {
        // To hide default constructor
    }

    LargeObject(String name, int mb) {
        this.name = name;
        blob = new byte[mb * 1024 * 1024];
    }

    @Override
    protected void finalize() {
        System.out.printf("Finalizing %s size %d MB%n", name, blob.length / 1024 / 1024);
    }
}
