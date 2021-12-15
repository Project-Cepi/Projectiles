package world.cepi.projectile.commands.properties

internal object SpreadPropertySubcommand : GeneralVectorPropertySubcommand(
    "spread",
    { copy(spread = it) },
    { spread }
)