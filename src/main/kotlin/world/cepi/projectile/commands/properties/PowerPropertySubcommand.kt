package world.cepi.projectile.commands.properties

internal object PowerPropertySubcommand : GeneralVectorPropertySubcommand(
    "power",
    { copy(power = it) }
)