package com.cartostatutils.replacelinkutils.properties;

import static com.cartostatutils.replacelinkutils.properties.ConvertUtils.convertDirectoryToPath;
import static com.cartostatutils.replacelinkutils.properties.ConvertUtils.convertFileToPath;
import java.nio.file.Path;
import lombok.Getter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.StringUtils;
import static com.cartostatutils.replacelinkutils.ReplaceLinkApp.mainApp;

@Getter
public final class AppProperties {
    
    private String charset;
    private Path codeFile;
    private Path outputDirectory;
    private Path srcDirectory;
    
    private AppProperties(String srcDirectory, String codeFile, String outputDirectory, String charSet) {
        setSrcDirectory(srcDirectory);
        setCodeFile(codeFile);
        setOutputDirectory(outputDirectory);
        setCharset(charSet);
    }
    
    public static AppProperties fromCommandLine(CommandLine commandLine) {
        return new AppProperties(
                commandLine.getOptionValue("srcdir"),
                commandLine.getOptionValue("codefile"),
                commandLine.getOptionValue("outputdir"),
                commandLine.getOptionValue("charset")
        );
    }
    
    private void setCharset(String charset) {
        if (StringUtils.trimToNull(charset) == null) {
            String defaultCharset = mainApp().getDefaultCharset();
            mainApp().printlnVerbose("Le jeu de caracteres n'est pas specifie; le jeu de caracteres par defaut (" + defaultCharset + ") sera utilise");
            this.charset = defaultCharset;
        } else {
            this.charset = charset;
        }
    }
    
    private void setCodeFile(String codeFile) {
        try {
            this.codeFile = convertFileToPath(codeFile);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Le chemin vers le fichier des codes n'est pas valide");
        }
        
    }
    
    private void setOutputDirectory(String outputDirectory) {
        try {
            if (StringUtils.trimToNull(outputDirectory) == null) {
                mainApp().printlnVerbose("Le chemin du repertoire de destination n'est pas renseigne; le repertoire source sera utilise comme repertoire de destination");
                this.outputDirectory = this.srcDirectory;
            } else {
                this.outputDirectory = convertDirectoryToPath(outputDirectory);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Le chemin du répertoire de destination n'est pas valide");
        }
    }
    
    private void setSrcDirectory(String srcDirectory) {
        try {
            this.srcDirectory = convertDirectoryToPath(srcDirectory);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Le chemin du repertoire source n'est pas valide");
        }
    }
    
}
