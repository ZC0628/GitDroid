package com.zc.githubdroid.favorite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zc.githubdroid.R;
import com.zc.githubdroid.favorite.model.LocalRepo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的收藏适配器
 */
public class FavoriteAdapter extends BaseAdapter{

    //填充的是本地仓库数据模型
    private List<LocalRepo> datas;

    public FavoriteAdapter() {
        datas = new ArrayList<>();//初始化
    }

    //设置数据
    public void setData(List<LocalRepo> localRepos){
        datas.clear();//填充之前先清空再设置数据
        datas.addAll(localRepos);//本地仓库数据填充到集合中
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //判断是不是为空
        return datas == null?0:datas.size();
    }

    @Override
    public LocalRepo getItem(int position) {
        //获取数据
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_repo,parent,false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        LocalRepo localRepo = getItem(position);//本地仓库
        viewHolder.tvRepoName.setText(localRepo.getFullName());
        viewHolder.tvRepoInfo.setText(localRepo.getDescription());
        viewHolder.tvRepoStars.setText(localRepo.getStargazersCount()+"");
        ImageLoader.getInstance().displayImage(localRepo.getAvatarUrl(),viewHolder.ivIcon);
        return convertView;
    }
    static class ViewHolder{
        @BindView(R.id.ivIcon)
        ImageView ivIcon;
        @BindView(R.id.tvRepoName)
        TextView tvRepoName;
        @BindView(R.id.tvRepoInfo) TextView tvRepoInfo;
        @BindView(R.id.tvRepoStars) TextView tvRepoStars;

        ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }
}
