package mods.thecomputerizer.plugin.forge

import mods.thecomputerizer.plugin.common.PluginCommonConfig
import mods.thecomputerizer.plugin.util.VersionHelper
import org.gradle.api.Project

import javax.annotation.Nonnull

class ForgePluginConfig extends PluginCommonConfig {

    String at = ''
    boolean fancy_gradle = false
    String mappings_channel = 'parchment'
    ForgeVersions versions

    ForgePluginConfig(@Nonnull final Project project) {
        super(project)
    }

    @Override void onEvaluateFinished(Project project) {
        project.plugins.getPlugin(ForgePlugin).onEvaluateFinished project
    }

    @Override void setVersions(int major, int minor) {
        this.versions = VersionHelper.getForgeVersions(major,minor)
    }
}
