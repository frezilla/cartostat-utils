package com.cartostatutils.replacelinkutils;

import com.cartostatutils.replacelinkutils.commandline.CommandLineManager;
import com.cartostatutils.replacelinkutils.processor.Processor;
import com.cartostatutils.replacelinkutils.processor.ProcessorException;
import com.cartostatutils.replacelinkutils.properties.AppProperties;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;

@Getter
public final class ReplaceLinkApp {
    
    private static class Holder {
        private static final ReplaceLinkApp INSTANCE;
        
        static {
            ReplaceLinkApp localInstance;
            try {
                localInstance = new ReplaceLinkApp();
            } catch (IOException e) {
                localInstance = null;
            }
            INSTANCE = localInstance;
        }
    }
    
    private final String defaultCharset;
    private final String description;
    private final String name;
    @Getter(AccessLevel.NONE) private final PrintStream ps;
    private boolean verbose;
    private final String version;
    
    private ReplaceLinkApp() throws IOException {
        Properties properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream("app.properties"));
        
        defaultCharset = properties.getProperty("app.defaultCharset");
        description = properties.getProperty("app.description");
        name = properties.getProperty("app.name");
        ps = System.out;
        verbose = false;
        version = properties.getProperty("app.version");
    }
    
    /**
     * Affiche les informations relative à la version de l'utilitaire.
     */
    public void displayVersion() {
        println(String.format("\n%s \"%s\"", name, version));
        println(String.format("%10s", description));
        println(String.format("%10s : %s", "Jeux de caracteres par defaut", defaultCharset));
    }
    
    /**
     * Retourne l'instance unique de l'application
     * @return instance
     */
    public static ReplaceLinkApp mainApp() {
        return Holder.INSTANCE;
    }
    
    /**
     * Méthode principale
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        try {
            ReplaceLinkApp.mainApp().run(args);
        } catch (ParseException e) {
            System.err.println(e);
        }
    }
    
    /**
     * Affiche une chaine de caractères vers le flux de sortie
     * @param s 
     */
    public void print(String s) {
        ps.print(StringUtils.defaultString(s));
    }
    
    /**
     * Affiche une chaine de caractères vers le flux de sortie et retourne à la 
     * ligne
     * @param s 
     */    
    public void println(String s) {
        ps.println(StringUtils.defaultString(s));
    }
    
    /**
     * Affiche une chaine de caractères vers le flux de sortie si le mode 
     * verbeux est activé
     * @param s 
     */
    public void printVerbose(String s) {
        if (isVerbose()) print(s);
    }

    /**
     * Affiche une chaine de caractères vers le flux de sortie si le mode 
     * verbeux est activé et retourne à la ligne
     * @param s 
     */    
    public void printlnVerbose(String s) {
        if (isVerbose()) println(s);
    }
    
    /**
     * Orchestre l'exécution de l'utilitaire
     * @param args
     * @throws ParseException 
     */
    public void run(String[] args) throws ParseException {
        CommandLine commandLine = CommandLineManager.getInstance().parse(args);
        
        if (commandLine.hasOption("help")) {
            CommandLineManager.getInstance().displayUsageAndHelp();
        } else if (commandLine.hasOption("version")) {
            displayVersion();
        } else {
            if (commandLine.hasOption("verbose")) {
                this.verbose = true;
            }
            
            AppProperties appProperties;
            try {
                appProperties = AppProperties.fromCommandLine(commandLine);
            } catch (IllegalArgumentException e) {
                println("Erreur : " + e.getMessage());
                println("Erreur : une erreur fatale a ete detectee. Le programme va se terminer.\n");
                CommandLineManager.getInstance().displayUsageAndHelp();    
                return;
            }
            
            try {
                new Processor().process(appProperties);
            } catch (ProcessorException e) {
                println("Erreur : " + e.getMessage());
                println("Erreur : une erreur fatale a ete detectee. Le programme va se terminer.\n");
                CommandLineManager.getInstance().displayUsageAndHelp();
            }
        }
    }
    
}
