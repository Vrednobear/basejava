package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.*;

class MapResumeStorage extends AbstractStorage <Resume> {
    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    protected Resume getSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected void doUpdate(Resume r, Resume resume) {
        storage.put(r.getUuid(), r);

    }

    @Override
    protected boolean isExist(Resume resume) {
      return (resume != null);
    }

    @Override
    protected void doSave(Resume r, Resume resume) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected void doDelete(Resume resume) {
        storage.remove(resume.getUuid());
    }

    @Override
    protected Resume doGet(Resume resume) {
        return resume;
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public int size() {
        return storage.size();
    }

    public List<Resume> doCopyAll() {
        return new ArrayList<>(storage.values());
    }
}

