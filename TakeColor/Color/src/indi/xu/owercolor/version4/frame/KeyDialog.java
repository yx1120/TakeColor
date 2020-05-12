package indi.xu.owercolor.version4.frame;

import indi.xu.owercolor.version4.util.PropertiesUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author a_apple
 */
class KeyDialog extends JDialog {
    private JTextField hotText;
    private JButton btn;
    private JButton clearBtn;
    private JLabel tipLabel;
    private int keyCode;
    private static int count = 0;
    private PropertiesUtils prop = new PropertiesUtils();

    JButton getBtn() {
        return btn;
    }

    KeyDialog(JFrame owner) {

        super(owner, "快捷键", true);
        hotText = new JTextField(8);
        btn = new JButton("确定");
        clearBtn = new JButton("清除");
        tipLabel = new JLabel("Alt为主键，输入一个副键:");

        this.setLayout(new FlowLayout());
        this.add(tipLabel);
        this.add(hotText);
        this.add(btn);
        this.add(clearBtn);

        this.setBounds(400, 400, 240, 160);

        hotTextEvent();
    }

    private void hotTextEvent() {
        //快捷键文本框
        hotText.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                keyCode = e.getKeyCode();
                String keyText = KeyEvent.getKeyText(keyCode);
                //只允许以alt为底键的组合快捷键
                hotText.setText(keyText);
                count++;
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        //清空按钮
        clearBtn.addActionListener(e -> {
            hotText.setText("");
            count = 0;
        });

        /**
         * 分析：第一次设置后，点击确定后，重置count=0,
         */
        //确定按钮
        btn.addActionListener(e -> {
            if (count == 1) {
                prop.setValue("hotKey", keyCode + "");
                count = 0;
            }
        });
    }
}
