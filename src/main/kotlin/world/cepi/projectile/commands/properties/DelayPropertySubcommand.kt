package world.cepi.projectile.commands.properties

import net.minestom.server.command.builder.arguments.ArgumentType
import java.time.Duration

internal object DelayPropertySubcommand : GeneralSingleArgumentPropertySubcommand<Duration>(
    "delay",
    { copy(delayOption = it) },
    { delayOption.toString() },
    ArgumentType.Time("delay")
)