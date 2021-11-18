package world.cepi.projectile.commands.properties

internal object RecoilPropertySubcommand : GeneralVectorPropertySubcommand(
    "recoil",
    { copy(recoil = it) }
)