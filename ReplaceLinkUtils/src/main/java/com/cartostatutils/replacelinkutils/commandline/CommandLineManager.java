package com.cartostatutils.replacelinkutils.commandline;

import com.cartostatutils.replacelinkutils.ReplaceLinkApp;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import static com.cartostatutils.replacelinkutils.ReplaceLinkApp.mainApp;


public final class CommandLineManager {
    
    private static final class Holder {
        private static final CommandLineManager INSTANCE = new CommandLineManager();
    }

    private final Options options;
    private final CommandLineParser parser;
    
    private CommandLineManager() {
        options = new Options();
        
        Option help = new Option("help", "affiche ce message d'aide");
        Option version = new Option("version", "affiche la version de l'utilitaire");
        Option verbose = new Option("verbose", "active le mode detaillé");
        Option srcDirectory = createOptionsWithArgument("srcdir", "dir", "chemin vers le repertoire des fichiers à traiter");
        Option codeFile = createOptionsWithArgument("codefile", "file", "chemin vers le fichier des codes");
        Option outputDirectory = createOptionsWithArgument("outputdir", "dir", "chemin vers le repertoire de sortie");
        Option charset = createOptionsWithArgument("charset", "name", "nom du jeu de caractères");

        options.addOption(help);
        options.addOption(version);
        options.addOption(verbose);
        options.addOption(srcDirectory);
        options.addOption(codeFile);
        options.addOption(outputDirectory);
        options.addOption(charset);
        
        parser = new DefaultParser();
    }
    
    private Option createOptionsWithArgument(String optionName, String argName, String description) {
        return Option
                .builder(optionName)
                .argName(argName)
                .hasArg()
                .desc(description)
                .build();
    }
    
    public void displayUsageAndHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(ReplaceLinkApp.mainApp().getName(), options);
        formatter.printHelp(mainApp().getWriter(), 
                120, 
                "test", 
                "", 
                options, 
                0, 
                0, 
                ""
        );
    }
    
    public static CommandLineManager getInstance() {
        return Holder.INSTANCE;
    }
    
    public CommandLine parse(String[] args) throws ParseException {
        return parser.parse(options, args);
    }

}
