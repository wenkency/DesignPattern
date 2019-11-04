package cn.carhouse.designpattern.db.update;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import cn.carhouse.designpattern.bean.User;
import cn.carhouse.designpattern.db.core.IQuickDao;
import cn.carhouse.designpattern.db.core.QuickDaoFactory;
import cn.carhouse.designpattern.db.utils.QuickDaoUtils;
import cn.carhouse.designpattern.db.utils.ThreadUtils;

/**
 * 数据库更新的
 */
public class UpdateDbUtils {


    public static final String USER = "user";
    public static final String LOGIN = "login";
    private List<User> userList;

    /**
     * 数据库版本升级
     */
    public void update(final Context context, final String oldVersion, final String newVersion) {
        ThreadUtils.getNormalPool().execute(new Runnable() {
            @Override
            public void run() {
                doUpdate(context, oldVersion, newVersion);
            }
        });
    }

    private void doUpdate(Context context, String oldVersion, String newVersion) {
        IQuickDao<User> quickDao = QuickDaoFactory.getInstance().getQuickDao(User.class);
        userList = quickDao.query().queryAll();
        if (userList == null || userList.size() <= 0) {
            Log.e("UpdateDbUtils", "查不到用户");
            return;
        }
        // 1. 新版本数据库建表
        UpdateDbXml updateDbXml = readDbXml(context);
        // 2. 找到创建表的对象
        CreateVersion createVersion = getCreateVersion(updateDbXml, newVersion);
        if (createVersion != null) {
            createVersionDB(context, createVersion);
        }
        // 3. 更新操作
        UpdateStep updateStep = getUpdateStep(updateDbXml, oldVersion, newVersion);
        if (updateStep != null) {
            updateStep(context, createVersion, updateStep);
        }
        Log.e("UpdateDbUtils", "升级成功");
        // 更新数据
        QuickDaoFactory.getInstance().clearCache();
    }

    private void updateStep(Context context, CreateVersion createVersion, UpdateStep updateStep) {
        List<UpdateDb> updateDbs = updateStep.getUpdateDbs();
        for (UpdateDb updateDb : updateDbs) {
            List<String> befores = updateDb.getSqlBefores();
            String dbName = updateDb.getDbName();
            List<String> afters = updateDb.getSqlAfters();
            if (USER.equalsIgnoreCase(dbName)) {
                // 1. rename: alter table tb_user rename to back_tb_user;
                // 2. 建表tb_user
                // 3. 查询备份back_tb_user的内容插入tb_user
                // 4. 删除备份back_tb_user表
                // 公共库处理
                SQLiteDatabase sqLiteDatabase = QuickDaoUtils.getPublicSQLiteDatabase(context);
                // before
                executeSql(sqLiteDatabase, befores);
                // crate
                CreateDb createDb = getCreateDb(createVersion, dbName);
                executeSql(sqLiteDatabase, createDb.getSqlCreates());
                // after
                executeSql(sqLiteDatabase, afters);
                sqLiteDatabase.close();

            } else if (LOGIN.equalsIgnoreCase(dbName)) {
                // 私有库处理
                for (User user : userList) {
                    SQLiteDatabase sqLiteDatabase = QuickDaoUtils.getPrivateSQLiteDatabase(context, user.getUserId());
                    executeSql(sqLiteDatabase, befores);
                    // crate
                    CreateDb createDb = getCreateDb(createVersion, dbName);
                    executeSql(sqLiteDatabase, createDb.getSqlCreates());
                    // after
                    executeSql(sqLiteDatabase, afters);
                    sqLiteDatabase.close();
                }
            }
        }
    }

    private UpdateStep getUpdateStep(UpdateDbXml updateDbXml, String oldVersion, String newVersion) {
        if (updateDbXml == null) {
            return null;
        }
        UpdateStep updateStep = null;
        List<UpdateStep> updateSteps = updateDbXml.getUpdateSteps();
        for (UpdateStep step : updateSteps) {
            String versionFrom = step.getVersionFrom();
            String versionTo = step.getVersionTo();
            if (TextUtils.isEmpty(versionFrom) || TextUtils.isEmpty(versionTo)) {
                continue;
            }
            String[] fromList = versionFrom.split(",");
            for (String from : fromList) {
                // 有一个配到update节点即升级数据
                if (oldVersion.equalsIgnoreCase(from) && versionTo.equalsIgnoreCase(newVersion)) {
                    updateStep = step;
                    break;
                }
            }
        }
        return updateStep;
    }

    private CreateDb getCreateDb(CreateVersion createVersion, String dbName) {
        List<CreateDb> createDbs = createVersion.getCreateDbs();
        for (CreateDb createDb : createDbs) {
            if (dbName.equalsIgnoreCase(createDb.getName())) {
                return createDb;
            }
        }
        return null;
    }

    private void createVersionDB(Context context, CreateVersion createVersion) {
        List<CreateDb> createDbs = createVersion.getCreateDbs();
        for (CreateDb createDb : createDbs) {
            String name = createDb.getName();
            List<String> sqlCreates = createDb.getSqlCreates();
            if (USER.equalsIgnoreCase(name)) {
                // 公共库处理
                SQLiteDatabase sqLiteDatabase = QuickDaoUtils.getPublicSQLiteDatabase(context);
                executeSql(sqLiteDatabase, sqlCreates);
                sqLiteDatabase.close();
            } else if (LOGIN.equalsIgnoreCase(name)) {
                // 私有库处理
                for (User user : userList) {
                    SQLiteDatabase sqLiteDatabase = QuickDaoUtils.getPrivateSQLiteDatabase(context, user.getUserId());
                    executeSql(sqLiteDatabase, sqlCreates);
                    sqLiteDatabase.close();
                }
            }
        }
    }

    private void executeSql(SQLiteDatabase sqLiteDatabase, List<String> sqlList) {
        // 检查参数
        if (sqlList == null || sqlList.size() == 0) {
            return;
        }
        // 事务
        sqLiteDatabase.beginTransaction();
        for (String sql : sqlList) {
            sql = sql.replaceAll("\r\n", " ")
                    .replaceAll("\n", " ");
            if (!TextUtils.isEmpty(sql)) {
                try {
                    sqLiteDatabase.execSQL(sql);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    /**
     * 创建数据库,获取数据库对应的SQLiteDatabase
     *
     * @param dbName
     * @return 设定文件
     * @throws throws [违例类型] [违例说明]sta
     * @see
     */
    private SQLiteDatabase getSQLiteDatabase(Context context, String dbName, String userId) {
        // 公有数据库 user.db
        if (QuickDaoUtils.COMM_DB.equalsIgnoreCase(dbName)) {
            return QuickDaoUtils.getPublicSQLiteDatabase(context);
        } else if (QuickDaoUtils.PRIVATE_DB.equalsIgnoreCase(dbName)) {
            return QuickDaoUtils.getPrivateSQLiteDatabase(context, userId);
        }
        return null;
    }


    private CreateVersion getCreateVersion(UpdateDbXml updateDbXml, String newVersion) {
        CreateVersion createVersion = null;
        if (updateDbXml != null && !TextUtils.isEmpty(newVersion)) {
            List<CreateVersion> createVersions = updateDbXml.getCreateVersions();
            for (CreateVersion version : createVersions) {
                if (version.getVersion().equalsIgnoreCase(newVersion)) {
                    createVersion = version;
                    break;
                }
            }
        }
        return createVersion;
    }

    /**
     * 读取升级xml
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
