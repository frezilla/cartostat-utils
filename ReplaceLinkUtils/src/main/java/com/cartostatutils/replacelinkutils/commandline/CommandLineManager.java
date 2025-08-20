package com.cartostatutils.replacelinkutils.commandline;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import static com.cartostatutils.replacelinkutils.ReplaceLinkApp.mainApp;

/**
 * Gère les valeurs passées en ligne de commande
 */
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
        Option verbose = new Option("verbose", "active le mode verbeux");
        Option srcDirectory = createOptionsWithArgument("srcdir", "dir", "chemin vers le repertoire des fichiers à traiter");
        Option codeFile = createOptionsWithArgument("codefile", "file", "chemin vers le fichier des codes");
        Option outputDirectory = createOptionsWithArgument("outputdir", "dir", "chemin vers le repertoire de sortie");
        Option charset = createOptionsWithArgument("charset", "name", "nom du jeu de caracteres");

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
    
    /**
     * Affiche l'usage et l'aide.
     */
    public void displayUsageAndHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
                240, 
                "ReplaceLink -srcdir <dir> -codefile <file>\n\n", 
                mainApp().getDescription() + "\n\n", 
                options, 
                "\nversion : " + mainApp().getVersion()
        );
    }
    
    /**
     * Retourne l'instance unique du manager.
     * @return instance
     */
    public static CommandLineManager getInstance() {
        return Holder.INSTANCE;
    }
    
    /**
     * Parse les arguments passés en paramètre.
     * @param args
     * @return
     * @throws ParseException 
     */
    public CommandLine parse(String[] args) throws ParseException {
        return parser.parse(options, args);
    }

}
