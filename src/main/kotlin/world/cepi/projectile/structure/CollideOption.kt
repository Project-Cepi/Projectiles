package world.cepi.projectile.structure

sealed class CollideOption {

    object Explode : CollideOption()
    object Dissapear : CollideOption()
    object None : CollideOption()

}