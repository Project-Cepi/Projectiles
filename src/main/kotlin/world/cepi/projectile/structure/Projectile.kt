package world.cepi.projectile.structure

import kotlinx.serialization.Serializable
import net.kyori.adventure.sound.Sound
import net.minestom.server.command.CommandSender
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Entity
import net.minestom.server.entity.Player
import net.minestom.server.sound.SoundEvent
import net.minestom.server.utils.time.TimeUnit
import world.cepi.energy.energy
import world.cepi.kstom.Manager
import world.cepi.kstom.item.get
import world.cepi.kstom.serializer.DurationSerializer
import world.cepi.kstom.serializer.SoundSerializer
import world.cepi.kstom.serializer.VectorSerializer
import world.cepi.kstom.util.*
import world.cepi.mob.mob.Mob
import world.cepi.mob.util.MobUtils
import java.time.Duration

@Serializable
class Projectile(
    @Serializable(with = VectorSerializer::class)
    var power: Vec = Vec(15.0, 15.0, 15.0),
    @Serializable(with = VectorSerializer::class)
    var recoil: Vec = Vec(.0, .0, .0),
    var lastTimeUsed: String = System.currentTimeMillis().toString(),
    var amount: Int = 1,
    @Serializable(with = SoundSerializer::class)
    var sound: Sound? = null,
    var usedEnergy: Int = 0,
    @Serializable(with = VectorSerializer::class)
    var spread: Vec = Vec(.0, .0, .0),
    @Serializable(with = DurationSerializer::class)
    var delayOption: Duration = Duration.of(10, TimeUnit.SERVER_TICK),
    @Serializable(with = DurationSerializer::class)
    var decayOption: Duration = Duration.of(3, TimeUnit.SECOND),
) {

    fun lastTime() = lastTimeUsed.toLong()

    fun shoot(mob: Mob, shooter: Entity) {

        // Respect cooldown
        if (System.currentTimeMillis() - lastTime() < delayOption.toMillis()) {

            if (shooter is Player) {
                shooter.playSound(
                    Sound.sound(SoundEvent.BLOCK_NOTE_BLOCK_PLING, Sound.Source.MASTER, 1f, 0.5f),
                    shooter.position.x(), shooter.position.y(), shooter.position.z()
                )
            }

            return
        }

        // Respect energy
        if (shooter is Player && shooter.energy < usedEnergy) {

            shooter.playSound(
                Sound.sound(SoundEvent.BLOCK_NOTE_BLOCK_PLING, Sound.Source.MASTER, 1f, 0.5f),
                shooter.position.x(), shooter.position.y(), shooter.position.z()
            )

            return
        } else if (shooter is Player) {
            shooter.energy -= usedEnergy
        }

        // Add sound
        if (sound != null && shooter is Player) {

            val (x, y, z) = shooter.position

            shooter.playSound(sound!!, x, y, z)
        }


        // Get the position to shoot from
        val shootPosition = shooter.position
            .add(.0, shooter.eyeHeight / 2, .0)
            .add(shooter.position.direction()
                .normalize()
                .div(Vec(5.0, 5.0, 5.0)).asPosition()
            )

        val shootDirection = shooter.eyePosition().direction()

        repeat(amount) {
            val entity = mob.generateMob() ?: return

            val spreadVector = shootDirection.spread(spread.x(), spread.y(), spread.z()).normalize()

            entity.setInstance(
                shooter.instance ?: return,
                shootPosition
            )

            Manager.scheduler.buildTask {
                entity.remove()
            }.delay(decayOption).schedule()

            // Add forward projectile speed
            entity.velocity =
                entity.velocity.add(spreadVector.normalize().mul(power))
        }

        // Add recoil
        shooter.velocity = shooter.velocity.add(shooter.position.direction()
            .normalize().mul(-1.0).mul(recoil))

        lastTimeUsed = System.currentTimeMillis().toString()
    }

    companion object {

        const val projectileKey = "projectile"

        fun hasProjectile(sender: CommandSender): Boolean {

            if (!MobUtils.hasMobEgg(sender)) {
                sender.sendMessage("You are not holding a mob egg!")
                return false
            }

            sender as Player

            if (sender.heldProjectile == null) {
                sender.sendMessage("You are not holding a projectile!")
                return false
            }

            return true
        }
    }

}

val Player.heldProjectile: Projectile?
    get() = this.itemInMainHand.meta.get(Projectile.projectileKey)


val Player.heldProjectileOffHand: Projectile?
    get() = this.itemInOffHand.meta.get(Projectile.projectileKey)