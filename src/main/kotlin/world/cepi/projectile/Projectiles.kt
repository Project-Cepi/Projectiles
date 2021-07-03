package world.cepi.projectile

import net.minestom.server.extensions.Extension;

class Projectiles : Extension() {

    override fun initialize() {
        logger.info("[Projectiles] has been enabled!")
    }

    override fun terminate() {
        logger.info("[Projectiles] has been disabled!")
    }

}