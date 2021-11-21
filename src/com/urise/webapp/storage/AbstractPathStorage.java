package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class AbstractPathStorage extends AbstractStorage<Path> {
    private Path directory;

    protected AbstractPathStorage(String dir) {
        directory = Paths.get(dir);
        Objects.requireNonNull(directory, "directory must not be null");
        if(!Files.isDirectory(directory) ||
                !Files.isWritable(directory) || Files.isReadable(directory)){
            throw new IllegalArgumentException(dir +
                    "is not directory or is not readable/writable");
        }
    }

    protected abstract void doWrite(Resume r, OutputStream os) throws IOException ;

    protected abstract Resume doRead(InputStream is) throws IOException;

    @Override
    protected Path getSearchKey(String uuid) {
        return Paths.get(directory.toString(),uuid);
    }

    @Override
    protected void doUpdate(Resume r, Path path) {
        try {
            doWrite(r, new BufferedOutputStream(new FileOutputStream(path.toString())));
        } catch (IOException e) {
            throw new StorageException("Path write error", r.getUuid(), e);
        }

    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected void doSave(Resume r, Path path) {
        try {
            Files.createFile(path);
            doWrite(r, new BufferedOutputStream(new FileOutputStream(path.toString())));
        } catch (IOException e) {
            throw new StorageException("Could not create the Path" +
                    path.toString(), r.getUuid(), e);
        }
    }


    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path) ;
    } catch (IOException e) {
        throw new StorageException("Path delete Error", path.getFileName().toString(),e);
        }

    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return doRead(new BufferedInputStream(new FileInputStream(path.toString())));
        } catch (IOException e) {
            throw new StorageException("Path read error",path.getFileName().toString(), e);
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> resumes = new ArrayList<>();
        try {
             Files.list(directory).forEach((path) -> resumes.add(doGet(path)));
        } catch (IOException e) {
            throw new StorageException("Directory read error",null);
        }
            return resumes;
    }

    //null
    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Path delete error",null);
        }

    }

    //null
    @Override
    public int size() {
        int size;
        try {
           size = (int) Files.list(directory).count();
        } catch (IOException e) {
            throw new StorageException("Directory read error",null);
        }
        return size;
    }


}
