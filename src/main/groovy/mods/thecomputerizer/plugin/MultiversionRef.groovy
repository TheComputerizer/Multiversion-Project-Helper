//file:noinspection unused
package mods.thecomputerizer.plugin

import mods.thecomputerizer.plugin.util.EasyDep

/**
 * Mostly version constants
 */
class MultiversionRef {

    static final BETTERWEATHER_16_5 = '3420517' //TODO These should really be registered to a map or something
    static final BETTERWEATHER_DEP = EasyDep.curse 'betterweather-400714', '1.0'

    static final BLOODMOON_12_2 = '2537917'
    static final BLOODMOON_DEP = EasyDep.curse 'bloodmoon-226321', '1.0'

    static final BLUESKIES_12_2 = '2827455'
    static final BLUESKIES_16_5 = '3411324'
    static final BLUESKIES_18_2 = '3788736'
    static final BLUESKIES_19_2 = '3966273'
    static final BLUESKIES_19_4 = '4525734'
    static final BLUESKIES_20_1 = '5010316'
    static final BLUESKIES_20_4 = '5083225'
    static final BLUESKIES_DEP = EasyDep.curse 'blueskies-312918', '1.0'

    static final BOOKSHELF_DEP = EasyDep.get 'net.darkhax.bookshelf', 'Bookshelf', '1.0'

    static final CHAMPIONS_12_2 = '3177947'
    static final CHAMPIONS_16_5 = '4297614'
    static final CHAMPIONS_18_2 = '4297619'
    static final CHAMPIONS_DEP = EasyDep.curse 'champions-307152', '1.0'

    static final CONFIGURED_16_5 = '4462830'
    static final CONFIGURED_18_2 = '4462832'
    static final CONFIGURED_19_2 = '4462837'
    static final CONFIGURED_19_4 = '4462894'
    static final CONFIGURED_DEP = EasyDep.curse 'configured-457570', '1.0'

    static final COROUTIL_DEP = EasyDep.curse 'coroutil-237749', '1.0'

    static final DYNAMIC_SURROUNDINGS_DEP = EasyDep.curse 'dsurround-238891', '1.0'

    static final ENHANCED_CELESTIALS_DEP = EasyDep.curse 'enhancedcelestials-438447', '1.0'

    static final String FABRIC_16_5 = '0.42.0+1.16'
    static final String FABRIC_18_2 = '0.77.0+1.18.2'
    static final String FABRIC_19_2 = '0.77.0+1.19.2'
    static final String FABRIC_19_4 = '0.87.2+1.19.4'
    static final String FABRIC_20_1 = '0.92.2+1.20.1'
    static final String FABRIC_20_4 = '0.97.2+1.20.4'
    static final String FABRIC_20_6 = '0.100.4+1.20.6'
    static final String FABRIC_21_1 = '0.105.0+1.21.1'

    static final String FORGE_12_2 = '14.23.5.2860'
    static final String FORGE_16_5 = '36.2.42'
    static final String FORGE_18_2 = '40.3.6'
    static final String FORGE_19_2 = '43.4.20'
    static final String FORGE_19_4 = '45.3.24'
    static final String FORGE_20_1 = '47.3.33'
    static final String FORGE_20_4 = '49.1.37'
    static final String FORGE_20_6 = '50.1.48'
    static final String FORGE_21_1 = '52.0.53'

    static final EasyDep FORGE_DEP = EasyDep.get 'net.minecraftforge', 'forge', '1.0'

    static final GAME_STAGES_DEP = EasyDep.get 'net.darkhax.gamestages', 'GameStages', '1.0'

    static final INFERNAL_MOBS_DEP = EasyDep.curse 'infernalmobs-227875', '1.0'

    static final String LOMBOK_VERSION = '1.+'
    static final EasyDep LOMBOK = EasyDep.get 'org.projectlombok', 'lombok', LOMBOK_VERSION

    static final String MC_12_2 = '1.12.2'
    static final String MC_16_5 = '1.16.5'
    static final String MC_18_2 = '1.18.2'
    static final String MC_19_2 = '1.19.2'
    static final String MC_19_4 = '1.19.4'
    static final String MC_20_1 = '1.20.1'
    static final String MC_20_4 = '1.20.4'
    static final String MC_20_6 = '1.20.6'
    static final String MC_21_1 = '1.21.1'

    static final String NEOFORGE_20_1 = '1.20.1'
    static final String NEOFORGE_20_4 = '1.20.4'
    static final String NEOFORGE_20_6 = '1.20.6'
    static final String NEOFORGE_21_1 = '1.21.1'

