package com.oceancx.androidlib.widget;

/**
 * 依赖于PullToRefreshLibrary
 * 让RecyclerView支持下拉刷新和上拉加载更多
 * Created by oceancx on 16/1/21.
 */
public class PullToRefreshRecyclerView{
        //extends PullToRefreshBase<RecyclerView> {
//
//    @Override
//    public Orientation getPullToRefreshScrollDirection() {
//        return Orientation.VERTICAL;
//    }

//    @Override
//    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
//
//        RecyclerView recyclerView = new RecyclerView(context, attrs);
//        recyclerView.setId(R.id.ryc_views);
//        return recyclerView;
//    }

//    protected boolean isReadyForPullEnd() {
//        try {
//            RecyclerView mRefreshableView = getRefreshableView();
//            int checkPos = mRefreshableView.getAdapter().getItemCount() - 1;
//
//            View mLastChild = mRefreshableView.getChildAt(mRefreshableView.getChildCount() - 1);
//            int rvPos = mRefreshableView.getChildAdapterPosition(mLastChild);
//            // 当且仅当第一个可视节点==0 切其top==0的时候 此时rv到达顶部
//            if (rvPos == checkPos && mLastChild.getBottom() - mLastChild.getTop() == mLastChild.getMeasuredHeight() && mLastChild.getBottom() == mRefreshableView.getBottom()) {
//                return true;
//            }
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    protected boolean isReadyForPullStart() {
//        try {
//            int checkPos = 0;
//            RecyclerView mRefreshableView = getRefreshableView();
//            View mFirstChild = mRefreshableView.getChildAt(0);
//            int rvPos = mRefreshableView.getChildLayoutPosition(mFirstChild);
//            // 当且仅当第一个可视节点==0 切其top==0的时候 此时rv到达顶部
//            if (rvPos == checkPos && mFirstChild.getTop() == mRefreshableView.getPaddingTop()) {
//                return true;
//            }
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
}
