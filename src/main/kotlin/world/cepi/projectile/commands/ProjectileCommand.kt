package world.cepi.projectile.commands

import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player
import net.minestom.server.tag.Tag
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.addSyntax
import world.cepi.kstom.command.arguments.literal
import world.cepi.kstom.item.and
import world.cepi.kstom.item.clientData
import world.cepi.kstom.item.withMeta
import world.cepi.mob.mob.mobEgg
import world.cepi.mob.util.MobUtils
import world.cepi.projectile.structure.Projectile
import world.cepi.projectile.structure.heldProjectile

internal object ProjectileCommand : Command("projectile") {

    init {
        val create = "create".literal()
        val shoot = "shoot".literal()

        addSyntax(create) {

            val player = sender as Player

            // Player needs a mob egg
            if (!MobUtils.hasMobEgg(sender)) return@addSyntax

            // Player can not have a projectile currently
            if (player.heldProjectile != null) {
                sender.sendFormattedTranslatableMessage("projectile", "required.none")
                return@addSyntax
            }

            val projectile = Projectile()

            player.itemInMainHand = player.itemInMainHand.and {
                withMeta {

                    this.set(Tag.Byte("noSpawn"), 1)

                    clientData {
                        this[Projectile.projectileKey] = projectile
                    }
                }
            }

            player.sendFormattedTranslatableMessage("projectile", "create")
        }

        addSyntax(shoot) {
            if (!Projectile.hasProjectile(sender)) {
                sender.sendFormattedTranslatableMessage("projectile", "required.hold")
                return@addSyntax
            }

            val player = sender as Player

            val projectile = player.heldProjectile ?: return@addSyntax
            projectile.shoot(player.mobEgg ?: return@addSyntax, player)
        }

        addSubcommand(PropertySubcommand)
    }

}