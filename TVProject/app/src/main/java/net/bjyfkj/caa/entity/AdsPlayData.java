package net.bjyfkj.caa.entity;

import java.util.List;

/**
 * Created by YFKJ-1 on 2016/11/14.
 */
public class AdsPlayData {

    /**
     * time : 1479139200
     * type : 1
     * title : 6d4b8bd5
     * imglist : ["http:\/\/admin.bjyfkj.net\/Public\/.\/POSTER\/2016-11-14\/58291ba39826e.jpg","http:\/\/admin.bjyfkj.net\/Public\/.\/POSTER\/2016-11-14\/58291ba3984a5.jpg","http:\/\/admin.bjyfkj.net\/Public\/.\/POSTER\/2016-11-14\/58291ba3986eb.jpg"]
     * content : 54c854c854c85f7c6b645f7c6b64
     * shop_name :
     * shop_address :
     * qrcode : http://admin.bjyfkj.net/Public/QRCode/52.jpeg
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int id;


        private String time;
        private String type;
        private String title;
        private String imglist;
        private String content;
        private String shop_name;
        private String shop_address;
        private String qrcode;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

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
