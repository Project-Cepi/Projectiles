package world.cepi.projectile

import net.minestom.server.extensions.Extension;
import world.cepi.kstom.command.register
import world.cepi.kstom.command.unregister
import world.cepi.projectile.commands.ProjectileCommand

class Projectiles : Extension() {

    override fun initialize() {

        ProjectileCommand.register()

        logger.info("[Projectiles] has been enabled!")
    }

    override fun terminate() {

        ProjectileCommand.unregister()

        logger.info("[Projectiles] has been disabled!")
    }

}