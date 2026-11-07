package com.example.client;

import com.example.client.mixin.RenderTypeInvoker;
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
	// 0 = normal, 1 = derinlik sıfırlama geçişi
	public static int phase = 0;

	private static final Map<Identifier, RenderType> CHAMS = new HashMap<>();
	private static final Map<Identifier, RenderType> RESET = new HashMap<>();

	private static final RenderPipeline CHAMS_PIPE = RenderPipelines.register(
		RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
			.withLocation(Identifier.fromNamespaceAndPath("modid", "pipeline/xqz_chams"))
			.withCull(true)
			.build());

	private static final RenderPipeline RESET_PIPE = RenderPipelines.register(
		RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
			.withLocation(Identifier.fromNamespaceAndPath("modid", "pipeline/xqz_reset"))
			.withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
			.withDepthWrite(true)
			.withColorWrite(false, false)
			.withDepthBias(0.0f, 5.0E7f)
			.withCull(true)
			.build());

	public static void init() {}

	private static RenderType make(RenderPipeline pipe, String name, Identifier tex) {
		RenderSetup setup = RenderSetup.builder(pipe)
			.withTexture("Sampler0", tex)
			.useLightmap()
			.useOverlay()
			.createRenderSetup();
		return RenderTypeInvoker.create(name, setup);
	}

	public static RenderType chams(Identifier tex) {
		return CHAMS.computeIfAbsent(tex, t -> make(CHAMS_PIPE, "xqz_chams", t));
	}

	public static RenderType reset(Identifier tex) {
		return RESET.computeIfAbsent(tex, t -> make(RESET_PIPE, "xqz_reset", t));
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