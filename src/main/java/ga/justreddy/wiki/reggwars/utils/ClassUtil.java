package ga.justreddy.wiki.reggwars.utils;

import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;


/**
 * @author JustReddy
 */
public class ClassUtil {

    @SneakyThrows
    public static <T> Set<Class<? extends T>> findClasses(File jarFile, Class<T> clazz) {
        final URL url = jarFile.toURI().toURL();
        Set<Class<? extends T>> classes = new HashSet<>();
        Set<String> matches = getClassNamesFromJarFile(jarFile);
        try (URLClassLoader loader = new URLClassLoader(new URL[]{url}, clazz.getClassLoader())) {
            for (String match : matches) {
                Class<?> loaded = Class.forName(match, true, loader);
                if (clazz.isAssignableFrom(loaded)) {
                    classes.add(loaded.asSubclass(clazz));
                }
            }
        }
        return classes;
    }

    @SneakyThrows
    private static Set<String> getClassNamesFromJarFile(File givenFile) {
        Set<String> classNames = new HashSet<>();
        try (JarFile jarFile = new JarFile(givenFile)) {
            Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    String className = jarEntry.getName()
                            .replace("/", ".")
                            .replace(".class", "");
                    classNames.add(className);
                }
            }
            return classNames;
        }
    }


}
