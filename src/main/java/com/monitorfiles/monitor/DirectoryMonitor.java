/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.monitorfiles.monitor;

import com.monitorfiles.exceptions.ParsingException;
import com.monitorfiles.utils.ExFile;
import com.monitorfiles.utils.MonitorFiles;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

/**
 *
 * @author thiagomazuhimcosta
 */
public class DirectoryMonitor
{

    private WatchService watchService;
    Path path;

    public void setup(String path)
    {
        this.path = Paths.get(path);

        try
        {
            this.watchService = FileSystems.getDefault().newWatchService();
            this.path.register(this.watchService, StandardWatchEventKinds.ENTRY_CREATE);
            this.processPendingFiles();
        } catch (IOException ex)
        {
            System.out.println("erro ao registrar servico de monitoria: ");
        }

        this.listenerDir(this.watchService);
    }

    private boolean checkFile(File file)
    {
        if (!file.exists())
        {
            return false;
        }
        if (!file.getName().endsWith(".dat"))
        {
            return false;
        }

        return true;
    }

    private void listenerDir(WatchService watchService)
    {
        new Thread(()
                ->
        {

            WatchKey key;
            try
            {
                while ((key = watchService.take()) != null)
                {
                    key.pollEvents().forEach((event)
                            ->
                    {
                        File file = new File(path.toFile(), event.context().toString());
                        if (this.checkFile(file))
                        {
                            this.process(file);
                        }
                    });
                    key.reset();
                }
            } catch (InterruptedException ex)
            {
                System.out.println("error: " + ex);
            }

        }).start();
    }

    private void processPendingFiles()
    {
        new Thread(() ->
        {
            File fileInDir = MonitorFiles.getFilesIndir();

            if (fileInDir.listFiles().length <= 0)
            {
                return;
            }

            for (File file : fileInDir.listFiles())
            {
                if (checkFile(file))
                {
                    this.process(file);
                }
            }
        }).start();
    }

    private void process(File file)
    {
        ProcessData processData = new ProcessData(file);
        try
        {
            processData.process();
            file.renameTo(new File(file.getAbsolutePath() + ".ready"));
        } catch (ParsingException ex)
        {
            ExFile exFileError = new ExFile(file.getAbsolutePath().replace(".dat", ".ERROR-DETAIL"));

            try
            {

                String nameFileError = file.getAbsolutePath().replace(".dat", ".error");
                exFileError.write(ex.getLocalizedMessage());
                exFileError.breakLine();
                exFileError.write(ex.getStackTraceString());
                exFileError.finishWriting();
                file.renameTo(new File(nameFileError));
            } catch (IOException exception)
            {
                System.out.println("error: " + exception);
            }
        }
    }
}
