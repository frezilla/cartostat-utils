package com.cartostatutils.replacelinkutils;

import java.io.IOException;
import java.util.Properties;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

public class ReplaceLinkUtils {
    
    private static class Holder {
        private static final ReplaceLinkUtils INSTANCE;
        
        static {
            ReplaceLinkUtils localInstance;
            try {
                localInstance = new ReplaceLinkUtils();
            } catch (IOException e) {
                localInstance = null;
            }
            INSTANCE = localInstance;
        }
    }
    
    public final String defaultCharset;
    public final String description;
    public final String name;
    public final String version;
    
    private ReplaceLinkUtils() throws IOException {
        Properties properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream("app.properties"));
                
        defaultCharset = properties.getProperty("app.defaultCharset");
        description = properties.getProperty("app.description");
        name = properties.getProperty("app.name");
        version = properties.getProperty("app.version");
    }
    
    public void displayVersion() {
        System.out.println(String.format("%s \"%s\"", name, version));
        System.out.println(String.format("%10s", description));
        System.out.println(String.format("%10s : %s", "registre des caracteres par default", defaultCharset));
    }
    
    public static ReplaceLinkUtils getApp() {
        return Holder.INSTANCE;
    }
    
    public static void main(String[] args) {
        try {
            ReplaceLinkUtils.getApp().run(args);
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
            System.out.println("Exécution du programme");
        }
    }
}
