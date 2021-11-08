package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SortedArrayStorage extends AbstractArrayStorage {


    protected static final Comparator<Resume> RESUME_COMPARATOR
            = new Comparator<Resume>() {
        @Override
        public int compare(Resume o1, Resume o2) {
            return o1.getUuid().compareTo(o2.getUuid());
        }
    };

    @Override
    protected void fillDeletedElement(int index) {
        int numMoved = size - index - 1;
        System.arraycopy(storage, index + 1, storage, index, numMoved);
    }

    @Override
    protected void insertElement(Resume r, int index) {
        int insertInd = -index - 1;
        System.arraycopy(storage, insertInd, storage, insertInd + 1, size() - insertInd);
        storage[insertInd] = r;
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid, "dummy");
        return Arrays.binarySearch(storage, 0, size, searchKey, RESUME_COMPARATOR);
    }


    //before binary search we have to order the array
    // have to make Resume class comparable because
    // binarySearch() is using compareTo;
}
