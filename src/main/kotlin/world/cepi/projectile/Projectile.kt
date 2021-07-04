package world.cepi.projectile

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.kyori.adventure.sound.Sound
import net.minestom.server.command.CommandSender
import net.minestom.server.entity.Entity
import net.minestom.server.entity.Player
import net.minestom.server.sound.SoundEvent
import net.minestom.server.utils.Vector
import net.minestom.server.utils.time.Cooldown
import net.minestom.server.utils.time.TimeUnit
import net.minestom.server.utils.time.UpdateOption
import world.cepi.kstom.item.get
import world.cepi.kstom.serializer.UpdateOptionSerializer
import world.cepi.kstom.serializer.VectorSerializer
import world.cepi.mob.mob.Mob
import world.cepi.mob.util.MobUtils

@Serializable
class Projectile(
    @Serializable(with = VectorSerializer::class)
    val speed: Vector = Vector(15.0, 15.0, 15.0),
    @Serializable(with = UpdateOptionSerializer::class)
    val updateOption: UpdateOption = UpdateOption(10, TimeUnit.TICK),
    var lastTimeUsed: String = System.currentTimeMillis().toString()
) {

    fun lastTime() = lastTimeUsed.toLong()

    fun shoot(mob: Mob, shooter: Entity) {

        if (Cooldown.hasCooldown(
                System.currentTimeMillis(),
                lastTime(),
                updateOption
            )) {

            if (shooter is Player) {
                shooter.playSound(
                    Sound.sound(SoundEvent.NOTE_BLOCK_PLING, Sound.Source.MASTER, 1f, 0.5f),
                    shooter.position.x, shooter.position.y, shooter.position.z
                )
            }

            return
        }

        val entity = mob.generateMob() ?: return

        entity.setInstance(
            shooter.instance ?: return,
            shooter.position.clone().add(.0, shooter.eyeHeight, .0)
        )

        entity.velocity.add(entity.position.direction.normalize().multiply(speed))

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