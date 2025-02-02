package mods.thecomputerizer.plugin.forge

import mods.thecomputerizer.plugin.common.PluginCommon
import mods.thecomputerizer.plugin.common.PluginCommonConfig
import mods.thecomputerizer.plugin.common.CommonVersions
import mods.thecomputerizer.plugin.util.EasyDep
import net.minecraftforge.gradle.common.util.RunConfig
import net.minecraftforge.gradle.userdev.UserDevExtension
import net.minecraftforge.gradle.userdev.UserDevPlugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginManager
import wtf.gofancy.fancygradle.FancyExtension
import wtf.gofancy.fancygradle.FancyGradle

class ForgePlugin extends PluginCommon {

    static void configureRun(Project project, RunConfig run, String name, List<String> args) {
        run.workingDirectory project.file("run_$name".toString())
        run.property 'forge.logging.markers', 'SCAN,REGISTRIES,REGISTRYDUMP'
        run.property 'forge.logging.console.level', 'debug'
        run.jvmArgs args
    }

    static void setFancyGradle(FancyExtension extension) {
        extension.patches.tap {
            coremods
            asm
            mergetool
        }
    }

    @Override
    void addMinecraft(Project project, CommonVersions versions) {
        def dep = EasyDep.get('net.minecraftforge','forge',"${versions.minecraft}-${((ForgeVersions)versions).forge}")
        project.dependencies {
            println "adding minecraft ${dep.get()}"
            minecraft dep.get()
        }
    }

    @Override
    void apply(Project project) {
        init project, ForgePluginConfig
        applyPlugins project
        applyRuntime project
        applyLib project
        applyShadow project
        project.afterEvaluate {
            configurePlugins project
            configureTasks project
            addMinecraft project, getConfig(project).versions
        }
    }

    void applyPlugins(Project project) {
        PluginManager manager = project.pluginManager
        applyPlugins manager
        def config = project.extensions.getByType(ForgePluginConfig)
        if(config.mixin) manager.apply 'org.spongepowered.mixin'
        if(config.parchment) manager.apply 'org.parchmentmc.librarian.forgegradle'
    }

    @Override
    void applyPlugins(PluginManager manager) {
        super.applyPlugins manager
        manager.apply UserDevPlugin
    }

    @Override
    void buildArgs(PluginCommonConfig config, List<String> args) {
        args.add '-Dtil.classpath.file=theimpossiblelibrary-0.4.0_mapped_parchment_2022.03.06-1.16.5.jar'
        super.buildArgs config, args
    }

    @Override
    void configurePlugins(Project project) {
        super.configurePlugins project
        if(project.extensions.findByType(ForgePluginConfig).fancy_gradle) {
            project.pluginManager.apply FancyGradle
            setFancyGradle project.extensions.getByType(FancyExtension)
        }
    }

    void setMinecraft(Project project, UserDevExtension minecraft) {
        ExtensionContainer extensions = project.extensions
        def config = extensions.findByType(ForgePluginConfig)
        def args = buildArgs(config)
        minecraft.tap {
            mappings channel: config.mappings_channel, version: config.versions.mappings
            if(!config.at.isEmpty()) accessTransformer = resourceFile(project,"META-INF\\${config.at}".toString())
            runs {
                client {
                    configureRun project, it, 'client', args
                }
                server {
                    configureRun project, it, 'server', args
                }
            }
        }
    }
}
