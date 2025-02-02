package mods.thecomputerizer.plugin.forge

import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import mods.thecomputerizer.plugin.common.PluginCommon
import mods.thecomputerizer.plugin.common.PluginCommonConfig
import mods.thecomputerizer.plugin.util.Misc
import net.minecraftforge.gradle.common.util.RunConfig
import net.minecraftforge.gradle.userdev.DependencyManagementExtension
import net.minecraftforge.gradle.userdev.UserDevPlugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.PluginManager
import wtf.gofancy.fancygradle.FancyExtension
import wtf.gofancy.fancygradle.FancyGradle

import static mods.thecomputerizer.plugin.MultiversionRef.FORGE_DEP

class ForgePlugin extends PluginCommon {

    static void configureRun(Project project, RunConfig run, String name, List<String> args) {
        run.workingDirectory project.file("run_$name".toString())
        run.property 'forge.logging.markers', 'SCAN,REGISTRIES,REGISTRYDUMP'
        run.property 'forge.logging.console.level', 'debug'
        run.jvmArgs args
    }

    @Override void addExtraDependencies(Project project, DependencyHandler dependencies) {
        super.addExtraDependencies project, dependencies
    }

    @Override void addMinecraft(Project project, DependencyHandler dependencies) {
        def versions = getConfig(project).versions as ForgeVersions
        def dep = FORGE_DEP.asVersion("${versions.minecraft}-${versions.forge}").get()
        dependencies.tap {
            println "adding minecraft $dep"
            minecraft dep
        }
    }

    @Override void addRepositories(Project project) {
        addMavenCurse project
        addMavenBlameJared project, 'net.darkhax.bookshelf', 'net.darkhax.gamestages'
        addMavenProgwml6 project, 'mezz.jei'
    }

    @Override void addTIL(Project project, DependencyHandler handler, String til) {
        def dep = project.extensions.getByType(DependencyManagementExtension).deobf(til)
        handler.implementation(dep)
    }

    @Override void apply(Project project) {
        init project
        project.afterEvaluate {
            configurePlugins it
            configureTasks it
            addRepositories it
        }
    }

    void applyForgePlugins(Project project) {
        def config = getConfig(project) as ForgePluginConfig
        def manager = project.pluginManager
        manager.apply UserDevPlugin
        if(config.mixin) manager.apply 'org.spongepowered.mixin'
        if(config.parchment) manager.apply 'org.parchmentmc.librarian.forgegradle'
        if(config.fancy_gradle) {
            manager.apply FancyGradle
            println 'applied FancyGradle plugin I think'
        }
        if(config.shadow) manager.apply ShadowPlugin
    }

    @Override void applyPlugins(PluginManager manager) {
        super.applyPlugins manager
    }

    @Override void buildArgs(PluginCommonConfig config, List<String> args) {
        args.add '-Dtil.classpath.file=theimpossiblelibrary-0.4.0_mapped_parchment_2022.03.06-1.16.5.jar'
        super.buildArgs config, args
    }

    @Override void configureMixin(Project project) {
        def refmap = getConfig(project).refmap
        if(Misc.isNotBlank(refmap)) {
            project.mixin {
                add project.sourceSets.main, refmap
            }
        }
    }

    @Override void configurePlugins(Project project) {
        super.configurePlugins project
        def config = getConfig(project) as ForgePluginConfig
        if(config.fancy_gradle) {
            project.extensions.getByType(FancyExtension).tap {
                patches.tap {
                    coremods
                    asm
                    mergetool
                }
            }
        }
    }

    @Override void configureShadowTasks(Project project) {
        project.reobf {
            shadowJar {
                dependsOn project.createMcpToSrg
                mappings = project.createMcpToSrg.output
            }
        }
        project.shadowJar {
            Task parentJar = getParentTask(project,'jar')
            if(Objects.nonNull parentJar) from(jarTree project, parentJar)
        }
    }

    @Override <C extends PluginCommonConfig> Class<C> getConfigType() {
        return ForgePluginConfig as Class<C>
    }

    @Override void onEvaluateFinished(Project project) {
        println 'Evaluate finished! Adding Forge stuff'
        applyForgePlugins project
        setMinecraft project
        super.onEvaluateFinished project
    }

    void setMinecraft(Project project) {
        def config = getConfig project
        def args = buildArgs(config)
        project.minecraft {
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

    @Override void setShadowOrder(Project project) {
        super.setShadowOrder project
        project.tasks.shadowJar.dependsOn 'reobfJar'
        project.tasks.jar.finalizedBy 'reobfJar'
    }
}
