package playground;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LargeObjectsManager {

    Map<String, List<LargeObject>> mapOfObjects = new HashMap<>();

    public void alloc(String prefix, int n, int mb) {
        List<LargeObject> newList = new ArrayList<>(n);
        for (int i = 1; i <= n; i++) {
            newList.add(new LargeObject(prefix + "-" + i, mb));
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        List<LargeObject> list = mapOfObjects.get(prefix);
        if (isNull(list)) {
            System.out.printf("Allocated %d objects of %d MB in new list %s%n", n, mb, prefix);
            mapOfObjects.put(prefix, newList);
        } else {
            System.out.printf("Allocated %d objects of %d MB and added to existing list %s%n", n, mb, prefix);
            list.addAll(newList);
        }
    }

    public void free(String prefix) {
        List<LargeObject> list = mapOfObjects.remove(prefix);
        if (nonNull(list)) {
            System.out.printf("Freed %s with %d large objects%n", prefix, list.size());
        }
    }

    public void show(String prefix) {
        System.out.printf("%s has %d large objects%n", prefix, mapOfObjects.get(prefix).size());
    }

    public void showAll() {
        mapOfObjects.forEach((x, y) -> System.out.printf("%s has %d large objects%n", x, y.size()));
    }

}
