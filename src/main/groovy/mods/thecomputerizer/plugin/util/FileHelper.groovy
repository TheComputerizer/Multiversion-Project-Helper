package mods.thecomputerizer.plugin.util

import org.gradle.api.initialization.Settings

import javax.annotation.Nullable
import java.util.function.Consumer

import static mods.thecomputerizer.plugin.util.Templates.FORGE_BUILD

class FileHelper {

    private static @Nullable Object getArgOrNull(int index, @Nullable Object ... args) {
        return Objects.isNull(args) || args.length<=index ? null : args[index]
    }

    private static String getResourceAsString(String resource) {
        try(def stream = getClass().getResourceAsStream(resource)) {
            return new Scanner(stream,"UTF-8").useDelimiter('\\A').next()
        } catch(Exception ex) {
            println("Failed to get lines of text from resource $resource")
            println(ex)
        }
        return ''
    }

    private static List<String> getResourceLines(String resource) {
        def text = getResourceAsString resource
        return text.isEmpty() ? ([] as List<String>) : (Arrays.asList(text.split '\n') as List<String>)
    }

    static void initProjectFromSettings(Settings settings, String path) {
        initProjectPaths settings.rootDir, path
    }

    private static void initProjectPaths(File directory, String path) {
        if(path.startsWith ':') path = path.substring 1
        def dir = directory
        for(String name : path.split(':')) {
            if(name.isBlank()) continue
            dir = new File(dir,name)
            makeProjectFiles dir, name.startsWith('1')
        }
    }

    /**
     * Performant? Probably not
     * Does it matter? Probably not
     */
    private static String injectArgs(String line, Object ... args) {
        if(Objects.isNull(args)) return line
        for(int i=0;i<args.length;i++) {
            def arg = getArgOrNull i, args
            if(Objects.nonNull arg) line = line.replaceAll("[\$]${i+1}",String.valueOf(arg))
        }
        return line
    }

    private static void makeProjectFiles(File directory, boolean withTemplates) {
        def build = new File(directory,'build.gradle')
        if(withTemplates) makeFile build, templatePopulate(FORGE_BUILD,18)
        else makeFile build, (file) -> {}
    }

    static void makeFile(File file, Consumer<File> fileSetup) {
        file.parentFile.mkdirs()
        def fileCreated = false
        if(!file.exists()) {
            try {
                fileCreated = file.createNewFile()
            } catch(IOException ex) {
                println(ex)
                fileCreated = false
            }
        }
        if(file.exists() && fileCreated) fileSetup.accept(file)
    }

    static Consumer<File> templatePopulate(List<String> template, Object ... args) {
        println "populating template"
        return (file) -> {
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                template.forEach {
                    writer.write(injectArgs it, args)
                    writer.newLine()
                }
            } catch(Exception ex) {
                println("Failed to write file $file")
                println(ex)
            }
        }
    }
}
