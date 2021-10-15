package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage implements Storage {
    private static  final  int STORAGE_LIMIT = 10000;
    private Resume[] storage = new Resume[STORAGE_LIMIT];
    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index != -1) {
            storage[index] = r;
            System.out.println("The resume is updated");
        } else {
            System.out.println("ERROR: required resume is not found");
        }
    }

    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        if (index != -1) {
            System.out.println("ERROR: " +
                    "cannot save the resume that already exists");
        } else if (size == storage.length) {
            System.out.println("ERROR: Not enough space for saving");
        } else {
            storage[size] = r;
            size++;
        }
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index == -1) {
            System.out.println("ERROR: required resume is not found");
            return null;
        }
        return storage[index];
    }


    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index == -1) {
            System.out.println("ERROR: required resume is not found");
        } else {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        }
    }


    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    public int size() {
        return size;
    }

    private int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }
}



