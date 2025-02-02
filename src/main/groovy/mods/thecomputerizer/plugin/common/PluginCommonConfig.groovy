package mods.thecomputerizer.plugin.common

import org.gradle.api.Project

import javax.annotation.Nonnull

abstract class PluginCommonConfig extends GroovyObjectSupport {

    List<String> api = []
    List<String> api_compile = []
    boolean dev = true
    String entrypoint
    boolean local_lib = false
    boolean mixin = true
    String mod_id = 'musictriggers'
    boolean parchment = true
    String parent = ''
    String preprocessor = ''
    String refmap = ''
    String resource_output = ''
    boolean shadow = true
    boolean shared = false
    List<String> shadow_exclusions = [ '**/LICENSE', '**/LICENSE.txt', 'META-INF/MANIFSET.MF', 'META-INF/maven/**',
            'META-INF/*.RSA', 'META-INF/*.SF', 'META-INF/services/*.Processor', 'META-INF/**/*.class', 'module-info.*' ]
    Map<String,List<String>> shadow_relocations
    CommonVersions versions

    PluginCommonConfig(@Nonnull final Project project) {}

    abstract void onEvaluateFinished(Project project)

    abstract void setVersions(int major, int minor)

    void setVersions(int major) {
        setVersions(major,0)
    }
}
