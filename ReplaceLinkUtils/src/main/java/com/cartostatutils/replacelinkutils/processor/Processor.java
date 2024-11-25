package com.cartostatutils.replacelinkutils.processor;

import com.cartostatutils.replacelinkutils.properties.AppProperties;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import static com.cartostatutils.replacelinkutils.ReplaceLinkApp.mainApp;
import java.nio.file.Files;

public final class Processor {
    
    private static final String RMPLCR = "RMPLCR";
    private static final String EXTENSION = "SVG";

    private Iterator<Pair<Integer, String>> computeProgressSteps(int maxValue, int nbSteps) {
        DecimalFormat df = new DecimalFormat("# %%");
        List<Pair<Integer, String>> steps = new ArrayList<>();

        int finalNbSteps = Math.min(maxValue, nbSteps);
        int stepVal = (int) (maxValue / finalNbSteps);

        int currentVal = 0;
        while (currentVal < maxValue) {
            double percentage = 100.0 * currentVal / maxValue;
            steps.add(
                    Pair.of(
                            currentVal,
                            df.format(percentage)
                    )
            );
            currentVal += stepVal;
        }

        steps.add(
                Pair.of(
                        maxValue,
                        df.format(100.0)
                )
        );

        return steps.iterator();
    }

    private Map<String, String> loadCodeFile(Path codeFile, String charset) throws ProcessorException {
        Map<String, String> map = new TreeMap<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(codeFile.toFile()), charset))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                String[] words = currentLine.split("\t");
                if (words.length >= 2) {
                    map.put(words[0], words[1]);
                }
            }
        } catch (IOException e) {
            throw new ProcessorException("Erreur à la lecture du fichier", e);
        }

        return map;
    }

    public void process(AppProperties properties) throws ProcessorException {
        mainApp().printVerbose("Chargement des codes...");
        Map<String, String> codeMap = loadCodeFile(properties.getCodeFile(), properties.getCharset());
        if (codeMap.isEmpty()) {
            mainApp().printlnVerbose("erreur");
            mainApp().println("Le fichier des codes est vide. Le programme va se terminer.");
            return;
        } else {
            mainApp().printlnVerbose("ok");
        }

        mainApp().printVerbose("Sélection des fichiers à traiter...");
        File[] files = properties.getSrcDirectory().toFile().listFiles((pathname) -> {
            return StringUtils.equalsIgnoreCase(
                    FilenameUtils.getExtension(pathname.getName()),
                    EXTENSION
            );
        });
        mainApp().printlnVerbose("ok");

        int nbFiles = files.length;

        if (nbFiles == 0) {
            mainApp().println("Aucun fichier sélectionné");
        } else {
            String charset = properties.getCharset();
            Path ouputDirectory = properties.getOutputDirectory();
            
            Iterator<Pair<Integer, String>> steps = computeProgressSteps(nbFiles, 10);
            Pair<Integer, String> currentStep = steps.next();

            StringBuilder progressMsg = new StringBuilder("Progression : ");
            boolean error = false;

            mainApp().println("Traitement en cours...");

            int indexFile = 0;
            while (indexFile < nbFiles) {
                File currentFile = files[indexFile];
                String titleValue = currentFile.getName();
                titleValue = StringUtils.substring(titleValue, 0, titleValue.length() - FilenameUtils.getExtension(titleValue).length() - 1);

                if (codeMap.containsKey(titleValue)) {
                    try {
                        replaceValue(currentFile, codeMap.get(titleValue), ouputDirectory, charset);
                    } catch (ProcessorException e) {
                        mainApp().println("Une erreur technique est survenue pendant le traitement du fichier " + currentFile.getName());
                        mainApp().printVerbose(e.getMessage());
                        error = true;
                    }
                } else {
                    mainApp().println("Le fichier " + currentFile.getAbsolutePath() + " n'a pas de valeur renseignée; il ne sera pas traité.");
                    error = true;
                }

                indexFile++;
                if (indexFile >= currentStep.getLeft()) {
                    progressMsg.append("...").append(currentStep.getRight());
                    if (error) {
                        mainApp().print(progressMsg.toString());
                        error = false;
                    } else {
                        mainApp().print("..." + currentStep.getRight());
                    }
                    if (steps.hasNext()) {
                        currentStep = steps.next();
                    }
                }
            }

            if (error) {
                mainApp().println(progressMsg.append("...").append(currentStep.getRight()).toString());
            } else {
                mainApp().println("..." + currentStep.getRight());
            }

            mainApp().print("Traitement terminé");
        }
    }

    private void replaceValue(File file, String codeValue, Path outputDirectory, String charset) throws ProcessorException {
        try {
            Path srcPath = file.toPath();
            Path outPath = new File(outputDirectory.toString() + file.getName()).toPath();

            String content = new String(Files.readAllBytes(srcPath), charset);
            content = content.replaceAll(RMPLCR, codeValue);
            Files.write(outPath, content.getBytes(charset));
        } catch (IOException e) {
            throw new ProcessorException(e);
        }
    }
}
