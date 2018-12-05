package com.fungo.sample.data.bean;

import java.util.List;

/**
 * @author Pinger
 * @since 18-12-5 下午3:40
 */
public class NovelDetailBean {


    /**
     * BookList : [{"Id":671,"Name":"校花的贴身高手","Author":"鱼人二代","Img":"671.jpg","Desc":"校花的贴身高手最新章节列：小说《校花的贴身高手》鱼人二代/著,校花的贴身高手全文阅读一个大山里走出来的绝世高手，一块能预知未来的神秘玉佩\u2026\u2026林逸是一名普通学生，不过，他还身负另外一个重任，那就是追校花！......","CName":"玄幻奇幻","Score":3},{"Id":24960,"Name":"永恒圣帝","Author":"千寻月","Img":"yonghengshengdi.jpg","Desc":"【NEXTIDEA暨2015星创奖征文大赏（玄幻）】\r\n　　\r\n　　    【网文界第一位日更百万字最新力作，无限精彩！】\r\n　　\r\n　　    在这天地间，有一帝座，至高无上，主宰天下沉浮。\r\n　　\r\n　　    诸天万域，万千种族，亿万生灵，莫不俯首称臣。\r\n　　\r\n　　    大世天骄，万域纷争，争相竞逐无上帝座，角逐最强帝者。\r\n　　\r\n　　    一代人族绝世天骄叶晨渡史上最可怕的大劫而殒落，转世重生，带着前世记忆再度修炼，欲","CName":"玄幻奇幻","Score":6}]
     * Page : 1
     * HasNext : true
     */

    private int Page;
    private boolean HasNext;
    private List<BookListBean> BookList;

    public int getPage() {
        return Page;
    }

    public void setPage(int Page) {
        this.Page = Page;
    }

    public boolean isHasNext() {
        return HasNext;
    }

    public void setHasNext(boolean HasNext) {
        this.HasNext = HasNext;
    }

    public List<BookListBean> getBookList() {
        return BookList;
    }

    public void setBookList(List<BookListBean> BookList) {
        this.BookList = BookList;
    }

    public static class BookListBean {
        /**
         * Id : 671
         * Name : 校花的贴身高手
         * Author : 鱼人二代
         * Img : 671.jpg
         * Desc : 校花的贴身高手最新章节列：小说《校花的贴身高手》鱼人二代/著,校花的贴身高手全文阅读一个大山里走出来的绝世高手，一块能预知未来的神秘玉佩……林逸是一名普通学生，不过，他还身负另外一个重任，那就是追校花！......
         * CName : 玄幻奇幻
         * Score : 3
         */

        private int Id;
        private String Name;
        private String Author;
        private String Img;
        private String Desc;
        private String CName;
        private float Score;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getAuthor() {
            return Author;
        }

        public void setAuthor(String Author) {
            this.Author = Author;
        }

        public String getImg() {
            return Img;
        }

        public void setImg(String Img) {
            this.Img = Img;
        }

        public String getDesc() {
            return Desc;
        }

        public void setDesc(String Desc) {
            this.Desc = Desc;
        }

        public String getCName() {
            return CName;
        }

        public void setCName(String CName) {
            this.CName = CName;
        }

        public float getScore() {
            return Score;
        }

        public void setScore(float Score) {
            this.Score = Score;
        }
    }
}
