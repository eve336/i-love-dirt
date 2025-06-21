package org.eve.i_love_soil.common.data;

import com.tterrag.registrate.util.entry.ItemEntry;
import org.eve.i_love_soil.api.registries.ILSRegistries;
import org.eve.i_love_soil.common.items.*;

public class ILSItems {


    // Creates a new food item with the id "i_love_soil:example_id", nutrition 1 and saturation 2
//    public static final RegistryObject<Item> EXAMPLE_ITEM = ILS_ITEMS.register("example_item",
//            () -> new Item(new Item.Properties()));

    public static ItemEntry<org.eve.i_love_soil.common.items.pHStripItem> pHStripItem = ILSRegistries.ILSREGISTRATE.item("ph_strip", pHStripItem::new)
            .register();

    public static ItemEntry<org.eve.i_love_soil.common.items.fertiliserItem> fertiliserItem = ILSRegistries.ILSREGISTRATE.item("fertiliser", fertiliserItem::new)
            .register();

    public static void init(){}

}
