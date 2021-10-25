package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {


    @Override
    protected void fillDeletedElement(int index) {
        int numMoved = size - index -1;
        System.arraycopy(storage, index + 1, storage, index, numMoved);
    }

    @Override
    protected void insertElement(Resume r, int index) {
        int insertInd = -index - 1;
            System.arraycopy(storage, insertInd, storage, insertInd + 1, size() - insertInd);
        storage[insertInd] = r;
    }

    //before binary search we have to order the array
    // have to make Resume class comparable because
    // binarySearch() is using compareTo;
    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume(uuid);
       // sort(storage);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}
