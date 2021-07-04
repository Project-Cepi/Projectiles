package world.cepi.projectile.commands.properties

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import net.minestom.server.utils.Vector
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.item.and
import world.cepi.kstom.item.clientData
import world.cepi.kstom.item.withMeta
import world.cepi.projectile.Projectile
import world.cepi.projectile.commands.PropertySubcommand
import world.cepi.projectile.heldProjectile

internal object AmountPropertySubcommand : Command("amount") {
    init {
        val amount = ArgumentType.Integer("amount").min(0).max(20)

        addSyntax(amount) {
            if (!Projectile.hasProjectile(sender)) {
                return@addSyntax
            }

            val player = sender as Player

            val projectile = player.heldProjectile ?: return@addSyntax

            projectile.amount = context[amount]

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