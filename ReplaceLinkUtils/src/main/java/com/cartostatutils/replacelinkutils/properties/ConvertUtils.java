package com.cartostatutils.replacelinkutils.properties;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

final class ConvertUtils {
    
    public static Path convertDirectoryToPath(String directory) {
        return convertToPath(
                directory, 
                (File f) -> f.exists() && f.isDirectory(), 
                "Le chemin indiqué ne correspond pas à un répertoire valide"
        );
    }
    
    public static Path convertFileToPath(String file) {
        return convertToPath(
                file, 
                (File f) -> f.exists() && f.isFile(),
                "Le chemin indiqué ne correspond pas à un fichier valide"
        );
    }
    
    private static Path convertToPath(String s, ConvertCtlFunction func, String msg) {
        try {
            Path path;
            File f = new File(Objects.requireNonNull(s));
            
            if (func.check(f)) path = f.toPath();
            else throw new IllegalArgumentException();
            
            return path;
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException(msg);
        }
    }
    
}
