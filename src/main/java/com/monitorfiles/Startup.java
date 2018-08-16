/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monitorfiles;

import com.monitorfiles.monitor.DirectoryMonitor;
import com.monitorfiles.utils.MonitorFiles;

/**
 *
 * @author thiagomazuhimcosta
 */
public class Startup {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MonitorFiles.initialize();

        DirectoryMonitor directoryMonitor = new DirectoryMonitor();
        directoryMonitor.setup(MonitorFiles.getFilesIndir().getAbsolutePath());

    }



}
