package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume r) {
        int index = getIndex(r.getUuid());
        if (index > 0) {
            storage[index] = r;
            System.out.println("The resume is updated");
        } else {
            throw new NotExistStorageException(r.getUuid());
            //System.out.println("ERROR: required resume is not found");
        }
    }

    public void save(Resume r) {
        int index = getIndex(r.getUuid());
        if (index >= 0) {
            throw new ExistStorageException(r.getUuid());
//            System.out.println("ERROR: " +
//                    "cannot save the resume that already exists");
        } else if (size == storage.length) {
            throw new StorageException("Storage overflow", r.getUuid());
           // System.out.println("ERROR: Not enough space for saving");
        } else {
            insertElement(r, index);
            size++;
        }
    }

    protected abstract void insertElement(Resume r, int index);

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
            //System.out.println("ERROR: required resume is not found");
        } else {
            fillDeletedElement(index);
            storage[size - 1] = null;
            size--;
        }

    }

    protected abstract void fillDeletedElement(int index);


    public int size() {
        return size;
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
//            System.out.println("ERROR: required resume is not found");
//            return null;
        }
        return storage[index];
    }

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    protected abstract int getIndex(String uuid);
}
