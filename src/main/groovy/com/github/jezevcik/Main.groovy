package com.github.jezevcik

import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

class Main {

    final static def VERSION = 1

    static void main(String[] args) {
        final Options options = new Options()

        options.addOption("h", "help", false, "Show help")
        options.addOption("f", "fonts", true, "Path to a folder containing TTF fonts")
        options.addOption("o", "output", true, "Path to a folder containing output msdf fonts")
        options.addOption("b", "bin", true, "Path to a binary file of https://github.com/Chlumsky/msdf-atlas-gen")
        options.addOption("c", "charset", true, "Path to a binary file containing the used charset")

        final CommandLineParser commandLineParser = new DefaultParser()
        final CommandLine commandLine = commandLineParser.parse(options, args)

        if (commandLine.hasOption("help")) {
            println "Bulk MSDF Font Generator v${VERSION}"
            for (Option option : options.getOptions()) {
                println "${option.getLongOpt()} (${option.getOpt()}): ${option.getDescription()}"
            }

            return
        }

        if (commandLine.hasOption("f") && commandLine.hasOption("o") && commandLine.hasOption("b") && commandLine.hasOption("c")) {
            final def fontFolderPath = commandLine.getOptionValue("f"),
                      outputFolderPath = commandLine.getOptionValue("o"),
                      binPath = commandLine.getOptionValue("b"),
                      charsetPath = commandLine.getOptionValue("c")
            final File fontFolder = new File((String) fontFolderPath),
                       outputFolder = new File((String) outputFolderPath),
                       binFile = new File((String) binPath),
                       charsetFile = new File((String) charsetPath)

            if (fontFolder.exists() && fontFolder.isDirectory() && outputFolder.exists() && outputFolder.isDirectory() && binFile.exists() && binFile.isFile() && charsetFile.exists() && charsetFile.isFile()) {
                try {
                    new MsdfGenerator(binFile, charsetFile, fontFolder, outputFolder).generate()
                } catch (Exception e) {
                    println e.getLocalizedMessage()
                }
            } else {
                println "One of the provided paths does not point to a valid file or folder"
            }

            println("Done!")
        } else {
            println("Usage: -f <fonts path> -o <output path> -b <bin path> -c <charset path>")
        }
    }
}