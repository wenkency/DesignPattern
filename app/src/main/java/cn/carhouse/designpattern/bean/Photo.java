package cn.carhouse.designpattern.bean;


import androidx.annotation.NonNull;

import cn.carhouse.designpattern.db.annotation.DbTable;

/**
 * Created by 48608 on 2018/3/12.
 */
@DbTable("tb_photo")
public class Photo {
    private String time;
    private String path;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "time='" + time + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}








