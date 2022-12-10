package com.story.community.core.utils;

import java.io.File;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Set;

import com.story.community.core.common.support.Resource;

public final class ResourceUtils {
    private static final char PKG_SEPARATOR = '.';

    private static final char DIR_SEPARATOR = '/';

    private static final String CLASS_FILE_SUFFIX = ".class";

    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

    private ResourceUtils() {
        throw new UnsupportedOperationException("Can't instance ResourceUtils");
    }

    private static Set<Class<?>> find(File file, String scannedPackage) {
        Set<Class<?>> classes = new LinkedHashSet<>();
        String resource = scannedPackage + PKG_SEPARATOR + file.getName();
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                classes.addAll(find(child, resource));
            }
        } else if (resource.endsWith(CLASS_FILE_SUFFIX)) {
            int endIndex = resource.length() - CLASS_FILE_SUFFIX.length();
            String className = resource.substring(0, endIndex);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException ignore) {
                // Ignore
            }
        }
        return classes;
    }

    public static Resource<Set<Class<?>>> getClassFromClasspath(ClassLoader classLoader, String scannedPackage) {
        String scannedPath = scannedPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR);
        URL scannedUrl = classLoader.getResource(scannedPath);
        if (scannedUrl == null) {
            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
        }
        File scannedDir = new File(scannedUrl.getFile());
        Set<Class<?>> classes = new LinkedHashSet<>();
        for (File file : scannedDir.listFiles()) {
            classes.addAll(find(file, scannedPackage));
        }
        return new Resource<>(classes);
    }
}
