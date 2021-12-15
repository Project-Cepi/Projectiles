package world.cepi.projectile.commands.properties

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Player
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.kommand.Kommand
import world.cepi.kstom.item.and
import world.cepi.kstom.item.set
import world.cepi.kstom.item.withMeta
import world.cepi.projectile.commands.PropertySubcommand
import world.cepi.projectile.structure.Projectile
import world.cepi.projectile.structure.heldProjectile

internal open class GeneralVectorPropertySubcommand(
    name: String,
    apply: Projectile.(Vec) -> Projectile,
    grab: Projectile.() -> Vec
) : Kommand({

    val oneAmount = ArgumentType.Double("amount").min(0.0).max(100.0)

    default {
        if (!Projectile.hasProjectile(sender)) {
            return@default
        }

        val position = grab(player.heldProjectile!!)

        player.sendFormattedTranslatableMessage(
            "projectile", "property.grab.vec",
            Component.text(name.replaceFirstChar { it.uppercase() }, NamedTextColor.GRAY),
            Component.text(position.x(), NamedTextColor.RED),
            Component.text(position.y(), NamedTextColor.GREEN),
            Component.text(position.z(), NamedTextColor.BLUE),
        )
    }

    syntax(PropertySubcommand.relativePosition).onlyPlayers {

        if (!Projectile.hasProjectile(sender)) {
            return@onlyPlayers
        }

        val x: Double = context[PropertySubcommand.relativePosition]["x"]
        val y: Double = context[PropertySubcommand.relativePosition]["y"]
        val z: Double = context[PropertySubcommand.relativePosition]["z"]

        val position = Vec(x, y, z)

        val projectile = player.heldProjectile?.let { apply(it, position) } ?: return@onlyPlayers

        player.itemInMainHand = projectile.generateItem(player.itemInMainHand)

        player.sendFormattedTranslatableMessage(
            "projectile", "property.set",
            Component.text(name, NamedTextColor.BLUE),
            Component.text("$x $y $z", NamedTextColor.BLUE)
        )
    }

    syntax(oneAmount).onlyPlayers {

        if (!Projectile.hasProjectile(sender)) {
            return@onlyPlayers
        }

        val projectile = player.heldProjectile ?: return@onlyPlayers

        val amount = context[oneAmount]

        val position = Vec(amount, amount, amount)

        apply(projectile, position)

        player.itemInMainHand = projectile.generateItem(player.itemInMainHand)

        player.sendFormattedTranslatableMessage(
            "projectile", "property.set",
            Component.text(name, NamedTextColor.BLUE),
            Component.text(amount, NamedTextColor.BLUE)
        )
    }


}, name)