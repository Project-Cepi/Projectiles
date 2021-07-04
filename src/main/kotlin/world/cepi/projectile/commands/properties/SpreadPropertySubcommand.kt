package world.cepi.projectile.commands.properties

internal object SpreadPropertySubcommand : GeneralVectorPropertySubcommand(
    "spread",
    { spread = it }
)