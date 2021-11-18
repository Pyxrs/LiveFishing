package io.github.simplycmd.fishing.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

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
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

@Mixin(FishingBobberEntity.class)
public class LiveFishingMixin {
    @ModifyArg(
            method = "use",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"),
            index = 0
    )
    private Entity spawnFish(Entity original) {
        FishingBobberEntity self = ((FishingBobberEntity) (Object) this);
        PlayerEntity user = self.getPlayerOwner();
        ItemEntity originalItem = ((ItemEntity) original);
        Item item = originalItem.getStack().getItem();
        FishEntity fish;

        // Hardcoded because I couldn't think of a better way
        if (item.equals(Items.COD))
            fish = new CodEntity(EntityType.COD, self.world);
        else if (item.equals(Items.SALMON))
            fish = new SalmonEntity(EntityType.SALMON, self.world);
        else if (item.equals(Items.TROPICAL_FISH))
            fish = new TropicalFishEntity(EntityType.TROPICAL_FISH, self.world);
        else if (item.equals(Items.PUFFERFISH))
            fish = new PufferfishEntity(EntityType.PUFFERFISH, self.world);
        else return original;

        PlayerEntity playerEntity = self.getPlayerOwner();
        fish.updatePosition(self.getX(), self.getY(), self.getZ());
        double d = playerEntity.getX() - self.getX();
        double e = playerEntity.getY() - self.getY();
        double f = playerEntity.getZ() - self.getZ();
        double yeetAmountUp = 0.4F; // How much to yeet the fish
        double yeetAmountForward = 0.2D;

        // Scary math by Mojank (not me)
        fish.setVelocity(d * yeetAmountForward, e * yeetAmountUp * 0.5 + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08D, f * yeetAmountForward);

        if (user.getStackInHand(Hand.MAIN_HAND).getItem() == Items.FISHING_ROD) {
            user.getStackInHand(Hand.MAIN_HAND).damage(1, user, (p) -> p.sendToolBreakStatus(Hand.MAIN_HAND));
        } else if (user.getStackInHand(Hand.MAIN_HAND).getItem() == Items.FISHING_ROD) {
            user.getStackInHand(Hand.OFF_HAND).damage(1, user, (p) -> p.sendToolBreakStatus(Hand.OFF_HAND));
        }
        user.fishHook = null;
        self.teleport(0, 1000, 0);

        return fish;
    }
}
