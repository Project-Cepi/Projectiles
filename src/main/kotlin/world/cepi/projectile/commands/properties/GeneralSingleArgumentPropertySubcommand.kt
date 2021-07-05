package world.cepi.projectile.commands.properties

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.entity.Player
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.item.and
import world.cepi.kstom.item.clientData
import world.cepi.kstom.item.withMeta
import world.cepi.projectile.structure.Projectile
import world.cepi.projectile.structure.heldProjectile

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