package cn.royan.scheduledtickvisualizer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class ScheduledTickEntities {
	public World world;
	public BlockPos pos;
	public long trigger;
	public int priority;
	public long scheduledid;
	public boolean isSummoned = false;

	private ArrayList<Entity> entityArrayList = new ArrayList<>();
	private long delay;

	public ScheduledTickEntities(BlockPos pos, long triggerTick, int priority, long scheduledid, World world, long delay) {
		this.pos = pos;
		this.trigger = triggerTick;
		this.priority = priority;
		this.scheduledid = scheduledid;
		this.world = world;
		this.delay = delay;
	}

	public boolean tick(){
		if(delay > 0) {
			delay--;
			return false;
		} else {
			return true;
		}
	}

	public void summonEntity(){
		if(isSummoned) return;

		if(ScheduledTickVisualizer.scheduledTickVisualizerConfig.detail) {
			ItemEntity triggerTickEntity = new ItemEntity(this.world);
			baseSetting(triggerTickEntity, "Trigger: " + this.trigger);
			triggerTickEntity.setPosition(this.pos.getX() + 0.5F, this.pos.getY() + ScheduledTickVisualizer.scheduledTickVisualizerConfig.offset, this.pos.getZ() + 0.5F);

			ItemEntity priorityEntity = new ItemEntity(this.world);
			baseSetting(priorityEntity, "Priority: " + this.priority);
			priorityEntity.setPosition(this.pos.getX() + 0.5F, this.pos.getY() + 0.2 + ScheduledTickVisualizer.scheduledTickVisualizerConfig.offset, this.pos.getZ() + 0.5F);

			ItemEntity subTickOrderEntity = new ItemEntity(this.world);
			baseSetting(subTickOrderEntity, "ScheduledID: " + this.scheduledid);
			subTickOrderEntity.setPosition(this.pos.getX() + 0.5F, this.pos.getY() + 0.4 + ScheduledTickVisualizer.scheduledTickVisualizerConfig.offset, this.pos.getZ() + 0.5F);

			this.entityArrayList.add(priorityEntity);
			this.entityArrayList.add(triggerTickEntity);
			this.entityArrayList.add(subTickOrderEntity);
		} else {
			ItemEntity all = new ItemEntity(this.world);
			baseSetting(all, this.scheduledid + "/" + this.priority + "/" + this.trigger);
			all.setPosition(this.pos.getX() + 0.5F, this.pos.getY() + ScheduledTickVisualizer.scheduledTickVisualizerConfig.offset, this.pos.getZ() + 0.5F);

			this.entityArrayList.add(all);
		}

		this.entityArrayList.forEach(entity -> {
			this.world.addEntity(entity);
		});

		isSummoned = true;
	}

	public void baseSetting(Entity entity, String name){
		ScheduledTickVisualizerManager.uuids.add(entity.getUuid());
		entity.setInvisible(true);
		entity.addCommandTag("visualizer");
		entity.setInvulnerable(true);
		entity.setCustomNameVisible(true);
		entity.setCustomName(name);
		entity.setNoGravity(true);
	}

	public void discord(){
		this.entityArrayList.forEach(entity -> {
			entity.remove();
		});
	}
}
