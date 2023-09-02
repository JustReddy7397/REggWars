package ga.justreddy.wiki.reggwars.utils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author JustReddy
 */
public class FileUtils {

    public static void copy(File source, File destination) throws IOException {
        try {
            List<String> ignore = new ArrayList<>(Arrays.asList("uid.dat", "session.lock"));
            if(!ignore.contains(source.getName())) {
                if(source.isDirectory()) {
                    if(!destination.exists())
                        if (!destination.mkdirs())
                            throw new IOException("Couldn't create world directory!");
                    String[] files = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(destination, file);
                        copy(srcFile, destFile);
                    }
                } else {
                    InputStream in = Files.newInputStream(source.toPath());
                    OutputStream out = Files.newOutputStream(destination.toPath());
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void delete(File file) {

        if (file.isDirectory()) {
            File[] f = file.listFiles();
            if (f == null) return;
            for (File child : f) {
                delete(child);
            }
        }
        file.delete();
    }


}
