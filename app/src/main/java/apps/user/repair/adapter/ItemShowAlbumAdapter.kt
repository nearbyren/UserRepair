package apps.user.repair.adapter


import com.bumptech.glide.Glide
import nearby.lib.uikit.recyclerview.BaseBindRecyclerAdapter
import apps.user.repair.R
import apps.user.repair.databinding.ItemShowAlbumBinding
import apps.user.repair.model.AlbumDto
import apps.user.repair.model.CompleteImageDto


class ItemShowAlbumAdapter : BaseBindRecyclerAdapter<ItemShowAlbumBinding, CompleteImageDto>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_show_album
    }

    override fun onBindingItem(
        binding: ItemShowAlbumBinding, item: CompleteImageDto, position: Int
    ) {
        Glide.with(context).load(item.pictureUrl).into(binding.imgBg)
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

}