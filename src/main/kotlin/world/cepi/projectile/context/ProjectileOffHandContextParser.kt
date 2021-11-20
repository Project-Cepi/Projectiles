package world.cepi.projectile.context

import net.kyori.adventure.text.Component
import net.minestom.server.command.CommandSender
import net.minestom.server.entity.Player
import world.cepi.kstom.command.arguments.context.ContextParser
import world.cepi.projectile.structure.Projectile
import world.cepi.projectile.structure.heldProjectileOffHand

object ProjectileOffHandContextParser : ContextParser<Projectile> {

    override fun parse(sender: CommandSender): Projectile? =
        (sender as? Player)?.heldProjectileOffHand

    override val callbackMessage = Component.text("No projectile found in off hand!")

}