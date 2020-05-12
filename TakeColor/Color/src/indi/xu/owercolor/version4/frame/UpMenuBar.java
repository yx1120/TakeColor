package indi.xu.owercolor.version4.frame;

import indi.xu.owercolor.version4.util.PropertiesUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author a_apple
 */
class UpMenuBar extends JMenuBar {

    private JMenu setMenu;
     private JMenuItem hotKeyItem;
     private JMenuItem sysColorItem;
     /**
     * 1. setMenu的子菜单
     * 2. 为倍数选项添加单选按钮组
     */
    private JMenu radiosOptionMenu;
    private ButtonGroup group;
    private JRadioButtonMenuItem towItem;
    private JRadioButtonMenuItem fourItem;
    private JRadioButtonMenuItem eightItem;

    private JMenu hisMenu;
    private JMenuItem saveItem;
    private JMenuItem exitItem;
    private JMenuItem openItem;

    private JMenu helpMenu;
    private JMenuItem aboutItem;
    private JMenuItem helpItem;
    private JRadioButtonMenuItem alwaysOnTopItem;

    /**
     * 子菜单+放大镜窗体size按钮体
     */
    private JMenu radiosOptionMenu2;
    private ButtonGroup group2;
    private JRadioButtonMenuItem halfBtnItem;
    private JRadioButtonMenuItem oneBtnItem;
    private JRadioButtonMenuItem oneHalfBtnItem;

    /**
     * 对话框
     */
    private KeyDialog dialog;
    private JButton dialogBtn;

    private JMenu fangMenu;
    private JMenuItem openFangItem;
    private JMenuItem closeFangItem;
    /**
     * 放大镜，初始化先隐藏
     */
    private Fang fang;
    private PropertiesUtils prop = new PropertiesUtils();
    private static final String MULTIPLE = "multiple";
    private static final String FANGSIZE = "fangSize";

    JMenuItem getSaveItem() {
        //提交到主界面进行事件注册,因为要显示到JList内
        return saveItem;
    }

    JMenuItem getOpenItem() {
        return openItem;
    }

    JMenuItem getExitItem() {
        return exitItem;
    }

    JMenuItem getSysColorItem() {
        return sysColorItem;
    }

    JRadioButtonMenuItem getAlwaysOnTopItem() {
        return alwaysOnTopItem;
    }

    UpMenuBar() {

        dialog = new KeyDialog(null);
        dialogBtn = dialog.getBtn();

        setMenu = new JMenu("设置");

        sysColorItem = new JMenuItem("系统取色");
        hotKeyItem = new JMenuItem("快捷键");
        //放大倍数

        //10.15 16:37
        radiosOptionMenu = new JMenu("放大");
        group = new ButtonGroup();

        //默认都不选中
        towItem = new JRadioButtonMenuItem("2倍");
        fourItem = new JRadioButtonMenuItem("4倍");
        eightItem = new JRadioButtonMenuItem("8倍");
        //注：读取配置文件。然后设置选中状态       2019-12.26-晚
        switch (prop.getValue(MULTIPLE, "2")) {
            case "2":
                towItem.setSelected(true);
                break;
            case "4":
                fourItem.setSelected(true);
                break;
            case "8":
                eightItem.setSelected(true);
                break;
            default:
                break;
        }

        //放大镜大小按钮组
        radiosOptionMenu2 = new JMenu("尺寸");
        group2 = new ButtonGroup();
        halfBtnItem = new JRadioButtonMenuItem("0.5倍");
        oneBtnItem = new JRadioButtonMenuItem("1.0倍");
        oneHalfBtnItem = new JRadioButtonMenuItem("1.5倍");
        switch (prop.getValue(FANGSIZE, "150")) {
            case "75":
                halfBtnItem.setSelected(true);
                break;
            case "150":
                oneBtnItem.setSelected(true);
                break;
            case "225":
                oneHalfBtnItem.setSelected(true);
                break;
            default:
                break;
        }

        //文件部分
        hisMenu = new JMenu("文件");
        saveItem = new JMenuItem("保存");
        openItem = new JMenuItem("打开");
        exitItem = new JMenuItem("退出");

        helpMenu = new JMenu("帮助");
        aboutItem = new JMenuItem("关于");
        helpItem = new JMenuItem("使用");
        alwaysOnTopItem = new JRadioButtonMenuItem("总是最前", false);

        fangMenu = new JMenu("放大镜");
        openFangItem = new JMenuItem("开启");
        closeFangItem = new JMenuItem("关闭");

        initMenuBar();
        initHotKey();
        itemEvent();

        fang = new Fang();
        fang.setVisible(false);
    }

