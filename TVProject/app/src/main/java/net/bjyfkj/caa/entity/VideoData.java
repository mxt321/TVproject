package net.bjyfkj.caa.entity;

import java.util.List;

/**
 * Created by YFKJ-1 on 2016/11/5.
 */
public class VideoData {

    /**
     * id : 5
     * title : 53734f7f662f59296daf6d7789d262114e5f8ffd5bfb4f60
     * url : http://admin.bjyfkj.net/Public/Video/2016-11-05/581d5485a354c.mp4
     * preview : http://admin.bjyfkj.net/Public/Video/2016-11-05/581d54859469c.jpg
     * create_time : 1478317189
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String title;
        private String url;
        private String preview;
        private String create_time;

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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPreview() {
            return preview;
        }

        public void setPreview(String preview) {
            this.preview = preview;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
