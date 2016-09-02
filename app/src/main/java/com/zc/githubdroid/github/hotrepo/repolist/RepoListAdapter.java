package com.zc.githubdroid.github.hotrepo.repolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zc.githubdroid.R;
import com.zc.githubdroid.github.hotrepo.repolist.model.Repo;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 16-8-30.
 *      仓库列表适配器
 */
public class RepoListAdapter extends BaseAdapter{

    private ArrayList<Repo> datas;//仓库

    public RepoListAdapter(){
        datas = new ArrayList<>();
    }

    //加载
    public void addAll(Collection<Repo> repos){
        datas.addAll(repos);
        notifyDataSetChanged();
    }

    //清空
    public void clear(){
        datas.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas == null?0:datas.size();
    }

    @Override
    public Repo getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_repo, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        Repo repo = datas.get(position);
        viewHolder.tvRepoName.setText(repo.getFullName());
        viewHolder.tvRepoInfo.setText(repo.getDescription());
        viewHolder.tvRepoStars.setText(repo.getStarCount()+"");
        //获取头像地址
        ImageLoader.getInstance().displayImage(repo.getOwner().getAvatar(), viewHolder.ivIcon);
        return convertView;
    }

    static class ViewHolder{
        @BindView(R.id.ivIcon) ImageView ivIcon;//头像
        @BindView(R.id.tvRepoName) TextView tvRepoName;//名字
        @BindView(R.id.tvRepoInfo) TextView tvRepoInfo;//描述信息
        @BindView(R.id.tvRepoStars) TextView tvRepoStars;//星星数量

        ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }

}
