package cn.royan.scheduledtickvisualizer.mixin;

import cn.royan.scheduledtickvisualizer.ScheduledTickVisualizerManager;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity{
	public ItemEntityMixin(World world) {
		super(world);
	}

	@WrapMethod(method = "tick")
	public void tickEntitiesInject(Operation<Void> original){
		if(!ScheduledTickVisualizerManager.uuids.contains(this.getUuid())) original.call();
	}
}
