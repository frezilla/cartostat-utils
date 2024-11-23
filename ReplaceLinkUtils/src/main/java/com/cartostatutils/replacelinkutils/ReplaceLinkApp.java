package com.cartostatutils.replacelinkutils;

import com.cartostatutils.replacelinkutils.commandline.CommandLineManager;
import com.cartostatutils.replacelinkutils.properties.AppProperties;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;
import lombok.Getter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

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
    private final PrintStream ps;
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
    
    public void displayVersion() {
        ps.println(String.format("%s \"%s\"", name, version));
        ps.println(String.format("%10s", description));
        ps.println(String.format("%10s : %s", "registre des caracteres par defaut", defaultCharset));
    }
    
    public static ReplaceLinkApp getApp() {
        return Holder.INSTANCE;
    }
    
    public static void main(String[] args) {
        try {
            ReplaceLinkApp.getApp().run(args);
        } catch (ParseException e) {
            System.err.println(e);
        }
    }
    
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
                this.ps.println("Erreur : " + e.getMessage());
                this.ps.println("Erreur : une erreur fatale a été détectée. Le programme va se terminer");
                return;
            }
            
            ps.println("coucou");
        }
    }
}
