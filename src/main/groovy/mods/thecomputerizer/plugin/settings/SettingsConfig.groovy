package mods.thecomputerizer.plugin.settings

import mods.thecomputerizer.plugin.util.FileHelper
import org.gradle.api.initialization.Settings

class SettingsConfig extends GroovyObjectSupport {

    private static String buildVersionPath(String parent, String version) {
        def split = version.split '\\.'
        if(split.length<2) return parent
        def path = "${split[0]}.${split[1]}"
        return ":$parent:${(split.length==2 ? path : "$path:${path}.${split[2]}")}"
    }

    /**
     * Assumes the input string has been verified to be a minor version
     */
    private static String extractMajorVersion(String version) {
        return version.substring(0, version.lastIndexOf('.'))
    }

    private static String extractParentName(String name) {
        def index = name.indexOf '_'
        def parent = name.substring 0, index
        def version = name.substring index
        return isMinorVersion(version) ? "${parent}_${extractMajorVersion version}" : parent
    }

    private static String extractParentPath(String path) {
        return path.substring(0,path.lastIndexOf(':'))
    }

    private static boolean isMinorVersion(String version) {
        return version.count('.')>=2
    }

    private final Set<String> definedProjects = new HashSet<>()
    List<String> defaultClasses = []
    String name
    String group
    String default_preprocessor
    String default_resource_output

    SettingsConfig() {}

    private void defineLayered(Settings settings, String path, String name) {
        def layers = path.count ':'
        settings.rootProject.projectDir
        if(layers>1) defineLayered settings, extractParentPath(path), extractParentName(name)
        if(isNotDefined name) {
            FileHelper.initProjectFromSettings settings, path
            settings.include path
            settings.project(path).name = name
        }
    }

    void defineMinorVersions(Settings settings, String parent, int majorVersion, int ... minorVersions) {
        for(String version : minorVersions) defineVersion settings, parent, "$majorVersion.$version"
    }

    void defineProject(Settings settings, String name) {
        if(isNotDefined name) {
            FileHelper.initProjectFromSettings settings, name
            settings.include name
        }
    }

    void defineVersion(Settings settings, String parent, String version) {
        version = "1.$version"
        defineLayered settings, buildVersionPath(parent,version), "${parent}_$version"
    }

    void defineVersions(Settings settings, String parent, String ... versions) {
        for(String version : versions) defineVersion settings, parent, version
    }

    boolean isNotDefined(String name) {
        if(this.definedProjects.contains name) return false
        this.definedProjects.add name
        return true
    }
}