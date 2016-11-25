package net.bjyfkj.caa.entity;

import java.util.List;

/**
 * Created by YFKJ-1 on 2016/11/14.
 */
public class AdsPlayData {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * time : 1479916800
         * type : 0
         * id : 64
         * title : 53e37f69ceshi
         * imglist : ["http:\/\/admin.bjyfkj.net\/Public\/.\/POSTER\/2016-11-22\/5833b475723c4.jpg","http:\/\/admin.bjyfkj.net\/Public\/.\/POSTER\/2016-11-22\/5833b47572658.jpg","http:\/\/admin.bjyfkj.net\/Public\/.\/POSTER\/2016-11-22\/5833b47572861.jpg"]
         * item_img :
         * description :
         * content : 60a8597d
         * shop_name : 5bc696c6
         * shop_address : 603b53f84ee4
         * qrcode : http://admin.bjyfkj.net/Public/QRCode/98-1476701094071.jpeg
         */
        private String time;
        private String type;
        private String id;
        private String title;
        private String imglist;
        private String item_img;
        private String description;
        private String content;
        private String shop_name;
        private String shop_address;
        private String qrcode;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImglist() {
            return imglist;
        }

        public void setImglist(String imglist) {
            this.imglist = imglist;
        }

        public String getItem_img() {
            return item_img;
        }

        public void setItem_img(String item_img) {
            this.item_img = item_img;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getShop_address() {
            return shop_address;
        }

        public void setShop_address(String shop_address) {
            this.shop_address = shop_address;
        }

        public String getQrcode() {
            return qrcode;
        }

        public void setQrcode(String qrcode) {
            this.qrcode = qrcode;
        }
    }
}
