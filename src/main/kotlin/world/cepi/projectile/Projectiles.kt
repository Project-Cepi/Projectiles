package world.cepi.projectile

import net.minestom.server.extensions.Extension
import world.cepi.kstom.event.listenOnly
import world.cepi.kstom.util.log
import world.cepi.kstom.util.node
import world.cepi.projectile.commands.ProjectileCommand
import world.cepi.projectile.listener.ProjectileHook

class Projectiles : Extension() {

    override fun initialize(): LoadStatus {

        node.listenOnly(ProjectileHook::hookUse)

        node.listenOnly(ProjectileHook::hookUseOnBlock)

        node.listenOnly(ProjectileHook::hookEntity)

        ProjectileCommand.register()

        log.info("[Projectiles] has been enabled!")

        return LoadStatus.SUCCESS
    }

    override fun terminate() {

        ProjectileCommand.unregister()

        log.info("[Projectiles] has been disabled!")
    }

}