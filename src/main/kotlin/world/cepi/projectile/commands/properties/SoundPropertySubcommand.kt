package world.cepi.projectile.commands.properties

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentEnum
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.command.arguments.defaultValue
import world.cepi.kstom.item.and
import world.cepi.kstom.item.clientData
import world.cepi.kstom.item.withMeta
import world.cepi.projectile.Projectile
import world.cepi.projectile.heldProjectile

internal object SoundPropertySubcommand : Command("sound") {
    init {
        val sound = ArgumentType.String("soundName").map {
            Key.key(it)
        }

        val source = ArgumentType.Enum("source", Sound.Source::class.java)
            .setFormat(ArgumentEnum.Format.LOWER_CASED)
            .setDefaultValue(Sound.Source.MASTER)

        val volume = ArgumentType.Float("volume").min(0f).max(2f).defaultValue(1f)
        val pitch = ArgumentType.Float("pitch").min(0f).max(2f).defaultValue(1f)

        addSyntax(sound, source, volume, pitch) {
            if (!Projectile.hasProjectile(sender)) {
                return@addSyntax
            }

            val player = sender as Player

            val projectile = player.heldProjectile ?: return@addSyntax

            projectile.sound = Sound.sound(
                context[sound], context[source],
                context[volume], context[pitch]
            )

            player.itemInMainHand = player.itemInMainHand.and {
                withMeta {
                    clientData {
                        this[Projectile.projectileKey] = projectile
                    }
                }
            }
        }
    }
}