    static final NYX_DEP = EasyDep.curse 'nyx-349214', '1.0'

    static final ORELIB_DEP = EasyDep.curse 'orelib-307806', '1.0'

    static final String PARCHMENT_16_5 = '2022.03.06'
    static final String PARCHMENT_18_2 = '2022.11.06'
    static final String PARCHMENT_19_2 = '2022.11.27'
    static final String PARCHMENT_19_4 = '2023.06.26'
    static final String PARCHMENT_20_1 = '2023.09.03'
    static final String PARCHMENT_20_4 = '2024.04.14'
    static final String PARCHMENT_20_6 = '2024.06.16'
    static final String PARCHMENT_21_1 = '2024.11.17'

    static final String FABRIC_MAPPINGS_16_5 = "$MC_16_5:$PARCHMENT_16_5"
    static final String FABRIC_MAPPINGS_18_2 = "$MC_18_2:$PARCHMENT_18_2"
    static final String FABRIC_MAPPINGS_19_2 = "$MC_19_2:$PARCHMENT_19_2"
    static final String FABRIC_MAPPINGS_19_4 = "$MC_19_4:$PARCHMENT_19_4"
    static final String FABRIC_MAPPINGS_20_1 = "$MC_20_1:$PARCHMENT_20_1"
    static final String FABRIC_MAPPINGS_20_4 = "$MC_20_4:$PARCHMENT_20_4"
    static final String FABRIC_MAPPINGS_20_6 = "$MC_20_6:$PARCHMENT_20_6"
    static final String FABRIC_MAPPINGS_21_1 = "$MC_21_1:$PARCHMENT_21_1"

    static final String FORGE_MAPPINGS_12_2 = '39-1.12'
    static final String FORGE_MAPPINGS_16_5 = "$PARCHMENT_16_5-$MC_16_5"
    static final String FORGE_MAPPINGS_18_2 = "$PARCHMENT_18_2-$MC_18_2"
    static final String FORGE_MAPPINGS_19_2 = "$PARCHMENT_19_2-$MC_19_2"
    static final String FORGE_MAPPINGS_19_4 = "$PARCHMENT_19_4-$MC_19_4"
    static final String FORGE_MAPPINGS_20_1 = "$PARCHMENT_20_1-$MC_20_1"
    static final String FORGE_MAPPINGS_20_4 = "$PARCHMENT_20_4-$MC_20_4"
    static final String FORGE_MAPPINGS_20_6 = "$PARCHMENT_20_6-$MC_20_6"
    static final String FORGE_MAPPINGS_21_1 = "$PARCHMENT_21_1-$MC_21_1"

    static final def PLUGIN_GROUPS = [ 'com.fasterxml', 'com.fasterxml.jackson', 'com.fasterxml.woodstox',
            'com.google.code.findbugs', 'com.google.code.gson', 'com.google.errorprone', 'com.google.guava',
            'com.google.j2objc', 'com.machinezoo.noexception', 'commons-codec', 'commons-io', 'commons-logging',
            'de.siegmar', 'io.fabric8', 'io.github.goooler.shadow', 'io.netty', 'it.unimi.dsi', 'jakarta.platform',
            'net.sf.jopt-simple', 'org.apache', 'org.apache.ant', 'org.apache.commons', 'org.apache.httpcomponents',
            'org.apache.logging', 'org.apache.logging.log4j', 'org.apache.maven', 'org.checkerframework',
            'org.codehaus.groovy', 'org.codehaus.plexus', 'org.codehaus.woodstox', 'org.eclipse.ee4j',
            'org.eclipse.jetty', 'org.jdom', 'org.junit', 'org.mockito', 'org.ow2', 'org.ow2.asm', 'org.slf4j',
            'org.sonatype.oss', 'org.springframework', 'org.tukaani', 'org.vafer'
    ]

    static final SERENE_SEASONS_DEP = EasyDep.curse 'sereneseasons-291874', '1.0'

    static final STRUCTURE_GEL_DEP = EasyDep.curse 'structure-gel-378802', '1.0'

    static final String TIL_VERSION = '0.4.0'
    static final EasyDep TIL = EasyDep.get 'mods.thecomputerizer.theimpossiblelibrary','theimpossiblelibrary',TIL_VERSION

    static final WEATHER_2_DEP = EasyDep.curse 'weather2-237746', '1.0'
}