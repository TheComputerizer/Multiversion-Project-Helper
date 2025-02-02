package mods.thecomputerizer.plugin.fabric

import mods.thecomputerizer.plugin.common.PluginCommonConfig
import mods.thecomputerizer.plugin.util.VersionHelper
import org.gradle.api.Project

import javax.annotation.Nonnull

class FabricPluginConfig extends PluginCommonConfig {

    String log_level = "debug"
    FabricVersions versions

    FabricPluginConfig(@Nonnull final Project project) {
        super(project)
    }

    @Override void onEvaluateFinished(Project project) {
        project.plugins.getPlugin(FabricPlugin).onEvaluateFinished project
    }

    @Override void setVersions(int major, int minor) {
        this.versions = VersionHelper.getFabricVersions(major,minor)
    }
}
