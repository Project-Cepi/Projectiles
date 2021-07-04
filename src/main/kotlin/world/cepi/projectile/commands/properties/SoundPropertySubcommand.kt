package world.cepi.projectile.commands.properties

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.arguments.ArgumentEnum
import net.minestom.server.command.builder.arguments.ArgumentType
import world.cepi.kstom.command.arguments.defaultValue

internal object SoundPropertySubcommand : GeneralSingleArgumentPropertySubcommand<CommandContext>(
    "sound",
    { sound = Sound.sound(
        it.get<Key>("soundName"), it.get<Sound.Source>("source"),
        it["volume"], it["pitch"]
    ) },
    ArgumentType.Group(
        "fullSound",
        ArgumentType.String("soundName").map {
            Key.key(it)
        },
        ArgumentType.Enum("source", Sound.Source::class.java)
            .setFormat(ArgumentEnum.Format.LOWER_CASED)
            .setDefaultValue(Sound.Source.MASTER),
        ArgumentType.Float("volume").min(0f).max(2f).defaultValue(1f),
        ArgumentType.Float("pitch").min(0f).max(2f).defaultValue(1f)
    )
)