    /**
     * 初始化菜单栏界面
     */
    private void initMenuBar() {

        //按钮组
        group.add(towItem);
        group.add(fourItem);
        group.add(eightItem);

        //放大倍数单选框
        radiosOptionMenu.add(towItem);
        radiosOptionMenu.add(fourItem);
        radiosOptionMenu.add(eightItem);

        //放大镜大小单选框
        group2.add(halfBtnItem);
        group2.add(oneBtnItem);
        group2.add(oneHalfBtnItem);
        radiosOptionMenu2.add(halfBtnItem);
        radiosOptionMenu2.add(oneBtnItem);
        radiosOptionMenu2.add(oneHalfBtnItem);

        //放大镜
        fangMenu.add(openFangItem);
        fangMenu.add(closeFangItem);
        fangMenu.addSeparator();
        fangMenu.add(radiosOptionMenu2);

        //“设置”菜单项
        setMenu.add(hotKeyItem);
        setMenu.add(sysColorItem);

        //添加2句
        setMenu.addSeparator();
        setMenu.add(radiosOptionMenu);

        //“历史”菜单项
        hisMenu.add(saveItem);
        hisMenu.add(openItem);
        hisMenu.add(exitItem);

        //帮助菜单项
        helpMenu.add(aboutItem);
        helpMenu.add(helpItem);
        helpMenu.add(alwaysOnTopItem);

        //菜单栏添加菜单
        this.add(hisMenu);
        this.add(setMenu);
        this.add(helpMenu);
        this.add(fangMenu);

        itemsSetFont();
    }

    /**
     * 菜单栏字体设置
     */
    private void itemsSetFont() {
        Font font1 = new Font("微软雅黑", Font.BOLD, 16);
        Component[] comps = this.getComponents();
        for (Component comp : comps) {
            comp.setFont(font1);
        }
    }

    /**
     * 菜单栏快捷键设置
     */
    private void initHotKey() {
        openItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        helpItem.setAccelerator(KeyStroke.getKeyStroke("ctrl H"));
    }

    /**
     * 菜单栏事件绑定
     */
    private void itemEvent() {

        //使用单选框优化
        toItemEvent(towItem, MULTIPLE, "2");
        toItemEvent(fourItem, MULTIPLE, "4");
        toItemEvent(eightItem, MULTIPLE, "8");

        //放大镜尺寸 --同上
        toItemEvent(halfBtnItem, FANGSIZE, "75");
        toItemEvent(oneBtnItem, FANGSIZE, "150");
        toItemEvent(oneHalfBtnItem, FANGSIZE, "225");

        //菜单栏快捷键设置
        hotKeyItem.addActionListener(e -> dialog.setVisible(true));

        aboutItem.addActionListener(e -> {
            String info = "作者: yx qzx lj\n有问题，请联系\n 12345678@qq.com";
            if (e.getSource() == aboutItem) {
                JOptionPane.showMessageDialog(null, info,
                        "关于", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        helpItem.addActionListener(e -> {
            String info = "1.默认取色快捷键为Alt+C\n" +
                    "2.点击列表即可复制到剪切板\n" +
                    "3.使用快捷键的同时，复制到剪切板\n" +
                    "4.自定义放大镜大小后，需要重启";
            JOptionPane.showMessageDialog(null, info,
                    "帮助", JOptionPane.INFORMATION_MESSAGE);
        });

        dialogBtn.addActionListener(e -> dialog.setVisible(false));
        openFangItem.addActionListener(e -> {
            fang.setVisible(true);
            fang.setAlwaysOnTop(true);
        });
        closeFangItem.addActionListener(e -> fang.setVisible(false));

    }

    /**
     * 传入组件，放大倍数Item事件绑定
     *
     * @param item  JMenuItem对象
     * @param key   配置文件的键
     * @param value 配置文件的值
     */
    private void toItemEvent(JMenuItem item, String key, String value) {
        /*
            multiple
            fangSize
        */
        item.addActionListener(e -> prop.setValue(key, value));
    }
}
