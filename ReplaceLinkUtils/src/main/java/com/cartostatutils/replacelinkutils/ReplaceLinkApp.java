package com.cartostatutils.replacelinkutils;

import com.cartostatutils.replacelinkutils.commandline.CommandLineManager;
import com.cartostatutils.replacelinkutils.processor.Processor;
import com.cartostatutils.replacelinkutils.processor.ProcessorException;
import com.cartostatutils.replacelinkutils.properties.AppProperties;
import java.io.Console;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
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
    
    @Getter(AccessLevel.NONE) private final Console console;
    private final String defaultCharset;
    private final String description;
    private final String name;
    @Getter(AccessLevel.NONE) private final PrintStream ps;
    private boolean verbose;
    private final String version;
    
    private ReplaceLinkApp() throws IOException {
        Properties properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream("app.properties"));
        
        console = System.console();
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
        ps.println(String.format("%10s : %s", "registre des caractères par défaut", defaultCharset));
    }
    
    public static ReplaceLinkApp mainApp() {
        return Holder.INSTANCE;
    }
    
    public PrintWriter getWriter() {
        if (console == null) {
            return new PrintWriter(ps);
        } else {
            return console.writer();
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        try {
            ReplaceLinkApp.mainApp().run(args);
        } catch (ParseException e) {
            System.err.println(e);
        }
    }
    
    public void print(String s) {
        String string = StringUtils.defaultString(s);
        
        if (console == null) {
            ps.print(string);
        } else {
            console.writer().write(string);
        }
    }
    
    public void println(String s) {
        print(StringUtils.defaultString(s) + StringUtils.CR);
    }
    
    public void printVerbose(String s) {
        if (isVerbose()) print(s);
    }
    
    public void printlnVerbose(String s) {
        if (isVerbose()) println(s);
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
                println("Erreur : " + e.getMessage());
                println("Erreur : une erreur fatale a été détectée. Le programme va se terminer.");
                CommandLineManager.getInstance().displayUsageAndHelp();
                return;
            }
            
            try {
                new Processor().process(appProperties);
            } catch (ProcessorException e) {
                println("Erreur : " + e.getMessage());
                println("Erreur : une erreur fatale a été détectée. Le programme va se terminer.");
            }
        }
    }
    
}
