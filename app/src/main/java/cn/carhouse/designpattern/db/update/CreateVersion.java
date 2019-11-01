package cn.carhouse.designpattern.db.update;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: CreateVersion
 * @Description: 数据库升级创建表脚本
 */
public class CreateVersion {
    /**
     * 版本信息
     */
    private String version;

    /**
     * 创建数据库表脚本
     */
    private List<CreateDb> createDbs;

    public CreateVersion(Element ele) {
        version = ele.getAttribute("version");
        {
            createDbs = new ArrayList<>();
            NodeList cs = ele.getElementsByTagName("CreateDb");
            for (int i = 0; i < cs.getLength(); i++) {
                Element ci = (Element) (cs.item(i));
                CreateDb cd = new CreateDb(ci);
                this.createDbs.add(cd);
            }
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<CreateDb> getCreateDbs() {
        return createDbs;
    }

    public void setCreateDbs(List<CreateDb> createDbs) {
        this.createDbs = createDbs;
    }

}
