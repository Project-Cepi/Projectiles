package world.cepi.projectile.commands.properties

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
import net.minestom.server.utils.Vector
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.item.and
import world.cepi.kstom.item.clientData
import world.cepi.kstom.item.withMeta
import world.cepi.projectile.commands.PropertySubcommand
import world.cepi.projectile.structure.Projectile
import world.cepi.projectile.structure.heldProjectile

internal open class GeneralVectorPropertySubcommand(
    name: String,
    apply: Projectile.(Vector) -> Unit
) : Command(name) {

    init {

        addSyntax(PropertySubcommand.relativePosition) {

            if (!Projectile.hasProjectile(sender)) {
                return@addSyntax
            }

            val player = sender as Player

            val projectile = player.heldProjectile ?: return@addSyntax

            val x: Double = context[PropertySubcommand.relativePosition]["x"]
            val y: Double = context[PropertySubcommand.relativePosition]["y"]
            val z: Double = context[PropertySubcommand.relativePosition]["z"]

            val position = Vector(
                x,
                y,
                z
            )

            apply(projectile, position)

            player.itemInMainHand = player.itemInMainHand.and {
                withMeta {
                    clientData {
                        this[Projectile.projectileKey] = projectile
                    }
                }
            }

            player.sendFormattedTranslatableMessage(
                "projectile", "property.set",
                Component.text(name, NamedTextColor.BLUE),
                Component.text("$x $y $z", NamedTextColor.BLUE)
            )
        }
    }

}