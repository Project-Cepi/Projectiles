package world.cepi.projectile.commands.properties

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Player
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.item.and
import world.cepi.kstom.item.set
import world.cepi.kstom.item.withMeta
import world.cepi.projectile.commands.PropertySubcommand
import world.cepi.projectile.structure.Projectile
import world.cepi.projectile.structure.heldProjectile

internal open class GeneralVectorPropertySubcommand(
    name: String,
    apply: Projectile.(Vec) -> Unit
) : Command(name) {

    init {

        val oneAmount = ArgumentType.Double("amount").min(0.0).max(100.0)

        addSyntax(PropertySubcommand.relativePosition) {

            if (!Projectile.hasProjectile(sender)) {
                return@addSyntax
            }

            val player = sender as Player

            val projectile = player.heldProjectile ?: return@addSyntax

            val x: Double = context[PropertySubcommand.relativePosition]["x"]
            val y: Double = context[PropertySubcommand.relativePosition]["y"]
            val z: Double = context[PropertySubcommand.relativePosition]["z"]

            val position = Vec(x, y, z)

            apply(projectile, position)

            player.itemInMainHand = player.itemInMainHand.and {
                withMeta {
                    this[Projectile.projectileKey] = projectile
                }
            }

            player.sendFormattedTranslatableMessage(
                "projectile", "property.set",
                Component.text(name, NamedTextColor.BLUE),
                Component.text("$x $y $z", NamedTextColor.BLUE)
            )
        }

        addSyntax(oneAmount) {

            if (!Projectile.hasProjectile(sender)) {
                return@addSyntax
            }

            val player = sender as Player

            val projectile = player.heldProjectile ?: return@addSyntax

            val amount = context[oneAmount]

            val position = Vec(amount, amount, amount)

            apply(projectile, position)

            player.itemInMainHand = player.itemInMainHand.and {
                withMeta {
                    this[Projectile.projectileKey] = projectile
                }
            }

            player.sendFormattedTranslatableMessage(
                "projectile", "property.set",
                Component.text(name, NamedTextColor.BLUE),
                Component.text(amount, NamedTextColor.BLUE)
            )
        }
    }

}