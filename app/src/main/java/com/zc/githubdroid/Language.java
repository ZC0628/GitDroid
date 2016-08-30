package com.zc.githubdroid;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 16-8-30.
 *          水平列表
 *          根据语言不同获取不同的数据
 */
public class Language implements Serializable{

    /**
     * {
     "path": "java",
     "name": "Java"
     }
     */

    private String path;
    private String name;

    private static List<Language> languages;

    //对本地的Json字符串文件进行读取和解析
    public static List<Language> getDefaultLanguage(Context context){
        if (languages!=null){
            return languages;
        }
        try {
            InputStream inputStream = context.getAssets().open("langs.json");//打开文件
            String content = IOUtil.toString(inputStream);//流转换成字符串
            Gson gson = new Gson();
            //fromJson——String的数——>List集合
            languages = gson.fromJson(content,new TypeToken<List<Language>>(){}.getType());
            return languages;
        } catch (IOException e) {
            throw new RuntimeException();//抛出运行时异常
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
