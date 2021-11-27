package world.cepi.projectile.commands

import world.cepi.actions.command.subcommands.ActionEventHandler
import world.cepi.actions.command.subcommands.EventSubcommand
import world.cepi.projectile.structure.heldProjectile

internal object ProjectileEventSubcommand : EventSubcommand(
    eventCondition = { player.heldProjectile != null },
    eventNodes = listOf(
        ActionEventHandler("decay", { heldProjectile!!.decayEvents }) {
            heldProjectile!!.copy(decayEvents = it).generateItem(itemInMainHand)
        }
    )
)