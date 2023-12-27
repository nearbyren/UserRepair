package apps.user.repair.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import apps.user.repair.R


class ItemAddAlbumAdapter(
    private val mContext: Context,
    requestList: MutableList<String>?,
    private val mMaxNumber: Int
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mRequestList: MutableList<String>?
    fun nodfiyData(list: MutableList<String>?) {
        if (list != null) {
            mRequestList!!.clear()
            mRequestList.addAll(list)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_add_album, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as apps.user.repair.adapter.ItemAddAlbumAdapter.Holder
        if (position == mRequestList!!.size) {
            holder.ivDelete.visibility = View.GONE
            val resourceId: Int = R.drawable.album_bg_add
            //显示图片 利用Glide centerCrop()
            Glide.with(mContext).load(resourceId).centerCrop().into(holder.image)
            if (position == mMaxNumber) {
                holder.image.visibility = View.GONE
            }
        } else {
            holder.ivDelete.visibility = View.VISIBLE
            //显示图片 利用Glide centerCrop()
            Glide.with(mContext).load(mRequestList[position]).centerCrop().into(holder.image)
        }
        //条目点击事件
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(View.OnClickListener {
                if (position == mMaxNumber) {
                    return@OnClickListener
                }
                if (mRequestList.size == position) {
                    mItemClickListener?.onItemClick("", position)
                } else {
                    mItemClickListener?.onItemClick(mRequestList[position], position)
                }
            })
        }
        if (mItemDeleteListener != null) {
            holder.ivDelete.setOnClickListener {
                mItemDeleteListener?.onDeleteClick(
                    mRequestList[position],
                    position
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return mRequestList!!.size + 1
    }

    internal inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: AppCompatImageView
        var ivDelete: AppCompatImageView

        init {
            image = itemView.findViewById<View>(R.id.img_bg) as AppCompatImageView
            ivDelete = itemView.findViewById<View>(R.id.img_del) as AppCompatImageView
        }
    }

    //使用接口回调点击事件
    private var mItemClickListener: apps.user.repair.adapter.ItemClickListener? = null
    fun setOnItemClickListener(itemClickListener: apps.user.repair.adapter.ItemClickListener?) {
        mItemClickListener = itemClickListener
    }

    //使用接口回调点击事件
    private var mItemDeleteListener: apps.user.repair.adapter.DeleteListener? = null

    init {
        mRequestList = ArrayList()
        if (mRequestList != null) {
            mRequestList.addAll(requestList!!)
        }
    }

    fun setOnDeleteClickListener(itemDeleteListener: apps.user.repair.adapter.DeleteListener?) {
        mItemDeleteListener = itemDeleteListener
    }
}
