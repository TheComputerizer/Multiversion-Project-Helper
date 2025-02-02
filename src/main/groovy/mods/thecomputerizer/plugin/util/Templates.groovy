package mods.thecomputerizer.plugin.util

/**
 * Yes this is a stupid way of doing it but I don't care
 */
class Templates {

    static List<String> FORGE_BUILD = [
            'plugins.apply(\'mods.thecomputerizer.plugin.forge\')',
            '',
            'multiversion {',
            '    entrypoint = mod_entrypoint',
            '    setVersions $1',
            '}',
            '',
            'sourceSets {',
            '    main.output.resourcesDir = rootProject.file(\'forge\\\\1.$1\\\\run_client\\\\MTResources\')',
            '}',
            '',
            'minecraft {',
            '    project.plugins.getPlugin(\'mods.thecomputerizer.plugin.forge\').setMinecraft(project,it)',
            '}'
    ]
}
