package mods.thecomputerizer.plugin.settings

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.toolchains.foojay.FoojayToolchainsConventionPlugin

class SettingsPlugin implements Plugin<Settings> {

    static SettingsConfig getConfig(Settings settings) {
        return getConfig(settings.extensions)
    }

    static SettingsConfig getConfig(ExtensionContainer extensions) {
        return extensions.findByType(SettingsConfig)
    }

    static void init(Settings settings) {
        settings.extensions.create 'multiversion', SettingsConfig
    }

    @Override void apply(Settings settings) {
        init settings
        settings.pluginManager.apply FoojayToolchainsConventionPlugin
        println 'finished initializing multiversion helper plugin'
    }
}
