package world.cepi.projectile

import net.minestom.server.extensions.Extension;
import world.cepi.kstom.command.register
import world.cepi.kstom.command.unregister
import world.cepi.kstom.event.listenOnly
import world.cepi.projectile.commands.ProjectileCommand
import world.cepi.projectile.listener.ProjectileHook

class Projectiles : Extension() {

    override fun initialize() {

        eventNode.listenOnly(ProjectileHook::hookUse)

        eventNode.listenOnly(ProjectileHook::hookUseOnBlock)

        eventNode.listenOnly(ProjectileHook::hookEntity)

        ProjectileCommand.register()

        logger.info("[Projectiles] has been enabled!")
    }

    override fun terminate() {

        ProjectileCommand.unregister()

        logger.info("[Projectiles] has been disabled!")
    }

}