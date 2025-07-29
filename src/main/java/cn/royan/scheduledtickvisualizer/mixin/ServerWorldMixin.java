package cn.royan.scheduledtickvisualizer.mixin;

import cn.royan.scheduledtickvisualizer.ScheduledTickEntities;
import cn.royan.scheduledtickvisualizer.ScheduledTickVisualizer;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.world.ScheduledTick;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
	@Shadow
	@Final
	private Set<ScheduledTick> scheduledTicks;

	@Inject(method = "tick",
		at = @At("HEAD"))
	public void tick(CallbackInfo ci) {
		scheduledTicks.forEach((scheduledTick) -> {
			ScheduledTickVisualizer.scheduledTickVisualizermanager.addScheduledTickEntities(scheduledTick.pos,new ScheduledTickEntities(scheduledTick.pos, scheduledTick.time - ((World) (Object)this).getData().getTime(), scheduledTick.priority, scheduledTick.id % 10000, (World)(Object) this, ScheduledTickVisualizer.scheduledTickVisualizerConfig.delay));
		});
	}

	@Inject(method = "doScheduledTicks",
		at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
	public void removeScheduledTickEntities(boolean flush, CallbackInfoReturnable<Boolean> cir, @Local ScheduledTick scheduledTick) {
		if(ScheduledTickVisualizer.scheduledTickVisualizerConfig.delay == 0) ScheduledTickVisualizer.scheduledTickVisualizermanager.removeScheduledTickEntities(scheduledTick.pos);
	}
}
