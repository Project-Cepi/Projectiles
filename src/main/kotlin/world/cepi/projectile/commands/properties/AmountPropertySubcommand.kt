package world.cepi.projectile.commands.properties

import net.minestom.server.command.builder.arguments.ArgumentType

internal object AmountPropertySubcommand : GeneralSingleArgumentPropertySubcommand<Int>(
    "amount",
    { copy(amount = it) },
    { amount.toString() },
    ArgumentType.Integer("amount").min(0).max(20)
)