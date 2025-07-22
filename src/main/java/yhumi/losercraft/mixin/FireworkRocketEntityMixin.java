package yhumi.losercraft.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.Util;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;

@Mixin(FireworkRocketEntity.class)
public class FireworkRocketEntityMixin {
    @Shadow private LivingEntity attachedToEntity;

    @Inject(
        method = "tick()V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;getHandHoldingItemAngle(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/phys/Vec3;",
            shift = At.Shift.BEFORE
        ),
        cancellable = true
    )
    protected void reduceElytraDurabilityExtra(CallbackInfo ci) {
        List<EquipmentSlot> list = EquipmentSlot.VALUES
            .stream()
            .filter(equipmentSlotx -> LivingEntity.canGlideUsing(this.attachedToEntity.getItemBySlot(equipmentSlotx), equipmentSlotx))
            .toList();
        
        if (list.isEmpty()) {
            return;
        }

        EquipmentSlot equipmentSlot = Util.getRandom(list, this.attachedToEntity.getRandom());
        ItemStack itemInSlot = this.attachedToEntity.getItemBySlot(equipmentSlot);

        int itemDurability = itemInSlot.getMaxDamage() - itemInSlot.getDamageValue();
        if (itemDurability > 1) {
            int tickDurabilityReduction = Math.min((itemDurability) - 1, 4);
            itemInSlot.hurtAndBreak(tickDurabilityReduction, this.attachedToEntity, equipmentSlot);
        }
    }
}
