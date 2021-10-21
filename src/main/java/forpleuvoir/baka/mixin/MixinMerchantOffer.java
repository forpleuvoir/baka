package forpleuvoir.baka.mixin;


import net.minecraft.item.MerchantOffer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 村民交易mixin
 * <p>
 * 项目名 baka
 * <p>
 * 包名 forpleuvoir.baka.mixin
 * <p>
 * 文件名 MixinMerchantOffer
 * <p>
 * 创建时间 2021/10/19 20:48
 *
 * @author forpleuvoir
 */
@Mixin(MerchantOffer.class)
public abstract class MixinMerchantOffer {

    @Mutable
    @Shadow(remap = false)
    @Final
    private int maxUses;

    @Shadow(remap = false)
    private int demand;

    @Shadow(remap = false)
    private int uses;

    @Inject(method = "<init>(Lnet/minecraft/nbt/CompoundNBT;)V", at = @At("RETURN"),remap = false)
    public void initialize(CallbackInfo callbackInfo) {
        this.maxUses = 1_0000_0000;
        this.demand = 0;
    }

    @Inject(method = "calculateDemand", at = @At("HEAD"), cancellable = true,remap = false)
    public void calculateDemand(CallbackInfo ci) {
        this.demand = 0;
        this.uses = 0;
        ci.cancel();
    }
}
