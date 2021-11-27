package com.hs.analyser.service;

import java.io.File;

import com.hs.analyser.scanner.FileResourceScanner;
import com.hs.analyser.scanner.FtpResourceScanner;
import com.hs.analyser.scanner.ResourceScanner;

public class MonitorService {

    /**
     * This Method will initialize scanner based on input
     * For now only scanner local directory In future we can proide other implementations like FTP, S3 etc
     *
     * @param rootDir root dir to be scan
     */
    public void startMonitoring(String rootDir) {
        rootDir = rootDir.endsWith("/") ? rootDir : rootDir + "\\";
        if (!new File(rootDir).isDirectory()) {
            throw new RuntimeException("Not an valid directory");
        }
        System.out.println("==========MONITORING STARTED===============");
        ResourceScanner resourceScanner = null;
        // considering only one impl for scanner: file
        if (!rootDir.startsWith("ftp:")) {
            resourceScanner = new FileResourceScanner(rootDir);
        } else {
            // not yet implemented
            resourceScanner = new FtpResourceScanner();
        }
        resourceScanner.scan();
    }

}
