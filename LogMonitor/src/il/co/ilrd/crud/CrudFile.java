package il.co.ilrd;

import il.co.ilrd.crud.CRUD;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Matan Keler, Infinity Labs
 * CrudFile, Use CRUD operations on file
 */
public class CrudFile implements CRUD<String, Integer> {
    private File monitoredFile;
    private File crudFile;
    private int size;

    public CrudFile(String crudFilePath, File monitoredFile) {
        Objects.requireNonNull(crudFilePath, "path cannot be null");
        Objects.requireNonNull(monitoredFile, "monitor file cannot be null");
        this.monitoredFile = monitoredFile;
        crudFile = new File(crudFilePath);
        if (!crudFile.exists()) { throw new IllegalArgumentException("path not exist"); }

        try {
            copyFile(monitoredFile, crudFile);
        }
        catch (IOException e) {
            e.getCause();
        }
    }

    private void copyFile(File in, File out) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(in));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(out))) {

            byte[] buffer = new byte[1024 * 1024];
            int read = 0;
            while ((read = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
                ++size;
            }

            bis.close();
            bos.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private int CountLines() {
        long numOfLines = 0;

        try (Stream<String> lines = Files.lines(crudFile.toPath(), Charset.defaultCharset())) {
            numOfLines = lines.count();
        }
        catch (IOException e) {
            e.getCause();
        }

        return (int)numOfLines;
    }

    @Override
    public Integer create(String entity) {
        Objects.requireNonNull(entity, "create cannot get null string");

        try {
            if (!entity.contains("\n")) {
                Files.write(crudFile.toPath(), (entity + "\n").getBytes(), StandardOpenOption.APPEND);
            }
            else {
                Files.write(crudFile.toPath(), (entity).getBytes(), StandardOpenOption.APPEND);
            }
        }
        catch (IOException e) {
            e.getCause();
        }

        return (++size);
    }

    @Override
    public String read(Integer specialKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Integer specialKey, String entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Integer specialKey) {
        throw new UnsupportedOperationException();
    }
}
