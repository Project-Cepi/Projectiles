package world.cepi.projectile.commands

import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.command.arguments.literal
import world.cepi.kstom.item.and
import world.cepi.kstom.item.clientData
import world.cepi.kstom.item.withMeta
import world.cepi.mob.mob.mobEgg
import world.cepi.mob.util.MobUtils
import world.cepi.projectile.Projectile
import world.cepi.projectile.heldProjectile

object ProjectileCommand : Command("projectile") {

    init {
        val create = "create".literal()
        val shoot = "shoot".literal()

        addSyntax(create) {

            val player = sender as Player

            // Player needs a mob egg
            if (!MobUtils.hasMobEgg(sender)) return@addSyntax

            // Player can not have a projectile currently
            if (player.heldProjectile != null) {
                sender.sendMessage("Do not hold a projectile!")
                return@addSyntax
            }

            val projectile = Projectile()

            player.itemInMainHand = player.itemInMainHand.and {
                withMeta {
                    clientData {
                        this[Projectile.projectileKey] = projectile
                    }
                }
            }

            player.sendMessage("Projectile created!")
        }

        addSyntax(shoot) {
            if (!Projectile.hasProjectile(sender)) {
                sender.sendMessage("You do not have a projectile!")
                return@addSyntax
            }

            val player = sender as Player

            val projectile = player.heldProjectile ?: return@addSyntax
            projectile.shoot(player.mobEgg ?: return@addSyntax, player)
        }
    }

}