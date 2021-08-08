package world.cepi.projectile.structure

sealed class CollideOption {

    class Explode : CollideOption()
    class Dissapear : CollideOption()
    class None : CollideOption()

}