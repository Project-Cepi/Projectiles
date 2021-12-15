package world.cepi.projectile.commands.properties

import net.minestom.server.command.builder.arguments.ArgumentType
import java.time.Duration

internal object DecayPropertySubcommand : GeneralSingleArgumentPropertySubcommand<Duration>(
    "decay",
    { copy(decayOption = it) },
    { decayOption.toString() },
    ArgumentType.Time("decay")
)