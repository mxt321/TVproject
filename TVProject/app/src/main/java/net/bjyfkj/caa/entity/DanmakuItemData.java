package net.bjyfkj.caa.entity;

import java.util.List;

/**
 * Created by YFKJ-1 on 2016/11/21.
 */

public class DanmakuItemData {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * user_id : 用户id
         * device_id : 电视编号
         * nickname : 微信昵称
         * content : 优惠券内容
         */

        private String user_id;
        private String device_id;
        private String nickname;
        private String content;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
