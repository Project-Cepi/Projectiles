package world.cepi.projectile.commands.properties

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.arguments.Argument
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.kommand.Kommand
import world.cepi.kstom.item.and
import world.cepi.kstom.item.set
import world.cepi.kstom.item.withMeta
import world.cepi.projectile.structure.Projectile
import world.cepi.projectile.structure.heldProjectile

internal open class GeneralSingleArgumentPropertySubcommand<T>(
    name: String,
    apply: Projectile.(T) -> Unit,
    argument: Argument<T>
) : Kommand({

    syntax(argument).onlyPlayers {

        if (!Projectile.hasProjectile(sender)) {
            return@onlyPlayers
        }

        val projectile = player.heldProjectile ?: return@onlyPlayers

        val genArg = context[argument]

        apply(projectile, genArg)

        player.itemInMainHand = player.itemInMainHand.and {
            withMeta {
                this[Projectile.projectileKey] = projectile
            }
        }

        player.sendFormattedTranslatableMessage(
            "projectile", "property.set",
            Component.text(name, NamedTextColor.BLUE),
            Component.text(genArg.toString(), NamedTextColor.BLUE)
        )
    }


}, name)