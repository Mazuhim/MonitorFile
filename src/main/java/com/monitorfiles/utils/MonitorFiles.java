/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monitorfiles.utils;

import java.io.File;

/**
 *
 * @author thiagomazuhimcosta
 */
public class MonitorFiles {
   private static File inDirectory;
    private static File outDirectory;
    private static File fileDataDirectory;

    public static void initialize()
    {
        File userHome = ExFile.getUserHome();

        fileDataDirectory = new File(userHome, "data");

        fileDataDirectory.mkdirs();

        inDirectory = new File(fileDataDirectory, "in");
        inDirectory.mkdirs();

        outDirectory = new File(fileDataDirectory, "out");
        outDirectory.mkdirs();

    }


    public static File getFilesIndir()
    {
        return inDirectory;
    }

    public static File getFilesOutdir()
    {
        return outDirectory;
    }


    public static File getFilesDataDir()
    {
        return fileDataDirectory;
    }
}
