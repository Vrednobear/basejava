package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.*;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractStorageTest {
    public static final String UUID_1 = "uuid1";
    public static final String UUID_2 = "uuid2";
    public static final String UUID_3 = "uuid3";
    public static final String UUID_4 = "uuid4";

    public static final String FULLNAME_1 = "fullName1";
    public static final String FULLNAME_2 = "fullName2";
    public static final String FULLNAME_3 = "fullName3";
    public static final String FULLNAME_4 = "fullName4";

    private static final Resume RESUME_1 = new Resume(UUID_1,FULLNAME_1);
    private static final Resume RESUME_2 = new Resume(UUID_2,FULLNAME_2);
    private static final Resume RESUME_3 = new Resume(UUID_3,FULLNAME_3);
    private static final Resume RESUME_4 = new Resume(UUID_4,FULLNAME_4);
    protected Storage storage;

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp()  {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
    }

    @Test
    public void size() {
        assertSize(3);
    }

    @Test(expected = NotExistStorageException.class)
    public void updateException() {
        Resume r = new Resume("");
        storage.update(r);
    }

    @Test
    public void update() {
        Resume newResume = new Resume(UUID_1,"New");
        storage.update(newResume);
        Assert.assertTrue(newResume == storage.get(UUID_1));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistException() {
        storage.save(RESUME_1);
    }


    @Test
    public void save() {
        storage.save(RESUME_4);
        assertSize(4);
        assertGet(RESUME_4);
    }


    @Test(expected = NotExistStorageException.class)
    public void deleteNotExistException() {
        storage.delete("dummy");
    }

    @Test
    public void delete() {
        storage.delete(UUID_1);
        assertSize(2);
    }

    @Test
    public void get() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExistException() {
        storage.get("dummy");
    }

    @Test
    public void getAllSorted() {
        List<Resume> list = storage.getAllSorted();
        Assert.assertEquals(3, list.size());
        Assert.assertEquals(list,Arrays.asList(RESUME_1,RESUME_2,RESUME_3));
//        Assert.assertEquals(RESUME_1, list.get(0));
//        Assert.assertEquals(RESUME_2, list.get(1));
//        Assert.assertEquals(RESUME_3, list.get(2));
    }

    private void assertSize(int size) {
        Assert.assertEquals(size, storage.size());
    }

    private void assertGet(Resume r) {
        Assert.assertEquals(r, storage.get(r.getUuid()));
    }
}