package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not readable/writable");
        }
        this.directory = directory;
    }

    protected abstract void doWrite(Resume r, OutputStream os) throws IOException ;

    protected abstract Resume doRead(InputStream is) throws IOException;

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        try {
            doWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File write error", r.getUuid(), e);
        }

    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            file.createNewFile();
            doWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Could not create the file" +
                    file.getAbsolutePath(), file.getName(), e);
        }
    }


    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("File delete Error", file.getName());
        }

    }

    @Override
    protected Resume doGet(File file) {
        try {
            return doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File read error", file.getName(), e);
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> resumes = new ArrayList<>();
        File[] files = directory.listFiles();
        if(files == null) {
            throw new StorageException("Directory read error",null);
        }
            for (File x :
                    files) {
                resumes.add(doGet(x));
            }
            return resumes;
    }

    //null
    @Override
    public void clear() {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File x :
                    files) {
                doDelete(x);
            }
        }

    }

    //null
    @Override
    public int size() {
        String [] list = directory.list();
        if(list == null){
            throw new StorageException("Directory read error",null);
        }
        return list.length;
    }


}
