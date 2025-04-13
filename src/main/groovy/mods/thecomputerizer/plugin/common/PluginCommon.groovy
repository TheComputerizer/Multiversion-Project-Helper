package mods.thecomputerizer.plugin.common

import mods.thecomputerizer.plugin.util.Misc
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.file.FileTree
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.PluginManager
import org.gradle.api.tasks.AbstractCopyTask
import org.gradle.api.tasks.compile.CompileOptions
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.tasks.Jar
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.language.jvm.tasks.ProcessResources
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.plugins.ide.idea.model.IdeaModel

import javax.annotation.Nullable
import java.util.function.Function

import static mods.thecomputerizer.plugin.MultiversionRef.LOMBOK_VERSION
import static mods.thecomputerizer.plugin.MultiversionRef.TIL
import static org.gradle.api.file.DuplicatesStrategy.EXCLUDE

abstract class PluginCommon implements Plugin<Project> {

    static void addLocalFolder(Project project, String path) {
        project.repositories.tap {
            flatDir {
                dirs "${project.rootDir}\\$path"
            }
        }
    }

    static void addLombok(Project project, DependencyHandler dependencies) {
        addMavenCentral project, 'org.projectlombok'
        dependencies.tap {
            annotationProcessor "org.projectlombok:lombok:$LOMBOK_VERSION"
            compileOnly "org.projectlombok:lombok:$LOMBOK_VERSION"
        }
    }

    static void addMaven(Project project, String name, String uri, String ... groups) {
        project.repositories.maven {
            it.name = name
            it.url = project.uri uri
            if(Objects.nonNull groups && groups.length>0) {
                content {
                    for(def group : groups) it.includeGroup group
                }
            }
        }
    }

    static void addMavenCentral(Project project, String ... groups) {
        if(Objects.isNull groups || groups.length==0) {
            project.repositories { mavenCentral() }
        }
        else {
            project.repositories {
                mavenCentral() {
                    it.content {
                        for(def group : groups) it.includeGroup group
                    }
                }
            }
        }
    }

    static void addMavenBlameJared(Project project, String ... groups) {
        addMaven project, 'BlameJared', 'https://maven.blamejared.com', groups
    }

    static void addMavenCurse(Project project) {
        addMaven project, 'CurseMaven', 'https://curse.cleanroommc.com', 'curse.maven'
    }

    static void addMavenModrinth(Project project) {
        addMaven project, 'Modrinth', 'https://api.modrinth.com/maven', 'maven.modrinth'
    }

    static void addMavenProgwml6(Project project, String ... groups) {
        addMaven project, 'Progwml6 Maven', 'https://dvs1.progwml6.com/files/maven/', groups
    }

    static void idea(ExtensionContainer extensions) {
        extensions.findByType(IdeaModel).module {
            downloadJavadoc = true
            downloadSources = true
            inheritOutputDirs
        }
    }

    static List<FileTree> jarTree(Project project, Task task) {
        return task instanceof Jar ? (task as Jar).archiveFile.collect { project.zipTree(it) } : []
    }

    static String resourceFile(Project project, String path) {
        return project.file("src\\main\\resources\\$path")
    }

    static File rootFile(Project project, String path) {
        return project.rootProject.file(path)
    }

    void addExtraDependencies(Project project, DependencyHandler dependencies) {
        addLombok project, dependencies
        addTIL project, getConfig(project).local_lib
    }

    abstract void addMinecraft(Project project, DependencyHandler dependencies)

    void addParentAPIs(Project project, DependencyHandler dependencies) {
        PluginCommonConfig config = getConfig project
        dependencies.tap {d ->
            config.api.forEach { d.api project.project(":$it")}
            config.api_compile.forEach { d.compileOnlyApi project.project(":$it")}
        }
    }

    abstract void addRepositories(Project project)

    void addTIL(Project project, boolean local) {
        if(local) addLocalFolder project, 'libs'
        project.dependencies {
            addTIL project, it, TIL.get()
        }
    }

    abstract void addTIL(Project project, DependencyHandler handler, String til)

    void applyFrom(Project project, Function<PluginCommonConfig,Boolean> func, String name) {
        if(func.apply getConfig(project)) project.apply from: rootFile(project,"${name}.gradle")
    }

    void applyPlugins(PluginManager manager) {
        manager.apply 'java'
        manager.apply 'java-base'
        manager.apply 'java-library'
        manager.apply IdeaPlugin
    }

    void buildArgs(PluginCommonConfig config, List<String> args) {
        if(config.dev) args.add '-Dtil.dev=true'
        if(!config.entrypoint.isEmpty()) args.add "-Dtil.classpath.mods=${config.entrypoint}".toString()
        args.add '-Dtil.classpath.coremods=mods.thecomputerizer.musictriggers.api.core.MTCoreEntryPoint'
    }

