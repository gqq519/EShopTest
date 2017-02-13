package com.feicuiedu.eshop.feature.category;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseFragment;
import com.feicuiedu.eshop.base.utils.LogUtils;
import com.feicuiedu.eshop.feature.EShopMainActivity;
import com.feicuiedu.eshop.network.EShopClient;
import com.feicuiedu.eshop.network.entity.CategoryPrimary;
import com.feicuiedu.eshop.network.entity.CategoryRsp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by gqq on 2017/2/10.
 */

// 商品分类页面

public class CategoryFragment extends BaseFragment {

    @BindView(R.id.standard_toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.standard_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.list_category)
    ListView mListCategory;
    @BindView(R.id.list_children)
    ListView mListChildren;

    private List<CategoryPrimary> mData;
    private CategoryAdapter mCategoryAdapter;
    private ChildrenAdapter mChildrenAdapter;
    private Handler mHandler = new Handler();

    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initView() {

        // toolbar
        mToolbarTitle.setText(R.string.category_title);

        // 设置适配器：创建及实现
        mCategoryAdapter = new CategoryAdapter();
        mListCategory.setAdapter(mCategoryAdapter);

        mChildrenAdapter = new ChildrenAdapter();
        mListChildren.setAdapter(mChildrenAdapter);

        if (mData!=null){
            updateCategory();
        }else {
            // 执行网络请求的操作
            enqueue();
        }
    }

    // 执行网络请求
    private void enqueue() {

        new Thread(){
            @Override
            public void run() {
                super.run();

                final CategoryRsp categoryRsp = EShopClient.getInstance().getCategory();

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (categoryRsp!=null){
                            mData = categoryRsp.getData();
                            updateCategory();
                        }
                    }
                });
            }
        }.start();
    }

    // 更新分类信息
    private void updateCategory() {
        mCategoryAdapter.reset(mData);
        chooseCategory(0);
    }

    // 切换一级展示的二级分类的内容
    private void chooseCategory(int position) {
        mListCategory.setItemChecked(position,true);
        mChildrenAdapter.reset(mData.get(position).getChildren());
    }

    @OnItemClick(R.id.list_category)
    public void onItemClick(int position){
        chooseCategory(position);
    }

    @OnItemClick(R.id.list_children)
    public void onChildItemClick(int position){
        Toast.makeText(getContext(), mChildrenAdapter.getItem(position).getName(), Toast.LENGTH_SHORT).show();
    }
}
