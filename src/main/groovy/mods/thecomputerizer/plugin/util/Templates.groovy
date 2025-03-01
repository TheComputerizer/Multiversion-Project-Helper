package mods.thecomputerizer.plugin.util

import mods.thecomputerizer.plugin.settings.SettingsConfig

/**
 * Yes this is a stupid way of doing it but I don't care
 */
class Templates {

    static final String FORGE_BUILD = '''plugins.apply 'mods.thecomputerizer.plugin.forge'

multiversion {
    api = [$3]
    entrypoint = mod_entrypoint
    local_lib = true
    parent = '$2'
    preprocessor = '$4'
    resource_output = '$5'
    setVersions $1
    shadow_relocations = [ "$shadow_package": relocate_these.split(',').toList()]
    onEvaluateFinished project
}'''

    static Object[] getTemplateArgs(File directory, SettingsConfig config) {
        def name = directory.name
        def split = name.split '\\.'
        def first = split.length>2 ? "${split[1]}, ${split[2]}" : split[1]
        def parent = directory.parentFile
        def second = split.length==1 ? '' : (split.length==2 ? parent.name : "${parent.parentFile.name}_${parent.name}")
        def third = getThirdArg directory, name, split
        def fourth = config.default_preprocessor
        def fifth = config.default_resource_output
        return [ first, second, third, fourth, fifth ]
    }

    static Object getThirdArg(File directory, String name, String ... split) {
        switch(split.length) {
            case 1: return name=='api' ? '' : ' \'api\'\' '
            case 2: {
                StringJoiner joiner = new StringJoiner(', ')
                joiner.add '\'api\''
                String parent = directory.parentFile.name
                joiner.add "\'$parent\'"
                if(parent!='shared' && parent!='legacy') joiner.add "\'shared:shared_$name\'"
                return " $joiner "
            }
            case 3: {
                StringJoiner joiner = new StringJoiner(', ','\'','\'')
                joiner.add '\'api\'\''
                String loader = directory.parentFile.parentFile.name
                String parent = directory.parentFile.name
                joiner.add "\'$loader:${loader}_$parent\'"
                if(loader!='shared' && loader!='legacy') joiner.add "\'shared:shared_$parent:shared_$name\'"
                return " $joiner "
            }
            default: return ''
        }
    }
}