    void applyPreprocessor(Project project) {
        def preprocessor = getConfig(project).preprocessor
        applyFrom project, (config) -> Misc.isNotBlank(preprocessor), preprocessor
    }

    List<String> buildArgs(PluginCommonConfig config) {
        List<String> args = new ArrayList<>()
        buildArgs config, args
        return args.asImmutable()
    }

    abstract void configureMixin(Project project)

    void configurePlugins(Project project) {
        ExtensionContainer extensions = project.extensions
        java extensions
        idea extensions
    }

    abstract void configureShadowTasks(Project project)

    void configureTasks(Project project) {
        def tasks = project.tasks
        tasks.withType(Jar).configureEach {
            it.duplicatesStrategy = EXCLUDE
            it.archiveBaseName
        }
        tasks.withType(JavaCompile).configureEach {
            CompileOptions options = it.options
            options.deprecation = false
            options.encoding = 'UTF-8'
        }
        tasks.withType(ProcessResources).configureEach {
            it.duplicatesStrategy = EXCLUDE
        }
        setResourceOutput project
        inheritParentOutputs project,'processResources'
        inheritParentOutputs project,'processTestResources'
        if(isMixin project) configureMixin project
        if(isShadow project) {
            initShadow project
            configureShadowTasks project
        }
        setTaskOrder project
    }

    final void evaluateDependencies(Project project) {
        project.dependencies {
            addMinecraft project, it
            addParentAPIs project, it
            addExtraDependencies project, it
        }
    }

    <C extends PluginCommonConfig> C getConfig(Project project) {
        return getConfig(project.extensions)
    }

    <C extends PluginCommonConfig> C getConfig(ExtensionContainer extensions) {
        return extensions.getByType(configType)
    }

    abstract <C extends PluginCommonConfig> Class<C> getConfigType()

    @Nullable Project getParent(Project project) {
        def name = getParentName project
        return Misc.isNotBlank(name) ? project.project(":$name") : null
    }

    String getParentName(Project project) {
        return getConfig(project).parent
    }

    @Nullable Task getParentTask(Project project, String name) {
        Project parent = getParent project
        return Objects.nonNull(parent) ? parent.tasks.named(name).get() : null
    }

    boolean hasParent(Project project) {
        return Misc.isNotBlank(getParentName(project))
    }

    void inheritParentOutputs(Project project, String taskName) {
        Task task = project.tasks.named(taskName).get()
        if(task instanceof AbstractCopyTask) {
            Task parentTask = getParentTask(project,taskName)
            if(Objects.nonNull parentTask) task.from parentTask.outputs
        }
    }

    void init(Project project) {
        ExtensionContainer extensions = project.extensions
        extensions.create 'multiversion', configType, project
        applyPlugins project.pluginManager
    }

    @SuppressWarnings('GroovyAssignabilityCheck')
    void initShadow(Project project) {
        PluginCommonConfig config = getConfig project
        project.configurations {
            shadow.extendsFrom(shaded)
        }
        project.shadowJar {
            duplicatesStrategy = EXCLUDE
            configurations = [project.configurations.shadow]
            setArchiveBaseName config.mod_id
            setArchiveClassifier ''
            mergeServiceFiles()
            exclude config.shadow_exclusions
            def relocations = config.shadow_relocations
            if(Objects.nonNull relocations) {
                relocations.each { k,v ->
                    v.forEach { pkg -> { relocate pkg, "$k.${pkg}" } }
                }
            }
        }
    }

    boolean isMixin(Project project) {
        return getConfig(project).mixin
    }

    boolean isShadow(Project project) {
        return getConfig(project).shadow
    }

    void java(ExtensionContainer extensions) {
        def version = JavaLanguageVersion.of getConfig(extensions).versions.java
        extensions.findByType(JavaPluginExtension).toolchain.languageVersion.set version
    }

    void onEvaluateFinished(Project project) {
        applyPreprocessor project
        evaluateDependencies project
    }

    void setResourceOutput(Project project) {
        def out = getConfig(project).resource_output
        if(Misc.isNotBlank(out)) {
            project.sourceSets {
                main.output.resourcesDir = project.file(out)
            }
        }
    }

    void setShadowOrder(Project project) {
        project.tasks.assemble.dependsOn 'shadowJar'
    }

    void setTaskOrder(Project project) {
        PluginCommonConfig config = getConfig project
        if(config.shadow) setShadowOrder project
        if(Misc.isNotBlank(config.parent)) project.tasks.jar.dependsOn ":${config.parent}:build"
    }
}