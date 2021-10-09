package world.cepi.projectile.commands

import net.minestom.server.command.builder.arguments.ArgumentType
import world.cepi.kstom.command.kommand.Kommand
import world.cepi.projectile.commands.properties.*
import world.cepi.projectile.commands.properties.AmountPropertySubcommand
import world.cepi.projectile.commands.properties.EnergyPropertySubcommand
import world.cepi.projectile.commands.properties.PowerPropertySubcommand
import world.cepi.projectile.commands.properties.RecoilPropertySubcommand
import world.cepi.projectile.commands.properties.SoundPropertySubcommand

internal object PropertySubcommand : Kommand(name ="property") {

    val relativePosition = ArgumentType.Group(
        "pos",
        ArgumentType.Double("x").min(0.0).max(100.0),
        ArgumentType.Double("y").min(0.0).max(100.0),
        ArgumentType.Double("z").min(0.0).max(100.0)
    )

    init {
        addSubcommands(
            RecoilPropertySubcommand, PowerPropertySubcommand,
            AmountPropertySubcommand, SoundPropertySubcommand,
            EnergyPropertySubcommand, SpreadPropertySubcommand,
            DelayPropertySubcommand, DecayPropertySubcommand,
            ParticlePropertySubcommand
        )
    }
}