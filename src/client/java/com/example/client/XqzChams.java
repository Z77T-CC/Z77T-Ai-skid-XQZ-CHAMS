package com.example.client;

import com.example.client.mixin.RenderTypeInvoker;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import java.util.HashMap;
import java.util.Map;

public class XqzChams {
	// Saydamlık: 0 = görünmez, 100 = tamamen opak
	public static final int OPACITY = 60;

	private static final Map<Identifier, RenderType> CACHE = new HashMap<>();

	private static final RenderPipeline PIPELINE = RenderPipelines.register(
		RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
			.withLocation(Identifier.fromNamespaceAndPath("modid", "pipeline/xqz_chams"))
			.withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
			.withCull(true)
			.withBlend(BlendFunction.TRANSLUCENT)
			.build());

	public static void init() {}

	public static int tint() {
		int alpha = Math.round(Math.max(0, Math.min(100, OPACITY)) * 2.55f);
		return (alpha << 24) | 0xFFFFFF;
	}

	public static RenderType get(Identifier texture) {
		return CACHE.computeIfAbsent(texture, t -> {
			RenderSetup setup = RenderSetup.builder(PIPELINE)
				.withTexture("Sampler0", t)
				.useLightmap()
				.useOverlay()
				.createRenderSetup();
			return RenderTypeInvoker.create("xqz_chams", setup);
		});
	}

	public static boolean hidden(double x, double y, double z) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null || mc.player == null) return false;
		Vec3 eye = mc.player.getEyePosition();
		for (double h : new double[] {0.2, 0.9, 1.6}) {
			Vec3 to = new Vec3(x, y + h, z);
			ClipContext ctx = new ClipContext(eye, to,
				ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mc.player);
			if (mc.level.clip(ctx).getType() == HitResult.Type.MISS) return false;
		}
		return true;
	}
}