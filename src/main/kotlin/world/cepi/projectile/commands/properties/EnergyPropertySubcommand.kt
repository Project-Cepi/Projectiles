package world.cepi.projectile.commands.properties

import net.minestom.server.command.builder.arguments.ArgumentType

internal object EnergyPropertySubcommand : GeneralSingleArgumentPropertySubcommand<Int>(
    "energy",
    { copy(usedEnergy = it) },
    ArgumentType.Integer("energy").min(0).max(20)
)