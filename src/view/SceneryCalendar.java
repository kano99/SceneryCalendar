package view;

import util.CalendarHelper;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Kano
 * @createTime 2019-08-21 10:53
 * @description 风景日历
 */
public class SceneryCalendar extends JFrame{
    CalendarPanel calendarPanel;//日历面板
    SceneryPanel sceneryPanel;//风景图片面板
    static final int IMAGE_AMOUNT = 20;//风景图片总数

    //初始化
    public SceneryCalendar(String title){
        super(title);
        calendarPanel = new CalendarPanel();
        sceneryPanel = new SceneryPanel();
        //向SceneryCalendar中加入两个Panel
        add(calendarPanel);
        add(sceneryPanel);
        //设置JFrame窗口的一些属性
        setVisible(true);
        setResizable(false);
        setSize(400,250);
        setLocation(200,100);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    //日历面板
    private class CalendarPanel extends JPanel{

        final String[] daysInWeek = {"周日","周一","周二","周三","周四","周五","周六"};
        CalendarHelper calHelper; //日历助手类的实例
        JLabel  yearLabel; //显示年份的标签
        JLabel  monthLabel; //显示月份的标签
        JTable calendarTable; //显示日历的表格
        JScrollPane jScrollPane; //包裹着calendarTable

        public CalendarPanel() {
            //实例化日历助手类
            calHelper = new CalendarHelper();
            //对日历面板进行设置
            setLayout(null);
            setSize(200,200);

            //初始化年份标签，以及左右的两个按钮：<< 和 >>
            yearLabel = new JLabel(calHelper.getYear()+"年");
            yearLabel.setBounds(80, 5, 45, 20);
            JButton yearLess = new JButton("<<");
            JButton yearAdd = new JButton(">>");
            yearLess.setMargin(new Insets(0,0,0,0));
            yearLess.setBounds(55,5,20,20);
            yearAdd.setMargin(new Insets(0,0,0,0));
            yearAdd.setBounds(125,5,20,20);

            //初始化月份标签，以及左右的两个按钮：<< 和 >>
            monthLabel = new JLabel(calHelper.getMonth()+"月");
            monthLabel.setBounds(90, 28, 40, 20);
            JButton monthLess = new JButton("<<");
            JButton monthAdd = new JButton(">>");
            monthLess.setMargin(new Insets(0,0,0,0));
            monthLess.setBounds(55,28,20,20);
            monthAdd.setMargin(new Insets(0,0,0,0));
            monthAdd.setBounds(125,28,20,20);

            //初始化日历表格
            calendarTable = new JTable(calHelper.getCalendar(calHelper.getYear(),calHelper.getMonth()), daysInWeek);
            calendarTable.setCellSelectionEnabled(false);
            //calendarTable.changeSelection(calHelper.getWeekOfMonth(),calHelper.getDayOfWeek(),false,false);
            jScrollPane = new JScrollPane(calendarTable);
            jScrollPane.setBounds(0,50,200,119);

            //将以上控件加入到calendarPanel中
            add(yearLabel);
            add(monthLabel);
            add(jScrollPane);
            add(yearLess);
            add(yearAdd);
            add(monthLess);
            add(monthAdd);

            //对日历面板进行点击监听，点击的时候更改风景图片
            calendarTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    int newImageNumber = (int) (Math.random() * IMAGE_AMOUNT) + 1;
                    Image newImage = sceneryPanel.getImage(newImageNumber);
                    sceneryPanel.imageShow.setIcon(new ImageIcon(newImage));
                    sceneryPanel.imagePicker.setSelectedIndex(newImageNumber - 1);
                }
            });
            //对调整年份和月份的四个按钮进行点击监听
            yearLess.addActionListener((e)->{
                changeCalendarTable(-1,0);
            });

            yearAdd.addActionListener((e)->{
                changeCalendarTable(1,0);
            });

            monthLess.addActionListener((e)->{
                changeCalendarTable(0,-1);
            });

            monthAdd.addActionListener((e)->{
                changeCalendarTable(0,-1);
            });
        }

        //更新日历的方法，同时也包含了对年份、月份标签的更新
        private void changeCalendarTable(int yearChange, int monthChange) {
            calHelper.changeYear(yearChange);
            calHelper.changeMonth(monthChange);
            String[][] newCalendar = calHelper.getCalendar(calHelper.getYear(), calHelper.getMonth());
            DefaultTableModel newModel = new DefaultTableModel(newCalendar, daysInWeek);
            calendarTable.setModel(newModel);
            yearLabel.setText(calHelper.getYear() + "年");
            monthLabel.setText(calHelper.getMonth() + "月");
            this.updateUI();
        }
    }

    //风景图片面板
    private class SceneryPanel extends JPanel{
        JLabel imageShow; //用于显示风景图片
        JComboBox imagePicker; //用于挑选自己想要的图片

        public SceneryPanel() {
            setSize(200,200);
            setLayout(null);
            //初始化风景图片
            imageShow = new JLabel();
            imageShow.setIcon(new ImageIcon(getImage(1)));
            imageShow.setBounds(200,20,200,200);
            //初始化一个标签，只是显示几个文字，无其他用途
            JLabel tipsLabel = new JLabel("选择图片：");
            tipsLabel.setBounds(220,0,90,20);
            //初始化用于挑选图片的下拉菜单
            imagePicker = new JComboBox();
            imagePicker.setBounds(300,0,50,20);
            for(int i = 1; i <= IMAGE_AMOUNT; i++){
                imagePicker.addItem(i+"");
            }
            //对下拉菜单进行点击监听，更改选项的时候，切换相应的风景图片
            imagePicker.addItemListener((e)->{
                int selected = imagePicker.getSelectedIndex() + 1;
                imageShow.setIcon(new ImageIcon(getImage(selected)));
            });
            //将以上控件加入到风景图片面板中
            this.add(imageShow);
            this.add(tipsLabel);
            this.add(imagePicker);
        }
        //用于从图片文件中加载图像的方法
        Image getImage(int imageNumber){
            BufferedImage src = null;
            try {
                src = ImageIO.read(getClass().getResource("/res/" + imageNumber + ".jpg")); // 读入文件
            }catch (IOException e){
                e.printStackTrace();
            }
            Image image = src.getScaledInstance(200,200, Image.SCALE_SMOOTH);
            return image;
        }
    }
}
