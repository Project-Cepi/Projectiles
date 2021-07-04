package world.cepi.projectile.commands.properties

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.entity.Player
import net.minestom.server.utils.Vector
import world.cepi.kstom.command.addSyntax
import world.cepi.projectile.Projectile
import world.cepi.projectile.heldProjectile
import world.cepi.kstom.item.and
import world.cepi.kstom.item.clientData
import world.cepi.kstom.item.withMeta
import world.cepi.projectile.commands.PropertySubcommand

internal open class GeneralSingleArgumentPropertySubcommand<T>(
    name: String,
    apply: Projectile.(T) -> Unit,
    argument: Argument<T>
) : Command(name) {

    init {

        addSyntax(argument) {

            if (!Projectile.hasProjectile(sender)) {
                return@addSyntax
            }

            val player = sender as Player

            val projectile = player.heldProjectile ?: return@addSyntax

            val genArg = context[argument]

            apply(projectile, genArg)

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