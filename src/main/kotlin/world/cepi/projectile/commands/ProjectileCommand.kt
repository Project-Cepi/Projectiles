package world.cepi.projectile.commands

import net.minestom.server.entity.Player
import net.minestom.server.tag.Tag
import world.cepi.kepi.command.subcommand.applyHelp
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.arguments.literal
import world.cepi.kstom.command.kommand.Kommand
import world.cepi.kstom.item.and
import world.cepi.kstom.item.withMeta
import world.cepi.mob.mob.mobEgg
import world.cepi.kstom.item.set
import world.cepi.mob.mob.Mob
import world.cepi.mob.util.MobUtils
import world.cepi.projectile.structure.Projectile
import world.cepi.projectile.structure.heldProjectile

internal object ProjectileCommand : Kommand({

    val create by literal
    val shoot by literal

    syntax(create).onlyPlayers {

        // Player can not have a projectile currently
        if (player.heldProjectile != null) {
            sender.sendFormattedTranslatableMessage("projectile", "required.none")
            return@onlyPlayers
        }

        if (player.mobEgg == null && !player.itemInMainHand.isAir) {
            player.sendFormattedTranslatableMessage("projectile", "required.mob_or_none")
            return@onlyPlayers
        }

        val projectile = Projectile()

        player.itemInMainHand = projectile.generateItem(player.mobEgg?.generateEgg(player.itemInMainHand) ?: Mob().generateEgg())

        player.sendFormattedTranslatableMessage("projectile", "create")
    }

    syntax(shoot).onlyPlayers {
        if (!Projectile.hasProjectile(sender)) {
            sender.sendFormattedTranslatableMessage("projectile", "required.hold")
            return@onlyPlayers
        }

        val player = sender as Player

        val projectile = player.heldProjectile ?: return@onlyPlayers
        projectile.shoot(player.mobEgg ?: return@onlyPlayers, player)
    }

    addSubcommands(PropertySubcommand, ProjectileEventSubcommand)

    applyHelp {
        """
            Projectiles allow you to throw 
            an amount of a mob with a <blue>force<gray>.
            
            To create a projectile, hold nothing or a mob in your hand and run
            <yellow>/projectile create
            
            To change how it works, use <yellow>/projectile property (name) (values...)
            
            Projectiles also have an <blue>Action Event System.
        """.trimIndent()
    }

}, "projectile")