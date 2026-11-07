package com.example.client;

import com.example.client.mixin.RenderTypeInvoker;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import java.util.HashMap;
import java.util.Map;

public class XqzChams {
	public static boolean active = false;
	private static final Map<Identifier, RenderType> CACHE = new HashMap<>();

	private static final RenderPipeline PIPELINE = RenderPipelines.register(
		RenderPipeline.builder(RenderPipelines.ENTITY_SNIPPET)
			.withLocation(Identifier.fromNamespaceAndPath("modid", "pipeline/xqz_chams"))
			.withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
			.build());

	public static void init() {}

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
}