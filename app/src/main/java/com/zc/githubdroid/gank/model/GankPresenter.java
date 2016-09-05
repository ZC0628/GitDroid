package com.zc.githubdroid.gank.model;

import com.zc.githubdroid.commons.LogUtils;
import com.zc.githubdroid.gank.model.network.GankClient;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 16-9-5.
 *  每日干货业务类——数据加载
 */
public class GankPresenter {

    private Call<GankResult> call;
    private GankView gankView;

    public GankPresenter(GankView gankView) {
        this.gankView = gankView;
    }


    public void getGanks(Date date) {//传入日期
        //去做获取干货数据的操作
        Calendar calendar = Calendar.getInstance();//以日历的形式展示
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;//1月份返回的0
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        call = GankClient.getInstance().getDailyData(year, month, day);
        call.enqueue(callback);
    }



    //异步
    private Callback<GankResult> callback = new Callback<GankResult>() {
        @Override
        public void onResponse(Call<GankResult> call, Response<GankResult> response) {
            //请求结果
            GankResult gankResult = response.body();//先拿出响应体
            if (gankResult == null) {
                LogUtils.d("数据是空的");
                gankView.showMessage("数据是空的！");
                return;
            }

            if (gankResult.isError()//是否发生错误
                    || gankResult.getResult() == null//实体类是不是空的
                    || gankResult.getResult().getAndroidItems() == null//
                    || gankResult.getResult().getAndroidItems().isEmpty()) {//
                //要获取的数据是空的
                gankView.showEmptyView();
                return;
            }
            //有数据...
            List<GankItem> androidItems = gankResult.getResult().getAndroidItems();
            LogUtils.d("请求到的数据"+androidItems.size());
            //获取到的数据交给视图，让视图展示和改变
            gankView.hideEmptyView();
            gankView.setData(androidItems);
        }

        @Override
        public void onFailure(Call<GankResult> call, Throwable t) {
            //请求失败
            gankView.showMessage("Error："+t.getMessage());
        }
    };

    public interface GankView {
        /**
         *1. 设置数据
         * 2. 显示空视图
         * 3. 显示信息
         * 4. 隐藏空视图
         */
        void setData(List<GankItem> list);

        void showEmptyView();

        void showMessage(String msg);

        void hideEmptyView();
    }
}
