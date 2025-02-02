package mods.thecomputerizer.plugin.util

import javax.annotation.Nullable

/**
 * Uncategorized helper methods
 */
class Misc {

    static @Nullable Object getArgOrNull(int index, @Nullable Object ... args) {
        return Objects.isNull(args) || args.length<=index ? null : args[index]
    }

    static boolean isNotBlank(@Nullable String value) {
        return Objects.nonNull(value) && !value.blank
    }
}
