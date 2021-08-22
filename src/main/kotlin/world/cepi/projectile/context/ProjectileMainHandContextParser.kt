package world.cepi.projectile.context

import net.minestom.server.command.CommandSender
import net.minestom.server.entity.Player
import world.cepi.kstom.command.arguments.context.ContextParser
import world.cepi.projectile.structure.Projectile
import world.cepi.projectile.structure.heldProjectile

object ProjectileMainHandContextParser : ContextParser<Projectile> {

    override fun parse(sender: CommandSender): Projectile? =
        (sender as? Player)?.heldProjectile

}