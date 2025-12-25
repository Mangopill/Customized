package mangopill.customized.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;

import static mangopill.customized.common.util.CStringUtil.*;

public interface ThrowableIItem {
    MutableComponent component = C_ITEM_TEXT.create("throwable").withStyle(ChatFormatting.WHITE);
}
