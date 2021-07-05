package world.cepi.projectile.listener

import net.minestom.server.event.player.PlayerEntityInteractEvent
import net.minestom.server.event.player.PlayerUseItemEvent
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent
import net.minestom.server.event.trait.PlayerEvent
import world.cepi.mob.mob.mobEgg
import world.cepi.projectile.structure.heldProjectile

internal object ProjectileHook {

    private fun hook(event: PlayerEvent) = with(event) {
        val projectile = player.heldProjectile ?: return

        projectile.shoot(player.mobEgg ?: return, player)
    }

    fun hookUseOnBlock(event: PlayerUseItemOnBlockEvent) = hook(event)

    fun hookUse(event: PlayerUseItemEvent) = hook(event)

    fun hookEntity(event: PlayerEntityInteractEvent) = hook(event)

}