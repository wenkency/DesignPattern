package cn.carhouse.designpattern.db.update;

import android.content.Context;

import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * 数据库更新的
 */
public class UpdateDbUtils {
    /**
     * 读取升级xml
     *
     * @param context
     * @return
     */
    private UpdateDbXml readDbXml(Context context) {
        InputStream is = null;
        Document document = null;
        try {
            is = context.getAssets().open("update_db.xml");
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = builder.parse(is);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (document == null) {
            return null;
        }
        UpdateDbXml xml = new UpdateDbXml(document);
        return xml;
    }
}
