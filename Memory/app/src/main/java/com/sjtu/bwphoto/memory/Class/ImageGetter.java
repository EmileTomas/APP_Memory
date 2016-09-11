package com.sjtu.bwphoto.memory.Class;

/**
 * Created by ly on 8/26/2016.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class ImageGetter {

    private List<String> list = new ArrayList<String>();
    private File file = null;

    public List<String> getFile(String path) {
        try {
            file = new File(path);
            if (file.exists() && file.isDirectory()) {// 检查path是否存在,并且是一个目录
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        System.out.println(f.getPath());
                        list.add(f.getPath());
                        // Log.i("path", f.getPath());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getAllFile(String path) {
        this.getFile(path);
        for (int i = 0; i < list.size(); i++) {
            this.getFile(list.get(i));
        }
        return list;
    }

    /**
     * 根据后缀名过滤文件
     *
     * @param suffixs
     *            后缀名数组
     * @param path
     *            路径
     * @return 指定后缀名的文件集合
     */
    public List<String> filterFileBySuffix(String[] suffixs, String path) {
        List<String> files = this.getAllFile(path);
        List<String> suffixList = new ArrayList<String>();
        for (String filePath : files) {
            file = new File(filePath);
            if (file.exists() && file.isFile()) {
                String tempSuffix = filePath.substring(filePath.lastIndexOf(".") + 1);
                for (String suffix : suffixs) {
                    if (suffix.toLowerCase().equals(tempSuffix.toLowerCase())) {
                        suffixList.add(filePath);
                        Log.i("filePath", filePath);
                        Log.i("tempSuffix", tempSuffix);
                    }
                }
            }
        }
        suffixs = null;
        files = null;
        return suffixList;
    }

}
