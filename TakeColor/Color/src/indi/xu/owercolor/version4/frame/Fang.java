package indi.xu.owercolor.version4.frame;

import indi.xu.owercolor.version4.util.PropertiesUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;


/**
 * @author a_apple
 */
public class Fang extends JFrame {

    /**
     * 放大镜x坐标
     * 计算方式：setX = absolute - relativeX
     */
    private int fangPanelX;
    /**
     * 放大镜y坐标
     * 计算方式：setY = absoluteY - relativeY
     */
    private int fangPanelY;

    private int mouseX;//鼠标绝对x坐标(相对（0,0）坐标)

    private int mouseY;//鼠标绝对y坐标

    private int relativeX;//鼠标按下时的相对x坐标（相对组件左上角坐标）

    private int relativeY;//鼠标按下时的相对y坐标

    private boolean mousePressed;//标记鼠标是否按下。如果按下则为true，否则为false

    private int fangSize;//放大镜尺寸

    private FangDaJPanel fangJPanel;//放大镜内容面板

    private Robot robot;

    private PropertiesUtils prop = new PropertiesUtils();//配置文件


    public Fang() {
        fangSize = Integer.parseInt(prop.getValue("fangSize", "150"));
        fangJPanel = new FangDaJPanel(fangSize);
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        setUndecorated(true);
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(fangJPanel);
        //事件监听
        this.addMouseListener(new MouseFunctions());
        this.addMouseMotionListener(new MouseMotionFunctions());
        //更新尺寸
        updateSize(fangSize);
        this.setVisible(true);
    }

    /**
     * 更新窗体
     *
     * @param fangSize 放大镜尺寸
     */
    private void updateSize(int fangSize) {
        fangJPanel.setFangSize(fangSize);
        this.setBounds(0, 0, fangSize, fangSize);
        validate(); // 更新所有子控件
    }

    //鼠标设配器
    private class MouseFunctions extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            // 如果鼠标左键点了一下，说明按住了窗体
            if (e.getClickCount() == 1) {
                mousePressed = true;
                //先隐藏自己，截图后显示。这是为了防止把自己截图进去
                Fang.this.setVisible(false);
                //点击的时候截图
                Image screenImage = robot.createScreenCapture(
                        new Rectangle(0, 0,
                        Toolkit.getDefaultToolkit().getScreenSize().width,
                        Toolkit.getDefaultToolkit().getScreenSize().height));
                //传递传递
                Fang.this.setVisible(true);
                fangJPanel.setScreenImage(screenImage);
                //读配置，放大倍数，实现动态修改放大倍数
                int multiple = Integer.parseInt(prop.getValue("multiple", "2"));
                fangJPanel.setMultiple(multiple);

                relativeX = e.getX();
                relativeY = e.getY();
                /*my-
                    这2个位置指的是相对组件左上角那个坐标的位置
                    getX():返回事件相对于源组件FangJFrame的水平x位置。
                    getY():返回事件相对于源组件FangJFrame的垂直y位置。
                * */
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mousePressed = false;
        }
    }

    //MouseMotionAdapter:鼠标运动适配器

    private class MouseMotionFunctions extends MouseMotionAdapter {
        /* my-
         * mouseDragged:鼠标拖动事件
         * java.awt.Component
         * public Point getLocationOnScreen() :以指定组件左上角的点的形式获取此组件的位置。
         * 返回值：Point :代表组件左上角的点的位置。
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            if (mousePressed) {
                /*  public Point getLocationOnScreen()
                 *  以指定组件左上角的点的形式获取此组件的位置。
                 * 如果此时鼠标按下了，说明在拖拽窗体
                 * -my-
                 * my-container.getLocationOnScreen().x
                 * absoluteX:此时鼠标的x坐标
                 * absoluteY:此时鼠标的Y坐标
                 */
                //因为这是一个拖动事件，e.getX()一直在变化
                mouseX = Fang.this.getLocationOnScreen().x + e.getX();
                mouseY = Fang.this.getLocationOnScreen().y + e.getY();

                //my-设置放大镜frame的x,y坐标,relativeX是第一次点击的位置
                fangPanelX = mouseX - relativeX;
                fangPanelY = mouseY - relativeY;

                //-my-左上角的那个位置
                fangJPanel.setFangLocation(fangPanelX, fangPanelY);
                Fang.this.setLocation(fangPanelX, fangPanelY);
            }
        }
    }
}

class FangDaJPanel extends JPanel {

    private Image screenImage;
    private int fangSize;
    private int locationX;
    private int locationY;
    private int multiple;

    public void setScreenImage(Image screenImage) {
        this.screenImage = screenImage;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public void setFangSize(int fangSize) {
        this.fangSize = fangSize;
    }

    /**
     * @param fangSize 放大尺寸
     */
    FangDaJPanel(int fangSize) {
        //150x150
        this.fangSize = fangSize;
    }

    /**
     * 设置放大镜的位置
     *
     * @param locationX x坐标
     * @param locationY y坐标
     */
    public void setFangLocation(int locationX, int locationY) {
        //和FangFrame的拖动事件关联--位置不断改变--不断的画图
        this.locationX = locationX;
        this.locationY = locationY;

        //注意重画控件，画图
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        /*
        分析：
            image的处理：在点击的时候截当前的图，传给fangdaFrame就可以了 2019.9.26 21:20

        关键处理代码
        目标A矩形：2个坐标确定一个矩形。
        前4个参数：(x1,y1),(x2,y2)

        被放大区域B矩形：
        后4个参数：（x3,y3),(x4,y4)

        将B矩形映射到A矩形
        * */
        switch (multiple) {
            case 4:
                g.drawImage(screenImage,
                        0, 0, fangSize, fangSize,
                        locationX + (fangSize / 8 * 3),
                        locationY + (fangSize / 8 * 3),
                        locationX + (fangSize / 8 * 5),
                        locationY + (fangSize / 8 * 5),
                        this);
                break;
            case 2:
                g.drawImage(screenImage,
                        0, 0, fangSize, fangSize,
                        locationX + (fangSize / 4),
                        locationY + (fangSize / 4),
                        locationX + (fangSize / 4 * 3),
                        locationY + (fangSize / 4 * 3),
                        this);
                break;
            case 8:
                g.drawImage(screenImage,
                        0, 0, fangSize, fangSize,
                        locationX + (fangSize / 16 * 7),
                        locationY + (fangSize / 16 * 7),
                        locationX + (fangSize / 16 * 9),
                        locationY + (fangSize / 16 * 9),
                        this);
                break;
            default:
                break;
        }

        //外框
        Graphics2D g2d = (Graphics2D) g;
        //绘制三角形
        int[] x = {0, 30, 0, 0};
        int[] y = {0, 0, 30, 0};
        g2d.setColor(Color.green);
        g2d.fillPolygon(x, y, 4);

        int[] x2 = {fangSize, fangSize, fangSize - 30, fangSize};
        int[] y2 = {fangSize - 30, fangSize, fangSize, fangSize - 30};
        g2d.setColor(Color.red);
        g2d.fillPolygon(x2, y2, 4);
    }

    public static void main(String[] args) {
        new Fang();
    }
}
