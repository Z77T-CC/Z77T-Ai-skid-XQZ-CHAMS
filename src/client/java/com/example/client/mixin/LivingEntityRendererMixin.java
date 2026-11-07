package com.example.client.mixin;

import com.example.client.XqzChams;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<S extends LivingEntityRenderState> {

	@Shadow
	public abstract Identifier getTextureLocation(S state);

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Inject(method = "submit", at = @At("HEAD"))
	private void xqzResetPass(S state, PoseStack poseStack, SubmitNodeCollector collector,
			CameraRenderState camera, CallbackInfo ci) {
		if (XqzChams.phase != 0 || !(state instanceof AvatarRenderState)
				|| !XqzChams.hidden(state.x, state.y, state.z)) return;
		XqzChams.phase = 1;
		try {
			((LivingEntityRenderer) (Object) this).submit(state, poseStack, collector, camera);
		} finally {
			XqzChams.phase = 0;
		}
	}

	@Inject(method = "getRenderType", at = @At("RETURN"), cancellable = true)
	private void xqzRenderType(S state, boolean visible, boolean translucent, boolean glowing,
			CallbackInfoReturnable<RenderType> cir) {
		if (cir.getReturnValue() == null || !(state instanceof AvatarRenderState)
				|| !XqzChams.hidden(state.x, state.y, state.z)) return;
		Identifier tex = this.getTextureLocation(state);
		cir.setReturnValue(XqzChams.phase == 1 ? XqzChams.reset(tex) : XqzChams.chams(tex));
	}
}