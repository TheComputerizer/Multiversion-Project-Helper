package mods.thecomputerizer.plugin.util

import mods.thecomputerizer.plugin.settings.SettingsConfig
import org.gradle.api.initialization.Settings

import java.util.function.Consumer

import static mods.thecomputerizer.plugin.util.Templates.FORGE_BUILD

class FileHelper {

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
        initProjectPaths settings.rootDir, path, settings.extensions.getByType(SettingsConfig)
    }

    private static void initProjectPaths(File directory, String path, SettingsConfig config) {
        if(path.startsWith ':') path = path.substring 1
        def dir = directory
        for(String name : path.split(':')) {
            if(name.isBlank()) continue
            dir = new File(dir,name)
            makeProjectFiles dir, name.startsWith('1'), config
        }
    }

    /**
     * Performant? Probably not
     * Does it matter? Probably not
     */
    private static String injectArgs(String line, Object ... args) {
        if(Objects.isNull(args)) return line
        for(int i=0;i<args.length;i++) {
            def arg = Misc.getArgOrNull i, args
            if(Objects.nonNull arg) line = line.replaceAll("[\$]${i+1}",String.valueOf(arg))
        }
        return line
    }

    private static void makeProjectFiles(File directory, boolean withTemplates, SettingsConfig config) {
        def build = new File(directory,'build.gradle')
        if(withTemplates) makeFile build, templatePopulate(FORGE_BUILD,Templates.getTemplateArgs(directory,config))
        else makeFile build, (file) -> {}
        makeDirectories directory, config
    }

    private static void makeDirectories(File root, SettingsConfig config) {
        File dir = makeDirectory root,'src'
        makeDirectory dir,'test\\java'
        makeDirectory dir,'test\\resources'
        dir = makeDirectory dir, 'main'
        makeDirectory dir, 'resources\\META-INF'
        dir = makeDirectory dir, "java\\${config.group.replaceAll('\\.','\\\\')}"
        makeVersionDir root, dir, config
    }

    private static void makeVersionDir(File root, File dir, SettingsConfig config) {
        def name = root.name
        String[] split = name.split '\\.'
        if(split.length==1) makeDirectory dir, name
        else {
            dir = makeDirectory dir, split.length==2 ? root.parentFile.name : root.parentFile.parentFile.name
            def major = split[1]
            dir = makeDirectory dir, "v$major"
            makeDirectory dir, "m${split.length==3 ? split[2] : guessMinorVersion(major)}"
        }
    }

    private static String guessMinorVersion(String major) {
        switch(major) {
            case '12': return '2'
            case '16': return '5'
            case '18': return '2'
            case '19': return '4'
            case '20': return '1'
            case '21': return '1'
            default: return '0'
        }
    }

    private static File makeDirectory(File parent, String path) {
        def dir = new File(parent,path)
        dir.mkdirs()
        return dir
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

    static Consumer<File> templatePopulate(String template, Object ... args) {
        return (file) -> {
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(injectArgs template, args)
            } catch(Exception ex) {
                println("Failed to write file $file")
                println(ex)
            }
        }
    }
}
