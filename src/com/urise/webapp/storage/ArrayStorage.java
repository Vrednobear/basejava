package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    private int size = 0;

    public void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
            size = 0;
        }
    }

    public void update(Resume r) {
        for (int i = 0; i < size; i++) {
            if (r.getUuid().equals(storage[i].getUuid())) {
                storage[i] = r;
                System.out.println("The resume is updated");
                return;
            }
        }
        System.out.println("ERROR: required resume is not found");
    }

    public void save(Resume r) {
        if(size == storage.length){
            System.out.println("ERROR: Not enough space for saving");
        }
        for (int i = 0; i < size; i++) {
            if (r.getUuid().equals(storage[i].getUuid())) {
                System.out.println("ERROR: " +
                        "cannot save the resume that already exists");
                return;
            }
        }
        storage[size] = r;
        size++;
    }

    public Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return storage[i];
            }
        }
        System.out.println("ERROR: required resume is not found");
        return null;
    }

    public void delete(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                storage[i] = storage[size - 1];
                storage[size - 1] = null;
                size--;
                return;
            }
        }
        System.out.println("ERROR: required resume is not found");
    }


    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        // return Arrays.copyOfRange(storage, 0, size);
        Resume[] resumesNew = new Resume[size];
        for (int i = 0; i < size; i++) {
            resumesNew[i] = storage[i];
        }
        return resumesNew;
    }

    public int size() {
        return size;
    }

    public boolean isInStorage(Resume r) {
        for (int i = 0; i < size; i++) {
            if (r.getUuid().equals(storage[i].getUuid())) {
                return true;
            }
        }
        return false;
    }
}

//  for (int j = i; j < size - 1; j++) {
//                    storage[j] = storage[j + 1];
//                }
//                storage[size - 1] = null;
//                size--;
//            }
