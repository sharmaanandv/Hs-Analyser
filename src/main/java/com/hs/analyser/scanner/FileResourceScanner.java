package com.hs.analyser.scanner;

import com.hs.analyser.reader.HsFileReader;
import com.hs.analyser.reader.TxtFileReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileResourceScanner implements ResourceScanner {

    private String rootDir;

    private String dirProcessed;

    public FileResourceScanner(String rootDir) {
        this.rootDir = rootDir;
        dirProcessed = rootDir + "processed" + "\\";
        // create processed dir if not present
        createProcessDir(dirProcessed);
    }

    /**
     * scan existing files in root dir
     * watch if new files is created inside root dir
     */
    @Override
    public void scan() {
        scheduleScanForExistingFiles();
        watchFolder(rootDir);
    }

    /**
     * method will scan root folder once then start scanning in an interval of 1 min
     */
    private void scheduleScanForExistingFiles() {
        scanAlreadyPresentFiles(rootDir);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> {
            scanAlreadyPresentFiles(rootDir);
        }, 1, TimeUnit.MINUTES);
    }

    private void scanAlreadyPresentFiles(String rootDir) {
        System.out.println("======SCHEDULER TASK STARTED======");
        List<String> files = Stream.of(new File(rootDir).listFiles()).filter(x -> x.isFile()).map(y -> y.getName())
                .collect(Collectors.toList());
        if (files.size() == 0) {
            System.out.println("No files found in root dir");
        } else {
            System.out.println(files.size() + " files found in root dir");
        }
        files.forEach(this::fileFound);
    }

    /**
     * As process is done,move file to pro processed dir
     * if file is already present in target dir then append random number to file name
     *
     * @param fileName name of file
     */
    private void moveToTarget(String fileName) {
        synchronized (this) {
            String targetFileName;
            if (new File(dirProcessed + fileName).exists()) {
                String[] split = fileName.split("\\.");
                Random random = new Random();
                targetFileName = split[0] + "_" + Calendar.getInstance().getTimeInMillis() + "_" + random.nextLong()
                        + "." + split[1];
            } else {
                targetFileName = fileName;
            }
            try {
                Files.move(Paths.get(rootDir + fileName), Paths.get(dirProcessed + targetFileName));
                System.out.println(fileName + " file moved to processed dir");

            } catch (IOException e) {
                System.out.println("moveToTarget: failed to move file: " + fileName);
            }
        }
    }

    /**
     * If any new file is created then then start processing of file in separate thread
     * newCachedThreadPool is used to manage thread creation
     *
     * @param filePath path of file
     */
    private void watchFolder(String filePath) {
        File dir = new File(filePath);
        Path path = dir.toPath();
        System.out.println("=============Watching path: " + path);
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            WatchService watcher = path.getFileSystem().newWatchService();
            path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
            WatchKey key;

            while ((key = watcher.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        executor.submit(() -> {
                            fileFound(event.context().toString());
                        });

                    }
                }
                key.reset();
            }
        } catch (Exception e) {
            System.out.println("Exception occured while watching " + e.getMessage());
            watchFolder(rootDir);
        }
    }

    /**
     * This method will process file, once done then move file to target dir
     *
     * @param fileName name of file
     */
    private void fileFound(String fileName) {
        processFile(fileName);
        moveToTarget(fileName);
    }

    /**
     * based on file extention this method will initialize
     *
     * @param fileName name of file
     */
    private void processFile(String fileName) {
        System.out.println("Processing file : " + fileName);
        String fileType = fileName.split("\\.")[1];
        HsFileReader hsFileReader = null;
        switch (fileType) {
            case "txt":
                hsFileReader = new TxtFileReader(rootDir + fileName);
                break;
            case "pdf":
                throw new RuntimeException("Unsupported file type: " + fileType);
            default:
                throw new IllegalStateException("Unexpected value: " + fileType);
        }
        hsFileReader.read();
    }

    /**
     * create Processed dir
     *
     * @param dirProcessed dir name
     */
    private void createProcessDir(String dirProcessed) {
        File dirProcessedFile = new File(dirProcessed);
        dirProcessedFile.mkdir();
    }
}
