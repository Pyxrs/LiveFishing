package com.pyxrs.fishing.mixin;

import com.pyxrs.fishing.data.FishManager;
import java.util.Optional;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FishingBobberEntity.class)
public class LiveFishingMixin {

    private static Optional<LivingEntity> matchFishEntity(World world, Item item) {
        // Hardcoded because I couldn't think of a better way; can't use switch statements because this uses items
        if (FishManager.manager().getFish(item) != null) {
            return Optional.ofNullable((LivingEntity) FishManager.manager().getFish(item).fish().create(world));
        } else {
            return Optional.empty();
        }
    }

    @ModifyArg(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private Entity spawnFish(Entity original) {
        final FishingBobberEntity bobber = ((FishingBobberEntity) (Object) this);
        final Item item = (original instanceof ItemEntity) ? ((ItemEntity) original).getStack().getItem() : Items.AIR;

        Optional<LivingEntity> fish = matchFishEntity(bobber.world, item);

        // Return early if it is not a fish
        if (!fish.isPresent()) return original;

        Entity owner = bobber.getOwner();
        fish.get().updatePosition(bobber.getX(), bobber.getY(), bobber.getZ());
        fish.get().setHealth(1);

        double dx = owner.getX() - bobber.getX();
        double dy = owner.getY() - bobber.getY();
        double dz = owner.getZ() - bobber.getZ();

        // How much to yeet the fish
        double yeetAmountUp = 0.5F;
        double yeetAmountForward = 0.11D;

        // Scary math by Mojank (not me)
        fish.get().setVelocity(
            dx * yeetAmountForward,
            dy * yeetAmountUp * 0.5 + Math.sqrt(Math.sqrt(dx * dx + dy * dy + dz * dz)) * 0.08D,
            dz * yeetAmountForward
        );

        return fish.get();
    }
}