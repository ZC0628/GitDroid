package com.zc.githubdroid.github.repoInfo;

/**
 * Created by Administrator on 2016/8/30.
 *
 *      仓库文本的结果
 */
public class RepoContentResult {

    /**
     * {
       "encoding":"base64",   加密
      "content":"encoded content ...."  加密的文本
      }
     *
     */
    private String encoding;
    private String content;

    public String getEncoding() {
        return encoding;
    }

    public String getContent() {
        return content;
    }
}
