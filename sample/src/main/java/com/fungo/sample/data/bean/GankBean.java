package com.fungo.sample.data.bean;

import java.util.List;

/**
 * @author Pinger
 * @since 18-12-3 上午10:59
 */
public class GankBean {


    private boolean error;
    private List<ResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * _id : 5be404e49d21223dd88989d3
         * createdAt : 2018-11-08T09:41:56.840Z
         * desc : 微博,微信图库效果,微信视频拖放效果
         * images : ["https://ww1.sinaimg.cn/large/0073sXn7gy1fxno5tb0h2g308w0g01l3","https://ww1.sinaimg.cn/large/0073sXn7gy1fxno5xiiyrg308w0g0npi"]
         * publishedAt : 2018-11-28T00:00:00.0Z
         * source : web
         * type : Android
         * url : https://github.com/moyokoo/Diooto
         * used : true
         * who : miaoyj
         */

        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;
        private List<String> images;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }

    @Override
    public String toString() {
        return "GankBean{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
