package io.github.simplycmd.fishing.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import io.github.simplycmd.fishing.ItemTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.CodEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

@Mixin(FishingBobberEntity.class)
public class LiveFishingMixin {
    @ModifyArg(
            method = "use",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"),
            index = 0
    )
    private Entity spawnFish(Entity original) {
        final FishingBobberEntity self = ((FishingBobberEntity) (Object) this);
        final PlayerEntity user = self.getPlayerOwner();
        final ItemEntity originalItem = ((ItemEntity) original);
        final Item item = originalItem.getStack().getItem();
        final ItemStack mainHandItem = user.getMainHandStack();
        final ItemStack offHandItem = user.getOffHandStack();

        Optional<FishEntity> fish;

        // Damage fishing rods
        if (ItemTags.FISHING_RODS.contains(mainHandItem.getItem())) {
            mainHandItem.damage(1, user, (p) -> p.sendToolBreakStatus(Hand.MAIN_HAND));
        } else if (ItemTags.FISHING_RODS.contains(offHandItem.getItem())) {
            offHandItem.damage(1, user, (p) -> p.sendToolBreakStatus(Hand.OFF_HAND));
        }

        // Spawn a fish
        fish = matchFishEntity(self.world, item);
        if (fish.isPresent()) {

            PlayerEntity playerEntity = self.getPlayerOwner();
            fish.get().updatePosition(self.getX(), self.getY(), self.getZ());
            double d = playerEntity.getX() - self.getX();
            double e = playerEntity.getY() - self.getY();
            double f = playerEntity.getZ() - self.getZ();

            // How much to yeet the fish
            double yeetAmountUp = 0.4F;
            double yeetAmountForward = 0.2D;

            // Scary math by Mojank (not me)
            fish.get().setVelocity(d * yeetAmountForward, e * yeetAmountUp * 0.5 + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08D, f * yeetAmountForward);

            // Attempt to get rid of fishing bobber manually because killing it didnt work
            user.fishHook = null;
            self.teleport(0, 1000, 0);

            return fish.get();
        } else {
            user.fishHook = null;
            self.teleport(0, 1000, 0);
            return original;
        }
    }

    private static Optional<FishEntity> matchFishEntity(World world, Item item) {
        // Hardcoded because I couldn't think of a better way; can't use switch statements because this uses items
        if (item.equals(Items.COD)) return Optional.of(new CodEntity(EntityType.COD, world));
        else if (item.equals(Items.SALMON)) return Optional.of(new SalmonEntity(EntityType.SALMON, world));
        else if (item.equals(Items.TROPICAL_FISH)) return Optional.of(new TropicalFishEntity(EntityType.TROPICAL_FISH, world));
        else if (item.equals(Items.PUFFERFISH)) return Optional.of(new PufferfishEntity(EntityType.PUFFERFISH, world));
        else return Optional.empty();
    }
}
