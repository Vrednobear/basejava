package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.*;
import org.junit.*;

import java.io.File;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractStorageTest {

   protected static final File STORAGE_DIR = Config.getInstance().getStorageDir();
   
    public static final String UUID_1 = "uuid1";
    public static final String UUID_2 = "uuid2";
    public static final String UUID_3 = "uuid3";
    public static final String UUID_4 = "uuid4";

    public static final String FULLNAME_1 = "fullName1";
    public static final String FULLNAME_2 = "fullName2";
    public static final String FULLNAME_3 = "fullName3";
    public static final String FULLNAME_4 = "fullName4";

    private static final Resume RESUME_1;
    private static final Resume RESUME_2;
    private static final Resume RESUME_3;
    private static final Resume RESUME_4;


    static {
        RESUME_3 = ResumeTestData.createResume(UUID_3,FULLNAME_3);
        RESUME_4 = ResumeTestData.createResume(UUID_4,FULLNAME_4);
        RESUME_1 = ResumeTestData.createResume(UUID_1, FULLNAME_1);

        RESUME_2 = new Resume(UUID_2, FULLNAME_2);
        RESUME_2.addContact(ContactType.PHONE,"43424");
        RESUME_2.addContact(ContactType.SKYPE,"resume2sk");
        RESUME_2.addSection(SectionType.OBJECTIVE, new TextSection("Text Section 1 obj"));
        RESUME_2.addSection(SectionType.PERSONAL, new TextSection("Text Section1 pers"));
        RESUME_2.addSection(SectionType.ACHIEVEMENT, new ListSection("achievement1.", "achievement2."));
        RESUME_2.addSection(SectionType.QUALIFICATIONS, new ListSection("qualification1.", "qualification2."));
        RESUME_2.addSection(SectionType.EXPERIENCE, new OrganizationSection(
                new Organization("Microsoft", "ms.com",
                        new Experience(2021, Month.APRIL, "Developer", "Created Bing")),
                new Organization("Apple", "apple.com",
                        new Experience(2015, Month.FEBRUARY, 2020, Month.DECEMBER, "Developer", "Created Siri"))
        ));
        RESUME_2.addSection(SectionType.EDUCATION, new OrganizationSection(
                new Organization("MIT", "MIT.com",
                        new Experience(2013, Month.APRIL, "Assistent",null)),
                new Organization("CalTech", "CaTech.com",
                        new Experience(2007, Month.SEPTEMBER, 2011, Month.JUNE, "Bachelor",null),
                        new Experience(2011,Month.SEPTEMBER,2013,Month.JUNE,"Master",null))
        ));

        System.out.println(RESUME_2.getSection(SectionType.EDUCATION));
    }

    protected Storage storage;

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
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
        Resume newResume = new Resume(UUID_1, "New");
        newResume.addContact(ContactType.PHONE,"3232");
        newResume.addContact(ContactType.SKYPE,"updateSk");
        newResume.addSection(SectionType.PERSONAL ,new TextSection("Ton manuch"));
        newResume.addSection(SectionType.OBJECTIVE ,new TextSection("Ton object"));
     //   Resume newResume = ResumeTestData.createResume(UUID_1,"New");
        storage.update(newResume);
       // Assert.assertTrue(newResume == storage.get(UUID_1));
        assertGet(newResume);
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
        Assert.assertEquals(Arrays.asList(RESUME_1, RESUME_2, RESUME_3),list);
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