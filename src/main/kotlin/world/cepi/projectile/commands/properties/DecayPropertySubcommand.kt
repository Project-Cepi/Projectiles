package world.cepi.projectile.commands.properties

import net.minestom.server.command.builder.arguments.ArgumentType
import java.time.Duration

internal object DecayPropertySubcommand : GeneralSingleArgumentPropertySubcommand<Duration>(
    "decay",
    { decayOption = it },
    ArgumentType.Time("decay")
)