package com.urise.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class MainFile {
    public static void main(String[] args) {

        File file = new File("D:\\Inteships\\basejava\\.gitignore");
        File file1 = new File("..\\.gitignore"); //parent directory
        File file2 = new File(".\\.gitignore"); //current directory

        File file3 = new File("D:\\Inteships\\basejava\\src\\com\\urise\\webapp");
        MainFile mainFile = new MainFile();
        mainFile.printListFilesPretty(file3);

//        MainFile mainFile = new MainFile();
//        mainFile.printListFiles(file3);

        try {
            System.out.println(file.getCanonicalPath());
            System.out.println(file1.getCanonicalPath());
            System.out.println(file2.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        File dir = new File("D:\\Inteships\\basejava\\src\\com\\urise\\webapp");
        System.out.println(dir.isDirectory());
        System.out.println(dir.isFile());
        String[] list = dir.list();
        if (list != null) {
            for (String name :
                    list) {
                System.out.println(name);
            }
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //   throw new RuntimeException();

        try (FileInputStream fis1 = new FileInputStream(file)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        }

        try {
            System.out.println(1 / 0);
        } catch (ArithmeticException e) {
            System.out.println("from catch");
            e.printStackTrace();
        }
        System.out.println("Pep");
    }

    public static void printListFiles(File file) {
        File[] files = file.listFiles();
        if(files != null) {
            for (File x :
                    files) {
                if (x.isFile()) {
                    System.out.println("File " + x.getName());
                } else {
                    System.out.println("Directory " + x.getName());
                        printListFiles(x);
                }
            }
        }
    }

    public static void printListFilesPretty(File file) {
        File[] files = file.listFiles();
        if(files != null) {
            for (File x :
                    files) {
                if (x.isFile()) {
                    System.out.println("File: " + x.getName());
                } else {
                    System.out.println("Directory: " + x.getName());
                    if(Arrays.asList(x.listFiles()).size() > 0) {
                        Stream.<File>of(x.listFiles()).forEach(path -> {
                            System.out.print("\t");
                            printListFilesPretty(path);
                        });
                    }
                }
            }
        }else{
            System.out.println("File: " + file.getName());
        }
    }

//    public static void deleteFiles(File file) {
//        if (file.isDirectory()) {
//            for (File x :
//                    file.listFiles()) {
//                deleteFiles(x);
//            }
//        }
//        file.delete();
//    }
}

