package apps.user.repair.adapter


import com.bumptech.glide.Glide
import nearby.lib.uikit.recyclerview.BaseBindRecyclerAdapter
import apps.user.repair.R
import apps.user.repair.databinding.ItemShowAlbumBinding
import apps.user.repair.model.AlbumDto


class ItemShowAlbumAdapter : BaseBindRecyclerAdapter<ItemShowAlbumBinding, AlbumDto>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_show_album
    }

    override fun onBindingItem(
        binding: ItemShowAlbumBinding, item: AlbumDto, position: Int
    ) {
        Glide.with(context).load(R.drawable.weizhi).into(binding.imgBg)
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

}