package com.example.client.mixin;

import com.example.client.XqzChams;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<S extends LivingEntityRenderState> {

	@Shadow
	public abstract Identifier getTextureLocation(S state);

	@Inject(method = "getRenderType", at = @At("RETURN"), cancellable = true)
	private void xqzRenderType(S state, boolean visible, boolean translucent, boolean glowing,
			CallbackInfoReturnable<RenderType> cir) {
		if (cir.getReturnValue() != null
				&& state instanceof AvatarRenderState
				&& XqzChams.hidden(state.x, state.y, state.z)) {
			cir.setReturnValue(XqzChams.get(this.getTextureLocation(state)));
		}
	}
}