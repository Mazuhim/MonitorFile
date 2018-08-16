package com.monitorfiles.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.imageio.ImageIO;

/**
 *
 * @author thiagomazuhimcosta
 */
public class ExFile {

    public static final int KB = 1024;
    public static final int MB = 1048576;

    private File file;
    private int bufferSize;
    private BufferedInputStream inputStream;
    private BufferedOutputStream outputStream;
    private Charset charset;
    private boolean append;

    public ExFile(String path) {
        this(new File(path));
    }

    public ExFile(File parentFile, String fileName) {
        this(new File(parentFile, fileName));
    }

    public ExFile(File file) {
        this(file, KB * 8);
    }

    public ExFile(File file, int bufferSize) {
        this.file = file;
        this.bufferSize = bufferSize;
        this.charset = StandardCharsets.UTF_8;
        this.append = false;
    }

    public boolean isAppend() {
        return append;
    }

    public void setAppend(boolean append) {
        this.append = append;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void startReading() throws FileNotFoundException, IOException {
        if (this.inputStream != null) {
            this.finishReading();
        }

        this.inputStream = new BufferedInputStream(new FileInputStream(this.file), this.bufferSize);
    }

    public void finishReading() throws IOException {
        if (this.inputStream != null) {
            this.inputStream.close();
            this.inputStream = null;
        }
    }

    public void startWriting() throws FileNotFoundException, IOException {
        if (this.outputStream != null) {
            this.finishWriting();
        }

        this.outputStream = new BufferedOutputStream(new FileOutputStream(this.file, this.append), this.bufferSize);
    }

    public void finishWriting() throws IOException {
        if (this.outputStream != null) {
            this.outputStream.close();
            this.outputStream = null;
        }
    }

    public byte[] next() throws FileNotFoundException, IOException {
        byte[] bytes = this.read(this.bufferSize);

        if (bytes == null) {
            this.finishReading();
        }

        return bytes;
    }

    public byte[] read(int size) throws FileNotFoundException, IOException {
        if (this.inputStream == null) {
            this.startReading();
        }

        byte[] bytes = new byte[size];
        int readed = this.inputStream.read(bytes);

        if (readed == -1) {
            return null;
        }

        if (readed < size) {
            return Arrays.copyOf(bytes, readed);
        }

        return bytes;
    }

    public void write(char c) throws FileNotFoundException, IOException {
        if (this.outputStream == null) {
            this.startWriting();
        }

        this.outputStream.write(c);
        this.outputStream.flush();
    }

    public void write(byte[] bytes) throws FileNotFoundException, IOException {
        if (this.outputStream == null) {
            this.startWriting();
        }

        this.outputStream.write(bytes);
        this.outputStream.flush();
    }

    public void write(String text) throws IOException {
        this.write(text.getBytes(this.charset));
    }

    public void breakLine() throws IOException {
        this.write("\r\n".getBytes(this.charset));
    }

    /**
     * Reads the entire file closing the resources on finishing
     *
     * @return the full file in bytes
     * <p>
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public byte[] readFully() throws FileNotFoundException, IOException {
        try (FileInputStream fileInput = new FileInputStream(this.file); BufferedInputStream input = new BufferedInputStream(fileInput, this.bufferSize)) {
            byte[] bytes = new byte[input.available()];
            input.read(bytes);

            return bytes;
        }
    }

    /**
     * Writes the byte array to the file and close the stream
     * <p>
     * @param bytes - the data to be writen
     * <p>
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void writeFully(byte[] bytes) throws FileNotFoundException, IOException {
        try (FileOutputStream fileOutput = new FileOutputStream(this.file); BufferedOutputStream output = new BufferedOutputStream(fileOutput, this.bufferSize)) {
            output.write(bytes);
            output.flush();
        }
    }

    public String readAsText() throws FileNotFoundException, IOException {
        StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), this.charset))) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                if (builder.length() > 0) {
                    builder.append("\n");
                }

                builder.append(line);
            }
        }

        return builder.toString();
    }

    public List<String> readAllLines() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), this.charset))) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                lines.add(line);
            }

            return lines;
        }
    }

    public BufferedImage readAsImage() throws IOException {
        return ImageIO.read(this.file);
    }

    public void writeAsImage(BufferedImage image) throws IOException {
        String fileName = this.file.getName();
        int dotIndex = fileName.lastIndexOf(".");

        String extension = "png";

        if (dotIndex > -1) {
            extension = fileName.substring(dotIndex + 1);
        }

        ImageIO.write(image, extension, this.file);
    }

    public void save(Serializable object) throws FileNotFoundException, IOException {
        try (FileOutputStream fileOutput = new FileOutputStream(this.file); ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput)) {
            objectOutput.writeObject(object);
            objectOutput.flush();
        }
    }

    public Serializable load() throws FileNotFoundException, IOException, ClassNotFoundException {
        try (FileInputStream fileInput = new FileInputStream(this.file); ObjectInputStream objectInput = new ObjectInputStream(fileInput)) {
            return (Serializable) objectInput.readObject();
        }
    }

    /**
     * Tries to find out what is the extension of the file, if it can't, returns
     * null
     *
     * @return the extension of the file, without the dot
     */
    public String getExtension() {
        String fileName = this.file.getName();

        try {
            String[] split = fileName.split("\\.");

            if (split.length == 1) {
                return null;
            }

            return split[split.length - 1];
        } catch (Exception e) {
            return null;
        }
    }

    public int getSize() {
        return (int) this.file.length();
    }

    public void deleteInBackground() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                delete();
            }
        };

        thread.start();
    }

    public void delete() {
        this.delete(this.file);
    }

    private void delete(File file) {
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                this.delete(child);
            }
        }

        file.delete();
    }

    /**
     * If this file denotes a directory, recursively counts and returns the
     * number of files that are found within it, else returns 1.
     * <p>
     * @param countDirectories - if it should count the directories as a file
     * <p>
     * @return the number of files within this file or 1 if it is not a
     * directory
     */
    public int countFiles(boolean countDirectories) {
        return this.countFiles(this.file, countDirectories);
    }

    private int countFiles(File file, boolean countDirectories) {
        if (!file.isDirectory()) {
            return 1;
        }

        int count = countDirectories ? 1 : 0;

        for (File f : file.listFiles()) {
            count += this.countFiles(f, countDirectories);
        }

        return count;
    }

    public boolean move(File dest) {
        return this.file.renameTo(dest);
    }

    public String getName() {
        return file.getName();
    }

    public boolean canRead() {
        return file.canRead();
    }

    public boolean canWrite() {
        return file.canWrite();
    }

    public boolean exists() {
        return file.exists();
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }

    public boolean isFile() {
        return file.isFile();
    }

    public boolean isHidden() {
        return file.isHidden();
    }

    public boolean createNewFile() throws IOException {
        return file.createNewFile();
    }

    public boolean mkdir() {
        return file.mkdir();
    }

    public boolean mkdirs() {
        return file.mkdirs();
    }

    public boolean canExecute() {
        return file.canExecute();
    }

    public String getPath() {
        return this.file.getPath();
    }

    public File getFile() {
        return this.file;
    }

    public Date lastModified() {
        return new Date(this.file.lastModified());
    }

    public void renameTo(File newFile) {
        this.file.renameTo(newFile);
    }

    public static File getHome() {
        String property = System.getProperty("user.dir");
        return new File(property);
    }

    public static File getUserHome() {
        return new File(System.getProperty("user.home"));
    }

    public static File getDocuments() {
        File userHome = getUserHome();

        File file = new File(userHome, "Documents");
        if (file.exists()) {
            return file;
        }

        return userHome;
    }

    /**
     * Returns the java system temp directory
     * <p>
     * @return the temp directory (System.getProperty("java.io.tmpdir"))
     */
    public static File getTemp() {
        String property = System.getProperty("java.io.tmpdir");
        return new File(property);
    }

    public static File createTempDirectory() {
        StringBuilder builder = new StringBuilder("temp_");
        builder.append(UUID.randomUUID());

        File tempDirectory = new File(ExFile.getTemp(), builder.toString());
        tempDirectory.mkdirs();

        return tempDirectory;
    }

    public static File createTempFile() {
        return new File(ExFile.getTemp(), UUID.randomUUID().toString() + ".tmpf");
    }
}
