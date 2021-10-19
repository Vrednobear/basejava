package com.urise.webapp;

import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.ArrayStorage;
import com.urise.webapp.storage.Storage;

/**
 * Test for your com.urise.webapp.storage.ArrayStorage implementation
 */
public class MainTestArrayStorage {
    private static final Storage ARRAY_STORAGE = new ArrayStorage();

    public static void main(String[] args) {
        Resume r1 = new Resume();
        r1.setUuid("uuid1");
        Resume r9 = new Resume();
        r9.setUuid("uuid9");
        Resume r6 = new Resume();
        r6.setUuid("uuid6");
        Resume r3 = new Resume();
        r3.setUuid("uuid3");
        Resume r8 = new Resume();
        r8.setUuid("uuid8");


        ARRAY_STORAGE.save(r1);
        ARRAY_STORAGE.save(r9);
        ARRAY_STORAGE.save(r6);
        ARRAY_STORAGE.save(r3);
        ARRAY_STORAGE.save(r8);

        ARRAY_STORAGE.get("uuid6");

        //System.out.println(Arrays.binarySearch(ARRAY_STORAGE.storage   ,0,ARRAY_STORAGE.size(),r2));

        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + ARRAY_STORAGE.size());
        System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));

        printAll();

// Update
        Resume newResume = new Resume();
        newResume.setUuid("uuid1");
        System.out.println(ARRAY_STORAGE.get("uuid1").hashCode());
        ARRAY_STORAGE.update(newResume);
        System.out.println(ARRAY_STORAGE.get("uuid1").hashCode());

        ARRAY_STORAGE.delete(r1.getUuid());
        printAll();
        ARRAY_STORAGE.clear();
        printAll();


        System.out.println("Size: " + ARRAY_STORAGE.size());
    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : ARRAY_STORAGE.getAll()) {
            System.out.println(r);
        }
        System.out.println("After");
    }
}
