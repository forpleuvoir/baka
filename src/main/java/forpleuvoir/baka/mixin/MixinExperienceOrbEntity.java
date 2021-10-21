package forpleuvoir.baka.mixin;

import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 经验球mixin
 * <p>
 * 项目名 baka
 * <p>
 * 包名 forpleuvoir.baka.mixin
 * <p>
 * 文件名 MixinExperienceOrbEntity
 * <p>
 * 创建时间 2021/10/19 20:52
 *
 * @author forpleuvoir
 */
@Mixin(ExperienceOrbEntity.class)
public abstract class MixinExperienceOrbEntity {

    @Inject(method = "onCollideWithPlayer", at = @At("RETURN"), remap = false)
    public void onCollideWithPlayer(PlayerEntity entityIn, CallbackInfo ci) {
        entityIn.xpCooldown = 0;
    }
}
