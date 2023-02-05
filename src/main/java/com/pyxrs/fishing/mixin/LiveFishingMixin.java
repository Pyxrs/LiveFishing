package com.pyxrs.fishing.mixin;

import com.pyxrs.fishing.Fishing;

import java.util.Optional;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FishingBobberEntity.class)
public class LiveFishingMixin {
    @ModifyArg(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private Entity spawnFish(Entity original) {
        final FishingBobberEntity bobber = ((FishingBobberEntity) (Object) this);
        final Item item = (original instanceof ItemEntity) ? ((ItemEntity) original).getStack().getItem() : Items.AIR;

        Optional<LivingEntity> fish = Optional.ofNullable((LivingEntity) Fishing.FISH.get(item).create(bobber.getWorld()));

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

        var player = bobber.getPlayerOwner();
        if (player != null) {
            player.fishHook = null;
            final ItemStack mainHandItem = player.getMainHandStack();
            final ItemStack offHandItem = player.getOffHandStack();
            if (mainHandItem.getItem() instanceof FishingRodItem) {
                mainHandItem.damage(1, player, (p) -> p.sendToolBreakStatus(Hand.MAIN_HAND));
            } else if (offHandItem.getItem() instanceof FishingRodItem) {
                offHandItem.damage(1, player, (p) -> p.sendToolBreakStatus(Hand.OFF_HAND));
            }
        }
        bobber.remove(Entity.RemovalReason.DISCARDED);

        return fish.get();
    }
}