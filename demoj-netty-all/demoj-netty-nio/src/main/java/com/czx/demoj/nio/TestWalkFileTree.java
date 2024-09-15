package com.czx.demoj.nio;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

public class TestWalkFileTree {
    public static void main(String[] args) throws IOException {
        AtomicInteger dllCount = new AtomicInteger(0);
        Files.walkFileTree(Paths.get("C:\\Users\\czx\\.jdks\\corretto-21.0.3"), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if(file.toString().endsWith(".dll")) {
                    System.out.println(file);
                    dllCount.incrementAndGet();
                    return FileVisitResult.TERMINATE;
                }
                return super.visitFile(file, attrs);
            }
        });
        System.out.println(dllCount);
    }
}
