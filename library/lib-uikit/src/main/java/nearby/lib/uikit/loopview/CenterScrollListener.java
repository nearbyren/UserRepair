package nearby.lib.uikit.loopview;


import androidx.recyclerview.widget.RecyclerView;



/**
 * A {@link RecyclerView.OnScrollListener} which helps {@link OveringLayoutManager}
 * to center the current position
 */
public class CenterScrollListener extends RecyclerView.OnScrollListener {
    private boolean mAutoSet = false;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        final OnPageChangeListener onPageChangeListener = ((OveringLayoutManager) layoutManager).onPageChangeListener;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrollStateChanged(newState);
        }

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (mAutoSet) {
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(((OveringLayoutManager) layoutManager).getCurrentPosition());
                }
                mAutoSet = false;
            } else {
                final int delta;
                delta = ((OveringLayoutManager) layoutManager).getOffsetToCenter();
                if (delta != 0) {
                    if (((OveringLayoutManager) layoutManager).getOrientation() == OveringLayoutManager.VERTICAL)
                        recyclerView.smoothScrollBy(0, delta);
                    else
                        recyclerView.smoothScrollBy(delta, 0);
                    mAutoSet = true;
                } else {
                    if (onPageChangeListener != null) {
                        onPageChangeListener.onPageSelected(((OveringLayoutManager) layoutManager).getCurrentPosition());
                    }
                    mAutoSet = false;
                }
            }
        } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
            mAutoSet = false;
        }
    }
}
