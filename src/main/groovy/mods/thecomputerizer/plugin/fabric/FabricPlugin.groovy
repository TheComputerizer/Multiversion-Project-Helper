package mods.thecomputerizer.plugin.fabric

import mods.thecomputerizer.plugin.common.PluginCommon
import mods.thecomputerizer.plugin.common.PluginCommonConfig
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager

class FabricPlugin extends PluginCommon {

    @Override
    void apply(Project project) {
        init project, FabricPluginConfig
        applyPlugins project.pluginManager
        configurePlugins project
        configureTasks project
    }

    @Override
    void applyPlugins(PluginManager manager) {
        super.applyPlugins manager
    }

    @Override
    void buildArgs(PluginCommonConfig config, List<String> args) {
        args.add '-Dfabric.log.level=debug'
        super.buildArgs config, args
    }

    @Override
    void configurePlugins(Project project) {
        super.configurePlugins project
    }
}
