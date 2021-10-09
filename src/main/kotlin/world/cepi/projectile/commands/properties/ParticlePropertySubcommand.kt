package world.cepi.projectile.commands.properties

import net.minestom.server.command.builder.arguments.minecraft.registry.ArgumentParticle
import net.minestom.server.particle.Particle

internal object ParticlePropertySubcommand : GeneralSingleArgumentPropertySubcommand<Particle>(
    "particle",
    { particle = it },
    ArgumentParticle("particleArgument")
)