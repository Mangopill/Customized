package mangopill.customized.common.item;

import mangopill.customized.Customized;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;

import static mangopill.customized.common.util.StringUtil.*;

public interface ThrowableIItem {
    MutableComponent component = getComponent("item_text." + Customized.MODID + ".throwable").withStyle(ChatFormatting.WHITE);
}
