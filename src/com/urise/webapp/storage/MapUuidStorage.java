package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.*;

public class MapUuidStorage extends AbstractStorage {
    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected void doUpdate(Resume r, Object uuid) {
        storage.put((String) uuid,r);
    }

    @Override
    protected boolean isExist(Object uuid) {
       return storage.containsKey((String)uuid);
    }

    @Override
    protected void doSave(Resume r, Object uuid) {
        storage.put((String) uuid,r);
    }

    @Override
    protected void doDelete(Object uuid) {
        storage.remove((String) uuid);

    }

    @Override
    protected Resume doGet(Object uuid) {
        return storage.get((String) uuid);
    }

    @Override
    public void clear() {
        storage.clear();
    }


    public List<Resume> doCopyAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public int size() {
        return storage.size();
    }
}
