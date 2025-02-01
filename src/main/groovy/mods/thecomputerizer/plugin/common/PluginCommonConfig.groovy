package mods.thecomputerizer.plugin.common

import org.gradle.api.Project

import javax.annotation.Nonnull

abstract class PluginCommonConfig extends GroovyObjectSupport {

    boolean dev = true
    String entrypoint
    boolean lib = false
    boolean mixin = true
    String mod_id = 'musictriggers'
    boolean parchment = true
    boolean runtime = true
    boolean shadow = true
    PluginCommonVersions versions

    PluginCommonConfig(@Nonnull final Project project) {}

    abstract void setVersions(int major, int minor)

    void setVersions(int major) {
        setVersions(major,0)
    }
}
