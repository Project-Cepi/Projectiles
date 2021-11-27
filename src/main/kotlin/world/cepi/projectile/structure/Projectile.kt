package world.cepi.projectile.structure

import kotlinx.serialization.Serializable
import net.kyori.adventure.sound.Sound
import net.minestom.server.command.CommandSender
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Entity
import net.minestom.server.entity.Player
import net.minestom.server.event.entity.EntityTickEvent
import net.minestom.server.item.ItemStack
import net.minestom.server.network.packet.server.play.ParticlePacket
import net.minestom.server.particle.Particle
import net.minestom.server.sound.SoundEvent
import net.minestom.server.tag.Tag
import net.minestom.server.utils.PacketUtils
import net.minestom.server.utils.time.TimeUnit
import world.cepi.actions.ActionItem
import world.cepi.actions.ActionSerializer
import world.cepi.energy.energy
import world.cepi.kstom.Manager
import world.cepi.kstom.event.listenOnly
import world.cepi.kstom.item.*
import world.cepi.kstom.nbt.TagUUID
import world.cepi.kstom.serializer.DurationSerializer
import world.cepi.kstom.serializer.ParticleSerializer
import world.cepi.kstom.serializer.SoundSerializer
import world.cepi.kstom.serializer.VectorSerializer
import world.cepi.kstom.util.*
import world.cepi.mob.mob.Mob
import world.cepi.mob.util.MobUtils
import java.time.Duration

@Serializable
data class Projectile(
    @Serializable(with = VectorSerializer::class)
    val power: Vec = Vec(15.0, 15.0, 15.0),

    @Serializable(with = VectorSerializer::class)
    val recoil: Vec = Vec(.0, .0, .0),

    val lastTimeUsed: Long = System.currentTimeMillis(),

    val amount: Int = 1,

    @Serializable(with = SoundSerializer::class)
    val sound: Sound? = null,

    val usedEnergy: Int = 0,

    @Serializable(with = VectorSerializer::class)
    val spread: Vec = Vec(.0, .0, .0),

    @Serializable(with = DurationSerializer::class)
    val delayOption: Duration = Duration.of(10, TimeUnit.SERVER_TICK),

    @Serializable(with = DurationSerializer::class)
    val decayOption: Duration = Duration.of(3, TimeUnit.SECOND),

    @Serializable(with = ParticleSerializer::class)
    val particle: Particle? = null,

    val decayEvents: List<ActionItem> = listOf()
) {

    fun generateItem(itemStack: ItemStack) = itemStack.and {
        this[Tag.Byte("noSpawn")] = 1
        this[projectileKey, ActionSerializer.module] = this@Projectile
    }

    fun shoot(mob: Mob, shooter: Entity): Projectile {

        // Respect cooldown
        if (System.currentTimeMillis() - lastTimeUsed < delayOption.toMillis()) {

            if (shooter is Player) {
                shooter.playSound(
                    Sound.sound(SoundEvent.BLOCK_NOTE_BLOCK_PLING, Sound.Source.MASTER, 1f, 0.5f),
                    shooter.position.x(), shooter.position.y(), shooter.position.z()
                )
            }

            return this
        }

        // Respect energy
        if (shooter is Player && shooter.energy < usedEnergy) {

            shooter.playSound(
                Sound.sound(SoundEvent.BLOCK_NOTE_BLOCK_PLING, Sound.Source.MASTER, 1f, 0.5f),
                shooter.position.x(), shooter.position.y(), shooter.position.z()
            )

            return this
        } else if (shooter is Player) {
            shooter.energy -= usedEnergy
        }

        // Add sound
        if (sound != null && shooter is Player) {

            val (x, y, z) = shooter.position

            shooter.playSound(sound, x, y, z)
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
            val entity = mob.generateMob() ?: return this

            val spreadVector = shootDirection.spread(spread.x(), spread.y(), spread.z()).normalize()

            entity.mob.setInstance(
                shooter.instance ?: return this,
                shootPosition
            )

            Manager.scheduler.buildTask {
                decayEvents.forEach { it(entity.mob, entity.mob) }
                entity.mob.remove()
            }.delay(decayOption).schedule()

            entity.mob.setTag(TagUUID("caster"), shooter.uuid)

            // Add forward projectile speed
            entity.mob.velocity =
                entity.mob.velocity.add(spreadVector.normalize().mul(power))

            // Display particle
            entity.eventNode.listenOnly<EntityTickEvent> {
                if (particle == null) return@listenOnly

                val packet = ParticlePacket().apply {
                    particleId = particle.id()
                    longDistance = false
                    x = entity.mob.position.x()
                    y = entity.mob.position.y()
                    z = entity.mob.position.z()
                    offsetX = 0f
                    offsetY = 0f
                    offsetZ = 0f
                    particleCount = 1;
                }

                PacketUtils.sendGroupedPacket(entity.mob.viewers, packet)
            }
        }

        // Add recoil
        shooter.velocity = shooter.velocity.add(shooter.position.direction()
            .normalize().mul(-1.0).mul(recoil))

        return this.copy(lastTimeUsed = System.currentTimeMillis())
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