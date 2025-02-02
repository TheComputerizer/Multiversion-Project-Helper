package mods.thecomputerizer.plugin.fabric

import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import mods.thecomputerizer.plugin.common.PluginCommon
import mods.thecomputerizer.plugin.common.PluginCommonConfig
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.PluginManager

class FabricPlugin extends PluginCommon {

    @Override void addMinecraft(Project project, DependencyHandler dependencies) {}

    @Override void addRepositories(Project project) {
        addMavenModrinth project
    }

    @Override void addTIL(Project project, DependencyHandler handler, String til) {
        handler.modImplementation(til)
    }

    @Override void apply(Project project) {
        init project
        applyPlugins project.pluginManager
        configurePlugins project
        configureTasks project
    }

    void applyFabricPlugins(Project project) {
        def config = getConfig(project) as FabricPluginConfig
        def manager = project.pluginManager
        if(config.parchment) manager.apply 'org.parchmentmc.librarian.forgegradle'
        if(config.shadow) manager.apply ShadowPlugin
    }

    @Override void applyPlugins(PluginManager manager) {
        super.applyPlugins manager
    }

    @Override void buildArgs(PluginCommonConfig config, List<String> args) {
        args.add '-Dfabric.log.level=debug'
        super.buildArgs config, args
    }

    @Override void configureMixin(Project project) {}

    @Override void configurePlugins(Project project) {
        super.configurePlugins project
    }

    @Override void configureShadowTasks(Project project) {
        project.remapJar {
            inputFile = project.shadowJar.archiveFile
        }
        project.shadowJar {
            Task parentJar = getParentTask(project,'jar')
            if(Objects.nonNull parentJar) from(jarTree project, parentJar)
        }
    }

    @Override <C extends PluginCommonConfig> Class<C> getConfigType() {
        return FabricPluginConfig as Class<C>
    }

    @Override void onEvaluateFinished(Project project) {
        println 'Evaluate finished! Adding Fabric stuff'
        applyFabricPlugins project
        super.onEvaluateFinished project
    }

    @Override void setShadowOrder(Project project) {
        super.setShadowOrder project
        project.tasks.shadowJar.finalizedBy 'remapJar'
        project.tasks.jar.finalizedBy 'shadowJar'
    }
}
