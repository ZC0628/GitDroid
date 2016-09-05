package com.zc.githubdroid.gank.model;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import com.zc.githubdroid.R;
import com.zc.githubdroid.commons.ActivityUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *      每日干货
 */
public class GankFragment extends Fragment implements GankPresenter.GankView{

    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.btnFilter)
    ImageButton btnFilter;//日历选择的控件
    @BindView(R.id.content)
    ListView content;
    @BindView(R.id.emptyView)
    FrameLayout emptyView;

    private Date date;//获取当前时间（毫秒数）  2016-09-05
    private SimpleDateFormat simpleDateFormat;//时间格式规范处理
    private Calendar calendar;//年月日的形式（日历类）

    private GankPresenter gankPresenter;
    private GankAdapter adapter;

    private ActivityUtils activityUtils;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        calendar = Calendar.getInstance(Locale.CHINA);//以中文形式进行展示
        //获取当前的时间
        date = new Date(System.currentTimeMillis());
        gankPresenter = new GankPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gank, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        //规范我们日期格式
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        tvDate.setText(simpleDateFormat.format(date));
        adapter = new GankAdapter();
        content.setAdapter(adapter);
        //item的点击跳转网址
        content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = adapter.getItem(position).getUrl();//item点击的网址
                activityUtils.startBrowser(url);//跳转浏览器
            }
        });

        gankPresenter.getGanks(date);
    }

    //显示时间控件
    @OnClick(R.id.btnFilter)
    public void showDateDialog(View view){
        int year = calendar.get(Calendar.YEAR);//根据当前某个字段获取当前年份
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //日历选择器
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),dateSetListener,year,month,day);
        datePickerDialog.show();//展示
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        //当我们选择了日期，就会触发，也就是说日期发生了变化
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(year,monthOfYear,dayOfMonth);//当前时间设置到日历上
            date = calendar.getTime();
            tvDate.setText(simpleDateFormat.format(date));//当前新的选择时间设置上去
            //更新了日期，重新执行业务，重载加载数据
            gankPresenter.getGanks(date);
        }
    };

//    1. 设置数据
    @Override
    public void setData(List<GankItem> list) {
        adapter.setDatas(list);
        adapter.notifyDataSetChanged();
    }

//    2. 显示空视图
    @Override
    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);//
        content.setVisibility(View.GONE);//Listview隐藏
    }

//    3. 显示信息
    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

//    4. 隐藏空视图
    @Override
    public void hideEmptyView() {
        emptyView.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
    }
}
