package com.github.jezevcik

class MsdfGenerator {

    final File binFile, charsetFile, fontFolder, outputFolder

    MsdfGenerator(File binFile, File charsetFile, File fontFolder, File outputFolder) {
        this.binFile = binFile
        this.charsetFile = charsetFile
        this.fontFolder = fontFolder
        this.outputFolder = outputFolder
    }

    void generate() {
        if (!fontFolder.exists() || !fontFolder.isDirectory() || !outputFolder.exists() || !outputFolder.isDirectory()) {
            throw new IllegalArgumentException("Output and/or Fonts path does not point to a valid file")
        }

        for(File font : fontFolder.listFiles()) {
            if(!font.getName().toLowerCase().endsWith(".ttf") && !font.getName().toLowerCase().endsWith(".otf")) {
                println("Skipping ${font.absolutePath} because it is not a TTF or OTF font")
                continue
            }

            final def fontName = font.getName().split("\\.")[0].replace(" ", "_")
            final def command = "\"${binFile.absolutePath}\" -type msdf -imageout ${fontName}.png -size 128 -pxrange 8 -json ${fontName}.json -charset \"${charsetFile.absolutePath}\" -font \"${font.absolutePath}\""
            println "Running command: $command"

            def processBuilder = new ProcessBuilder(command)
            processBuilder.directory(outputFolder)
            def proc = processBuilder.start()

            if (proc.waitFor() != 0) {
                println "Failed to generate msdf font for ${font.getAbsolutePath()}"
            } else {
                println "Successfully generated msdf font for ${font.getAbsolutePath()}"
            }

        }
    }

}
