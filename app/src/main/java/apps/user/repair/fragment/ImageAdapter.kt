package apps.user.repair.fragment


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import apps.user.repair.R
import apps.user.repair.model.AdBannerDto
import com.youth.banner.adapter.BannerAdapter

class ImageAdapter(var context: Context, imageUrls: List<AdBannerDto>) : BannerAdapter<AdBannerDto, ImageAdapter.ImageHolder>(imageUrls) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ImageHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.item_ad_img, parent, false)
//        //通过裁剪实现圆角
//        BannerUtils.setBannerRound(layout, 20f)
        return ImageHolder(layout)
    }

    override fun onBindView(holder: ImageHolder, advertise: AdBannerDto, position: Int, size: Int) {
        if (advertise.pictureUrl.isNullOrEmpty()) {
            Glide.with( context).load(advertise.pictureUrlInt)
                .placeholder(R.drawable.ad_banner)
                .thumbnail(Glide.with(context).load(R.drawable.ad_banner)).into(holder.imageView)
        } else {
            Glide.with(holder.itemView).load(advertise.pictureUrl).into(holder.imageView)
        }
    }

    class ImageHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view.findViewById(R.id.index_banner_img)
    }

}