package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public abstract class  AbstractArrayStorage implements Storage{
    protected static  final  int STORAGE_LIMIT = 10000;

    //protected
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public int size() {
        return size;
    }

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index == -1) {
            System.out.println("ERROR: required resume is not found");
            return null;
        }
        return storage[index];
    }

    protected abstract int getIndex(String uuid);
}
