package apps.user.repair.adapter


import androidx.core.content.ContextCompat
import apps.user.repair.R
import apps.user.repair.databinding.ItemContentTagBinding
import apps.user.repair.model.IndexItemTagDto
import nearby.lib.uikit.recyclerview.BaseBindRecyclerAdapter



class ItemIndexContentTagAdapter : BaseBindRecyclerAdapter<ItemContentTagBinding, IndexItemTagDto>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_content_tag
    }

    override fun onBindingItem(
        binding: ItemContentTagBinding,
        item: IndexItemTagDto,
        position: Int
    ) {
        binding.indexIndexItemTvAdText.text = item.s
        binding.image.background = ContextCompat.getDrawable(context, item.icon)
    }


}