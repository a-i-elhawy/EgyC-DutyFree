package com.mycompany.egyc_dutyfree5;

import com.toedter.calendar.JDateChooser;
//import static com.mycompany.egyc_dutyfree5.StartFrame.img;
import java.awt.CardLayout;

import java.sql.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;
//import com.toedter.calendar.JDateChooser;
//import static database.JFrame.datechooser;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
//import org.apache.commons.dbutils.DbUtils;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
//import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public final class MainFrame extends javax.swing.JFrame {

    Connection con;
    Statement stmt;
    ResultSet rs;
//    String host = "jdbc:ucanaccess://C:\\Users\\Public\\Documents\\accounts.accdb";
//    String uName = "Ahmed";
//    String uPass = "0000";
    String SQL1 = "SELECT * FROM Table1";
    String SQL2 = "SELECT * FROM Table2";
    String SQL3 = "SELECT * FROM Table3";
    String SQL4 = "SELECT * FROM taxTable";
//    String SQL4 = "SELECT * FROM Table3 LIMIT 0,3 ";
//    String SQL5 = "SELECT * FROM Table3 WHERE id =?  ";
//    String SQL6 = "SELECT * FROM Table3 WHERE addDate LIKE '%8%' ";
//    String SQL7 = "SELECT * FROM Table3 WHERE addDate LIKE '2023-__-%'";
    String filepath;
    String Name, UserName, EditDate, startDate, endDate, Category, lastScreen = "";
    public static String detectedBranch, DateFrom, DateTo;
    byte[] pImage = null;
    Image img = null;
    int searchid, month, year;
    int getmonth;
    String addDate;
    ImageIcon imageicon;
    double exchangePrice, cigPrice, cigarPrice;
    public static boolean isEgyNationalitySelected, isForNationalitySelected, isComponentVisible;

    String idTable1, idTable2, idTable3;
    // أسماء الشاشات للتعامل مع CardLayout
    private final String CARD_SETTINGS = "Settings";
    private final String CARD_TAXS = "Taxs";
    private final String CARD_REPORTS = "Reports";
    private final String CARD_BILLS = "Bills";

//private JPopupMenu suggestionPopup;
//private JList<String> suggestionList;
    private DefaultListModel<String> listModel;
    private List<String> allCodes; // قائمة تحتوي على جميع الأكواد المتاحة لديك من قاعدة البيانات

    JDateChooser datechooser = new JDateChooser();
    JDateChooser datechooser1 = new JDateChooser();
    JDateChooser datechooser2 = new JDateChooser();
    QueryResultFrame JFrameCodeSearch = new QueryResultFrame();
    DailySalesFrame JFrameDailySales = new DailySalesFrame();
    TaxQueryResultFrame TaxQueryResultFrame = new TaxQueryResultFrame();
    JOptionPane JOptionPane = new JOptionPane();
    ShowPictureFrame ShowPictureFrame = new ShowPictureFrame();
    DecimalFormat df = new DecimalFormat("#.#");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    JPanel panel = new JPanel();
    JPasswordField pass = new JPasswordField(10);
    JLabel label = new JLabel("أدخل كلمة المرور :");

    @SuppressWarnings("empty-statement")
    public MainFrame() {
        checkRemoteActivationOnStartup();
        initComponents();
        getTime();
        getToday();
        getmonth();
        show_data_of_count();
        fillBranchCombo();
        loadLocalBranchSettings();
        loadSettings();

        BillsButtons(true);
        ReportsButtons(false);
        TaxButtons(false);
        SettingsButtons(false);
        QuaryButtons(false);

        CheckCig.addItemListener(e -> jPanel001.setVisible(CheckCig.isSelected()));
//        CheckCigar.addItemListener(e -> jPanel002.setVisible(CheckCigar.isSelected()));
        CheckWin.addItemListener(e -> jPanel002.setVisible(CheckWin.isSelected()));
        Checkbeer.addItemListener(e -> jPanel003.setVisible(Checkbeer.isSelected()));
        CheckPerfum.addItemListener(e -> jPanel005.setVisible(CheckPerfum.isSelected()));
        CheckDevice.addItemListener(e -> jPanel006.setVisible(CheckDevice.isSelected()));
        CheckGift.addItemListener(e -> jPanel007.setVisible(CheckGift.isSelected()));
        CheckFood.addItemListener(e -> jPanel004.setVisible(CheckFood.isSelected())); // استبدلها بال panel ا

        // 1. إنشاء موديل القائمة والـ JList والـ JScrollPane ووضعها داخل الـ JPopupMenu المصمم
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> suggestionList = new JList<>(listModel);
// جعل الخط بحجم 12 و Bold مع الحفاظ على نوع الخط الافتراضي للنظام
        suggestionList.setFont(suggestionList.getFont().deriveFont(java.awt.Font.BOLD, 12f));
        JScrollPane scrollPane = new JScrollPane(suggestionList);

        suggestionPopup.removeAll();
        suggestionPopup.add(scrollPane);
        suggestionPopup.setFocusable(false);

// 2. جلب جميع الأكواد من قاعدة البيانات مرة واحدة عند فتح الشاشة
        List<String> allCodes = getAllCodesFromDatabase();

// 3. مراقبة الكتابة في حقل البحث txtSearch لحظة بلحظة مع تصحيح الأقواس
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterSuggestions(suggestionPopup, suggestionList, listModel, allCodes);
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterSuggestions(suggestionPopup, suggestionList, listModel, allCodes);
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterSuggestions(suggestionPopup, suggestionList, listModel, allCodes);
            }
        });

// 4. الحدث عند اختيار كود من القائمة بالماوس
        suggestionList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 1) {
                    String selectedCode = suggestionList.getSelectedValue();
                    if (selectedCode != null) {
                        txtSearch.setText(selectedCode);
                        suggestionPopup.setVisible(false);
                        GetTaxQuary();
                    }
                }
            }
        });

//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//
//// تحديد حجم التطبيق ليكون 80% من عرض الشاشة و 80% من ارتفاعها
//int width = (int)(screenSize.width * 0.8); 
//int height = (int)(screenSize.height * 0.8);
//
//this.setSize(width, height);
//
//// جعل التطبيق يظهر في المنتصف دائماً مهما كان حجم الشاشة
//this.setLocationRelativeTo(null);
        copyRightLab.setText("Created by_ Ahmed  Elhawy 2022" + " - " + year);
        copyRightLab1.setText("Created by_ Ahmed  Elhawy 2022" + " - " + year);
        lblPicDate.setText("");
        Count.setText(String.valueOf(1));

        animateWidthFromLeft(userName, 0, 250, 100);
        animateWidthFromLeft(password, 0, 250, 100);

        icon();

        centerCell3(tablecount1, SwingConstants.CENTER);
        centerheader(tablecount1, SwingConstants.CENTER);
        centerCell3(table, SwingConstants.CENTER);
        centerheader(table, SwingConstants.CENTER);
        centerCell3(tablefor, SwingConstants.CENTER);
        centerheader(tablefor, SwingConstants.CENTER);

        datechooser.setBounds(0, 0, 110, 27);
        datechooser.setVisible(true);
        datechooser.setDateFormatString("yyyy-MM-dd");
        datechooser.setBackground(Color.GRAY);
        datechooser.getCalendarButton().setBackground(Color.GRAY);
        jPanelDateTo.add(datechooser);

        datechooser1.setBounds(0, 0, 110, 27);
        datechooser1.setVisible(true);
        datechooser1.setDateFormatString("yyyy-MM-dd");
        datechooser1.setBackground(Color.GRAY);
        datechooser1.getCalendarButton().setBackground(Color.GRAY);
        jPanelDateFrom.add(datechooser1);

        datechooser2.setBounds(0, 0, 110, 27);
        datechooser2.setVisible(true);
        datechooser2.setDateFormatString("yyyy-MM-dd");
        datechooser2.setBackground(Color.GRAY);
        datechooser2.getCalendarButton().setBackground(Color.GRAY);
        jPanelShiftDate.add(datechooser2);
        setDefaultCloseOperation(MainFrame.EXIT_ON_CLOSE);

        ButtonGroup ButtonGroup = new ButtonGroup();
        ButtonGroup ButtonGroup1 = new ButtonGroup();
        ButtonGroup ButtonGroup2 = new ButtonGroup();
        ButtonGroup ButtonGroup3 = new ButtonGroup();

        ButtonGroup.add(RadiCodeNum);
        ButtonGroup.add(RadioOPDate);
        RadiCodeNum.setSelected(true);

        ButtonGroup1.add(RadioGetDaily);
        ButtonGroup1.add(RadioGetTax);
        ButtonGroup1.add(RadioGetReport);
        RadioGetDaily.setSelected(true);

        ButtonGroup2.add(RadioDiscount);
        ButtonGroup2.add(RadioImport);
        RadioDiscount.setSelected(true);

        ButtonGroup3.add(RadioGetOnlineDaily);
        ButtonGroup3.add(RadioGetOnlineItems);
        RadioGetOnlineDaily.setSelected(true);

        panel.add(pass);
        panel.add(label);

        JOptionPane.setFont(new Font("Traditional Arabic", Font.BOLD, 14));

        Delete.setEnabled(false);
        jLabel29.setVisible(false);
        jLabel30.setVisible(false);
        winTaxP.setVisible(false);
        winTaxD.setVisible(false);
//        ComboB_currancy.setVisible(false);
        jPanel13.setVisible(true);
        jLabel20.setVisible(false);
        input.setVisible(false);

        upload_downLabel.setVisible(false);

        winTaxP.setText("0");
        winTaxD.setText("0");
        

        jPanelReports.setVisible(false);
        SaveDailyReport.setVisible(false);

        jPanel5.setVisible(false);

        jSeparator10.setVisible(false);

        current_Balance.setEnabled(false);
        current_Balance1.setEnabled(false);
        view_Shift_Sales.setEnabled(false);
        view_Shift_Sales1.setEnabled(false);
        Edit.setEnabled(false);
        Edit2.setEnabled(false);
        Edit3.setEnabled(false);
        txtbranchName.setVisible(false);
        moreOption2.setVisible(false);
        moreOption.setVisible(false);

//        jLabel33.setVisible(false);
        Newpassword.setVisible(false);
        jSeparator9.setVisible(false);

//        jPanel22.setVisible(false);
        progBar.setVisible(false);
        InactivityManager.startInactivityMonitor(this);
//        Show_image.setVisible(false);
//        labelImage.setIcon(noImage);
//        BufferedImage bimage = ImageIO.read(new File (this.getClass().getResource("/images/no_image.png"))

lblPicShift.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupMenu = new javax.swing.JPopupMenu();
        Save_Edit = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        view_Shift_Sales = new javax.swing.JMenuItem();
        current_Balance = new javax.swing.JMenuItem();
        Dialog = new javax.swing.JDialog();
        jPanel14 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        popupMenu2 = new javax.swing.JPopupMenu();
        Save_Edit2 = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        SaveAsExcel = new javax.swing.JMenuItem();
        OpenExcel = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        Delete2 = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel30 = new javax.swing.JPanel();
        jDialogDaily = new javax.swing.JDialog();
        jPanel27 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();
        jPanel007 = new javax.swing.JPanel();
        txt1 = new javax.swing.JTextField();
        txt2 = new javax.swing.JTextField();
        txt21 = new javax.swing.JTextField();
        txt22 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel006 = new javax.swing.JPanel();
        txt3 = new javax.swing.JTextField();
        txt4 = new javax.swing.JTextField();
        txt23 = new javax.swing.JTextField();
        txt24 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel005 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt6 = new javax.swing.JTextField();
        txt5 = new javax.swing.JTextField();
        txt25 = new javax.swing.JTextField();
        txt26 = new javax.swing.JTextField();
        jPanel004 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt7 = new javax.swing.JTextField();
        txt8 = new javax.swing.JTextField();
        txt27 = new javax.swing.JTextField();
        txt28 = new javax.swing.JTextField();
        jPanel003 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txt9 = new javax.swing.JTextField();
        txt10 = new javax.swing.JTextField();
        txt30 = new javax.swing.JTextField();
        txt29 = new javax.swing.JTextField();
        jPanel002 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt11 = new javax.swing.JTextField();
        txt12 = new javax.swing.JTextField();
        txt31 = new javax.swing.JTextField();
        txt32 = new javax.swing.JTextField();
        jPanel001 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txt13 = new javax.swing.JTextField();
        txt14 = new javax.swing.JTextField();
        txt33 = new javax.swing.JTextField();
        txt34 = new javax.swing.JTextField();
        jSeparator20 = new javax.swing.JSeparator();
        jPanel16 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        txt35 = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        txt36 = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        Edit3 = new javax.swing.JButton();
        SaveDailyReport1 = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();
        jPanelShiftDate = new javax.swing.JPanel();
        ComboBoxShift = new javax.swing.JComboBox();
        suggestionPopup = new javax.swing.JPopupMenu();
        jPanelMainLogIn = new javax.swing.JPanel();
        jPanelLogInSreen = new javax.swing.JPanel();
        labelIcon = new javax.swing.JLabel();
        loginLabl = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        copyRightLab1 = new javax.swing.JLabel();
        loginLabelDate = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        userName = new javax.swing.JTextField();
        LogIn = new javax.swing.JButton();
        Exite = new javax.swing.JButton();
        password = new javax.swing.JPasswordField();
        Newpassword = new javax.swing.JPasswordField();
        setNewPassword = new javax.swing.JButton();
        jSeparator15 = new javax.swing.JSeparator();
        jPanelMainContainer = new javax.swing.JPanel();
        jPanelSpeace1 = new javax.swing.JPanel();
        jPanelContainer = new javax.swing.JPanel();
        jPanelScreens = new javax.swing.JPanel();
        jPanelBills = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        operation_date = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        labelImage = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        ComboB_type = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        RadioDiscount = new javax.swing.JRadioButton();
        RadioImport = new javax.swing.JRadioButton();
        jLabel19 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        search = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        output = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        input = new javax.swing.JTextField();
        price = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        count = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        billNum = new javax.swing.JTextField();
        name = new javax.swing.JTextField();
        passport = new javax.swing.JTextField();
        birthDate = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        ComboB_national = new javax.swing.JComboBox();
        jLabel29 = new javax.swing.JLabel();
        winTaxP = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        winTaxD = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        Show_image = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        notes = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tablecount1 = new javax.swing.JTable();
        jPanelReports = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        tab1 = new javax.swing.JTabbedPane();
        jScrollPane9 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jScrollPane12 = new javax.swing.JScrollPane();
        tablefor = new javax.swing.JTable();
        jPanelTaxs = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jPanel19 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        ComboB_type1 = new javax.swing.JComboBox();
        Description = new javax.swing.JTextField();
        Value = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        Count = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        Salesprice1 = new javax.swing.JTextField();
        FobValue = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        codeLebel = new javax.swing.JLabel();
        jPanelSettings = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        CigarPriceTxt = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jSeparator24 = new javax.swing.JSeparator();
        CigPriceTxt = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jSeparator25 = new javax.swing.JSeparator();
        exchangePriceTxt = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        CheckCig = new javax.swing.JCheckBox();
        CheckCigar = new javax.swing.JCheckBox();
        CheckWin = new javax.swing.JCheckBox();
        Checkbeer = new javax.swing.JCheckBox();
        CheckPerfum = new javax.swing.JCheckBox();
        CheckDevice = new javax.swing.JCheckBox();
        CheckGift = new javax.swing.JCheckBox();
        CheckFood = new javax.swing.JCheckBox();
        jSeparator12 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jPanel38 = new javax.swing.JPanel();
        ComboB_SubBranch = new javax.swing.JComboBox();
        jLabel34 = new javax.swing.JLabel();
        ItemsListEdit = new javax.swing.JButton();
        jPanelQuary = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jPanelDateTo = new javax.swing.JPanel();
        jLabelTo = new javax.swing.JLabel();
        jPanelDateFrom = new javax.swing.JPanel();
        jLabelFrom = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        RadioGetReport = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        RadioGetTax = new javax.swing.JRadioButton();
        jSeparator16 = new javax.swing.JSeparator();
        RadioGetDaily = new javax.swing.JRadioButton();
        jPanel34 = new javax.swing.JPanel();
        AppendData = new javax.swing.JButton();
        jSeparator22 = new javax.swing.JSeparator();
        uploadData = new javax.swing.JButton();
        jSeparator17 = new javax.swing.JSeparator();
        downloadData = new javax.swing.JButton();
        jSeparator18 = new javax.swing.JSeparator();
        ConnectionTest = new javax.swing.JButton();
        jSeparator19 = new javax.swing.JSeparator();
        jLabel24 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        ComboB_SubBranch1 = new javax.swing.JComboBox();
        jLabel50 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        checkBoxGetForNationality = new javax.swing.JCheckBox();
        jSeparator21 = new javax.swing.JSeparator();
        checkBoxGetEgyNationality = new javax.swing.JCheckBox();
        jPanel36 = new javax.swing.JPanel();
        RadioGetOnlineItems = new javax.swing.JRadioButton();
        jSeparator23 = new javax.swing.JSeparator();
        RadioGetOnlineDaily = new javax.swing.JRadioButton();
        jPanel26 = new javax.swing.JPanel();
        progBar = new javax.swing.JProgressBar();
        upload_downLabel = new javax.swing.JLabel();
        jPanelOptionsBar = new javax.swing.JPanel();
        GetFromExcel = new javax.swing.JButton();
        SaveAsExcel1 = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JSeparator();
        RadioOPDate = new javax.swing.JRadioButton();
        RadiCodeNum = new javax.swing.JRadioButton();
        code_view = new javax.swing.JButton();
        Delete = new javax.swing.JButton();
        Clear = new javax.swing.JButton();
        Refreh = new javax.swing.JButton();
        current_Balance1 = new javax.swing.JButton();
        view_Shift_Sales1 = new javax.swing.JButton();
        Details_of_DailySales = new javax.swing.JButton();
        Back = new javax.swing.JButton();
        Quary = new javax.swing.JButton();
        Edit2 = new javax.swing.JButton();
        Edit = new javax.swing.JButton();
        SaveDailyReport = new javax.swing.JButton();
        saveOperation = new javax.swing.JButton();
        searchByDate = new javax.swing.JButton();
        saveSettings = new javax.swing.JButton();
        GetTax = new javax.swing.JButton();
        moreOption = new javax.swing.JButton();
        txtbranchName = new javax.swing.JTextField();
        moreOption2 = new javax.swing.JButton();
        copyRightLab = new javax.swing.JLabel();
        jPanelDetailsBar = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        labelBranchName = new javax.swing.JLabel();
        labelShift = new javax.swing.JLabel();
        lblPicShift = new javax.swing.JLabel();
        jSeparator14 = new javax.swing.JSeparator();
        labelDate = new javax.swing.JLabel();
        lblPicDate = new javax.swing.JLabel();
        labelUser = new javax.swing.JLabel();
        lblPic = new javax.swing.JLabel();
        jSeparator13 = new javax.swing.JSeparator();
        jLabel31 = new javax.swing.JLabel();
        jPanelSpeace2 = new javax.swing.JPanel();
        drawerPanel = new javax.swing.JPanel();
        BillsNavigator = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        LogOut = new javax.swing.JButton();
        jSeparator11 = new javax.swing.JSeparator();
        TaxNavigator = new javax.swing.JButton();
        ReportsNavigator = new javax.swing.JButton();
        SettingsNavigator1 = new javax.swing.JButton();
        jPanelSpeace3 = new javax.swing.JPanel();

        popupMenu.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        popupMenu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Save_Edit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Save_Edit.setIcon(new javax.swing.ImageIcon("D:\\programs\\JavaApplications\\EgyC_DutyFree5.0.0\\src\\main\\resources\\edit_note.png")); // NOI18N
        Save_Edit.setText("حفظ التعديل  ");
        Save_Edit.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Save_Edit.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        Save_Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Save_EditActionPerformed(evt);
            }
        });
        popupMenu.add(Save_Edit);

        jSeparator2.setPreferredSize(new java.awt.Dimension(0, 15));
        popupMenu.add(jSeparator2);

        view_Shift_Sales.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        view_Shift_Sales.setIcon(new javax.swing.ImageIcon("D:\\programs\\JavaApplications\\EgyC_DutyFree5.0.0\\src\\main\\resources\\add_task.png")); // NOI18N
        view_Shift_Sales.setText("عرض يومية   ");
        view_Shift_Sales.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        view_Shift_Sales.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        view_Shift_Sales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                view_Shift_SalesActionPerformed(evt);
            }
        });
        popupMenu.add(view_Shift_Sales);

        current_Balance.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        current_Balance.setIcon(new javax.swing.ImageIcon("D:\\programs\\JavaApplications\\EgyC_DutyFree5.0.0\\src\\main\\resources\\checklist.png")); // NOI18N
        current_Balance.setText("عرض الأرصدة");
        current_Balance.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        current_Balance.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        current_Balance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                current_BalanceActionPerformed(evt);
            }
        });
        popupMenu.add(current_Balance);

        Dialog.setAlwaysOnTop(true);
        Dialog.setIconImage(null);
        Dialog.setLocation(new java.awt.Point(0, 0));
        Dialog.setLocationRelativeTo(null);
        Dialog.setModalityType(java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
        Dialog.setUndecorated(true);

        jLabel14.setForeground(new java.awt.Color(0, 102, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("جارى التحميل");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(315, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel14)
                .addGap(0, 20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout DialogLayout = new javax.swing.GroupLayout(Dialog.getContentPane());
        Dialog.getContentPane().setLayout(DialogLayout);
        DialogLayout.setHorizontalGroup(
            DialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DialogLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        DialogLayout.setVerticalGroup(
            DialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DialogLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        popupMenu2.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N

        Save_Edit2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Save_Edit2.setIcon(new javax.swing.ImageIcon("D:\\programs\\JavaApplications\\EgyC_DutyFree5.0.0\\src\\main\\resources\\edit_note.png")); // NOI18N
        Save_Edit2.setText("حفظ التعديل  ");
        Save_Edit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Save_Edit2ActionPerformed(evt);
            }
        });
        popupMenu2.add(Save_Edit2);

        jSeparator8.setPreferredSize(new java.awt.Dimension(0, 15));
        popupMenu2.add(jSeparator8);

        SaveAsExcel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        SaveAsExcel.setIcon(new javax.swing.ImageIcon("D:\\programs\\JavaApplications\\EgyC_DutyFree5.0.0\\src\\main\\resources\\format_list.png")); // NOI18N
        SaveAsExcel.setText("حفظ ك_ Excel");
        SaveAsExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveAsExcelActionPerformed(evt);
            }
        });
        popupMenu2.add(SaveAsExcel);

        OpenExcel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        OpenExcel.setIcon(new javax.swing.ImageIcon("D:\\programs\\JavaApplications\\EgyC_DutyFree5.0.0\\src\\main\\resources\\read_more.png")); // NOI18N
        OpenExcel.setText("فتح ملف Excel");
        OpenExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenExcelActionPerformed(evt);
            }
        });
        popupMenu2.add(OpenExcel);

        jSeparator4.setPreferredSize(new java.awt.Dimension(0, 15));
        popupMenu2.add(jSeparator4);

        Delete2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Delete2.setIcon(new javax.swing.ImageIcon("D:\\programs\\JavaApplications\\EgyC_DutyFree5.0.0\\src\\main\\resources\\delete.png")); // NOI18N
        Delete2.setText("حذف             ");
        Delete2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Delete2ActionPerformed(evt);
            }
        });
        popupMenu2.add(Delete2);

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jDialogDaily.pack();

        // 3. جعل النافذة تظهر في منتصف الشاشة الرئيسية تماماً (بدلاً من الركن)
        jDialogDaily.setLocationRelativeTo(this);
        jDialogDaily.setResizable(false);

        jPanel7 = new RoundedPanel();

        //jPanel007 = new RoundedPanel();
        jPanel007.setBackground(new java.awt.Color(255, 255, 255));
        jPanel007.setPreferredSize(new java.awt.Dimension(291, 76));

        txt1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt1.setNextFocusableComponent(txt34);

        txt2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt2.setNextFocusableComponent(txt1);

        txt21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt21.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt21.setNextFocusableComponent(txt35);

        txt22.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt22.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt22.setNextFocusableComponent(txt21);

        jLabel10.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("ق/ الهدايا");
        jLabel10.setPreferredSize(new java.awt.Dimension(56, 23));

        jLabel9.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("و/ الهدايا");
        jLabel9.setPreferredSize(new java.awt.Dimension(56, 23));

        javax.swing.GroupLayout jPanel007Layout = new javax.swing.GroupLayout(jPanel007);
        jPanel007.setLayout(jPanel007Layout);
        jPanel007Layout.setHorizontalGroup(
            jPanel007Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel007Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel007Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel007Layout.createSequentialGroup()
                        .addComponent(txt21, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel007Layout.createSequentialGroup()
                        .addComponent(txt22, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(txt2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jPanel007Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );
        jPanel007Layout.setVerticalGroup(
            jPanel007Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel007Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel007Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt22, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel007Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel007Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel007Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt21, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        //jPanel006 = new RoundedPanel();
        jPanel006.setBackground(new java.awt.Color(255, 255, 255));
        jPanel006.setPreferredSize(new java.awt.Dimension(291, 76));

        txt3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt3.setNextFocusableComponent(txt2);
        txt3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt3ActionPerformed(evt);
            }
        });

        txt4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt4.setNextFocusableComponent(txt3);

        txt23.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt23.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt23.setNextFocusableComponent(txt22);

        txt24.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt24.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt24.setNextFocusableComponent(txt23);

        jLabel18.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("ق/ الأجهزة");
        jLabel18.setPreferredSize(new java.awt.Dimension(56, 23));

        jLabel17.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("و/ الأجهزة");
        jLabel17.setPreferredSize(new java.awt.Dimension(56, 23));

        javax.swing.GroupLayout jPanel006Layout = new javax.swing.GroupLayout(jPanel006);
        jPanel006.setLayout(jPanel006Layout);
        jPanel006Layout.setHorizontalGroup(
            jPanel006Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel006Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel006Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt24, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt23, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(jPanel006Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel006Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );
        jPanel006Layout.setVerticalGroup(
            jPanel006Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel006Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel006Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt24, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel006Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt23, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        //jPanel005 = new RoundedPanel();
        jPanel005.setBackground(new java.awt.Color(255, 255, 255));
        jPanel005.setPreferredSize(new java.awt.Dimension(291, 76));

        jLabel6.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("و/ العطور");
        jLabel6.setPreferredSize(new java.awt.Dimension(56, 23));

        jLabel7.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("ق/ العطور");
        jLabel7.setPreferredSize(new java.awt.Dimension(56, 23));

        txt6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt6.setNextFocusableComponent(txt5);

        txt5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt5.setNextFocusableComponent(txt4);

        txt25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt25.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt25.setNextFocusableComponent(txt24);

        txt26.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt26.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt26.setNextFocusableComponent(txt25);

        javax.swing.GroupLayout jPanel005Layout = new javax.swing.GroupLayout(jPanel005);
        jPanel005.setLayout(jPanel005Layout);
        jPanel005Layout.setHorizontalGroup(
            jPanel005Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel005Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel005Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt25, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt26, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(jPanel005Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel005Layout.createSequentialGroup()
                        .addComponent(txt6, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel005Layout.createSequentialGroup()
                        .addComponent(txt5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 23, 23))
        );
        jPanel005Layout.setVerticalGroup(
            jPanel005Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel005Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel005Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt6, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt26, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel005Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt25, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        //jPanel004 = new RoundedPanel();
        jPanel004.setBackground(new java.awt.Color(255, 255, 255));
        jPanel004.setPreferredSize(new java.awt.Dimension(291, 76));

        jLabel8.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("ق/ المأكولات");
        jLabel8.setPreferredSize(new java.awt.Dimension(56, 23));

        jLabel5.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("و/ المأكولات");
        jLabel5.setPreferredSize(new java.awt.Dimension(56, 23));

        txt7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt7.setNextFocusableComponent(txt6);

        txt8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt8.setNextFocusableComponent(txt7);

        txt27.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt27.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt27.setNextFocusableComponent(txt26);

        txt28.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt28.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt28.setNextFocusableComponent(txt27);

        javax.swing.GroupLayout jPanel004Layout = new javax.swing.GroupLayout(jPanel004);
        jPanel004.setLayout(jPanel004Layout);
        jPanel004Layout.setHorizontalGroup(
            jPanel004Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel004Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel004Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt27, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt28, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(jPanel004Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt8, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt7, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel004Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );
        jPanel004Layout.setVerticalGroup(
            jPanel004Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel004Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel004Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt28, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel004Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt27, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        //jPanel003 = new RoundedPanel();
        jPanel003.setBackground(new java.awt.Color(255, 255, 255));
        jPanel003.setPreferredSize(new java.awt.Dimension(291, 76));

        jLabel3.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("ق/ البيرة");
        jLabel3.setPreferredSize(new java.awt.Dimension(56, 23));

        jLabel16.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("و/ البيرة");
        jLabel16.setPreferredSize(new java.awt.Dimension(56, 23));

        txt9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt9.setNextFocusableComponent(txt8);

        txt10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt10.setNextFocusableComponent(txt9);

        txt30.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt30.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt30.setNextFocusableComponent(txt29);

        txt29.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt29.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt29.setNextFocusableComponent(txt28);

        javax.swing.GroupLayout jPanel003Layout = new javax.swing.GroupLayout(jPanel003);
        jPanel003.setLayout(jPanel003Layout);
        jPanel003Layout.setHorizontalGroup(
            jPanel003Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel003Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel003Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt29, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt30, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(jPanel003Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel003Layout.createSequentialGroup()
                        .addComponent(txt9, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel003Layout.createSequentialGroup()
                        .addComponent(txt10, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 23, 23))
        );
        jPanel003Layout.setVerticalGroup(
            jPanel003Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel003Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel003Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel003Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt10, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt30, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel003Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt29, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel003Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt9, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        //jPanel002 = new RoundedPanel();
        jPanel002.setBackground(new java.awt.Color(255, 255, 255));
        jPanel002.setPreferredSize(new java.awt.Dimension(291, 76));

        jLabel15.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("ق/ الخمور");
        jLabel15.setPreferredSize(new java.awt.Dimension(56, 23));

        jLabel4.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("و/ الخمور");
        jLabel4.setPreferredSize(new java.awt.Dimension(56, 23));

        txt11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt11.setNextFocusableComponent(txt10);

        txt12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt12.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt12.setNextFocusableComponent(txt11);

        txt31.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt31.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt31.setNextFocusableComponent(txt30);

        txt32.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt32.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt32.setNextFocusableComponent(txt31);

        javax.swing.GroupLayout jPanel002Layout = new javax.swing.GroupLayout(jPanel002);
        jPanel002.setLayout(jPanel002Layout);
        jPanel002Layout.setHorizontalGroup(
            jPanel002Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel002Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel002Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt32, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt31, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(jPanel002Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt12, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt11, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel002Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );
        jPanel002Layout.setVerticalGroup(
            jPanel002Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel002Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel002Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt12, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt32, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel002Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt11, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt31, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        //jPanel001 = new RoundedPanel();
        jPanel001.setBackground(new java.awt.Color(255, 255, 255));
        jPanel001.setPreferredSize(new java.awt.Dimension(291, 76));

        jLabel2.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("ق/ السجائر");
        jLabel2.setPreferredSize(new java.awt.Dimension(56, 23));

        jLabel1.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("و/ السجائر");
        jLabel1.setPreferredSize(new java.awt.Dimension(56, 23));

        txt13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt13.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt13.setNextFocusableComponent(txt12);

        txt14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt14.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt14.setNextFocusableComponent(txt13);

        txt33.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt33.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt33.setNextFocusableComponent(txt32);

        txt34.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt34.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt34.setNextFocusableComponent(txt33);

        javax.swing.GroupLayout jPanel001Layout = new javax.swing.GroupLayout(jPanel001);
        jPanel001.setLayout(jPanel001Layout);
        jPanel001Layout.setHorizontalGroup(
            jPanel001Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel001Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel001Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt33, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt34, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(jPanel001Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt14, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt13, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel001Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );
        jPanel001Layout.setVerticalGroup(
            jPanel001Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel001Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel001Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel001Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt14, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt34, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel001Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel001Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt33, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt13, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel006, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel005, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel004, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel002, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel003, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel007, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel001, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel001, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel002, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanel003, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel004, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanel005, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel006, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanel007, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane2.setViewportView(jPanel7);

        jSeparator20.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jPanel16 = new RoundedPanel();
        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setPreferredSize(new java.awt.Dimension(122, 92));

        jLabel35.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("المحصل من ضريبة 1 % ");

        jLabel36.setFont(new java.awt.Font("Traditional Arabic", 1, 18)); // NOI18N
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel36.setText("الدولار");
        jLabel36.setPreferredSize(new java.awt.Dimension(54, 23));

        txt35.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt35.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt35.setNextFocusableComponent(txt36);

        jLabel37.setFont(new java.awt.Font("Traditional Arabic", 1, 18)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel37.setText("الجنيه");
        jLabel37.setPreferredSize(new java.awt.Dimension(54, 23));

        txt36.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txt36.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt35, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                            .addComponent(txt36))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel37, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt36)
                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8))
        );

        jLabel12.setFont(new java.awt.Font("Traditional Arabic", 1, 18)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("الأجنبى");

        jLabel11.setFont(new java.awt.Font("Traditional Arabic", 1, 18)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("المصرى");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(132, 132, 132))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jSeparator20, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addComponent(jSeparator20, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(93, Short.MAX_VALUE))
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(jPanel27Layout.createSequentialGroup()
                                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );

        jPanel35 = new RoundedPanel();
        jPanel35.setBackground(new java.awt.Color(255, 255, 255));

        Edit3.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        Edit3.setForeground(new java.awt.Color(0, 102, 0));
        Edit3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edit_note_30dp_2854C5_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        Edit3.setToolTipText("تعديل");
        Edit3.setFocusPainted(false);
        Edit3.setPreferredSize(new java.awt.Dimension(50, 28));
        Edit3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Edit3ActionPerformed(evt);
            }
        });
        jPanel35.add(Edit3);

        SaveDailyReport1.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        SaveDailyReport1.setForeground(new java.awt.Color(0, 102, 0));
        SaveDailyReport1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/add_task_30dp_4B77D1_FILL0_wght300_GRAD0_opsz24.png"))); // NOI18N
        SaveDailyReport1.setToolTipText("حفظ اليومية");
        SaveDailyReport1.setFocusPainted(false);
        SaveDailyReport1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        SaveDailyReport1.setInheritsPopupMenu(true);
        SaveDailyReport1.setPreferredSize(new java.awt.Dimension(50, 28));
        SaveDailyReport1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveDailyReport1ActionPerformed(evt);
            }
        });
        jPanel35.add(SaveDailyReport1);

        jSeparator10.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator10.setPreferredSize(new java.awt.Dimension(5, 30));
        jPanel35.add(jSeparator10);

        jPanelShiftDate.setBackground(new java.awt.Color(255, 255, 255));
        jPanelShiftDate.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanelShiftDate.setToolTipText("التقويم");
        jPanelShiftDate.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanelShiftDateLayout = new javax.swing.GroupLayout(jPanelShiftDate);
        jPanelShiftDate.setLayout(jPanelShiftDateLayout);
        jPanelShiftDateLayout.setHorizontalGroup(
            jPanelShiftDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 106, Short.MAX_VALUE)
        );
        jPanelShiftDateLayout.setVerticalGroup(
            jPanelShiftDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );

        jPanel35.add(jPanelShiftDate);
        jPanelShiftDate.getAccessibleContext().setAccessibleName("");

        ComboBoxShift.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        ComboBoxShift.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "أختر الوردية", "الصباحية", "المسائية" }));
        ComboBoxShift.setFocusable(false);
        ComboBoxShift.setNextFocusableComponent(txtbranchName);
        ComboBoxShift.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxShiftActionPerformed(evt);
            }
        });
        jPanel35.add(ComboBoxShift);
        ComboBoxShift.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout jDialogDailyLayout = new javax.swing.GroupLayout(jDialogDaily.getContentPane());
        jDialogDaily.getContentPane().setLayout(jDialogDailyLayout);
        jDialogDailyLayout.setHorizontalGroup(
            jDialogDailyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogDailyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jDialogDailyLayout.setVerticalGroup(
            jDialogDailyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogDailyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setTitle("الرئيسية");
        setResizable(false);
        setSize(new java.awt.Dimension(0, 0));
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanelMainLogIn.setBackground(new java.awt.Color(204, 204, 204));

        jPanelLogInSreen = new RoundedPanel();
        jPanelLogInSreen.setBackground(new java.awt.Color(255, 255, 255));

        labelIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelIcon.setIcon(new javax.swing.ImageIcon("D:\\programs\\JavaApplications\\EgyC_DutyFree5.0.0\\src\\main\\resources\\aa.png")); // NOI18N

        loginLabl.setBackground(new java.awt.Color(216, 212, 212));
        loginLabl.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        loginLabl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        loginLabl.setText("تسجيل الدخول");
        loginLabl.setMaximumSize(new java.awt.Dimension(192, 25));
        loginLabl.setMinimumSize(new java.awt.Dimension(192, 25));
        loginLabl.setPreferredSize(new java.awt.Dimension(192, 25));

        jPanel25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel25.setPreferredSize(new java.awt.Dimension(482, 1));

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 478, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        copyRightLab1.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        copyRightLab1.setForeground(new java.awt.Color(51, 102, 255));
        copyRightLab1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        copyRightLab1.setText("Created by_ Ahmed  El hawy 2022");

        loginLabelDate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        loginLabelDate.setForeground(new java.awt.Color(51, 102, 255));
        loginLabelDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        loginLabelDate.setText("date");

        jPanel1 = new RoundedPanel();
        jPanel24.setBackground(new java.awt.Color(255, 255, 255));
        jPanel24.setFont(new java.awt.Font("Traditional Arabic", 1, 18)); // NOI18N

        userName.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        userName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        userName.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "أسم المستخدم", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(51, 102, 255))); // NOI18N
        userName.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        userName.setNextFocusableComponent(password);
        userName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userNameActionPerformed(evt);
            }
        });

        LogIn.setForeground(new java.awt.Color(0, 102, 0));
        LogIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/login_30dp_48752C_FILL0_wght300_GRAD0_opsz24.png"))); // NOI18N
        LogIn.setToolTipText("دخول");
        LogIn.setFocusPainted(false);
        LogIn.setNextFocusableComponent(Exite);
        LogIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogInActionPerformed(evt);
            }
        });

        Exite.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Exite.setForeground(new java.awt.Color(255, 0, 0));
        Exite.setIcon(new javax.swing.ImageIcon(getClass().getResource("/logout_30dp_BB271A_FILL0_wght300_GRAD0_opsz24.png"))); // NOI18N
        Exite.setToolTipText("خروج");
        Exite.setFocusPainted(false);
        Exite.setNextFocusableComponent(setNewPassword);
        Exite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExiteActionPerformed(evt);
            }
        });

        password.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        password.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        password.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "كلمة المرور", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(51, 102, 255))); // NOI18N
        password.setNextFocusableComponent(LogIn);

        Newpassword.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Newpassword.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Newpassword.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "كلمة المرور الجديدة ", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(51, 102, 255))); // NOI18N

        setNewPassword.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        setNewPassword.setForeground(new java.awt.Color(0, 102, 255));
        setNewPassword.setIcon(new javax.swing.ImageIcon("D:\\programs\\JavaApplications\\EgyC_DutyFree6.0.0\\src\\main\\resources\\key_30dp_2854C5_FILL0_wght400_GRAD0_opsz24.png")); // NOI18N
        setNewPassword.setText("تغيير كلمة المرور");
        setNewPassword.setToolTipText("تغيير كلمة المرور");
        setNewPassword.setFocusPainted(false);
        setNewPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setNewPasswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(Exite, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LogIn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(password, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                    .addComponent(userName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Newpassword, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))
                .addContainerGap(93, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(setNewPassword)
                .addGap(131, 131, 131))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(userName, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Newpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LogIn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Exite, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(setNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Newpassword.getAccessibleContext().setAccessibleName("");

        jSeparator15.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanelLogInSreenLayout = new javax.swing.GroupLayout(jPanelLogInSreen);
        jPanelLogInSreen.setLayout(jPanelLogInSreenLayout);
        jPanelLogInSreenLayout.setHorizontalGroup(
            jPanelLogInSreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLogInSreenLayout.createSequentialGroup()
                .addGroup(jPanelLogInSreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLogInSreenLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(labelIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelLogInSreenLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(copyRightLab1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 257, Short.MAX_VALUE)
                .addGroup(jPanelLogInSreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLogInSreenLayout.createSequentialGroup()
                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLogInSreenLayout.createSequentialGroup()
                        .addComponent(loginLabelDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(loginLabl, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLogInSreenLayout.createSequentialGroup()
                        .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanelLogInSreenLayout.setVerticalGroup(
            jPanelLogInSreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLogInSreenLayout.createSequentialGroup()
                .addGroup(jPanelLogInSreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLogInSreenLayout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addComponent(labelIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 111, Short.MAX_VALUE)
                        .addComponent(copyRightLab1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelLogInSreenLayout.createSequentialGroup()
                        .addGap(148, 148, 148)
                        .addGroup(jPanelLogInSreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelLogInSreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(loginLabl, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(loginLabelDate, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator15, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanelMainLogInLayout = new javax.swing.GroupLayout(jPanelMainLogIn);
        jPanelMainLogIn.setLayout(jPanelMainLogInLayout);
        jPanelMainLogInLayout.setHorizontalGroup(
            jPanelMainLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelMainLogInLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelLogInSreen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelMainLogInLayout.setVerticalGroup(
            jPanelMainLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelMainLogInLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanelLogInSreen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        getContentPane().add(jPanelMainLogIn, "card4");

        jPanelMainContainer.setBackground(new java.awt.Color(216, 212, 212));
        jPanelMainContainer.setLayout(new javax.swing.BoxLayout(jPanelMainContainer, javax.swing.BoxLayout.LINE_AXIS));

        jPanelSpeace1.setBackground(new java.awt.Color(200, 204, 211));
        jPanelSpeace1.setPreferredSize(new java.awt.Dimension(8, 660));

        javax.swing.GroupLayout jPanelSpeace1Layout = new javax.swing.GroupLayout(jPanelSpeace1);
        jPanelSpeace1.setLayout(jPanelSpeace1Layout);
        jPanelSpeace1Layout.setHorizontalGroup(
            jPanelSpeace1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanelSpeace1Layout.setVerticalGroup(
            jPanelSpeace1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 660, Short.MAX_VALUE)
        );

        jPanelMainContainer.add(jPanelSpeace1);

        jPanelContainer.setBackground(new java.awt.Color(200, 204, 211));

        jPanelScreens.setBackground(new java.awt.Color(204, 204, 204));
        jPanelScreens.setLayout(new java.awt.CardLayout());

        jPanelBills = new RoundedPanel();
        jPanelBills.setPreferredSize(new java.awt.Dimension(1050, 555));

        jPanel21 = new RoundedPanel();
        jPanel21.setBackground(new java.awt.Color(255, 255, 255));

        operation_date.setEditable(false);
        operation_date.setBackground(new java.awt.Color(255, 255, 255));
        operation_date.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        operation_date.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        operation_date.setBorder(null);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        labelImage.setFont(new java.awt.Font("Traditional Arabic", 1, 18)); // NOI18N
        labelImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labelImageMouseClicked(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(204, 0, 0));
        jLabel13.setText("x");
        jLabel13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel13MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelImage, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 11, Short.MAX_VALUE)
                .addComponent(jLabel13))
        );

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(operation_date)
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(operation_date, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10 = new RoundedPanel();
        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        ComboB_type.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        ComboB_type.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "نوع الصنف", "سجائر", "مشروبات كحولية", "بيرة", "مأكولات", "عطور", "أجهزة", "هدايا" }));

        jPanel3= new RoundedPanel();
        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        RadioDiscount.setBackground(new java.awt.Color(204, 204, 204));
        RadioDiscount.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        RadioDiscount.setForeground(new java.awt.Color(255, 51, 51));
        RadioDiscount.setText("خصم");
        RadioDiscount.setFocusPainted(false);
        RadioDiscount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        RadioDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioDiscountActionPerformed(evt);
            }
        });

        RadioImport.setBackground(new java.awt.Color(204, 204, 204));
        RadioImport.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        RadioImport.setForeground(new java.awt.Color(0, 102, 0));
        RadioImport.setText("إضافة");
        RadioImport.setFocusPainted(false);
        RadioImport.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        RadioImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioImportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RadioImport)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(RadioDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RadioImport, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RadioDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel19.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("كود الصنف");

        id.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        id.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        search.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        search.setForeground(new java.awt.Color(0, 0, 204));
        search.setIcon(new javax.swing.ImageIcon(getClass().getResource("/search.png"))); // NOI18N
        search.setToolTipText("بحث");
        search.setFocusPainted(false);
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(ComboB_type, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(id)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(3, 3, 3))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(search, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(ComboB_type)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel18 = new RoundedPanel();
        jPanel18.setBackground(new java.awt.Color(255, 255, 255));

        jLabel21.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 51, 51));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("المباع");
        jLabel21.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jLabel21.setPreferredSize(new java.awt.Dimension(60, 26));

        output.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        output.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        output.setMinimumSize(new java.awt.Dimension(64, 27));
        output.setPreferredSize(new java.awt.Dimension(123, 27));
        output.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outputActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 102, 0));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("الوارد");
        jLabel20.setPreferredSize(new java.awt.Dimension(60, 26));

        input.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        input.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        input.setPreferredSize(new java.awt.Dimension(123, 27));

        price.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        price.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        price.setPreferredSize(new java.awt.Dimension(123, 27));

        jLabel25.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("السعر");
        jLabel25.setPreferredSize(new java.awt.Dimension(60, 26));

        count.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        count.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        count.setPreferredSize(new java.awt.Dimension(123, 27));

        jLabel22.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("الرصيد  ");
        jLabel22.setPreferredSize(new java.awt.Dimension(60, 26));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                        .addComponent(output, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(input, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addComponent(count, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(price, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(output, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(input, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(price, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(count, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13 = new RoundedPanel();
        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setPreferredSize(new java.awt.Dimension(405, 131));

        billNum.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        billNum.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        billNum.setPreferredSize(new java.awt.Dimension(123, 27));

        name.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        name.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        name.setPreferredSize(new java.awt.Dimension(123, 27));

        passport.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        passport.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        passport.setPreferredSize(new java.awt.Dimension(123, 27));

        birthDate.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        birthDate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        birthDate.setPreferredSize(new java.awt.Dimension(123, 27));

        jLabel26.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("رقم الفاتورة");
        jLabel26.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jLabel26.setPreferredSize(new java.awt.Dimension(60, 26));

        jLabel27.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("الأسم");
        jLabel27.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jLabel27.setPreferredSize(new java.awt.Dimension(60, 26));

        jLabel28.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("رقم الجواز");
        jLabel28.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jLabel28.setPreferredSize(new java.awt.Dimension(60, 26));

        jLabel39.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel39.setText("ت الميلاد");
        jLabel39.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jLabel39.setPreferredSize(new java.awt.Dimension(60, 26));

        ComboB_national.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        ComboB_national.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "الجنسية", "مصرى", "أجنبى" }));
        ComboB_national.setPreferredSize(new java.awt.Dimension(123, 27));
        ComboB_national.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboB_nationalActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 51, 51));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("ض بالجنيه");
        jLabel29.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jLabel29.setPreferredSize(new java.awt.Dimension(60, 26));

        winTaxP.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        winTaxP.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        winTaxP.setPreferredSize(new java.awt.Dimension(123, 27));

        jLabel30.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 51, 51));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("ض بالدولار");
        jLabel30.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jLabel30.setPreferredSize(new java.awt.Dimension(60, 26));

        winTaxD.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        winTaxD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        winTaxD.setPreferredSize(new java.awt.Dimension(123, 27));

        jLabel38.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel38.setText("الجنسية");
        jLabel38.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jLabel38.setPreferredSize(new java.awt.Dimension(60, 26));

        Show_image.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        Show_image.setForeground(new java.awt.Color(76, 51, 151));
        Show_image.setText("عرض الصورة");
        Show_image.setToolTipText("الإستعلام عن عملية");
        Show_image.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Show_imageActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(winTaxD, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(winTaxP, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(passport, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(billNum, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(Show_image, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(birthDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                                        .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(ComboB_national, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(billNum, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passport, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ComboB_national, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Show_image, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(name, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(birthDate, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(winTaxP, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(winTaxD, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel17= new RoundedPanel();
        jPanel17.setBackground(new java.awt.Color(255, 255, 255));

        jLabel23.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("ملاحظات");

        notes.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        notes.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(notes, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel23)
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(notes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel20 = new RoundedPanel();

        jScrollPane10.setBackground(new java.awt.Color(255, 255, 255));

        tablecount1.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        tablecount1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "الصنف", "السعر", "الرصيد", "المباع", "الوارد", "التاريخ", "الكود"
            }
        ));
        tablecount1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tablecount1.setRowHeight(30);
        tablecount1.setShowGrid(false);
        tablecount1.getTableHeader().setReorderingAllowed(false);
        tablecount1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablecount1MouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(tablecount1);
        if (tablecount1.getColumnModel().getColumnCount() > 0) {
            tablecount1.getColumnModel().getColumn(5).setPreferredWidth(120);
            tablecount1.getColumnModel().getColumn(6).setPreferredWidth(170);
        }

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanelBillsLayout = new javax.swing.GroupLayout(jPanelBills);
        jPanelBills.setLayout(jPanelBillsLayout);
        jPanelBillsLayout.setHorizontalGroup(
            jPanelBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelBillsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
        );
        jPanelBillsLayout.setVerticalGroup(
            jPanelBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBillsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelBillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanelScreens.add(jPanelBills, "Bills");

        jPanelReports = new RoundedPanel();
        jPanelReports.setBackground(new java.awt.Color(255, 255, 255));
        jPanelReports.setPreferredSize(new java.awt.Dimension(1110, 570));

        jPanel6 = new RoundedPanel();

        tab1.setForeground(new java.awt.Color(51, 51, 255));
        tab1.setFont(new java.awt.Font("Traditional Arabic", 1, 18)); // NOI18N
        tab1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tab1StateChanged(evt);
            }
        });

        table.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ض  1 % بالجنية", "ض  1 % بالدولار", "ق للهدايا", "و/ للهدايا", "ق للأجهزة", "و/ للأجهزة", "ق للعطور", "و/ للعطور", "ق للمأكولات", "و/ للمأكولات", "ق للبيرة", "و/ البيرة", "ق للخمور", "و/  للخمور", "ق للسجائر", "و/ للسجائر", "التاريخ", "الوردية", "الفرع"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(16).setPreferredWidth(120);
            table.getColumnModel().getColumn(17).setPreferredWidth(40);
        }

        tab1.addTab("مشتريات المصرى", jScrollPane9);

        tablefor.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        tablefor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ض  1 % بالجنية", "ض  1 % بالدولار", "ق للهدايا", "و/ للهدايا", "ق للأجهزة", "و/ للأجهزة", "ق للعطور", "و/ للعطور", "ق للمأكولات", "و/ للمأكولات", "ق للبيرة", "و/ البيرة", "ق للخمور", "و/  للخمور", "ق للسجائر", "و/ للسجائر", "التاريخ", "الوردية", "الفرع"
            }
        ));
        tablefor.setRowHeight(35);
        tablefor.setShowGrid(false);
        tablefor.getTableHeader().setReorderingAllowed(false);
        tablefor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableforMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tablefor);
        if (tablefor.getColumnModel().getColumnCount() > 0) {
            tablefor.getColumnModel().getColumn(16).setPreferredWidth(120);
            tablefor.getColumnModel().getColumn(17).setPreferredWidth(40);
        }

        tab1.addTab("مشتريات الأجنبى", jScrollPane12);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tab1, javax.swing.GroupLayout.DEFAULT_SIZE, 1086, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tab1, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanelReportsLayout = new javax.swing.GroupLayout(jPanelReports);
        jPanelReports.setLayout(jPanelReportsLayout);
        jPanelReportsLayout.setHorizontalGroup(
            jPanelReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelReportsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanelReportsLayout.setVerticalGroup(
            jPanelReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelReportsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanelScreens.add(jPanelReports, "Reports");

        jPanelTaxs = new RoundedPanel();
        jPanelTaxs.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2 = new RoundedPanel();
        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMaximumSize(new java.awt.Dimension(100, 50));
        jPanel2.setMinimumSize(new java.awt.Dimension(100, 30));
        jPanel2.setPreferredSize(new java.awt.Dimension(100, 30));

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jTextPane2.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jTextPane2.setMinimumSize(new java.awt.Dimension(0, 25));
        jTextPane2.setPreferredSize(new java.awt.Dimension(0, 25));
        jScrollPane7.setViewportView(jTextPane2);
        StyledDocument style2 = jTextPane2.getStyledDocument();
        SimpleAttributeSet align2 = new SimpleAttributeSet();
        StyleConstants.setAlignment(align2, StyleConstants.ALIGN_RIGHT);
        style2.setParagraphAttributes(0, style2.getLength(), align2, false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));

        txtSearch.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtSearch.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSearch.setComponentPopupMenu(suggestionPopup);

        ComboB_type1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ComboB_type1.setForeground(new java.awt.Color(204, 102, 0));
        ComboB_type1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "نوع الصنف", "سجائر", "مشروبات كحولية", "بيرة", "مأكولات", "عطور", "أجهزة", "هدايا" }));
        ComboB_type1.setMaximumSize(new java.awt.Dimension(100, 50));
        ComboB_type1.setMinimumSize(new java.awt.Dimension(100, 30));
        ComboB_type1.setPreferredSize(new java.awt.Dimension(100, 30));
        ComboB_type1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboB_type1ActionPerformed(evt);
            }
        });

        Description.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Description.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Description.setMaximumSize(new java.awt.Dimension(100, 50));
        Description.setMinimumSize(new java.awt.Dimension(100, 30));
        Description.setPreferredSize(new java.awt.Dimension(100, 30));

        Value.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Value.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Value.setMaximumSize(new java.awt.Dimension(100, 50));
        Value.setMinimumSize(new java.awt.Dimension(100, 30));
        Value.setPreferredSize(new java.awt.Dimension(100, 30));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 102, 255));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("كود الصنف");

        jLabel41.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(0, 102, 255));
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("أسم الصنف");

        jLabel42.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(0, 102, 255));
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel42.setText("القيمة ");

        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(0, 102, 255));
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setText("عدد الوحدات");

        Count.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Count.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Count.setMaximumSize(new java.awt.Dimension(100, 50));
        Count.setMinimumSize(new java.awt.Dimension(100, 30));
        Count.setPreferredSize(new java.awt.Dimension(100, 30));

        jLabel43.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(0, 102, 255));
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel43.setText("السعر");

        Salesprice1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Salesprice1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Salesprice1.setMaximumSize(new java.awt.Dimension(100, 50));
        Salesprice1.setMinimumSize(new java.awt.Dimension(100, 30));
        Salesprice1.setPreferredSize(new java.awt.Dimension(100, 30));

        FobValue.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        FobValue.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        FobValue.setMaximumSize(new java.awt.Dimension(100, 50));
        FobValue.setMinimumSize(new java.awt.Dimension(100, 30));
        FobValue.setPreferredSize(new java.awt.Dimension(100, 30));

        jLabel44.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(0, 102, 255));
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel44.setText("القيمة FOB");

        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel15.setPreferredSize(new java.awt.Dimension(482, 1));

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 480, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        codeLebel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        codeLebel.setForeground(new java.awt.Color(204, 102, 0));
        codeLebel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        codeLebel.setPreferredSize(new java.awt.Dimension(148, 28));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel19Layout.createSequentialGroup()
                            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel19Layout.createSequentialGroup()
                                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(jPanel19Layout.createSequentialGroup()
                                            .addComponent(FobValue, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel19Layout.createSequentialGroup()
                                            .addComponent(Salesprice1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel19Layout.createSequentialGroup()
                                            .addComponent(Count, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel40))
                                        .addGroup(jPanel19Layout.createSequentialGroup()
                                            .addComponent(Value, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(9, 9, 9))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel32)
                            .addContainerGap()))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel19Layout.createSequentialGroup()
                                .addComponent(codeLebel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(ComboB_type1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(Description, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(codeLebel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ComboB_type1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Description, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FobValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Value, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Count, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Salesprice1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(60, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(273, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout jPanelTaxsLayout = new javax.swing.GroupLayout(jPanelTaxs);
        jPanelTaxs.setLayout(jPanelTaxsLayout);
        jPanelTaxsLayout.setHorizontalGroup(
            jPanelTaxsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelTaxsLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1085, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );
        jPanelTaxsLayout.setVerticalGroup(
            jPanelTaxsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTaxsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelScreens.add(jPanelTaxs, "Taxs");

        jPanelSettings = new RoundedPanel();

        jPanel4 = new RoundedPanel();
        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setPreferredSize(new java.awt.Dimension(980, 434));

        jPanel23 = new RoundedPanel();
        jPanel23.setBackground(new java.awt.Color(255, 255, 255));

        CigarPriceTxt.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        CigarPriceTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        CigarPriceTxt.setPreferredSize(new java.awt.Dimension(80, 30));
        jPanel23.add(CigarPriceTxt);

        jLabel49.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(0, 102, 255));
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel49.setText("متوسط سعر السيجار");
        jPanel23.add(jLabel49);

        jSeparator24.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator24.setPreferredSize(new java.awt.Dimension(20, 20));
        jPanel23.add(jSeparator24);

        CigPriceTxt.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        CigPriceTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        CigPriceTxt.setPreferredSize(new java.awt.Dimension(80, 30));
        jPanel23.add(CigPriceTxt);

        jLabel48.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(0, 102, 255));
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel48.setText("متوسط سعر السجائر");
        jPanel23.add(jLabel48);

        jSeparator25.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator25.setPreferredSize(new java.awt.Dimension(20, 20));
        jPanel23.add(jSeparator25);

        exchangePriceTxt.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        exchangePriceTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        exchangePriceTxt.setPreferredSize(new java.awt.Dimension(80, 30));
        jPanel23.add(exchangePriceTxt);

        jLabel45.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(0, 102, 255));
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel45.setText("سعر الصرف");
        jPanel23.add(jLabel45);

        jPanel29 = new RoundedPanel();
        jPanel29.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1 = new RoundedPanel();
        jPanel32.setBackground(new java.awt.Color(255, 255, 255));

        CheckCig.setBackground(new java.awt.Color(255, 255, 255));
        CheckCig.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        CheckCig.setText("السجائر");
        CheckCig.setFocusPainted(false);
        CheckCig.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        CheckCig.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        CheckCigar.setBackground(new java.awt.Color(255, 255, 255));
        CheckCigar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        CheckCigar.setText("السيجار");
        CheckCigar.setFocusPainted(false);
        CheckCigar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        CheckCigar.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        CheckWin.setBackground(new java.awt.Color(255, 255, 255));
        CheckWin.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        CheckWin.setText("المشروبات الكحولية");
        CheckWin.setFocusPainted(false);
        CheckWin.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        CheckWin.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        Checkbeer.setBackground(new java.awt.Color(255, 255, 255));
        Checkbeer.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Checkbeer.setText("البيرة");
        Checkbeer.setFocusPainted(false);
        Checkbeer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Checkbeer.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        CheckPerfum.setBackground(new java.awt.Color(255, 255, 255));
        CheckPerfum.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        CheckPerfum.setText("العطور");
        CheckPerfum.setFocusPainted(false);
        CheckPerfum.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        CheckPerfum.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        CheckDevice.setBackground(new java.awt.Color(255, 255, 255));
        CheckDevice.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        CheckDevice.setText("الاجهزة");
        CheckDevice.setFocusPainted(false);
        CheckDevice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        CheckDevice.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        CheckGift.setBackground(new java.awt.Color(255, 255, 255));
        CheckGift.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        CheckGift.setText("الهدايا");
        CheckGift.setFocusPainted(false);
        CheckGift.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        CheckGift.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        CheckFood.setBackground(new java.awt.Color(255, 255, 255));
        CheckFood.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        CheckFood.setText("المأكولات");
        CheckFood.setFocusPainted(false);
        CheckFood.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        CheckFood.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jSeparator12.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CheckCigar, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CheckFood, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CheckDevice, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Checkbeer, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CheckWin)
                            .addComponent(CheckPerfum, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CheckGift, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(CheckCig, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator6)
                    .addComponent(jSeparator12)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CheckCigar)
                            .addComponent(CheckCig, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Checkbeer)
                            .addComponent(CheckWin))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CheckDevice)
                            .addComponent(CheckPerfum))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(CheckGift, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(CheckFood, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        jPanel29.add(jPanel32);

        jPanel38.setBackground(new java.awt.Color(255, 255, 255));

        ComboB_SubBranch.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ComboB_SubBranch.setForeground(new java.awt.Color(51, 102, 255));
        ComboB_SubBranch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "أختر" }));
        ComboB_SubBranch.setToolTipText("أختر الفرع");
        ComboB_SubBranch.setPreferredSize(new java.awt.Dimension(485, 30));
        ComboB_SubBranch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboB_SubBranchActionPerformed(evt);
            }
        });
        jPanel38.add(ComboB_SubBranch);

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(0, 102, 255));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("تعيين الفرع المحلى");
        jPanel38.add(jLabel34);

        ItemsListEdit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ItemsListEdit.setForeground(new java.awt.Color(0, 102, 255));
        ItemsListEdit.setText("تخصيص قائمة الأصناف <");
        ItemsListEdit.setToolTipText("إظهار /إخفاء القائمة");
        ItemsListEdit.setFocusPainted(false);
        ItemsListEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ItemsListEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ItemsListEdit)
                .addContainerGap(76, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ItemsListEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(181, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelSettingsLayout = new javax.swing.GroupLayout(jPanelSettings);
        jPanelSettings.setLayout(jPanelSettingsLayout);
        jPanelSettingsLayout.setHorizontalGroup(
            jPanelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSettingsLayout.createSequentialGroup()
                .addGap(177, 177, 177)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 757, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(177, Short.MAX_VALUE))
        );
        jPanelSettingsLayout.setVerticalGroup(
            jPanelSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSettingsLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanelScreens.add(jPanelSettings, "Settings");

        jPanelQuary = new RoundedPanel();

        jPanel22 = new RoundedPanel();

        jPanel11= new RoundedPanel();
        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setPreferredSize(new java.awt.Dimension(431, 38));

        jPanel31.setBackground(new java.awt.Color(255, 255, 255));

        jPanelDateTo.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDateTo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanelDateTo.setToolTipText("التقويم");
        jPanelDateTo.setNextFocusableComponent(searchByDate);
        jPanelDateTo.setPreferredSize(new java.awt.Dimension(110, 27));
        jPanelDateTo.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanelDateToLayout = new javax.swing.GroupLayout(jPanelDateTo);
        jPanelDateTo.setLayout(jPanelDateToLayout);
        jPanelDateToLayout.setHorizontalGroup(
            jPanelDateToLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 106, Short.MAX_VALUE)
        );
        jPanelDateToLayout.setVerticalGroup(
            jPanelDateToLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel31.add(jPanelDateTo);

        jLabelTo.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        jLabelTo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTo.setText("إلى");
        jLabelTo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabelTo.setPreferredSize(new java.awt.Dimension(19, 28));
        jPanel31.add(jLabelTo);

        jPanelDateFrom.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDateFrom.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanelDateFrom.setToolTipText("التقويم");
        jPanelDateFrom.setNextFocusableComponent(jPanelDateTo);
        jPanelDateFrom.setPreferredSize(new java.awt.Dimension(110, 27));
        jPanelDateFrom.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanelDateFromLayout = new javax.swing.GroupLayout(jPanelDateFrom);
        jPanelDateFrom.setLayout(jPanelDateFromLayout);
        jPanelDateFromLayout.setHorizontalGroup(
            jPanelDateFromLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 106, Short.MAX_VALUE)
        );
        jPanelDateFromLayout.setVerticalGroup(
            jPanelDateFromLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel31.add(jPanelDateFrom);

        jLabelFrom.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        jLabelFrom.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelFrom.setText(" من");
        jLabelFrom.setPreferredSize(new java.awt.Dimension(14, 28));
        jPanel31.add(jLabelFrom);
        jLabelFrom.getAccessibleContext().setAccessibleName("");

        jPanel33.setBackground(new java.awt.Color(255, 255, 255));

        RadioGetReport.setBackground(new java.awt.Color(255, 255, 255));
        RadioGetReport.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        RadioGetReport.setText("تقرير بالضرائب والرسوم");
        RadioGetReport.setFocusPainted(false);
        RadioGetReport.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        RadioGetReport.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        RadioGetReport.setNextFocusableComponent(jPanelDateFrom);
        jPanel33.add(RadioGetReport);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setPreferredSize(new java.awt.Dimension(20, 20));
        jPanel33.add(jSeparator1);

        RadioGetTax.setBackground(new java.awt.Color(255, 255, 255));
        RadioGetTax.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        RadioGetTax.setText("ضريبة 1%");
        RadioGetTax.setFocusPainted(false);
        RadioGetTax.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        RadioGetTax.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        RadioGetTax.setNextFocusableComponent(RadioGetReport);
        jPanel33.add(RadioGetTax);

        jSeparator16.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator16.setPreferredSize(new java.awt.Dimension(20, 20));
        jPanel33.add(jSeparator16);

        RadioGetDaily.setBackground(new java.awt.Color(255, 255, 255));
        RadioGetDaily.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        RadioGetDaily.setText("يوميات");
        RadioGetDaily.setFocusPainted(false);
        RadioGetDaily.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        RadioGetDaily.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        RadioGetDaily.setNextFocusableComponent(RadioGetTax);
        jPanel33.add(RadioGetDaily);

        jPanel34.setBackground(new java.awt.Color(255, 255, 255));

        AppendData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/upload_file_30dp_F19E39_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        AppendData.setToolTipText("إضافة بيان على الشبكة");
        AppendData.setFocusPainted(false);
        AppendData.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        AppendData.setPreferredSize(new java.awt.Dimension(60, 28));
        AppendData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AppendDataActionPerformed(evt);
            }
        });
        jPanel34.add(AppendData);

        jSeparator22.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator22.setPreferredSize(new java.awt.Dimension(20, 20));
        jPanel34.add(jSeparator22);

        uploadData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloud_upload_30dp_2854C5_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        uploadData.setToolTipText("حفظ البيانات على الشبكة");
        uploadData.setFocusPainted(false);
        uploadData.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        uploadData.setPreferredSize(new java.awt.Dimension(60, 28));
        uploadData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadDataActionPerformed(evt);
            }
        });
        jPanel34.add(uploadData);

        jSeparator17.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator17.setPreferredSize(new java.awt.Dimension(20, 20));
        jPanel34.add(jSeparator17);

        downloadData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cloud_download_30dp_48752C_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        downloadData.setToolTipText("استرجاع البيانات من على الشبكة");
        downloadData.setFocusPainted(false);
        downloadData.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        downloadData.setPreferredSize(new java.awt.Dimension(60, 28));
        downloadData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadDataActionPerformed(evt);
            }
        });
        jPanel34.add(downloadData);

        jSeparator18.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator18.setPreferredSize(new java.awt.Dimension(20, 20));
        jPanel34.add(jSeparator18);

        ConnectionTest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/captive_portal_30dp_2854C5_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        ConnectionTest.setToolTipText("اختبار الإتصال بالشبكة");
        ConnectionTest.setFocusPainted(false);
        ConnectionTest.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ConnectionTest.setPreferredSize(new java.awt.Dimension(60, 28));
        ConnectionTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnectionTestActionPerformed(evt);
            }
        });
        jPanel34.add(ConnectionTest);

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 102, 0));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText(" البيانات على الشبكة");

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 102, 255));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("تاريخ الاستعلام:");

        jLabel47.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(0, 102, 255));
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText("البيان المطلوب:");

        ComboB_SubBranch1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ComboB_SubBranch1.setForeground(new java.awt.Color(51, 102, 255));
        ComboB_SubBranch1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "أختر" }));
        ComboB_SubBranch1.setToolTipText("أختر الفرع");
        ComboB_SubBranch1.setNextFocusableComponent(RadioGetDaily);
        ComboB_SubBranch1.setPreferredSize(new java.awt.Dimension(75, 28));
        ComboB_SubBranch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboB_SubBranch1ActionPerformed(evt);
            }
        });

        jLabel50.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(0, 102, 255));
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel50.setText("الجنسية :");

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));

        checkBoxGetForNationality.setBackground(new java.awt.Color(255, 255, 255));
        checkBoxGetForNationality.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        checkBoxGetForNationality.setText("سائح أو عابر");
        checkBoxGetForNationality.setFocusPainted(false);
        checkBoxGetForNationality.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        checkBoxGetForNationality.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel28.add(checkBoxGetForNationality);

        jSeparator21.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator21.setPreferredSize(new java.awt.Dimension(20, 20));
        jPanel28.add(jSeparator21);

        checkBoxGetEgyNationality.setBackground(new java.awt.Color(255, 255, 255));
        checkBoxGetEgyNationality.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        checkBoxGetEgyNationality.setText("مقيم");
        checkBoxGetEgyNationality.setFocusPainted(false);
        checkBoxGetEgyNationality.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        checkBoxGetEgyNationality.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel28.add(checkBoxGetEgyNationality);

        jPanel36.setBackground(new java.awt.Color(255, 255, 255));

        RadioGetOnlineItems.setBackground(new java.awt.Color(255, 255, 255));
        RadioGetOnlineItems.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        RadioGetOnlineItems.setText("بيانات الأصناف");
        RadioGetOnlineItems.setFocusPainted(false);
        RadioGetOnlineItems.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        RadioGetOnlineItems.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        RadioGetOnlineItems.setNextFocusableComponent(RadioGetTax);
        RadioGetOnlineItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioGetOnlineItemsActionPerformed(evt);
            }
        });
        jPanel36.add(RadioGetOnlineItems);

        jSeparator23.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator23.setPreferredSize(new java.awt.Dimension(20, 20));
        jPanel36.add(jSeparator23);

        RadioGetOnlineDaily.setBackground(new java.awt.Color(255, 255, 255));
        RadioGetOnlineDaily.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        RadioGetOnlineDaily.setText("بيانات اليوميات");
        RadioGetOnlineDaily.setFocusPainted(false);
        RadioGetOnlineDaily.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        RadioGetOnlineDaily.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        RadioGetOnlineDaily.setNextFocusableComponent(RadioGetTax);
        RadioGetOnlineDaily.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RadioGetOnlineDailyActionPerformed(evt);
            }
        });
        jPanel36.add(RadioGetOnlineDaily);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(157, 157, 157)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ComboB_SubBranch1, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel47)
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel50, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addGap(0, 61, Short.MAX_VALUE)
                        .addComponent(jSeparator19, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel24))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel36, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(ComboB_SubBranch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addComponent(jSeparator19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(177, 177, 177)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 740, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(177, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        progBar.setBackground(new java.awt.Color(102, 142, 179));
        progBar.setForeground(new java.awt.Color(0, 204, 0));
        progBar.setStringPainted(true);

        upload_downLabel.setText("down");

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(upload_downLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(progBar, javax.swing.GroupLayout.PREFERRED_SIZE, 1008, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(progBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
            .addComponent(upload_downLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanelQuaryLayout = new javax.swing.GroupLayout(jPanelQuary);
        jPanelQuary.setLayout(jPanelQuaryLayout);
        jPanelQuaryLayout.setHorizontalGroup(
            jPanelQuaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelQuaryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
            .addGroup(jPanelQuaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelQuaryLayout.createSequentialGroup()
                    .addContainerGap(9, Short.MAX_VALUE)
                    .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(8, Short.MAX_VALUE)))
        );
        jPanelQuaryLayout.setVerticalGroup(
            jPanelQuaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelQuaryLayout.createSequentialGroup()
                .addContainerGap(443, Short.MAX_VALUE)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanelQuaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelQuaryLayout.createSequentialGroup()
                    .addGap(14, 14, 14)
                    .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(51, Short.MAX_VALUE)))
        );

        jPanelScreens.add(jPanelQuary, "card6");

        jPanelOptionsBar = new RoundedPanel();
        jPanelOptionsBar.setBackground(new java.awt.Color(255, 255, 255));

        GetFromExcel.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        GetFromExcel.setForeground(new java.awt.Color(0, 102, 0));
        GetFromExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/file_export_24dp_78A75A_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        GetFromExcel.setToolTipText("إستيراد من ملف Excel");
        GetFromExcel.setFocusPainted(false);
        GetFromExcel.setPreferredSize(new java.awt.Dimension(50, 28));
        GetFromExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GetFromExcelActionPerformed(evt);
            }
        });
        jPanelOptionsBar.add(GetFromExcel);

        SaveAsExcel1.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        SaveAsExcel1.setForeground(new java.awt.Color(0, 102, 0));
        SaveAsExcel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/file_save_24dp_78A75A_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        SaveAsExcel1.setToolTipText("حفظ كملف Excel");
        SaveAsExcel1.setFocusPainted(false);
        SaveAsExcel1.setPreferredSize(new java.awt.Dimension(50, 28));
        SaveAsExcel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveAsExcel1ActionPerformed(evt);
            }
        });
        jPanelOptionsBar.add(SaveAsExcel1);

        jSeparator9.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator9.setPreferredSize(new java.awt.Dimension(5, 30));
        jPanelOptionsBar.add(jSeparator9);

        RadioOPDate.setBackground(new java.awt.Color(255, 255, 255));
        RadioOPDate.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        RadioOPDate.setText("تاريخ العملية ");
        RadioOPDate.setNextFocusableComponent(BillsNavigator);
        jPanelOptionsBar.add(RadioOPDate);

        RadiCodeNum.setBackground(new java.awt.Color(255, 255, 255));
        RadiCodeNum.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        RadiCodeNum.setText("رقم الكود ");
        RadiCodeNum.setNextFocusableComponent(RadioOPDate);
        jPanelOptionsBar.add(RadiCodeNum);

        code_view.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        code_view.setForeground(new java.awt.Color(76, 51, 151));
        code_view.setIcon(new javax.swing.ImageIcon(getClass().getResource("/search_insights_30dp_2854C5_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        code_view.setToolTipText("الإستعلام عن عملية");
        code_view.setFocusPainted(false);
        code_view.setNextFocusableComponent(RadiCodeNum);
        code_view.setPreferredSize(new java.awt.Dimension(50, 28));
        code_view.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                code_viewMouseClicked(evt);
            }
        });
        code_view.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                code_viewActionPerformed(evt);
            }
        });
        jPanelOptionsBar.add(code_view);

        Delete.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        Delete.setForeground(new java.awt.Color(0, 102, 0));
        Delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/delete_24dp_EA3323_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        Delete.setToolTipText("حذف");
        Delete.setFocusPainted(false);
        Delete.setPreferredSize(new java.awt.Dimension(50, 28));
        Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteActionPerformed(evt);
            }
        });
        jPanelOptionsBar.add(Delete);

        Clear.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        Clear.setForeground(new java.awt.Color(0, 102, 0));
        Clear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/clear_all_30dp_BB271A_FILL0_wght300_GRAD0_opsz24.png"))); // NOI18N
        Clear.setToolTipText("مسح");
        Clear.setFocusPainted(false);
        Clear.setPreferredSize(new java.awt.Dimension(50, 28));
        Clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearActionPerformed(evt);
            }
        });
        jPanelOptionsBar.add(Clear);

        Refreh.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        Refreh.setForeground(new java.awt.Color(0, 102, 0));
        Refreh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/refresh_24dp_2854C5_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        Refreh.setToolTipText("تحديث البيانات");
        Refreh.setFocusPainted(false);
        Refreh.setPreferredSize(new java.awt.Dimension(50, 28));
        Refreh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefrehActionPerformed(evt);
            }
        });
        jPanelOptionsBar.add(Refreh);

        current_Balance1.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        current_Balance1.setForeground(new java.awt.Color(0, 102, 0));
        current_Balance1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/inventory_30dp_2854C5_FILL0_wght300_GRAD0_opsz24.png"))); // NOI18N
        current_Balance1.setToolTipText("الأرصدة الحالية");
        current_Balance1.setFocusPainted(false);
        current_Balance1.setPreferredSize(new java.awt.Dimension(50, 28));
        current_Balance1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                current_Balance1ActionPerformed(evt);
            }
        });
        jPanelOptionsBar.add(current_Balance1);

        view_Shift_Sales1.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        view_Shift_Sales1.setForeground(new java.awt.Color(0, 102, 0));
        view_Shift_Sales1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/format_list_bulleted_30dp_2854C5_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        view_Shift_Sales1.setToolTipText("عرض يومية");
        view_Shift_Sales1.setFocusPainted(false);
        view_Shift_Sales1.setPreferredSize(new java.awt.Dimension(50, 28));
        view_Shift_Sales1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                view_Shift_Sales1ActionPerformed(evt);
            }
        });
        jPanelOptionsBar.add(view_Shift_Sales1);

        Details_of_DailySales.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        Details_of_DailySales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/read_more_30dp_4B77D1_FILL0_wght300_GRAD0_opsz24 (2).png"))); // NOI18N
        Details_of_DailySales.setToolTipText("عرض تفاصيل اليومية");
        Details_of_DailySales.setFocusPainted(false);
        Details_of_DailySales.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Details_of_DailySales.setInheritsPopupMenu(true);
        Details_of_DailySales.setPreferredSize(new java.awt.Dimension(50, 28));
        Details_of_DailySales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Details_of_DailySalesActionPerformed(evt);
            }
        });
        jPanelOptionsBar.add(Details_of_DailySales);

        Back.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        Back.setForeground(new java.awt.Color(0, 102, 0));
        Back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arrow_back_30dp_C6782F_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        Back.setToolTipText("رجوع");
        Back.setFocusPainted(false);
        Back.setPreferredSize(new java.awt.Dimension(50, 28));
        Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackActionPerformed(evt);
            }
        });
        jPanelOptionsBar.add(Back);

        Quary.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        Quary.setForeground(new java.awt.Color(0, 102, 0));
        Quary.setIcon(new javax.swing.ImageIcon(getClass().getResource("/manage_search_30dp_4B77D1_FILL0_wght300_GRAD0_opsz24.png"))); // NOI18N
        Quary.setToolTipText("استعلام");
        Quary.setFocusPainted(false);
        Quary.setPreferredSize(new java.awt.Dimension(50, 28));
        Quary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QuaryActionPerformed(evt);
            }
        });
        jPanelOptionsBar.add(Quary);

        Edit2.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        Edit2.setForeground(new java.awt.Color(0, 102, 0));
        Edit2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edit_note_30dp_2854C5_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        Edit2.setToolTipText("تعديل");
        Edit2.setFocusPainted(false);
        Edit2.setPreferredSize(new java.awt.Dimension(50, 28));
        Edit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Edit2ActionPerformed(evt);
            }
        });
        jPanelOptionsBar.add(Edit2);

        Edit.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        Edit.setForeground(new java.awt.Color(0, 102, 0));
        Edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edit_note_30dp_2854C5_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        Edit.setToolTipText("تعديل");
        Edit.setFocusPainted(false);
        Edit.setPreferredSize(new java.awt.Dimension(50, 28));
        Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditActionPerformed(evt);
            }
        });
        jPanelOptionsBar.add(Edit);

        SaveDailyReport.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        SaveDailyReport.setForeground(new java.awt.Color(0, 102, 0));
        SaveDailyReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/add_circle_30dp_4B77D1_FILL0_wght300_GRAD0_opsz24.png"))); // NOI18N
        SaveDailyReport.setToolTipText("اضافة يومية");
        SaveDailyReport.setFocusPainted(false);
        SaveDailyReport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        SaveDailyReport.setInheritsPopupMenu(true);
        SaveDailyReport.setPreferredSize(new java.awt.Dimension(50, 28));
        SaveDailyReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveDailyReportActionPerformed(evt);
            }
        });
        jPanelOptionsBar.add(SaveDailyReport);

        saveOperation.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        saveOperation.setForeground(new java.awt.Color(0, 102, 0));
        saveOperation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/add_circle_30dp_4B77D1_FILL0_wght300_GRAD0_opsz24.png"))); // NOI18N
        saveOperation.setToolTipText("حفظ جديد");
        saveOperation.setFocusPainted(false);
        saveOperation.setPreferredSize(new java.awt.Dimension(50, 28));
        saveOperation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveOperationActionPerformed(evt);
            }
        });
        jPanelOptionsBar.add(saveOperation);

        searchByDate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        searchByDate.setForeground(new java.awt.Color(51, 102, 255));
        searchByDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/database_search_30dp_2854C5_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        searchByDate.setText("استعلام");
        searchByDate.setToolTipText("ابحث");
        searchByDate.setFocusPainted(false);
        searchByDate.setPreferredSize(new java.awt.Dimension(108, 28));
        searchByDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchByDateActionPerformed(evt);
            }
        });
        jPanelOptionsBar.add(searchByDate);

        saveSettings.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        saveSettings.setForeground(new java.awt.Color(51, 102, 255));
        saveSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rule_settings_30dp_48752C_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        saveSettings.setText("حفظ الإعدادات");
        saveSettings.setToolTipText("حفظ الإعدادت");
        saveSettings.setFocusPainted(false);
        saveSettings.setPreferredSize(new java.awt.Dimension(150, 28));
        saveSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveSettingsActionPerformed(evt);
            }
        });
        jPanelOptionsBar.add(saveSettings);

        GetTax.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        GetTax.setForeground(new java.awt.Color(51, 102, 255));
        GetTax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/search.png"))); // NOI18N
        GetTax.setText("استعلام");
        GetTax.setFocusPainted(false);
        GetTax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GetTaxActionPerformed(evt);
            }
        });
        jPanelOptionsBar.add(GetTax);

        moreOption.setFont(new java.awt.Font("Simplified Arabic", 1, 18)); // NOI18N
        moreOption.setForeground(new java.awt.Color(255, 51, 51));
        moreOption.setIcon(new javax.swing.ImageIcon(getClass().getResource("/more.png"))); // NOI18N
        moreOption.setToolTipText("مزيد من الخيارات");
        moreOption.setComponentPopupMenu(popupMenu);
        moreOption.setFocusPainted(false);
        moreOption.setPreferredSize(new java.awt.Dimension(50, 28));
        moreOption.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                moreOptionMouseClicked(evt);
            }
        });
        jPanelOptionsBar.add(moreOption);

        txtbranchName.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        txtbranchName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtbranchName.setPreferredSize(new java.awt.Dimension(100, 30));
        jPanelOptionsBar.add(txtbranchName);

        moreOption2.setFont(new java.awt.Font("Simplified Arabic", 1, 18)); // NOI18N
        moreOption2.setForeground(new java.awt.Color(255, 51, 51));
        moreOption2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/more.png"))); // NOI18N
        moreOption2.setToolTipText("مزيد من الخيارات");
        moreOption2.setComponentPopupMenu(popupMenu);
        moreOption2.setFocusPainted(false);
        moreOption2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        moreOption2.setInheritsPopupMenu(true);
        moreOption2.setPreferredSize(new java.awt.Dimension(50, 28));
        moreOption2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                moreOption2MouseClicked(evt);
            }
        });
        jPanelOptionsBar.add(moreOption2);

        copyRightLab.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        copyRightLab.setForeground(new java.awt.Color(51, 102, 255));
        copyRightLab.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        copyRightLab.setText("Created by_ Ahmed  El hawy 2022");

        jPanelDetailsBar.setBackground(new java.awt.Color(255, 255, 255));

        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel12.setPreferredSize(new java.awt.Dimension(300, 1));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 277, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel37.setBackground(new java.awt.Color(255, 255, 255));
        jPanel37.setPreferredSize(new java.awt.Dimension(665, 30));
        jPanel37.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        labelBranchName.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelBranchName.setForeground(new java.awt.Color(0, 102, 255));
        labelBranchName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelBranchName.setText("الفرع");
        labelBranchName.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        labelBranchName.setPreferredSize(new java.awt.Dimension(210, 25));
        jPanel37.add(labelBranchName);

        labelShift.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelShift.setForeground(new java.awt.Color(0, 102, 255));
        labelShift.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelShift.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        labelShift.setPreferredSize(new java.awt.Dimension(50, 25));
        jPanel37.add(labelShift);

        lblPicShift.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPicShift.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblPicShift.setPreferredSize(new java.awt.Dimension(30, 25));
        jPanel37.add(lblPicShift);

        jSeparator14.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator14.setPreferredSize(new java.awt.Dimension(5, 25));
        jPanel37.add(jSeparator14);

        labelDate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelDate.setForeground(new java.awt.Color(0, 102, 255));
        labelDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelDate.setText("التاريخ");
        labelDate.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        labelDate.setPreferredSize(new java.awt.Dimension(134, 25));
        jPanel37.add(labelDate);

        lblPicDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPicDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/calendar_month_30dp_48752C_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        lblPicDate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblPicDate.setPreferredSize(new java.awt.Dimension(30, 25));
        jPanel37.add(lblPicDate);

        labelUser.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelUser.setForeground(new java.awt.Color(0, 102, 255));
        labelUser.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelUser.setText("المستخدم");
        labelUser.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        labelUser.setPreferredSize(new java.awt.Dimension(200, 25));
        jPanel37.add(labelUser);

        lblPic.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPic.setIcon(new javax.swing.ImageIcon("D:\\programs\\JavaApplications\\EgyC_DutyFree6.0.0\\src\\main\\resources\\account_circle_30dp_48752C_FILL0_wght400_GRAD0_opsz24.png")); // NOI18N
        lblPic.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblPic.setPreferredSize(new java.awt.Dimension(30, 25));
        jPanel37.add(lblPic);

        jSeparator13.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator13.setPreferredSize(new java.awt.Dimension(5, 25));
        jPanel37.add(jSeparator13);

        jLabel31.setBackground(new java.awt.Color(216, 212, 212));
        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("إدارة الأرصدة ");
        jLabel31.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jLabel31.setMaximumSize(new java.awt.Dimension(200, 25));
        jLabel31.setMinimumSize(new java.awt.Dimension(200, 25));
        jLabel31.setPreferredSize(new java.awt.Dimension(280, 25));
        jPanel37.add(jLabel31);

        javax.swing.GroupLayout jPanelDetailsBarLayout = new javax.swing.GroupLayout(jPanelDetailsBar);
        jPanelDetailsBar.setLayout(jPanelDetailsBarLayout);
        jPanelDetailsBarLayout.setHorizontalGroup(
            jPanelDetailsBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanelDetailsBarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDetailsBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.TRAILING, 279, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, 1099, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanelDetailsBarLayout.setVerticalGroup(
            jPanelDetailsBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDetailsBarLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
        );

        javax.swing.GroupLayout jPanelContainerLayout = new javax.swing.GroupLayout(jPanelContainer);
        jPanelContainer.setLayout(jPanelContainerLayout);
        jPanelContainerLayout.setHorizontalGroup(
            jPanelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(copyRightLab, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelContainerLayout.createSequentialGroup()
                .addGroup(jPanelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelDetailsBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelOptionsBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanelScreens, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelContainerLayout.setVerticalGroup(
            jPanelContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelContainerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelDetailsBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelScreens, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelOptionsBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addComponent(copyRightLab))
        );

        jPanelMainContainer.add(jPanelContainer);

        jPanelSpeace2.setBackground(new java.awt.Color(200, 204, 211));
        jPanelSpeace2.setMinimumSize(new java.awt.Dimension(6, 570));
        jPanelSpeace2.setPreferredSize(new java.awt.Dimension(12, 570));

        javax.swing.GroupLayout jPanelSpeace2Layout = new javax.swing.GroupLayout(jPanelSpeace2);
        jPanelSpeace2.setLayout(jPanelSpeace2Layout);
        jPanelSpeace2Layout.setHorizontalGroup(
            jPanelSpeace2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );
        jPanelSpeace2Layout.setVerticalGroup(
            jPanelSpeace2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 649, Short.MAX_VALUE)
        );

        jPanelMainContainer.add(jPanelSpeace2);

        drawerPanel = new RoundedPanel();
        drawerPanel.setBackground(new java.awt.Color(255, 255, 255));

        BillsNavigator.setFont(new java.awt.Font("Traditional Arabic", 1, 13)); // NOI18N
        BillsNavigator.setIcon(new javax.swing.ImageIcon(getClass().getResource("/checkbook_30dp_2854C5_FILL0_wght300_GRAD0_opsz24.png"))); // NOI18N
        BillsNavigator.setToolTipText("إدارة الأرصدة");
        BillsNavigator.setFocusPainted(false);
        BillsNavigator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BillsNavigatorActionPerformed(evt);
            }
        });

        LogOut.setFont(new java.awt.Font("Simplified Arabic", 1, 12)); // NOI18N
        LogOut.setForeground(new java.awt.Color(255, 51, 51));
        LogOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/logout_30dp_BB271A_FILL0_wght300_GRAD0_opsz24.png"))); // NOI18N
        LogOut.setToolTipText("تسجيل الخروج");
        LogOut.setComponentPopupMenu(popupMenu);
        LogOut.setFocusPainted(false);
        LogOut.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        LogOut.setInheritsPopupMenu(true);
        LogOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogOutActionPerformed(evt);
            }
        });

        TaxNavigator.setFont(new java.awt.Font("Traditional Arabic", 1, 12)); // NOI18N
        TaxNavigator.setIcon(new javax.swing.ImageIcon(getClass().getResource("/calculate_30dp_2854C5_FILL0_wght300_GRAD0_opsz24.png"))); // NOI18N
        TaxNavigator.setToolTipText("الضرائب والرسوم");
        TaxNavigator.setFocusPainted(false);
        TaxNavigator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TaxNavigatorActionPerformed(evt);
            }
        });

        ReportsNavigator.setFont(new java.awt.Font("Traditional Arabic", 1, 12)); // NOI18N
        ReportsNavigator.setIcon(new javax.swing.ImageIcon(getClass().getResource("/add_chart_30dp_2854C5_FILL0_wght300_GRAD0_opsz24.png"))); // NOI18N
        ReportsNavigator.setToolTipText("التقارير");
        ReportsNavigator.setFocusPainted(false);
        ReportsNavigator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReportsNavigatorActionPerformed(evt);
            }
        });

        SettingsNavigator1.setFont(new java.awt.Font("Traditional Arabic", 1, 12)); // NOI18N
        SettingsNavigator1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/settings_30dp_2854C5_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        SettingsNavigator1.setToolTipText("الإعدادات");
        SettingsNavigator1.setFocusPainted(false);
        SettingsNavigator1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SettingsNavigator1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout drawerPanelLayout = new javax.swing.GroupLayout(drawerPanel);
        drawerPanel.setLayout(drawerPanelLayout);
        drawerPanelLayout.setHorizontalGroup(
            drawerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(drawerPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(drawerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(BillsNavigator, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(LogOut, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(TaxNavigator, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(ReportsNavigator, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(SettingsNavigator1, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(jSeparator11, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                .addContainerGap())
        );
        drawerPanelLayout.setVerticalGroup(
            drawerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(drawerPanelLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(BillsNavigator, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ReportsNavigator, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TaxNavigator, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SettingsNavigator1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 364, Short.MAX_VALUE)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );

        jPanelMainContainer.add(drawerPanel);

        jPanelSpeace3.setBackground(new java.awt.Color(200, 204, 211));
        jPanelSpeace3.setPreferredSize(new java.awt.Dimension(8, 660));

        javax.swing.GroupLayout jPanelSpeace3Layout = new javax.swing.GroupLayout(jPanelSpeace3);
        jPanelSpeace3.setLayout(jPanelSpeace3Layout);
        jPanelSpeace3Layout.setHorizontalGroup(
            jPanelSpeace3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanelSpeace3Layout.setVerticalGroup(
            jPanelSpeace3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 660, Short.MAX_VALUE)
        );

        jPanelMainContainer.add(jPanelSpeace3);

        getContentPane().add(jPanelMainContainer, "card2");

        setSize(new java.awt.Dimension(1231, 656));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void checkRemoteActivationOnStartup() {
        // 1. معرف شيت التفعيل الخاص بك على جوجل درايف
        String activationSheetID = "1weWK2m7yOb1zutBCc2HcmwNrJ5OdBODFR7t_LPKjaT4";

        if (isInternetAvailable()) {
            try {
                // سحب بيانات التفعيل من السحابة (تم تعديل النطاق إلى A1:B لقراءة السطر الأول بنجاح)
                List<List<Object>> remoteActivationData = DataSyncManager.readSheetData(activationSheetID, "Sheet1!A2:B");

                if (remoteActivationData != null && !remoteActivationData.isEmpty()) {
                    Connection con = DBConnection.getInstance().getConnection();

                    // تفريغ جدول التفعيل المحلي وتحديثه بأحدث بيانات قادمة من السحابة
                    try (Statement stmt = con.createStatement()) {
                        stmt.executeUpdate("DELETE FROM ActiveTable");
                    }

                    String insertSQL = "INSERT INTO ActiveTable (code) VALUES (?)";
                    try (PreparedStatement pstmt = con.prepareStatement(insertSQL)) {
                        for (List<Object> row : remoteActivationData) {
                            if (row != null && !row.isEmpty() && row.get(0) != null) {
                                pstmt.setString(1, row.get(0).toString());
                                pstmt.executeUpdate();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("فشل التحديث من السحابة، سيتم الاعتماد على الجدول المحلي: " + e.getMessage());
            }
        }

        // 2. التحقق الفوري من قاعدة البيانات المحلية بعد التحديث (أو من النسخة المحلية إن لم يكن هناك انترنت)
        if (isActivationRestricted()) {
            // إظهار رسالة الفشل مرة واحدة وبشكل صارم للمستخدم
            javax.swing.JOptionPane.showMessageDialog(null,
                    "فشل في تفعيل التطبيق، يرجى التواصل مع الدعم الفني.",
                    "خطأ ترخيص",
                    javax.swing.JOptionPane.ERROR_MESSAGE);

            // إغلاق البرنامج نهائياً وفوراً لمنع تجاوزه أو فتح الواجهة
            System.exit(0);
        }
    }

// دالة لفحص ما إذا كان جدول ActiveTable يحتوي على الكود المحظور
    private static boolean isActivationRestricted() {
        String query = "SELECT COUNT(*) FROM ActiveTable WHERE code = ?";
        try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {

            // الرقم أو الكود المعتمد لإيقاف وتجميد التطبيق عن بُعد (مثلاً 9999)
            pstmt.setInt(1, 9999);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // إذا وجد الكود، تعود القيمة True (أي محظور)
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

// دالة التحقق من الاتصال بالإنترنت
    private static boolean isInternetAvailable() {
        try {
            java.net.InetAddress.getByName("script.google.com");
            return true;
        } catch (java.io.IOException e) {
            return false;
        }
    }

    public ArrayList<Bills> BillsList(String sql) {
        ArrayList<Bills> billsList = new ArrayList<>();

        // 1. الحصول على الاتصال من الكلاس المخصص
        Connection con = DBConnection.getInstance().getConnection();

        // 2. التحقق من أن الاتصال ليس null
        if (con == null) {
            JOptionPane.showMessageDialog(null, "فشل الاتصال بقاعدة البيانات.");
            return billsList;
        }

        try (
                // 3. استخدام try-with-resources لضمان إغلاق الموارد تلقائياً
                Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); ResultSet rs = stmt.executeQuery(sql)) {
            Bills bills;
            while (rs.next()) {
                bills = new Bills(
                        rs.getBytes("image"),
                        rs.getString("nation"),
                        rs.getString("notes"),
                        rs.getString("type"),
                        rs.getInt("winTaxP"),
                        rs.getInt("winTaxD"),
                        rs.getInt("unitPrice"),
                        rs.getInt("price"),
                        rs.getInt("count"),
                        rs.getInt("output"),
                        rs.getInt("input"),
                        rs.getString("addDate"),
                        rs.getInt("id"),
                        rs.getInt("billNum"),
                        rs.getString("name"),
                        rs.getString("passport"),
                        rs.getString("birthDate")
                );
                billsList.add(bills);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "حدث خطأ في قاعدة البيانات: " + e.getMessage());
            System.err.println("خطأ SQL في BillsList: " + e.getMessage());
        }
        return billsList;
    }

    public ArrayList<Bills> UserListCount() {
        ArrayList<Bills> usersListCount = new ArrayList<>();

        String query = "SELECT * FROM Table3 WHERE addDate LIKE ?";
        Connection con = DBConnection.getInstance().getConnection();

        if (con == null) {
            JOptionPane.showMessageDialog(null, "فشل الاتصال بقاعدة البيانات.");
            return usersListCount;
        }

        try (
                // 4. استخدام try-with-resources لضمان إغلاق الموارد تلقائياً
                PreparedStatement pstmt = con.prepareStatement(query,
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);) {
            // 5. بناء القيمة الكاملة لشرط LIKE وتعيينها للاستعلام
            String datePattern = year + "-_" + month + "-%";
            pstmt.setString(1, datePattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                Bills user;
                while (rs.next()) {
                    user = new Bills(
                            rs.getBytes("image"),
                            rs.getString("nation"),
                            rs.getString("notes"),
                            rs.getString("type"),
                            rs.getInt("winTaxP"),
                            rs.getInt("winTaxD"),
                            rs.getInt("unitPrice"),
                            rs.getInt("price"),
                            rs.getInt("count"),
                            rs.getInt("output"),
                            rs.getInt("input"),
                            rs.getString("addDate"),
                            rs.getInt("id"),
                            rs.getInt("billNum"),
                            rs.getString("name"),
                            rs.getString("passport"),
                            rs.getString("birthDate")
                    );
                    usersListCount.add(user);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "حدث خطأ في قاعدة البيانات: " + e.getMessage());
            System.err.println("خطأ SQL في UserListCount: " + e.getMessage());
        }
        return usersListCount;

    }

    public ArrayList<SalesFor> SalesForList() {
        ArrayList<SalesFor> salesForList = new ArrayList<>();

//        System.out.println(">>> [check point 1  SalesForList] تم استدعاء الدالة بنجاح. Year = " + year + " | Month = " + month);
        String query = "SELECT * FROM Table2 WHERE addDate LIKE ?";
        Connection con = DBConnection.getInstance().getConnection();

        if (con == null) {
            JOptionPane.showMessageDialog(null, "فشل الاتصال بقاعدة البيانات.");
            return salesForList;
        }

        try (
                PreparedStatement pstmt = con.prepareStatement(query,
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

            // التصحيح: إزالة الـ "_" الزائدة ليصبح النمط بالشكل الصحيح YYYY-MM-%
            String finalDatePattern = year + "-" + month + "-%";
            pstmt.setString(1, finalDatePattern);

//            System.out.println(">>> [فحص check point 2 SalesForList] النمط المستخدم في البحث (addDate LIKE): " + finalDatePattern);
            try (
                    ResultSet rs = pstmt.executeQuery()) {
                SalesFor salesFor;
//                int recordCount = 0;
                while (rs.next()) {
//                    recordCount++; // زيادة العداد مع كل سجل يتم العثور عليه
                    salesFor = new SalesFor(
                            rs.getString("users"),
                            rs.getString("EditDate"),
                            rs.getString("branchName"),
                            rs.getInt("id"),
                            rs.getString("addDate"),
                            rs.getInt("cigNum"),
                            rs.getInt("cigPrice"),
                            rs.getInt("winNum"),
                            rs.getInt("winPrice"),
                            rs.getInt("beerNum"),
                            rs.getInt("beerPrice"),
                            rs.getInt("foodNum"),
                            rs.getInt("foodPrice"),
                            rs.getInt("prfuNum"),
                            rs.getInt("prfuprice"),
                            rs.getInt("deviceNum"),
                            rs.getInt("devicePrice"),
                            rs.getInt("gifNum"),
                            rs.getInt("gifprice"),
                            rs.getInt("winTaxD"),
                            rs.getInt("winTaxP")
                    );
                    salesForList.add(salesFor);
                }
//                System.out.println(">>> [فحص SalesForList] إجمالي السجلات المسترجعة من القاعدة = " + recordCount);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "حدث خطأ في قاعدة البيانات: " + e.getMessage());
            System.err.println("خطأ SQL في salesList: " + e.getMessage());
        }
        return salesForList;
    }

    public ArrayList<SalesFor> SubSalesForList() {
        ArrayList<SalesFor> salesForList = new ArrayList<>();

        String query = "SELECT * FROM SubTable2 WHERE addDate LIKE ?";
        Connection con = DBConnection.getInstance().getConnection();

        if (con == null) {
            JOptionPane.showMessageDialog(null, "فشل الاتصال بقاعدة البيانات.");
            return salesForList;
        }

        try (
                PreparedStatement pstmt = con.prepareStatement(query,
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

            // التصحيح: إزالة الـ "_" الزائدة ليصبح النمط بالشكل الصحيح YYYY-MM-%
            String finalDatePattern = year + "-" + month + "-%";
            pstmt.setString(1, finalDatePattern);

            System.out.println(">>> [SubSalesForList] النمط المستخدم في البحث: " + finalDatePattern);

            try (
                    ResultSet rs = pstmt.executeQuery()) {
                SalesFor salesFor;
                int recordCount = 0;
                while (rs.next()) {
                    recordCount++;
                    salesFor = new SalesFor(
                            rs.getString("users"),
                            rs.getString("EditDate"),
                            rs.getString("branchName"),
                            rs.getInt("id"),
                            rs.getString("addDate"),
                            rs.getInt("cigNum"),
                            rs.getInt("cigPrice"),
                            rs.getInt("winNum"),
                            rs.getInt("winPrice"),
                            rs.getInt("beerNum"),
                            rs.getInt("beerPrice"),
                            rs.getInt("foodNum"),
                            rs.getInt("foodPrice"),
                            rs.getInt("prfuNum"),
                            rs.getInt("prfuprice"),
                            rs.getInt("deviceNum"),
                            rs.getInt("devicePrice"),
                            rs.getInt("gifNum"),
                            rs.getInt("gifprice"),
                            rs.getInt("winTaxD"),
                            rs.getInt("winTaxP")
                    );
                    salesForList.add(salesFor);
                }
                System.out.println(">>> [SubSalesForList] إجمالي السجلات المسترجعة = " + recordCount);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "حدث خطأ في قاعدة البيانات: " + e.getMessage());
            System.err.println("خطأ SQL في SubSalesForList: " + e.getMessage());
        }
        return salesForList;
    }

    public ArrayList<Sales> salesList() {
        ArrayList<Sales> salesList = new ArrayList<>();

        String query = "SELECT * FROM Table1 WHERE addDate LIKE ?";
        Connection con = DBConnection.getInstance().getConnection();

        if (con == null) {
            JOptionPane.showMessageDialog(null, "فشل الاتصال بقاعدة البيانات.");
            return salesList;
        }

        try (
                PreparedStatement pstmt = con.prepareStatement(query,
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

            // التصحيح سليم هنا
            String finalDatePattern = year + "-" + month + "-%";
            pstmt.setString(1, finalDatePattern);

            try (
                    ResultSet rs = pstmt.executeQuery()) {
                Sales sales;
                while (rs.next()) {
                    sales = new Sales(
                            rs.getString("users"),
                            rs.getString("EditDate"),
                            rs.getString("branchName"),
                            rs.getInt("id"),
                            rs.getString("addDate"),
                            rs.getInt("cigNum"),
                            rs.getInt("cigPrice"),
                            rs.getInt("winNum"),
                            rs.getInt("winPrice"),
                            rs.getInt("beerNum"),
                            rs.getInt("beerPrice"),
                            rs.getInt("foodNum"),
                            rs.getInt("foodPrice"),
                            rs.getInt("prfuNum"),
                            rs.getInt("prfuprice"),
                            rs.getInt("deviceNum"),
                            rs.getInt("devicePrice"),
                            rs.getInt("gifNum"),
                            rs.getInt("gifprice"),
                            rs.getInt("winTaxD"),
                            rs.getInt("winTaxP")
                    );
                    salesList.add(sales);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "حدث خطأ في قاعدة البيانات: " + e.getMessage());
            System.err.println("خطأ SQL في salesList: " + e.getMessage());
        }
        return salesList;
    }

    public ArrayList<Sales> SubsalesList() {
        ArrayList<Sales> salesList = new ArrayList<>();

        String query = "SELECT * FROM SubTable1 WHERE addDate LIKE ?";
        Connection con = DBConnection.getInstance().getConnection();

        if (con == null) {
            JOptionPane.showMessageDialog(null, "فشل الاتصال بقاعدة البيانات.");
            return salesList;
        }

        try (
                PreparedStatement pstmt = con.prepareStatement(query,
                        ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {

            // تم تصحيح الخطأ هنا بإزالة الـ "_" الزائدة
            String finalDatePattern = year + "-" + month + "-%";
            pstmt.setString(1, finalDatePattern);

            try (
                    ResultSet rs = pstmt.executeQuery()) {
                Sales sales;
                while (rs.next()) {
                    sales = new Sales(
                            rs.getString("users"),
                            rs.getString("EditDate"),
                            rs.getString("branchName"),
                            rs.getInt("id"),
                            rs.getString("addDate"),
                            rs.getInt("cigNum"),
                            rs.getInt("cigPrice"),
                            rs.getInt("winNum"),
                            rs.getInt("winPrice"),
                            rs.getInt("beerNum"),
                            rs.getInt("beerPrice"),
                            rs.getInt("foodNum"),
                            rs.getInt("foodPrice"),
                            rs.getInt("prfuNum"),
                            rs.getInt("prfuprice"),
                            rs.getInt("deviceNum"),
                            rs.getInt("devicePrice"),
                            rs.getInt("gifNum"),
                            rs.getInt("gifprice"),
                            rs.getInt("winTaxD"),
                            rs.getInt("winTaxP")
                    );
                    salesList.add(sales);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "حدث خطأ في قاعدة البيانات: " + e.getMessage());
            System.err.println("خطأ SQL في SubsalesList: " + e.getMessage());
        }
        return salesList;
    }

    public Settings loadSettings() {

        String query = "SELECT CigarPrice, CigPrice, exchangePrice, CigStatus, CigarStatus, winStatus, beerStatus, perfumStatus, devicesStatus, giftsStatus, foodStatus FROM SettingTable";
        Connection con = DBConnection.getInstance().getConnection();

        try (PreparedStatement pstmt = con.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                double cigarPrice = rs.getDouble("CigarPrice");
                double cigPrice = rs.getDouble("CigPrice");
                double exchangePrice = rs.getDouble("exchangePrice");

                // جلب حالات الأصناف الجديدة
                String cigStatus = rs.getString("CigStatus");
                String cigarStatus = rs.getString("CigarStatus");
                String winStatus = rs.getString("winStatus");
                String beerStatus = rs.getString("beerStatus");
                String perfumStatus = rs.getString("perfumStatus");
                String devicesStatus = rs.getString("devicesStatus");
                String giftsStatus = rs.getString("giftsStatus");
                String foodStatus = rs.getString("foodStatus");

                // تعبئة حقول النصوص
                exchangePriceTxt.setText(Double.toString(exchangePrice));
                CigarPriceTxt.setText(Double.toString(cigarPrice));
                CigPriceTxt.setText(Double.toString(cigPrice));

                // تحديد الـ CheckBoxes وإظهار/إخفاء اللوحات المرتبطة بها دفعة واحدة
                boolean isCigChecked = "true".equalsIgnoreCase(cigStatus);
                CheckCig.setSelected(isCigChecked);
                jPanel001.setVisible(isCigChecked);

                boolean isCigarChecked = "true".equalsIgnoreCase(cigarStatus);
                CheckCigar.setSelected(isCigarChecked);
//            jPanel002.setVisible(isCigarChecked);

                boolean isWinChecked = "true".equalsIgnoreCase(winStatus);
                CheckWin.setSelected(isWinChecked);
                jPanel002.setVisible(isWinChecked); // تأكد من رقم اللوحة للمشروبات

                boolean isBeerChecked = "true".equalsIgnoreCase(beerStatus);
                Checkbeer.setSelected(isBeerChecked);
                jPanel003.setVisible(isBeerChecked);

                boolean isPerfChecked = "true".equalsIgnoreCase(perfumStatus);
                CheckPerfum.setSelected(isPerfChecked);
                jPanel005.setVisible(isPerfChecked);

                boolean isDevChecked = "true".equalsIgnoreCase(devicesStatus);
                CheckDevice.setSelected(isDevChecked);
                jPanel006.setVisible(isDevChecked);

                boolean isGiftChecked = "true".equalsIgnoreCase(giftsStatus);
                CheckGift.setSelected(isGiftChecked);
                jPanel007.setVisible(isGiftChecked);

                boolean isFoodChecked = "true".equalsIgnoreCase(foodStatus);
                CheckFood.setSelected(isFoodChecked);
                jPanel004.setVisible(isFoodChecked);
                // أضف اسم الـ Panel الخاصة بالمأكولات هنا إن وجدت، مثلاً: 

                // إرجاع كائن الإعدادات في النهاية
                return new Settings(cigarPrice, cigPrice, exchangePrice, cigStatus, cigarStatus, winStatus, beerStatus, perfumStatus, devicesStatus, giftsStatus, foodStatus);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "خطأ في استرجاع الإعدادات: " + e.getMessage());
        }
        return null; // إذا كان الجدول فارغاً
    }

    public boolean saveSettings(double cigarPrice, double cigPrice, double exchangePrice) {
        // 1. فحص حالات الـ CheckBoxes وتحويلها إلى نصوص
        String cigStat = CheckCig.isSelected() ? "true" : "false";
        String cigarStat = CheckCigar.isSelected() ? "true" : "false";
        String winStat = CheckWin.isSelected() ? "true" : "false";
        String beerStat = Checkbeer.isSelected() ? "true" : "false";
        String perfStat = CheckPerfum.isSelected() ? "true" : "false";
        String devStat = CheckDevice.isSelected() ? "true" : "false";
        String giftStat = CheckGift.isSelected() ? "true" : "false";
        String foodStat = CheckFood.isSelected() ? "true" : "false";

        // 2. جملة التحديث الشاملة لتحديث الأسعار وحالات الأصناف في نفس الصف
        String query = "UPDATE [SettingTable] SET "
                + "[CigarPrice] = ?, [CigPrice] = ?, [exchangePrice] = ?, "
                + "[CigStatus] = ?, [CigarStatus] = ?, [winStatus] = ?, [beerStatus] = ?, "
                + "[perfumStatus] = ?, [devicesStatus] = ?, [giftsStatus] = ?, [foodStatus] = ?";

        Connection con = DBConnection.getInstance().getConnection();

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            // تعيين قيم الأسعار وسعر الصرف
            pstmt.setDouble(1, cigarPrice);
            pstmt.setDouble(2, cigPrice);
            pstmt.setDouble(3, exchangePrice);

            // تعيين قيم حالات الأصناف النصية
            pstmt.setString(4, cigStat);
            pstmt.setString(5, cigarStat);
            pstmt.setString(6, winStat);
            pstmt.setString(7, beerStat);
            pstmt.setString(8, perfStat);
            pstmt.setString(9, devStat);
            pstmt.setString(10, giftStat);
            pstmt.setString(11, foodStat);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                showSuccessNotification();
                return true;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "خطأ في حفظ الإعدادات: " + e.getMessage());
        }
        return false;
    }

    public void SaveSettingsNavegation() {

        try {
            // 1. قراءة القيم من حقول النص في الواجهة
            double cigar = Double.parseDouble(CigarPriceTxt.getText().trim());
            double cig = Double.parseDouble(CigPriceTxt.getText().trim());
            double exchange = Double.parseDouble(exchangePriceTxt.getText().trim());

            // 2. إرسالها لدالة الحفظ الموحدة (التي أصبحت تحفظ الأسعار والحالات معاً)
            boolean isSaved = saveSettings(cigar, cig, exchange);

            if (isSaved) {
                // تم استدعاء الإشعار أو رسالة النجاح بداخل دالة saveSettings مسبقاً
            } else {
                JOptionPane.showMessageDialog(this, "فشل الحفظ، تأكد من وجود صف بيانات SettingTable", "تنبيه", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "الرجاء إدخال أرقام صحيحة أو عشرية صحيحة في جميع الحقول", "خطأ إدخال", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void showSales_data() {

        ArrayList<Sales> List = salesList();

        DefaultTableModel model = (DefaultTableModel) table.getModel();

        model.setRowCount(0);
        Object[] row = new Object[20];

        for (int i = 0; i < List.size(); i++) {

            row[18] = List.get(i).getbranchName();
            row[17] = List.get(i).getId();
            row[16] = List.get(i).getaddDate();
            row[15] = List.get(i).getcigNum();
            row[14] = List.get(i).getcigPrice();
            row[13] = List.get(i).getwinNum();
            row[12] = List.get(i).getwinprice();
            row[11] = List.get(i).getbeerNum();
            row[10] = List.get(i).getbeerPrice();
            row[9] = List.get(i).getfoodNum();
            row[8] = List.get(i).getfoodprice();
            row[7] = List.get(i).getprfuNum();
            row[6] = List.get(i).getprfuprice();
            row[5] = List.get(i).getdeviceNum();
            row[4] = List.get(i).getdevicePrice();
            row[3] = List.get(i).getgifNum();
            row[2] = List.get(i).getgifprice();
            row[1] = List.get(i).getwinTaxD();
            row[0] = List.get(i).getwinTaxP();

            model.addRow(row);
        }
        labelUser.setText(Name);
        getToday();

        Delete.setEnabled(false);
        Edit2.setEnabled(false);
        Edit3.setEnabled(false);
        SaveDailyReport.setEnabled(true);
        ComboBoxShift.setSelectedIndex(0);
            labelShift.setText("");
            lblPicShift.setIcon(null);
    }

    public void SubshowSales_data() {

        ArrayList<Sales> List = SubsalesList();

        DefaultTableModel model = (DefaultTableModel) table.getModel();

        model.setRowCount(0);
        Object[] row = new Object[20];

        for (int i = 0; i < List.size(); i++) {

            row[18] = List.get(i).getbranchName();
            row[17] = List.get(i).getId();
            row[16] = List.get(i).getaddDate();
            row[15] = List.get(i).getcigNum();
            row[14] = List.get(i).getcigPrice();
            row[13] = List.get(i).getwinNum();
            row[12] = List.get(i).getwinprice();
            row[11] = List.get(i).getbeerNum();
            row[10] = List.get(i).getbeerPrice();
            row[9] = List.get(i).getfoodNum();
            row[8] = List.get(i).getfoodprice();
            row[7] = List.get(i).getprfuNum();
            row[6] = List.get(i).getprfuprice();
            row[5] = List.get(i).getdeviceNum();
            row[4] = List.get(i).getdevicePrice();
            row[3] = List.get(i).getgifNum();
            row[2] = List.get(i).getgifprice();
            row[1] = List.get(i).getwinTaxD();
            row[0] = List.get(i).getwinTaxP();

            model.addRow(row);
        }
        labelUser.setText(Name);
        getToday();

        Delete.setEnabled(false);
        Edit2.setEnabled(false);
        Edit3.setEnabled(false);
        ComboBoxShift.setSelectedIndex(0);
            labelShift.setText("");
            lblPicShift.setIcon(null);
    }

    public void showSalesFor_data() {

        ArrayList<SalesFor> List1 = SalesForList();

        DefaultTableModel modelfor = (DefaultTableModel) tablefor.getModel();

        modelfor.setRowCount(0);

        Object[] row = new Object[20];
        for (int i = 0; i < List1.size(); i++) {

            row[18] = List1.get(i).getbranchName();
            row[17] = List1.get(i).getId();
            row[16] = List1.get(i).getaddDate();
            row[15] = List1.get(i).getcigNum();
            row[14] = List1.get(i).getcigPrice();
            row[13] = List1.get(i).getwinNum();
            row[12] = List1.get(i).getwinprice();
            row[11] = List1.get(i).getbeerNum();
            row[10] = List1.get(i).getbeerPrice();
            row[9] = List1.get(i).getfoodNum();
            row[8] = List1.get(i).getfoodprice();
            row[7] = List1.get(i).getprfuNum();
            row[6] = List1.get(i).getprfuprice();
            row[5] = List1.get(i).getdeviceNum();
            row[4] = List1.get(i).getdevicePrice();
            row[3] = List1.get(i).getgifNum();
            row[2] = List1.get(i).getgifprice();
            row[1] = List1.get(i).getwinTaxD();
            row[0] = List1.get(i).getwinTaxP();

            modelfor.addRow(row);

        }
        labelUser.setText(Name);
        getToday();

        Delete.setEnabled(false);
        Edit2.setEnabled(false);
        Edit3.setEnabled(false);
        SaveDailyReport.setEnabled(true);
        ComboBoxShift.setSelectedIndex(0);
            labelShift.setText("");
            lblPicShift.setIcon(null);
    }

    public void SubshowSalesFor_data() {

        ArrayList<SalesFor> List1 = SubSalesForList();
        System.out.println("=== نقطة فحص  check point 2: عدد السجلات المسترجعة في List1 = " + List1.size());
        DefaultTableModel modelfor = (DefaultTableModel) tablefor.getModel();

        modelfor.setRowCount(0);

        Object[] row = new Object[20];
        for (int i = 0; i < List1.size(); i++) {

            row[18] = List1.get(i).getbranchName();
            row[17] = List1.get(i).getId();
            row[16] = List1.get(i).getaddDate();
            row[15] = List1.get(i).getcigNum();
            row[14] = List1.get(i).getcigPrice();
            row[13] = List1.get(i).getwinNum();
            row[12] = List1.get(i).getwinprice();
            row[11] = List1.get(i).getbeerNum();
            row[10] = List1.get(i).getbeerPrice();
            row[9] = List1.get(i).getfoodNum();
            row[8] = List1.get(i).getfoodprice();
            row[7] = List1.get(i).getprfuNum();
            row[6] = List1.get(i).getprfuprice();
            row[5] = List1.get(i).getdeviceNum();
            row[4] = List1.get(i).getdevicePrice();
            row[3] = List1.get(i).getgifNum();
            row[2] = List1.get(i).getgifprice();
            row[1] = List1.get(i).getwinTaxD();
            row[0] = List1.get(i).getwinTaxP();

            modelfor.addRow(row);

        }
        labelUser.setText(Name);
        getToday();

        Delete2.setEnabled(false);
        Edit2.setEnabled(false);
        Edit3.setEnabled(false);
        ComboBoxShift.setSelectedIndex(0);
            labelShift.setText("");
            lblPicShift.setIcon(null);
    }

    public void show_data_of_count() {

        ArrayList<Bills> List = UserListCount();

        DefaultTableModel modelcount = (DefaultTableModel) tablecount1.getModel();

        modelcount.setRowCount(0);
        Object[] row = new Object[13];

        for (int i = 0; i < List.size(); i++) {

//            row[0] = List.get(i).getimage();
//            row[0] = List.get(i).getnation();
//            row[1] = List.get(i).getnotes();
            row[0] = List.get(i).gettype();
//            row[3] = List.get(i).getwinTaxP();
//            row[4] = List.get(i).getwinTaxD();
//            row[5] = List.get(i).getunitPrice();
            row[1] = List.get(i).getprice();
            row[2] = List.get(i).getcount();
            row[3] = List.get(i).getoutput();
            row[4] = List.get(i).getinput();
            row[5] = List.get(i).getaddDate();
            row[6] = List.get(i).getId();

            modelcount.addRow(row);

        }
//        Edit.setEnabled(false);
        labelUser.setText(Name);
        getToday();
        ComboBoxShift.setSelectedIndex(0);
            labelShift.setText("");
            lblPicShift.setIcon(null);
    }

    public void search_code() {

        if (id.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "يرجى كتابة كود الصنف لاستكمال عملية البحث.");
            id.setFocusable(true);
            return;
        }

        saveOperation.setEnabled(true);
        Edit.setEnabled(false);

        String SQL3 = "SELECT * FROM Table3";

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {

            con = DBConnection.getInstance().getConnection();

            if (con == null || con.isClosed()) {
                JOptionPane.showMessageDialog(null, "فشل الاتصال بقاعدة البيانات.");
                return;
            }

            int searchid = Integer.parseInt(id.getText());

            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL3);

            int found = 0;
//        rs.absolute(0);
            while (rs.next()) {
                if (searchid == rs.getInt("id")) {
                    found = 1;
                    int id_col = rs.getInt("id");
                    int count2 = rs.getInt("count");
                    String notes1 = rs.getString("notes");
                    String addDate = rs.getString("addDate");
                    int price1 = rs.getInt("unitPrice");
                    String type1 = rs.getString("type");

                    if (rs.getBytes("image") == null) {
                        labelImage.setIcon(null);
                        labelImage.setText("لايوجد صورة");
                    } else {
                        byte[] image = rs.getBytes("image");
                        ImageIcon image1 = new ImageIcon(new ImageIcon(image).getImage()
                                .getScaledInstance(labelImage.getWidth(), labelImage.getHeight(), Image.SCALE_SMOOTH));
                        labelImage.setText("");
                        labelImage.setIcon(image1);
                    }

                    input.setText(String.valueOf(0));
                    output.setText(String.valueOf(0));
                    count.setText(String.valueOf(count2));
                    notes.setText(notes1);
                    price.setText(String.valueOf(price1));
                    ComboB_type.setSelectedItem(type1);
                    id.setText(String.valueOf(id_col));
                    operation_date.setText(addDate);
                    winTaxP.setText(String.valueOf(0));
                    winTaxD.setText(String.valueOf(0));
                    ComboB_national.setSelectedIndex(0);

//                break; 
                }
            }

            if (found == 0) {
                JOptionPane.showMessageDialog(null, "لايوجد نتائج مطابقة");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "يرجى إدخال رقم صحيح لكود الصنف.", "خطأ في الإدخال", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "حدث خطأ في قاعدة البيانات: " + e.getMessage(), "خطأ SQL", JOptionPane.ERROR_MESSAGE);

        }

//        // 1. التحقق من الإدخال
//        String searchText = id.getText().trim();
//        if (searchText.isEmpty()) {
//            JOptionPane.showMessageDialog(null, "يرجى كتابة كود الصنف لاستكمال عملية البحث.");
//            id.requestFocus();
//            return;
//        }
//
//        saveOperation.setEnabled(true);
//        Save_Edit.setEnabled(false);
//
//        // 2. نص الاستعلام (استخدام LIKE للبحث المرن)
//        String SQL3 = "SELECT * FROM ItemsTable WHERE EAN LIKE ? OR Material LIKE ? OR OldMaterial LIKE ? OR Description LIKE ?";
//
//        try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement ps = con.prepareStatement(SQL3)) {
//
//            if (con == null || con.isClosed()) {
//                JOptionPane.showMessageDialog(null, "فشل الاتصال بقاعدة البيانات.");
//                return;
//            }
//
//            // 3. إعداد قيمة البحث بالـ %
//            String searchParam = "%" + searchText + "%";
//            ps.setString(1, searchParam);
//            ps.setString(2, searchParam);
//            ps.setString(3, searchParam);
//            ps.setString(4, searchParam);
//
//            // 4. تنفيذ الاستعلام
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) { // إذا وجد نتيجة
//
//                    // جلب البيانات من الأعمدة (تأكد من نوع البيانات في Access)
//                    // ملحوظة: إذا كان Material نصاً في قاعدة البيانات استخدم rs.getString
//                    String materialVal = rs.getString("Material");
//                    int count2 = rs.getInt("Quantity");
//                    String notes1 = rs.getString("Description");
//                    String addDate = rs.getString("AddDate");
//                    double price1 = rs.getDouble("SalesPrice");
//                    String type1 = rs.getString("Category");
//
//                    // 5. تحديث الحقول في الواجهة
//                    input.setText("0");
//                    output.setText("0");
//                    Count.setText(String.valueOf(count2));
//                    notes.setText(notes1);
//                    price.setText(String.valueOf(price1));
//                    ComboB_type.setSelectedItem(type1);
//
//                    // التصحيح هنا: نضع القيمة مباشرة في الحقل
//                    id.setText(materialVal);
//
//                    operation_date.setText(addDate);
//                    winTaxP.setText("0");
//                    winTaxD.setText("0");
//                    ComboB_national.setSelectedIndex(0);
//
//                    // معالجة الصورة (إذا أردت تفعيلها لاحقاً)
////                byte[] image = rs.getBytes("image");
////                if (image != null && image.length > 0) {
////                     ImageIcon image1 = new ImageIcon(new ImageIcon(image).getImage()
////                             .getScaledInstance(labelImage.getWidth(), labelImage.getHeight(), Image.SCALE_SMOOTH));
////                     labelImage.setText("");
////                     labelImage.setIcon(image1);
////                } else {
////                     labelImage.setIcon(null);
////                     labelImage.setText("لا يوجد صورة");
////                }
//                } else {
//                    JOptionPane.showMessageDialog(null, "لا يوجد نتائج مطابقة للبحث.");
//                }
//            }
//
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "حدث خطأ في قاعدة البيانات: " + e.getMessage(), "خطأ SQL", JOptionPane.ERROR_MESSAGE);
//            e.printStackTrace();
//        }
    }

    public void Details_of_DailySales() {

        DefaultTableModel model = (DefaultTableModel) DailySalesFrame.EgyTable.getModel();
        DefaultTableModel model2 = (DefaultTableModel) DailySalesFrame.ForTabel.getModel();

// مسح الجداول قبل إضافة بيانات جديدة
        model.setRowCount(0);
        model2.setRowCount(0);

// المصفوفات الآن يجب أن تكون بحجم يتسع لكل الأنواع بالإضافة للبيانات
// الأفضل استخدام List<Integer> ولكن هذا تعديل طفيف على المصفوفات
        int[] egyptianOutput = new int[20];
        int[] egyptianPrice = new int[20];
        int[] egyptianWinTaxD = new int[20]; // مصفوفة جديدة للضريبة
        int[] egyptianWinTaxP = new int[20]; // مصفوفة جديدة للضريبة
        int[] foreignOutput = new int[20];
        int[] foreignPrice = new int[20];
        int[] foreignWinTaxD = new int[20]; // مصفوفة جديدة للضريبة
        int[] foreignWinTaxP = new int[20]; // مصفوفة جديدة للضريبة

// مصفوفة أنواع السلع لسهولة البحث
        String[] searchWord = new String[]{"سجائر", "مشروبات كحولية", "بيرة", "مأكولات", "عطور", "أجهزة", "هدايا"};

        String sqlQuery = "SELECT nation, type, SUM(output) AS total_output, SUM(price) AS total_price, "
                + "SUM(winTaxD) AS total_winTaxD, SUM(winTaxP) AS total_winTaxP "
                + "FROM Table3 "
                + "WHERE addDate = ? AND type IN ('هدايا','أجهزة','عطور','مأكولات','بيرة','مشروبات كحولية','سجائر') "
                + "GROUP BY nation, type";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            pstmt.setString(1, addDate);
            ResultSet rst = pstmt.executeQuery();

            while (rst.next()) {
                String nation = rst.getString("nation");
                String type = rst.getString("type");
                int Output = rst.getInt("total_output");
                int Price = rst.getInt("total_price");
                int WinTaxD = rst.getInt("total_winTaxD");
                int WinTaxP = rst.getInt("total_winTaxP");

                for (int i = 0; i < searchWord.length; i++) {
                    if (searchWord[i].equals(type)) {
                        if (nation.equals("مصرى")) {
                            egyptianOutput[i] = Output;
                            egyptianPrice[i] = Price;
                            egyptianWinTaxD[i] = WinTaxD;
                            egyptianWinTaxP[i] = WinTaxP;
                        } else {
                            foreignOutput[i] = Output;
                            foreignPrice[i] = Price;
                            foreignWinTaxD[i] = WinTaxD;
                            foreignWinTaxP[i] = WinTaxP;
                        }
                        break;
                    }
                }
            }

            // هنا يتم تعبئة الجداول بناءً على المصفوفات
            // هذا الجزء يعتمد على هيكل جدولك
            // سأفترض أن كل نوع سلعة له صف منفصل في الجدول
            for (int i = 0; i < searchWord.length; i++) {

                Object[] egyRow = {egyptianWinTaxP[i], egyptianWinTaxD[i], egyptianPrice[i], egyptianOutput[i], searchWord[i]};
                model.addRow(egyRow);

                Object[] forRow = {foreignWinTaxP[i], foreignWinTaxD[i], foreignPrice[i], foreignOutput[i], searchWord[i]};
                model2.addRow(forRow);
            }

            // لإضافة بيانات الضرائب لجدول ثالث
            // يجب معرفة هيكل هذا الجدول
            // مثال:
            // Object[] taxRow = { "ضرائب", مجموع الضرائب المصرية, مجموع الضرائب الأجنبية };
            // model3.addRow(taxRow);
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);
        }

        JFrameDailySales.setVisible(true);
//        JFrameDailySales.setIconImage(img);
    }

    public void getBranchName() {
        // الاستعلام لجلب الاسم الأكثر تكراراً في عمود branchName
        // ملاحظة: في Access نستخدم TOP 1 بدلاً من LIMIT 1
        String query = "SELECT TOP 1 branchName, COUNT(branchName) AS freq "
                + "FROM Table1 "
                + "GROUP BY branchName "
                + "ORDER BY COUNT(branchName) DESC";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                detectedBranch = rs.getString("branchName");

                // تحديث الـ Label مباشرة
                // نستخدم SwingUtilities لضمان التحديث الصحيح للواجهة
                javax.swing.SwingUtilities.invokeLater(() -> {
                    labelBranchName.setText(detectedBranch);
//                    txtbranchName.setText(detectedBranch);
                });

//            System.out.println("تم تحديد الفرع بنجاح: " + detectedBranch);
            } else {
                // في حال كان الجدول فارغاً تماماً
//            labelBranchName.setText(ComboB_Branch.getSelectedItem().toString());
                labelBranchName.setText("");
            }

        } catch (SQLException e) {
            System.err.println("خطأ أثناء جلب اسم الفرع: " + e.getMessage());
            labelBranchName.setText("خطأ في الاتصال");
        }
    }

    public boolean compareBranchName(String inputBranchName) {
        // 1. حماية أولية: التأكد من أن الاسم المُدخل ليس فارغاً
        if (inputBranchName == null || inputBranchName.trim().isEmpty()) {
            return false;
        }

        // 2. الاستعلام لجلب الاسم الأكثر تكراراً في الجدول
        String query = "SELECT TOP 1 branchName "
                + "FROM SubTable1 "
                + "GROUP BY branchName "
                + "ORDER BY COUNT(branchName) DESC";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                // تخزين الاسم المسحوب من الجدول في "متغير محلي" فقط داخل هذه الدالة
                String dbBranchName = rs.getString("branchName");

                // 3. مقارنة الاسم المُدخل بالاسم المسحوب من الجدول
                if (dbBranchName != null) {
                    return inputBranchName.trim().equalsIgnoreCase(dbBranchName.trim());
                }
            }

        } catch (SQLException e) {
            System.err.println("خطأ أثناء جلب اسم الفرع للمقارنة: " + e.getMessage());
        }

        // تُرجع false في حال: عدم التطابق، أو الجدول فارغ، أو حدوث خطأ في قاعدة البيانات
        return false;

    }

    private void executeInsertDetailed(Connection conn, String tableName, List<List<Object>> data) throws SQLException {
        // 1. مسح البيانات القديمة
        String deleteSQL = "DELETE FROM " + tableName;
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(deleteSQL);
        }

        String insertSQL = "INSERT INTO " + tableName + " (winTaxP, winTaxD, gifPrice, gifNum, devicePrice, deviceNum, "
                + "prfuPrice, prfuNum, foodPrice, foodNum, beerPrice, beerNum, winPrice, winNum, "
                + "cigPrice, cigNum, addDate, id, branchName, users, EditDate) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
            for (List<Object> row : data) {
                if (row == null || row.isEmpty() || row.get(0).toString().trim().equalsIgnoreCase("winTaxP")) {
                    continue;
                }

                for (int i = 0; i < 21; i++) {
                    Object cellValue = (i < row.size()) ? row.get(i) : null;
                    String valStr = (cellValue == null) ? "" : cellValue.toString().trim();

                    if (i < 16) {
                        // معالجة الأرقام: الحفاظ على الصفر في حال الفراغ
                        try {
                            String numericOnly = valStr.replaceAll("[^\\d.]", "");
                            double dVal = numericOnly.isEmpty() ? 0.0 : Double.parseDouble(numericOnly);
                            ps.setDouble(i + 1, dVal);
                        } catch (NumberFormatException e) {
                            ps.setDouble(i + 1, 0.0);
                        }
                    } else {
                        // معالجة النصوص والتواريخ
                        // ملاحظة: إذا كان الحقل في Access هو Date/Time، يفضل تحويل valStr إلى java.sql.Date
                        if (valStr.isEmpty()) {
                            ps.setNull(i + 1, java.sql.Types.VARCHAR);
                        } else {
                            ps.setString(i + 1, valStr);
                        }
                    }
                }
                ps.addBatch();
            }
            ps.executeBatch();
            System.out.println("✅ تم تحديث جدول " + tableName + " بنجاح.");
        }
    }

    public void saveRestoredDataToAccess_Detailed(List<List<Object>> table1Data, List<List<Object>> table2Data, String selectedBranchName) {

        // اسم الفرع المحلي لهذا الجهاز (المخزن في متغير البرنامج)
        String localBranch = detectedBranch;

        // تحديد هل الفرع المختار خارجي أم محلي
        boolean isExternal = !selectedBranchName.equalsIgnoreCase(localBranch);

        // تحديد أسماء الجداول المستهدفة بشكل صريح
        String table1Target = "";
        String table2Target = "";

        if (isExternal) {
            table1Target = "SubTable1";
            table2Target = "SubTable2";
            System.out.println("📍 الوضع: فرع خارجي. الهدف: الجداول الفرعية (SubTable)");
        } else {
            table1Target = "Table1";
            table2Target = "Table2";
            System.out.println("📍 الوضع: فرع محلي. الهدف: الجداول الرئيسية (Table)");
        }

        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false); // بدء المعاملة لضمان سلامة البيانات

            // 1. معالجة الجدول الأول
            if (table1Data != null && !table1Data.isEmpty()) {
                System.out.println("🚀 جاري تفريغ وحفظ بيانات " + table1Target);
                executeInsertDetailed(conn, table1Target, table1Data);
            }

            // 2. معالجة الجدول الثاني
            if (table2Data != null && !table2Data.isEmpty()) {
                System.out.println("🚀 جاري تفريغ وحفظ بيانات " + table2Target);
                executeInsertDetailed(conn, table2Target, table2Data);
            }

            conn.commit(); // تنفيذ كل العمليات بنجاح
            System.out.println("🎉 تمت عملية الحفظ في Access بنجاح.");

            // 3. تحديث الواجهة لعرض البيانات الجديدة
            final boolean finalIsExternal = isExternal;
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (finalIsExternal) {
                        SubshowSales_data();    // عرض بيانات الجداول الفرعية
                        SubshowSalesFor_data();
                    } else {
                        showSales_data();       // عرض بيانات الجداول الرئيسية
                        showSalesFor_data();
                    }
//                     showSuccessNotification();
                    JOptionPane.showMessageDialog(null, "تم استرجاع وعرض بيانات فرع: " + selectedBranchName);
                }
            });

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "خطأ في قاعدة البيانات أثناء الحفظ: " + e.getMessage());
        }
    }

    public void DownloadData() {
        // 1. نافذة تحذير للمستخدم قبل بدء عملية التنزيل
        int confirm = JOptionPane.showConfirmDialog(null,
                "تحذير هام: الاستمرار في عملية التنزيل سيؤدي إلى إزالة كافة البيانات الحالية في قاعدة البيانات المحلية!\nهل تريد الاستمرار؟",
                "تأكيد تنزيل البيانات",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        // إذا لم يضغط المستخدم على "نعم"، يتم إيقاف العملية الخروج من الدالة فوراً
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Thread downloadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 1. تحديد الفرع المختار
                    String selectedBranchName = ComboB_SubBranch1.getSelectedItem().toString();

                    if (selectedBranchName.equals("اختر الفرع")) {
                        JOptionPane.showMessageDialog(null, "يرجى اختيار فرع من القائمة أولاً");
                        return;
                    }

                    // 🚀 إظهار شريط التقدم وتصفيره في بداية العملية
                    SwingUtilities.invokeLater(() -> {
                        progBar.setVisible(true);
                        upload_downLabel.setVisible(true);
                        java.net.URL downloadImgUrl = getClass().getResource("/downloading_30dp_F19E39_FILL0_wght400_GRAD0_opsz24.png");

                        if (downloadImgUrl != null) {
                            upload_downLabel.setIcon(new ImageIcon(downloadImgUrl));
                            upload_downLabel.setText(""); // إزالة النص إن وجد لضمان ظهور الأيقونة وحدها
                        } else {
                            upload_downLabel.setText("❌");
                            System.out.println("تحذير: لم يتم العثور على ملف الأيقونة في المسار المحدد!");
                        }
                        progBar.setValue(0);
                        progBar.setStringPainted(true); // لإظهار النسبة المئوية %
                        progBar.setString("جاري بدء الاتصال بجوجل...");
                    });

                    // 2. التحقق من المعرفات
                    String targetID1 = idTable1;
                    String targetID2 = idTable2;

                    if (targetID1 == null || targetID1.isEmpty()) {
                        SwingUtilities.invokeLater(() -> progBar.setVisible(false));
                        JOptionPane.showMessageDialog(null, "خطأ: معرف الجدول 1 غير موجود");
                        return;
                    }

                    // تحديث التقدم (20%)
                    updateProg(20, "جاري قراءة بيانات مشتريات المصرى...");

                    // 3. قراءة البيانات فعلياً من جوجل شيت
                    List<List<Object>> remoteData1 = DataSyncManager.readSheetData(targetID1, "Sheet1!A2:U");

                    // تحديث التقدم (50%)
                    updateProg(50, "جاري قراءة بيانات مشتريات الأجنبى...");

                    List<List<Object>> remoteData2 = DataSyncManager.readSheetData(targetID2, "Sheet1!A2:U");

                    // 4. فحص أولي للبيانات المجلوبة
                    if (remoteData1 == null || remoteData1.isEmpty()) {
                        SwingUtilities.invokeLater(() -> progBar.setVisible(false));
                        JOptionPane.showMessageDialog(null, "قاعدة البيانات السحابية فارغة.");
                        return;
                    }

                    // تحديث التقدم (80%) قبل الحفظ في Access
                    updateProg(80, "جاري حفظ البيانات في قاعدة البيانات المحلية...");

                    // 5. إرسال البيانات لدالة الحفظ التفصيلية
                    saveRestoredDataToAccess_Detailed(remoteData1, remoteData2, selectedBranchName);

                    // تحديث التقدم (100%) والانتهاء
                    updateProg(100, "تم الاسترجاع بنجاح!");

                    // تأخير بسيط قبل الإخفاء ليشعر المستخدم بالانتهاء
                    Thread.sleep(500);

                    SwingUtilities.invokeLater(() -> {
                        progBar.setVisible(false);
                        upload_downLabel.setVisible(false);
                    });

                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> progBar.setVisible(false));
                    upload_downLabel.setVisible(false);
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "حدث خطأ: " + e.getMessage());
                }
            }
        });
        downloadThread.start();
    }

    private void updateProg(int value, String text) {
        SwingUtilities.invokeLater(() -> {
            progBar.setValue(value);
            progBar.setString(text);
        });
    }

    public void testGoogleConnection(String spreadsheetId) {
        try {
            System.out.println("بدء اختبار الاتصال بجوجل...");

            // 1. محاولة الحصول على الخدمة
            var service = GoogleSheetsService.getSheetsService();

            // 2. محاولة قراءة عنوان ملف الشيت للتأكد من الصلاحيات
            var response = service.spreadsheets().get(spreadsheetId).execute();
            String title = response.getProperties().getTitle();

            System.out.println("نجح الاتصال! اسم الملف المربوط هو: " + title);
            javax.swing.JOptionPane.showMessageDialog(null, "تم الاتصال بنجاح بقاعدة البيانات على الشبكة و الخاصة ب: " + title);

        } catch (Exception e) {
            System.err.println("فشل الاتصال: " + e.getMessage());
            String errorMsg = e.getMessage();

            if (errorMsg.contains("403")) {
                errorMsg = "خطأ 403: تأكد من مشاركة (Share) الملف مع إيميل الخدمة.";
            } else if (errorMsg.contains("404")) {
                errorMsg = "خطأ 404: معرف الملف (ID) غير صحيح.";
            }

            javax.swing.JOptionPane.showMessageDialog(null, "فشل الاختبار: \n" + errorMsg, "خطأ", 0);
        }
    }

    public void handleBranchSelection() {
        // 1. جلب الاسم الذي اختاره المستخدم من القائمة
        String selectedBranch = ComboB_SubBranch1.getSelectedItem().toString();

        // 2. التأكد من أن الاختيار ليس النص الافتراضي "اختر الفرع"
        if (selectedBranch.equals("اختر الفرع")) {
            return;
        }

        // 3. استعلام لجلب معرفات الجداول لهذا الفرع تحديداً
        String query = "SELECT id1, id2, id3 FROM branchesTable WHERE BranchName = ?";

        try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, selectedBranch);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // تخزين المعرفات في متغيرات الكلاس لاستخدامها لاحقاً في التحميل
                    this.idTable1 = rs.getString("id1");
                    this.idTable2 = rs.getString("id2");
                    this.idTable3 = rs.getString("id3");

                    // تحديث حقل النص باسم الفرع الحالي
//                    txtbranchName.setText(selectedBranch);
                    // إظهار اللوحة التي تحتوي على الجداول
                    jPanel12.setVisible(true);

                    System.out.println("✅ تم اختيار فرع: " + selectedBranch);
                    System.out.println("📌 المعرف النشط حالياً: " + this.idTable1);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "خطأ في قاعدة البيانات: " + e.getMessage());
        }
    }

    public void saveAndSetLocalBranch(String branchName) {
        String resetQuery = "UPDATE branchesTable SET status = ''";
        String setLocalQuery = "UPDATE branchesTable SET status = 'Local' WHERE BranchName = ?";

        try (Connection con = DBConnection.getInstance().getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement pstmtReset = con.prepareStatement(resetQuery); PreparedStatement pstmtSet = con.prepareStatement(setLocalQuery)) {

                pstmtReset.executeUpdate();
                pstmtSet.setString(1, branchName);
                pstmtSet.executeUpdate();
                con.commit();

                labelBranchName.setText(branchName);
                detectedBranch = branchName;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadLocalBranchSettings() {
        String query = "SELECT BranchName FROM branchesTable WHERE status = 'Local'";

        try (Connection con = DBConnection.getInstance().getConnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                String savedBranch = rs.getString("BranchName");

                // 1. تحديث الليبل
                labelBranchName.setText(savedBranch);
                detectedBranch = savedBranch;
                ComboB_SubBranch.setSelectedItem(savedBranch);
                ComboB_SubBranch1.setSelectedItem(savedBranch);

                System.out.println("📂 تم استعادة إعدادات الفرع المحلي: " + savedBranch);
            } else {
                labelBranchName.setText("لم يتم تحديد فرع");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fillBranchCombo() {
        // 1. تخزين المستمعين (Listeners) الحاليين في مصفوفة مؤقتة قبل حذفهم
        java.awt.event.ActionListener[] listeners = ComboB_SubBranch1.getActionListeners();
        java.awt.event.ActionListener[] listeners1 = ComboB_SubBranch.getActionListeners();

        // 2. إزالة المستمعين مؤقتاً لمنع استدعاء أحداث الـ ActionPerformed أثناء التحديث
        for (java.awt.event.ActionListener al : listeners) {
            ComboB_SubBranch1.removeActionListener(al);
        }
        for (java.awt.event.ActionListener al : listeners1) {
            ComboB_SubBranch.removeActionListener(al);
        }

        String query = "SELECT BranchName FROM branchesTable";

        try {
            // 3. تنظيف القوائم وإضافة خيار التعريف الافتراضي
            ComboB_SubBranch1.removeAllItems();
            ComboB_SubBranch1.addItem("اختر الفرع");

            ComboB_SubBranch.removeAllItems();
            ComboB_SubBranch.addItem("اختر الفرع");

            // 4. الاتصال بقاعدة البيانات وجلب الأسماء
            try (Connection con = DBConnection.getInstance().getConnection(); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    String branch = rs.getString("BranchName");
                    ComboB_SubBranch1.addItem(branch);
                    ComboB_SubBranch.addItem(branch);
                }

                System.out.println("✅ تم تحديث قوائم الفروع بنجاح.");

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "خطأ في قاعدة البيانات: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("⚠️ خطأ غير متوقع: " + e.getMessage());
        } finally {
            // 5. الأهم: إعادة المستمعين مرة أخرى للعمل بعد انتهاء التعبئة تماماً
            for (java.awt.event.ActionListener al : listeners) {
                ComboB_SubBranch1.addActionListener(al);
            }
            for (java.awt.event.ActionListener al : listeners1) {
                ComboB_SubBranch.addActionListener(al);
            }
        }
    }

    public JTable sum_Tables_Col(JTable table) {

        int sum[] = new int[15];

        for (int col = 0; col < table.getColumnCount(); col++) {

            for (int row = 0; row < table.getRowCount(); row++) {

                sum[col] = sum[col] + Integer.parseInt(table.getValueAt(row, col).toString());

            }

        }

        txt1.setText(String.valueOf(sum[0]));
        txt2.setText(String.valueOf(sum[1]));
        txt5.setText(String.valueOf(sum[2]));
        txt6.setText(String.valueOf(sum[3]));
        txt7.setText(String.valueOf(sum[4]));
        txt8.setText(String.valueOf(sum[5]));
        txt11.setText(String.valueOf(sum[6]));
        txt12.setText(String.valueOf(sum[7]));
        txt13.setText(String.valueOf(sum[8]));
        txt14.setText(String.valueOf(sum[9]));

        return table;
    }

    public JTable sum_Tables_Col2(JTable table) {

        int sum[] = new int[15];

        for (int col = 0; col < table.getColumnCount(); col++) {

            for (int row = 0; row < table.getRowCount(); row++) {

                sum[col] = sum[col] + Integer.parseInt(table.getValueAt(row, col).toString());

            }

        }
        txt36.setText(String.valueOf(sum[0]));
        txt35.setText(String.valueOf(sum[1]));
        txt21.setText(String.valueOf(sum[2]));
        txt22.setText(String.valueOf(sum[3]));
        txt25.setText(String.valueOf(sum[4]));
        txt26.setText(String.valueOf(sum[5]));
        txt27.setText(String.valueOf(sum[6]));
        txt28.setText(String.valueOf(sum[7]));
        txt31.setText(String.valueOf(sum[8]));
        txt32.setText(String.valueOf(sum[9]));
        txt33.setText(String.valueOf(sum[10]));
        txt34.setText(String.valueOf(sum[11]));

        return table;
    }

    public JTable sum_Tables_Col3(JTable table) {

        int sum[] = new int[15];

        for (int col = 0; col < table.getColumnCount(); col++) {

            for (int row = 0; row < table.getRowCount(); row++) {

                sum[col] = sum[col] + Integer.parseInt(table.getValueAt(row, col).toString());

            }

        }

        txt36.setText(String.valueOf(sum[0]));
        txt35.setText(String.valueOf(sum[1]));

        return table;
    }

    public int check_if_found(int found) {
        try {

            rs = stmt.executeQuery(SQL3);
            int searchid2 = Integer.parseInt(id.getText());
            rs.absolute(0);
            while (rs.next()) {
                if (searchid2 == rs.getInt("id")) {
                    found = 1;

                    int count2 = rs.getInt("count");
                    String notes1 = rs.getString("notes");
                    String addDate = rs.getString("addDate");
                    int price1 = rs.getInt("unitPrice");
                    String type1 = rs.getString("type");

                    count.setText(String.valueOf(count2));
                    notes.setText(notes1);
                    price.setText(String.valueOf(price1));
                    ComboB_type.setSelectedItem(type1);

                    operation_date.setText(addDate);
//                    break;
                }
            }
            if (found == 0) {

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return found;
    }

    public boolean numericTest(String str) {
        if (str == null) {
            return true;
        }
        try {
            Integer.parseInt(str);

        } catch (NumberFormatException nfe) {
//JOptionPane.showMessageDialog(null, "أدخل رقم صحيح");
            return false;
        }
        return true;
    }

    public void save_Edit() {
        int passw = 1111;
        String[] options = new String[]{"Cancel", "OK"};

        // 1. التحقق من اختيار صف
        int row = tablecount1.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "يجب اختيار ملف واحد من الجدول لتعديله");
            return;
        }
        if (ComboBoxShift.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "يرجى اختيار الوردية.");
            return;
        }

        // 2. التحقق من كلمة المرور
        int option = JOptionPane.showOptionDialog(null, panel, "تأكيد التعديل",
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);

        if (option == 1) {
            String inputPass = new String(pass.getPassword());
            if (passw == Integer.parseInt(inputPass)) {

                String updateQuery = "UPDATE Table3 SET id=?, addDate=?, input=?, output=?, count=?, "
                        + "price=?, winTaxP=?, winTaxD=?, type=?, notes=?, nation=?, image=?, "
                        + "name=?, passport=?, birthDate=?, place=?, shift=? "
                        + "WHERE id=? AND addDate=? AND count=?";

                try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement pstmt = con.prepareStatement(updateQuery)) {

                    // 4. تجهيز البيانات من الواجهة
                    // بيانات الصنف والعملية
                    pstmt.setInt(1, Integer.parseInt(id.getText()));
                    pstmt.setString(2, labelDate.getText());
                    pstmt.setInt(3, Integer.parseInt(input.getText()));
                    pstmt.setInt(4, Integer.parseInt(output.getText()));
                    pstmt.setInt(5, Integer.parseInt(count.getText()));
                    pstmt.setDouble(6, Double.parseDouble(price.getText()));
                    pstmt.setDouble(7, Double.parseDouble(winTaxP.getText()));
                    pstmt.setDouble(8, Double.parseDouble(winTaxD.getText()));
                    pstmt.setString(9, ComboB_type.getSelectedItem().toString());
                    pstmt.setString(10, notes.getText());
                    pstmt.setString(11, ComboB_national.getSelectedItem().toString());
                    pstmt.setBytes(12, pImage);

                    // 5. إضافة الحقول الزائدة (بيانات المسافر والوردية)
                    pstmt.setString(13, name.getText());
                    pstmt.setString(14, passport.getText());
                    pstmt.setString(15, birthDate.getText());
                    pstmt.setString(16, labelBranchName.getText());
                    pstmt.setString(17, labelShift.getText());

                    // 6. شروط التحديد (WHERE) - باستخدام القيم الأصلية من الجدول
                    pstmt.setString(18, tablecount1.getValueAt(row, 6).toString());
                    pstmt.setString(19, tablecount1.getValueAt(row, 5).toString());
                    pstmt.setInt(20, Integer.parseInt(tablecount1.getValueAt(row, 2).toString()));

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        show_data_of_count();
                        showSuccessNotification();
                    } else {
                        JOptionPane.showMessageDialog(null, "لم يتم العثور على السجل!");
                    }
                    pass.setText("");

                } catch (SQLException | NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "خطأ في التعديل: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "كلمة المرور خطأ!");
                pass.setText("");
            }
        }
    }

    public void centerCell(JTable table, int alignment) {

        DefaultTableCellRenderer centerR = new DefaultTableCellRenderer();
        centerR.setHorizontalAlignment(alignment);
        TableModel TableModel = table.getModel();
        for (int col = 0; col < TableModel.getColumnCount(); col++) {
            table.getColumnModel().getColumn(col).setCellRenderer(centerR);
            table.getColumnModel().getColumn(col).setHeaderRenderer(centerR);

        }

    }

    public void centerheader(JTable table, int alignment) {

        DefaultTableCellRenderer centerR = new DefaultTableCellRenderer();
        centerR.setHorizontalAlignment(alignment);
        centerR.setForeground(Color.BLUE);
        centerR.setBackground(Color.LIGHT_GRAY);

        TableModel TableModel = table.getModel();
        for (int col = 0; col < TableModel.getColumnCount(); col++) {
            table.getColumnModel().getColumn(col).setHeaderRenderer(centerR);

        }

    }

    public void centerheader2(JMenuBar jMenuBar1, int alignment) {

        DefaultTableCellRenderer centerR = new DefaultTableCellRenderer();
        centerR.setHorizontalAlignment(alignment);
        centerR.setForeground(Color.BLUE);
        centerR.setBackground(Color.LIGHT_GRAY);

//        JMenuBar jMenuBar1=
//        TableModel TableModel = table.getModel();
//        for (int col = 0; col < TableModel.getColumnCount(); col++) {
//            table.getColumnModel().getColumn(col).setHeaderRenderer(centerR);
//
//        }
    }

    public void centerCell3(JTable table, int alignment) {

        // إنشاء كائن من المُصمم الجديد الذي يحتوي على التلوين والمحاذاة
        // ونمرر له نوع المحاذاة المطلوب (مثلاً SwingConstants.CENTER)
        AlternatingColorRenderer customRenderer = new AlternatingColorRenderer(alignment);

        // إذا كنت تريد محاذاة رؤوس الأعمدة، فاستخدم مُصمم بسيط منفصل
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(alignment);
        table.setDefaultRenderer(Object.class, customRenderer);

        // (اختياري) يمكنك أيضًا تطبيق المُصمم على أنواع محددة مثل String و Integer
        table.setDefaultRenderer(String.class, customRenderer);
        table.setDefaultRenderer(Integer.class, customRenderer);
        TableModel TableModel = table.getModel();

        for (int col = 0; col < TableModel.getColumnCount(); col++) {

            // تطبيق المُصمم الجديد على خلايا الجدول
            table.getColumnModel().getColumn(col).setCellRenderer(customRenderer);

            // (مهم!) تطبيق مُصمم رؤوس الأعمدة المنفصل (إذا كنت ترغب في محاذاتها)
            // أو يمكنك حذف هذا السطر إذا لم تكن بحاجة لمحاذاة رؤوس الأعمدة
            table.getColumnModel().getColumn(col).setHeaderRenderer(headerRenderer);
        }
        table.repaint();
    }

    public void clear() {
        
        ComboBoxShift.setSelectedIndex(0);
            labelShift.setText("");
            lblPicShift.setIcon(null);


        if (jPanelBills.isVisible()) {
            operation_date.setText("");
            id.setText("");
            output.setText("");
            input.setText("");
            count.setText("");
            price.setText("");
            notes.setText("");
            billNum.setText("");
            name.setText("");
            passport.setText("");
            winTaxP.setText("");
            winTaxD.setText("");
            ComboB_type.setSelectedIndex(0);
            ComboB_national.setSelectedIndex(0);

        } else if (jPanelReports.isVisible()) {

            txt1.setText("");
            txt2.setText("");
            txt3.setText("");
            txt4.setText("");
            txt5.setText("");
            txt6.setText("");
            txt7.setText("");
            txt8.setText("");
            txt9.setText("");
            txt10.setText("");
            txt11.setText("");
            txt12.setText("");
            txt13.setText("");
            txt14.setText("");
            txt21.setText("");
            txt22.setText("");
            txt23.setText("");
            txt24.setText("");
            txt25.setText("");
            txt26.setText("");
            txt27.setText("");
            txt28.setText("");
            txt29.setText("");
            txt30.setText("");
            txt31.setText("");
            txt32.setText("");
            txt33.setText("");
            txt34.setText("");
            txt35.setText("");
            txt36.setText("");
            
        } else if (jPanelTaxs.isVisible()) {

            ComboB_type1.setSelectedIndex(0);
            txtSearch.setText("");
            Salesprice1.setText("");
            FobValue.setText("");
            Value.setText("");
            Count.setText("");
            Clear.setText("");
            codeLebel.setText("");
            Description.setText("");
            jPanel5.setVisible(false);

        } else if (jPanelQuary.isVisible()) {
            datechooser.setDate(null);
            datechooser1.setDate(null);
            RadioGetDaily.setSelected(true);

        }

    }

    public String getTime() {

        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return now.format(formatter);

    }

    public void getToday() {

        LocalDate Date = LocalDate.now();
        labelDate.setText(String.valueOf(Date));
        EditDate = String.valueOf(Date);

        loginLabelDate.setText(String.valueOf(Date));

    }

    public void getmonth() {

        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        month = localDate.getMonthValue();
        year = localDate.getYear();

//        if (month == 10) {
//            month = 0;
//            ComboB_month.setSelectedItem(10);
////            ComboB_month1.setSelectedItem(10);
//
//        } else if (month == 11) {
//            month = 1;
//            ComboB_month.setSelectedItem(11);
////            ComboB_month1.setSelectedItem(11);
//        } else if (month == 12) {
//            month = 2;
//            ComboB_month.setSelectedItem(12);
////            ComboB_month1.setSelectedItem(12);
//        } else {
//            month = month;
//            ComboB_month.setSelectedItem(month);
////            ComboB_month1.setSelectedItem(month);
//        }
//        System.out.println("-" + month + "-" + year);
//if(month == )
//        txtmonth.setText(String.valueOf(month));
////        txtyear.setText(String.valueOf(year));
//        txtyear1.setText(String.valueOf(year));
//        datechooser.setMaxSelectableDate(date);
//        datechooser.setMinSelectableDate(date);
    }

    public void getImage0() {
        try {
            if (rs.getBytes("image") == null) {
                ShowPictureFrame.labelImage.setIcon(null);
                ShowPictureFrame.labelImage.setText("لايوجد صورة");
                pImage = null;

            } else {
                pImage = rs.getBytes("image");
                imageicon = new ImageIcon(new ImageIcon(pImage).getImage()
                        .getScaledInstance(ShowPictureFrame.labelImage.getWidth(), ShowPictureFrame.labelImage.getHeight(), Image.SCALE_SMOOTH));
                ShowPictureFrame.labelImage.setText("");
//                ShowPictureFrame.labelImage.setIcon(imageicon);

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    public void icon() {
        try {
            // الطريقة الصحيحة للوصول للملفات داخل مشروع Maven
            // نفترض أن الصورة aa.png موجودة مباشرة داخل src/main/resources
            java.net.URL imgURL = getClass().getResource("/aa.png");

            if (imgURL != null) {
                img = ImageIO.read(imgURL);

                // تأكد من أن labelIcon ليس null قبل وضع الصورة فيه
                if (labelIcon != null && img != null) {
                    Image scaledImg = img.getScaledInstance(369, 328, Image.SCALE_SMOOTH);
                    ImageIcon image2 = new ImageIcon(scaledImg);
                    labelIcon.setIcon(image2);
                }
            } else {
                // إذا لم يجد المسار، اطبع خطأ في الكونسول بدلاً من تحطيم البرنامج
                System.err.println("تعذر العثور على ملف الصورة: aa.png في المسار المحدد");
            }

        } catch (IOException ex) {
            System.err.println("خطأ أثناء قراءة الصورة: " + ex.getMessage());
        }
    }
    
    
     public void shiftIconChanged() {
    String shiftText = labelShift.getText();
    
    if ("1".equals(shiftText)) {
        java.net.URL downloadImgUrl = getClass().getResource("/clear_day_30dp_48752C_FILL0_wght400_GRAD0_opsz24.png");

        if (downloadImgUrl != null) {
            lblPicShift.setIcon(new ImageIcon(downloadImgUrl));
            lblPicShift.setText(""); // إزالة النص إن وجد لضمان ظهور الأيقونة وحدها
        } else {
            lblPicShift.setText("");
            System.out.println("تحذير: لم يتم العثور على ملف الأيقونة 1 في المسار المحدد!");
        }

    } else if ("2".equals(shiftText)) {
        java.net.URL downloadImgUrl = getClass().getResource("/bedtime_30dp_48752C_FILL0_wght400_GRAD0_opsz24.png");

        if (downloadImgUrl != null) {
            lblPicShift.setIcon(new ImageIcon(downloadImgUrl));
            lblPicShift.setText(""); // إزالة النص إن وجد لضمان ظهور الأيقونة وحدها
        } else {
            lblPicShift.setText("");
            System.out.println("تحذير: لم يتم العثور على ملف الأيقونة 2 في المسار المحدد!");
        }
    } else {
        // في حال كانت القيمة غير ذلك (إفراغ الأيقونة مثلاً)
        lblPicShift.setIcon(null);
        lblPicShift.setText("");
    }
}

    public boolean authenticateUser(String username, String pass) {

        // تم إضافة Category إلى استعلام SQL لجلب صفة المستخدم
        String query = "SELECT password, userName, Name, Category FROM userTable WHERE userName = ?";
        Connection con = DBConnection.getInstance().getConnection();

        // استخدام try-with-resources لـ PreparedStatement
        try (PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // تم العثور على المستخدم، الآن نقارن كلمة المرور
                    int storedPass = rs.getInt("password");
                    UserName = rs.getString("userName");
                    Name = rs.getString("Name");

                    // تخزين صفة المستخدم (admin أو employee) في المتغير Category
                    Category = rs.getString("Category");

                    if (storedPass == Integer.parseInt(pass)) {
                        // System.out.println(storedPass);
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "خطأ في قاعدة البيانات أثناء التحقق: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "كلمة المرور يجب أن تكون رقمًا صحيحًا.");
        }
        return false;
    }

    private boolean updatePasswordInDatabase(String username, String newPass) {
        // 1. استخدام دالة Trim في الاستعلام لتجنب مشاكل المسافات المخفية في قاعدة البيانات
        String query = "UPDATE [userTable] SET [password] = ? WHERE TRIM([userName]) = ?";
        Connection con = DBConnection.getInstance().getConnection();

        try (PreparedStatement pstmt = con.prepareStatement(query)) {

            // تحويل كلمة المرور الجديدة لرقم (تأكد أن عمود password في Access نوعه Number / Long Integer)
            pstmt.setInt(1, Integer.parseInt(newPass));
            pstmt.setString(2, username.trim());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("اسم المستخدم الذي تم البحث عنه: [" + username + "]");
            System.out.println("عدد الصفوف التي تم تحديثها: " + rowsAffected);

            if (rowsAffected == 0) {
                JOptionPane.showMessageDialog(null, "فشل التحديث: اسم المستخدم (" + username + ") غير موجود في جدول userTable تماماً!", "تنبيه", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            return true;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "خطأ Access: " + e.getMessage(), "خطأ قاعدة بيانات", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

// دالة تجميعية لتنفيذ الانتقال للواجهة الرئيسية بعد نجاح الدخول أو التغيير
    private void executeSuccessfulLogin(String loggedUser) {
        jPanelMainLogIn.setVisible(false);
        jPanelMainContainer.setVisible(true);

        labelUser.setText(loggedUser);
//        labelUser.setText("المستخدم الحالي : " + loggedUser);
        labelShift.setText(labelShift.getText());

        // تنظيف الحقول
        userName.setText("");
        password.setText("");
        Newpassword.setText("");
        Newpassword.setVisible(false); // ضمان إخفاء الحقل للرجل العادي

        jPanelMainContainer.requestFocusInWindow();
    }

    public void applyUserPermissions() {

        if (Category != null && Category.equalsIgnoreCase("admin")) {
            // صلاحيات المدير: إظهار وتفعيل الأزرار
            ComboB_SubBranch.setEnabled(true);
            uploadData.setEnabled(true);
            Delete.setEnabled(true);
            downloadData.setEnabled(true);

        } else {
            ComboB_SubBranch.setEnabled(false);
            uploadData.setEnabled(false);
            Delete.setEnabled(false);
            downloadData.setEnabled(false);
        }
    }

    public void checkEditPermissionBasedOnDate(String recordDateStr) {
        if (recordDateStr == null || recordDateStr.trim().isEmpty()) {
            Edit2.setEnabled(false);
            return;
        }

        try {
            // تنظيف نص التاريخ في حال كان يحتوي على وقت (مثال: 取 أول 10 أحرف YYYY-MM-DD)
            if (recordDateStr.length() > 10) {
                recordDateStr = recordDateStr.substring(0, 10);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate recordDate = LocalDate.parse(recordDateStr.trim(), formatter);
            LocalDate currentDate = LocalDate.now();

            // حساب فارق الأيام بين تاريخ السجل اليوم
            long daysDifference = ChronoUnit.DAYS.between(recordDate, currentDate);

            // إذا تخطى الفارق يومين (> 2)
            if (daysDifference > 2) {
                // نتحقق مما إذا كان المستخدم الحالي Admin
                if (Category != null && Category.equalsIgnoreCase("admin")) {
                    Edit2.setEnabled(true); // المدير يمكنه التعديل حتى لو مر أكثر من يومين
                } else {
                    Edit2.setEnabled(false); // الموظف العادي لا يمكنه التعديل
                }
            } else {
                // إذا كان الفارق يومين أو أقل، يسمح بالتعديل للجميع
                Edit2.setEnabled(true);
            }

        } catch (Exception e) {
            System.err.println("خطأ في تحليل التاريخ: " + e.getMessage());
            Edit2.setEnabled(false);
        }
    }

    public void showScreen(String cardName) {
        if (jPanelScreens == null) {
            System.err.println("❌ خطأ: الحاوية jPanelScreens غير معرفة (Null)!");
            return;
        }

        LayoutManager layout = jPanelScreens.getLayout();

        if (layout instanceof CardLayout) {
            CardLayout cl = (CardLayout) layout;

            // محاولة الانتقال
            cl.show(jPanelScreens, cardName);

            // إجبار الواجهة على التحديث
            jPanelScreens.revalidate();
            jPanelScreens.repaint();

            System.out.println("✅ محاولة عرض الكارت: " + cardName);
        } else {
            System.err.println("❌ خطأ: jPanelScreens لا تستخدم CardLayout. النوع الحالي هو: " + layout.getClass().getName());
        }
    }

    // متغير للتحقق من حالة القائمة (مفتوحة أم مغلقة)
    private boolean isDrawerExpanded = false;

    public void toggleDrawer() {
        // تحديد العرض المستهدف للقائمة (مثلاً 250 بكسل)
        int targetWidth = 250;
        int currentX = drawerPanel.getX();

        // استخدام Thread بسيطة لعمل حركة انسيابية (Animation)
        new Thread(() -> {
            try {
                if (!isDrawerExpanded) {
                    // فتح القائمة تدريجياً
                    drawerPanel.setVisible(true);
                    for (int i = drawerPanel.getWidth(); i <= targetWidth; i += 10) {
                        final int w = i;
                        javax.swing.SwingUtilities.invokeLater(() -> drawerPanel.setSize(w, drawerPanel.getHeight()));
                        Thread.sleep(5);
                    }
                    isDrawerExpanded = true;
                } else {
                    // إغلاق القائمة تدريجياً
                    for (int i = drawerPanel.getWidth(); i >= 0; i -= 10) {
                        final int w = i;
                        javax.swing.SwingUtilities.invokeLater(() -> drawerPanel.setSize(w, drawerPanel.getHeight()));
                        Thread.sleep(5);
                    }
                    drawerPanel.setVisible(false);
                    isDrawerExpanded = false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void BillsButtons(boolean Status) {

        saveOperation.setVisible(Status);
        Edit.setVisible(Status);
        view_Shift_Sales1.setVisible(Status);
        current_Balance1.setVisible(Status);
        Details_of_DailySales.setVisible(Status);
        code_view.setVisible(Status);
        RadiCodeNum.setVisible(Status);
        RadioOPDate.setVisible(Status);

    }

    public void ReportsButtons(boolean Status) {

        SaveDailyReport.setVisible(Status);
        Edit2.setVisible(Status);

        SaveAsExcel1.setVisible(Status);
        GetFromExcel.setVisible(Status);
        Delete.setVisible(Status);

        jSeparator9.setVisible(Status);
        jPanelShiftDate.setVisible(Status);
        ComboBoxShift.setVisible(Status);

    }

    public void TaxButtons(boolean Status) {

        GetTax.setVisible(Status);

    }

    public void QuaryButtons(boolean Status) {

        searchByDate.setVisible(Status);
        Back.setVisible(Status);

    }

    public void SettingsButtons(boolean Status) {

        saveSettings.setVisible(Status);
    }

    public void NavigateToBills() {

        jPanelBills.setVisible(true);
        jPanelReports.setVisible(false);
        jPanelQuary.setVisible(false);
        jPanelTaxs.setVisible(false);
        jPanelSettings.setVisible(false);
        jLabel31.setText("إدارة الأرصدة");
        labelUser.setText(Name);
        getToday();
        BillsButtons(true);
        ReportsButtons(false);
        TaxButtons(false);
        QuaryButtons(false);
        SettingsButtons(false);
        jPanel32.setVisible(false);
        ItemsListEdit.setText("تخصيص قائمة الأصناف <");
        lastScreen = "Bills";
        Quary.setVisible(true);

        animateWidth(jPanel12, 0, jPanel12.getWidth(), 100);

        show_data_of_count();

    }

    public void NavigateToSetting() {

        jPanelBills.setVisible(false);
        jPanelReports.setVisible(false);
        jPanelQuary.setVisible(false);
        jPanelTaxs.setVisible(false);
        jPanelSettings.setVisible(true);
        jLabel31.setText("الإعدادات");
        labelUser.setText(Name);
        getToday();
        BillsButtons(false);
        ReportsButtons(false);
        QuaryButtons(false);
        TaxButtons(false);
        SettingsButtons(true);
        jPanel32.setVisible(false);
        ItemsListEdit.setText("تخصيص قائمة الأصناف <");
        Quary.setVisible(false);

        animateWidth(jPanel12, 0, jPanel12.getWidth(), 100);

    }

    public void NavigateToReport() {

        jPanelBills.setVisible(false);
        jPanelSettings.setVisible(false);
        jPanelTaxs.setVisible(false);
        jPanelReports.setVisible(true);
        jPanelQuary.setVisible(false);
        jLabel31.setText("إدارة التقارير و اليوميات");
        labelUser.setText(Name);
        getToday();
        BillsButtons(false);
        ReportsButtons(true);
        TaxButtons(false);
        QuaryButtons(false);
        SettingsButtons(false);
        jPanel32.setVisible(false);
        ItemsListEdit.setText("تخصيص قائمة الأصناف <");
        lastScreen = "Reports";
        Quary.setVisible(true);

        animateWidth(jPanel12, 0, jPanel12.getWidth(), 100);

        showSales_data();
        showSalesFor_data();

    }

    public void NavigateToReportwithoutRefrsh() {

        jPanelBills.setVisible(false);
        jPanelSettings.setVisible(false);
        jPanelTaxs.setVisible(false);
        jPanelReports.setVisible(true);
        jPanelQuary.setVisible(false);
        jLabel31.setText("إدارة التقارير و اليوميات");
        labelUser.setText(Name);
        getToday();
        BillsButtons(false);
        ReportsButtons(true);
        QuaryButtons(false);
        TaxButtons(false);
        SettingsButtons(false);
        jPanel32.setVisible(false);
        ItemsListEdit.setText("تخصيص قائمة الأصناف <");
        Quary.setVisible(true);

        animateWidth(jPanel12, 0, jPanel12.getWidth(), 100);

    }

    public void NavigateToTax() {

        jPanelTaxs.setVisible(true);
        jPanelReports.setVisible(false);
        jPanelQuary.setVisible(false);
        jPanelBills.setVisible(false);
        jPanelSettings.setVisible(false);
        jLabel31.setText("إدارة الضرائب والرسوم");
        labelUser.setText(Name);
        getToday();
        BillsButtons(false);
        ReportsButtons(false);
        TaxButtons(true);
        QuaryButtons(false);
        SettingsButtons(false);
        jPanel32.setVisible(false);
        ItemsListEdit.setText("تخصيص قائمة الأصناف <");
         lastScreen = "Tax";
        Quary.setVisible(true);
        animateWidth(jPanel12, 0, jPanel12.getWidth(), 100);

    }

    public void NavigateToQuary() {

        jPanelBills.setVisible(false);
        jPanelReports.setVisible(false);

        jPanelQuary.setVisible(true);
        jPanelTaxs.setVisible(false);
        jPanelSettings.setVisible(false);

        jLabel31.setText("إدارة الاستعلام");
        labelUser.setText(Name);
        getToday();
        BillsButtons(false);
        ReportsButtons(false);
        TaxButtons(false);
        QuaryButtons(true);
        SettingsButtons(false);
        Quary.setVisible(false);
//        jPanel32.setVisible(false);
//        ItemsListEdit.setText("تخصيص قائمة الأصناف <");
        animateWidth(jPanel12, 0, jPanel12.getWidth(), 100);

    }

    // 1. تحديث دالة جلب الأكواد للتأكد من جلب بيانات
    private List<String> getAllCodesFromDatabase() {
        List<String> codes = new ArrayList<>();

        // جلب عمودي Material و EAN معاً من جدول ItemsTable[cite: 3]
        String query = "SELECT Material, EAN FROM ItemsTable";

        try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement pstmt = con.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // 1. إضافة Material إذا كانت موجودة
                String material = rs.getString("Material");
                if (material != null && !material.trim().isEmpty()) {
                    String matStr = material.trim();
                    if (!codes.contains(matStr)) {
                        codes.add(matStr);
                    }
                }

                // 2. إضافة EAN إذا كانت موجودة وليست فارغة
                String ean = rs.getString("EAN");
                if (ean != null && !ean.trim().isEmpty()) {
                    String eanStr = ean.trim();
                    if (!codes.contains(eanStr)) {
                        codes.add(eanStr);
                    }
                }
            }
            System.out.println(">>> [فحص الاقتراحات] تم جلب " + codes.size() + " عنصر (Material و EAN) بنجاح.");
        } catch (SQLException e) {
            System.err.println("خطأ في جلب الأكواد للاقتراحات: " + e.getMessage());
        }
        return codes;
    }

// 2. تحديث دالة الفلترة لمتابعة ما يحدث عند الكتابة
    private void filterSuggestions(javax.swing.JPopupMenu popup, javax.swing.JList<String> list, DefaultListModel<String> model, List<String> codes) {
        String typedText = txtSearch.getText().trim();
        System.out.println(">>> [فحص الكتابة] النص المكتوب حالياً: " + typedText);

        if (typedText.isEmpty()) {
            popup.setVisible(false);
            return;
        }

        model.clear();
        boolean found = false;

        for (String code : codes) {
            if (code != null && code.contains(typedText)) {
                model.addElement(code);
                found = true;
            }
        }

        System.out.println(">>> [فحص النتائج] هل وُجدت مطابقة؟ " + found + " | عدد العناصر المطابقة: " + model.size());

        if (found) {
            popup.setPopupSize(txtSearch.getWidth(), 120);
            popup.show(txtSearch, 0, txtSearch.getHeight());
            txtSearch.requestFocusInWindow();
        } else {
            popup.setVisible(false);
        }
    }

    public void GetTaxQuary() {

        String searchText = txtSearch.getText().trim();

        // إذا لم يترك المستخدم حقل البحث فارغاً، يقوم بالبحث في قاعدة البيانات كالمعتاد
        if (!searchText.isEmpty()) {
            String SQL3 = "SELECT * FROM ItemsTable WHERE EAN LIKE ? OR Material LIKE ? OR OldMaterial LIKE ? OR Description LIKE ?";

            try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement ps = con.prepareStatement(SQL3)) {

                if (con == null || con.isClosed()) {
                    JOptionPane.showMessageDialog(null, "فشل الاتصال بقاعدة البيانات.");
                    return;
                }

                String searchVal = "%" + searchText + "%";

                ps.setString(1, searchVal);
                ps.setString(2, searchVal);
                ps.setString(3, searchVal);
                ps.setString(4, searchVal);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String ean_col = rs.getString("EAN");
                        int qty = rs.getInt("Quantity");
                        String category = rs.getString("Category");
                        String addDate = rs.getString("AddDate");
                        double price1 = rs.getDouble("FobPrice");
                        double price2 = rs.getDouble("SalesPrice");
                        String desc = rs.getString("Description");
                        int Material = rs.getInt("Material");

                        Value.setText(String.valueOf(df.format(price1 * 1.2)));
                        FobValue.setText(String.valueOf(price1));
                        ComboB_type1.setSelectedItem(category);

                        codeLebel.setText(String.valueOf(Material + " " + "|" + " " + ean_col));
                        Description.setText(desc);
                        Salesprice1.setText(String.valueOf(price2));

                    } else {
                        JOptionPane.showMessageDialog(null, "لم يتم العثور على أي صنف يحتوي على هذا النص.");
                        return;
                    }
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "خطأ في قاعدة البيانات: " + e.getMessage());
                return;
            }
        }

        if (ComboB_type1.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "أختر نوع الصنف.");
        } else {
            if ("".equals(Value.getText())) {
                JOptionPane.showMessageDialog(null, "أدخل القيمة بالدولار.");
            } else {
                if ("".equals(Count.getText()) || Integer.parseInt(Count.getText()) <= 0) {
                    JOptionPane.showMessageDialog(null, "أدخل العدد.");
                } else {

                    ItemTax Item = new ItemTax();
                    Double TotalValue = (Double.parseDouble(Value.getText()) * Double.parseDouble(exchangePriceTxt.getText())) * Double.parseDouble(Count.getText());

                    if (ComboB_type1.getSelectedItem() == "مشروبات كحولية") {
                        Item.setName("مشروبات كحولية");

                        TaxesResult Taxes1 = Item.getTaxes("مشروبات كحولية", TotalValue, Integer.parseInt(Count.getText()));
                        double Tax1 = (Taxes1.getImportTax() / Double.parseDouble(exchangePriceTxt.getText())) * 1 / 100;
                        if (Tax1 < 13) {
                            Tax1 = 13 * Double.parseDouble(Count.getText());
                        }
                        double Tax25 = (Taxes1.getImportTax() / Double.parseDouble(exchangePriceTxt.getText())) / 4;

                        jTextPane2.setText(
                                "الضرائب والرسوم بالجنيه*"
                                + "\n"
                                + "ـــــــــــــــــــــــــ"
                                + "\n" + "\n"
                                + String.valueOf(df.format(Taxes1.getImportTax())) + " : ضريبة الوارد"
                                + "\n" + "\n"
                                + String.valueOf(df.format(Taxes1.getTableTax()) + " : ضريبة الجدول"
                                        + "\n" + "\n"
                                        + String.valueOf(df.format(Taxes1.getAddValueTax())) + " : ضريبة ق المضافة"
                                        + "\n" + "\n"
                                        + "ـــــــــــــــــــــــــ" + "\n"
                                        + String.valueOf(df.format(Taxes1.getTotal())) + " : الإجمالى"
                                        + "\n" + "\n" + "\n"
                                        + " ضرائب ورسوم أخرى بالدولار*" + "\n"
                                        + "ـــــــــــــــــــــــــ"
                                        + "\n" + "\n"
                                        + String.valueOf(df.format(Tax1)) + " : 1% ضريبة"
                                        + "\n" + "\n"
                                        + String.valueOf(df.format(Tax25)) + " : %25 ضريبة"));

                        animateWidthFromRight(jPanel5, 0, 266, 5);

                    } else if (ComboB_type1.getSelectedItem() == "سجائر") {
                        Item.setName("سجائر");
                        TaxesResult Taxes = Item.getTaxes("سجائر", Double.parseDouble(Value.getText()), Integer.parseInt(Count.getText()));

                        jTextPane2.setText(
                                "الضرائب والرسوم بالجنيه*"
                                + "\n"
                                + "ـــــــــــــــــــــــــ"
                                + "\n" + "\n"
                                + String.valueOf(df.format(Taxes.getImportTax())) + " : ضريبة الوارد"
                                + "\n" + "\n"
                                + String.valueOf(df.format(Taxes.getTableTax())) + " : ضريبة الجدول"
                                + "\n" + "\n"
                                + String.valueOf(df.format(Taxes.getAddValueTax())) + " : ضريبة ق المضافة"
                                + "\n" + "\n"
                                + String.valueOf(df.format(Taxes.getDevelopTax())) + " : رسم التنمية"
                                + "\n" + "\n"
                                + "ـــــــــــــــــــــــــ" + "\n"
                                + (String.valueOf(df.format(Taxes.getTotal())) + " : الإجمالى"));

                        animateWidthFromRight(jPanel5, 0, 278, 5);

                    } else if (ComboB_type1.getSelectedItem() == "بيرة") {
                        Item.setName("بيرة");
                        TaxesResult Taxes = Item.getTaxes("بيرة", TotalValue, Integer.parseInt(Count.getText()));
                        double Tax1 = (Taxes.getImportTax() / Double.parseDouble(exchangePriceTxt.getText())) * 1 / 100;
                        if (Tax1 < 13) {
                            Tax1 = 13 * Double.parseDouble(Count.getText());
                        }
                        double Tax25 = (Taxes.getImportTax() / Double.parseDouble(exchangePriceTxt.getText())) / 4;
                        jTextPane2.setText(
                                "الضرائب والرسوم بالجنيه*"
                                + "\n"
                                + "ـــــــــــــــــــــــــ"
                                + "\n" + "\n"
                                + String.valueOf(df.format(Taxes.getImportTax())) + " : ضريبة الوارد"
                                + "\n" + "\n"
                                + String.valueOf(df.format(Taxes.getTableTax()) + " : ضريبة الجدول"
                                        + "\n" + "\n"
                                        + String.valueOf(df.format(Taxes.getAddValueTax())) + " : ضريبة ق المضافة"
                                        + "\n" + "\n"
                                        + "ـــــــــــــــــــــــــ" + "\n"
                                        + (String.valueOf(df.format(Taxes.getTotal())) + " : الإجمالى")
                                        + "\n" + "\n" + "\n"
                                        + " ضرائب ورسوم أخرى بالدولار*" + "\n"
                                        + "ـــــــــــــــــــــــــ"
                                        + "\n" + "\n"
                                        + String.valueOf(df.format(Tax1)) + " : 1% ضريبة"
                                        + "\n" + "\n"
                                        + String.valueOf(df.format(Tax25)) + " : %25 ضريبة"));

                        animateWidthFromRight(jPanel5, 0, 278, 5);

                    } else if (ComboB_type1.getSelectedItem() == "مأكولات") {
                        Item.setName("مأكولات");

                        TaxesResult Taxes = Item.getTaxes("مأكولات", TotalValue, Integer.parseInt(Count.getText()));

                        jTextPane2.setText(
                                "الضرائب والرسوم بالجنيه*"
                                + "\n"
                                + "ـــــــــــــــــــــــــ"
                                + "\n" + "\n"
                                + String.valueOf(df.format(Taxes.getImportTax())) + " : ضريبة الوارد"
                                + "\n" + "\n"
                                + String.valueOf(df.format(Taxes.getTableTax())) + " : ضريبة الجدول"
                                + "\n" + "\n"
                                + String.valueOf(df.format(Taxes.getAddValueTax())) + " : ضريبة ق المضافة"
                                + "\n" + "\n"
                                + "ـــــــــــــــــــــــــ" + "\n"
                                + (String.valueOf(df.format(Taxes.getTotal())) + " : الإجمالى"));
                        animateWidthFromRight(jPanel5, 0, 278, 5);

                    } else if (ComboB_type1.getSelectedItem() == "عطور") {
                        Item.setName("عطور");
                        TaxesResult Taxes = Item.getTaxes("عطور", TotalValue, Integer.parseInt(Count.getText()));

                        jTextPane2.setText(
                                "الضرائب والرسوم بالجنيه*"
                                + "\n"
                                + "ـــــــــــــــــــــــــ"
                                + "\n" + "\n"
                                + String.valueOf(df.format(Taxes.getImportTax())) + " : ضريبة الوارد"
                                + "\n" + "\n"
                                + String.valueOf(df.format(Taxes.getTableTax())) + " : ضريبة الجدول"
                                + "\n" + "\n"
                                + String.valueOf(df.format(Taxes.getAddValueTax())) + " : ضريبة ق المضافة"
                                + "\n" + "\n"
                                + "ـــــــــــــــــــــــــ" + "\n"
                                + (String.valueOf(df.format(Taxes.getTotal())) + " : الإجمالى"));
                        animateWidthFromRight(jPanel5, 0, 278, 5);

                    } else if (ComboB_type1.getSelectedItem() == "أجهزة") {
                        Item.setName("أجهزة");
                        TaxesResult Taxes = Item.getTaxes("أجهزة", TotalValue, Integer.parseInt(Count.getText()));

                        jTextPane2.setText(
                                "الضرائب والرسوم بالجنيه*"
                                + "\n"
                                + "ـــــــــــــــــــــــــ"
                                + "\n" + "\n"
                                + String.valueOf(df.format(Taxes.getImportTax())) + " : ضريبة الوارد"
                                + "\n" + "\n"
                                + String.valueOf(df.format(Taxes.getTableTax())) + " : ضريبة الجدول"
                                + "\n" + "\n"
                                + String.valueOf(df.format(Taxes.getAddValueTax())) + " : ضريبة ق المضافة"
                                + "\n" + "\n"
                                + "ـــــــــــــــــــــــــ" + "\n"
                                + (String.valueOf(df.format(Taxes.getTotal())) + " : الإجمالى"));
                        animateWidthFromRight(jPanel5, 0, 278, 5);

                    } else if (ComboB_type1.getSelectedItem() == "هدايا") {
                        Item.setName("هدايا");
                        TaxesResult Taxes = Item.getTaxes("هدايا", TotalValue, Integer.parseInt(Count.getText()));

                        jTextPane2.setText(
                                "الضرائب والرسوم بالجنيه*"
                                + "\n"
                                + "ـــــــــــــــــــــــــ"
                                + "\n" + "\n"
                                + String.valueOf(df.format(Taxes.getImportTax())) + " : ضريبة الوارد"
                                + "\n" + "\n"
                                + String.valueOf(df.format(Taxes.getTableTax())) + " : ضريبة الجدول"
                                + "\n" + "\n"
                                + String.valueOf(df.format(Taxes.getAddValueTax())) + " : ضريبة ق المضافة"
                                + "\n" + "\n"
                                + "ـــــــــــــــــــــــــ" + "\n"
                                + (String.valueOf(df.format(Taxes.getTotal())) + " : الإجمالى"));
                        animateWidthFromRight(jPanel5, 0, 278, 5);
                    }

                }
            }
        }

    }

    public static void animateHeight(Component component, int startHeight, int endHeight, int duration) {

        // حساب مقدار التغير في العرض لكل خطوة.
        final int totalSteps = 30; // عدد الخطوات في الحركة (تقريبًا 60 إطار في الثانية).
        final int delay = duration / totalSteps; // التأخير بين كل خطوة.
        final int deltaPerStep = (endHeight - startHeight) / totalSteps;
        component.setVisible(true);
        // إنشاء Timer لتنفيذ الحركة.
        Timer timer = new Timer(delay, null);

        // تهيئة Listener للـ Timer.
        timer.addActionListener(new ActionListener() {
            private int currentStep = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                // إذا وصلت الحركة للنهاية، نتوقف.
                if (currentStep >= totalSteps) {
                    timer.stop();
                    // لضمان دقة العرض النهائي.
//     
                    component.setBounds(component.getX(), component.getY(), component.getWidth(), endHeight);
                    component.setPreferredSize(new Dimension(component.getWidth(), endHeight));
                    component.revalidate();
                    component.getParent().revalidate();
                    return;
                }

                // حساب العرض الجديد وتحديث المكون.
                int newHeight = startHeight + (deltaPerStep * currentStep);
                component.setBounds(component.getX(), component.getY(), component.getWidth(), newHeight);

                // الانتقال إلى الخطوة التالية.
                currentStep++;
            }
        });

        // بدء الـ Timer.
        timer.start();

    }

    public static void animateHeightFromBottom(Component component, int startHeight, int endHeight, int duration) {
        // حساب مقدار التغير في العرض لكل خطوة.
        final int totalSteps = 30; // عدد الخطوات في الحركة (تقريبًا 60 إطار في الثانية).
        final int delay = duration / totalSteps; // التأخير بين كل خطوة.

        // الفرق الكلي في الارتفاع.
        final int heightDifference = endHeight - startHeight;

        // مقدار التغير في الارتفاع لكل خطوة.
        final int deltaHeightPerStep = heightDifference / totalSteps;

        // تخزين الموضع الأصلي Y للمكون، وهو يمثل الحد العلوي.
        int originalY = component.getY();

        component.setVisible(true);

        // إنشاء Timer لتنفيذ الحركة.
        Timer timer = new Timer(delay, null);

        // تهيئة Listener للـ Timer.
        timer.addActionListener(new ActionListener() {
            private int currentStep = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                // إذا وصلت الحركة للنهاية، نتوقف.
                if (currentStep >= totalSteps) {
                    timer.stop();
                    // لضمان دقة الارتفاع والموضع النهائيين.
                    // الارتفاع النهائي هو endHeight.
                    // الموضع Y النهائي هو originalY + startHeight - endHeight، 
                    // وهذا يضمن أن الحد السفلي يبقى ثابتًا.
                    int finalY = originalY + startHeight - endHeight;
                    component.setBounds(component.getX(), finalY, component.getWidth(), endHeight);
                    return;
                }

                // حساب الارتفاع الجديد.
                int newHeight = startHeight + (deltaHeightPerStep * currentStep);

                // حساب التغير في الارتفاع حتى هذه الخطوة.
                // هذا التغير هو مقدار الزيادة في الارتفاع عن startHeight.
                int currentHeightDelta = newHeight - startHeight;

                // لحركة من الأسفل للأعلى: 
                // يجب أن ينخفض الموضع Y للمكون بمقدار يساوي الزيادة في الارتفاع.
                int newY = originalY - currentHeightDelta;

                // تحديث حدود المكون.
                component.setBounds(component.getX(), newY, component.getWidth(), newHeight);

                // الانتقال إلى الخطوة التالية.
                currentStep++;
            }
        });

        // لضمان أن المكون يبدأ بالارتفاع الأولي startHeight والموضع Y المناسب.
        // الموضع Y الأولي يجب أن يكون originalY + heightDifference لكي يكون الحد السفلي عند نفس النقطة.
        // عند startHeight، يجب أن يكون المكون أعلى.
        // إذا كنت تريد البدء من الـ Y الحالي للمكون (الحد العلوي)، يمكنك إزالة هذا السطر أو تعديله.
        // لكن لضمان ثبات الحد السفلي عند البدء، يجب أن يكون:
        // الحد السفلي الثابت = originalY + component.getHeight() (قبل الحركة)
        // هذا يعني: الحد السفلي الثابت = originalY + startHeight (عند البدء)
        // عند كل خطوة: newY + newHeight = الحد السفلي الثابت
        // newY = الحد السفلي الثابت - newHeight
        // يجب إعادة تعيين الموضع الأولي لـ Y بحيث يظهر المكون بالارتفاع startHeight ويكون الحد السفلي له عند نفس نقطة الحد السفلي النهائي.
        // الحد السفلي الثابت (final bottom edge) = originalY (الـ Y الذي ستبلغه الحركة في النهاية) + endHeight
        // الـ Y الذي يجب أن يبدأ به المكون: startY = (final bottom edge) - startHeight
        int startY = (originalY + endHeight) - startHeight;
        component.setBounds(component.getX(), startY, component.getWidth(), startHeight);

        // بدء الـ Timer.
        timer.start();
    }

    public static void animateComponent(Component component, int startY, int endY, int duration) {

        // حفظ العرض والارتفاع الأصليين للمكون
        final int fixedWidth = component.getWidth();
        final int fixedHeight = component.getHeight();

        // حساب مقدار التغير في الموضع Y لكل خطوة.
        final int totalSteps = 30; // عدد الخطوات في الحركة (تقريبًا 60 إطار في الثانية).
        final int delay = duration / totalSteps; // التأخير بين كل خطوة.
        final int deltaPerStep = (endY - startY) / totalSteps;

        // تهيئة الموضع الأولي قبل بدء الحركة (لتجنب القفز المفاجئ)
        component.setBounds(component.getX(), startY, fixedWidth, fixedHeight);
        component.setVisible(true);

        // إنشاء Timer لتنفيذ الحركة.
        Timer timer = new Timer(delay, null);

        // تهيئة Listener للـ Timer.
        timer.addActionListener(new ActionListener() {
            private int currentStep = 0;

            @Override
            public void actionPerformed(ActionEvent e) {

                // إذا وصلت الحركة للنهاية، نتوقف.
                if (currentStep >= totalSteps) {
                    timer.stop();

                    // لضمان دقة الموضع النهائي.
                    // يتم تعيين المكون على موضع Y النهائي مع الحفاظ على الأبعاد.
                    component.setBounds(component.getX(), endY, fixedWidth, fixedHeight);

                    return;
                }

                // حساب الموضع Y الجديد (وهو التصحيح الرئيسي)
                // يتم استخدام startY و deltaPerStep لحساب الموضع الجديد
                int newY = startY + (deltaPerStep * currentStep);

                // تحديث المكون: يتم تطبيق الموضع Y الجديد 
                // مع الحفاظ على العرض والارتفاع الأصليين للمكون.
                component.setBounds(component.getX(), newY, fixedWidth, fixedHeight);

                // الانتقال إلى الخطوة التالية.
                currentStep++;
            }
        });

        // بدء الـ Timer.
        timer.start();
    }

    public static void animateComponent2(Component component, int startY, int endY, int duration) {

        // حفظ العرض والارتفاع الأصليين للمكون
        final int fixedWidth = component.getWidth();
        final int fixedHeight = component.getHeight();

        // حساب مقدار التغير في الموضع Y لكل خطوة.
        final int totalSteps = 30; // عدد الخطوات في الحركة (تقريبًا 60 إطار في الثانية).
        final int delay = duration / totalSteps; // التأخير بين كل خطوة.
        final int deltaPerStep = (endY - startY) / totalSteps;

        // تهيئة الموضع الأولي قبل بدء الحركة (لتجنب القفز المفاجئ)
        component.setBounds(component.getX(), startY, fixedWidth, fixedHeight);
        component.setVisible(true);

        // إنشاء Timer لتنفيذ الحركة.
        Timer timer = new Timer(delay, null);

        // تهيئة Listener للـ Timer.
        timer.addActionListener(new ActionListener() {
            private int currentStep = 0;

            @Override
            public void actionPerformed(ActionEvent e) {

                // إذا وصلت الحركة للنهاية، نتوقف.
                if (currentStep >= totalSteps) {
                    timer.stop();

                    // لضمان دقة الموضع النهائي.
                    // يتم تعيين المكون على موضع Y النهائي مع الحفاظ على الأبعاد.
                    component.setBounds(component.getX(), endY, fixedWidth, fixedHeight);

                    return;
                }

                // حساب الموضع Y الجديد (وهو التصحيح الرئيسي)
                // يتم استخدام startY و deltaPerStep لحساب الموضع الجديد
                int newY = startY + (deltaPerStep * currentStep);

                // تحديث المكون: يتم تطبيق الموضع Y الجديد 
                // مع الحفاظ على العرض والارتفاع الأصليين للمكون.
                component.setBounds(component.getX(), newY, fixedWidth, fixedHeight);

                // الانتقال إلى الخطوة التالية.
                currentStep++;
            }
        });

        // بدء الـ Timer.
        timer.start();
    }

    public static void animateWidth(Component component, int startWidth, int endWidth, int duration) {

        // 1. تحديد الموضع الثابت (الحد الأيمن) الذي ستنطلق منه الحركة.
        // هذا هو X الأصلي للمكون + عرضه الأصلي.
        final int fixedRightEdgeX = component.getX() + component.getWidth();

        // 2. حساب مقدار التغير في العرض لكل خطوة.
        final int totalSteps = 30;
        final int delay = duration / totalSteps;
        // (endWidth - startWidth) يمكن أن يكون سالبًا أو موجبًا حسب اتجاه التكبير/التصغير
        final int deltaPerStep = (endWidth - startWidth) / totalSteps;

        component.setVisible(true);

        Timer timer = new Timer(delay, null);

        timer.addActionListener(new ActionListener() {
            private int currentStep = 0;
            // يجب أن نستخدم عرض البداية الفعلية للمكون الحالي لتجنب الأخطاء إذا كانت الدالة تُستدعى عدة مرات
            private final int initialWidth = component.getWidth();

            // ** ملاحظة هامة: يجب أن نحتفظ بـ startWidth الأصلي للدالة **
            // ونبدأ الحركة منه بدلاً من component.getWidth() إذا كنا نعتمد على startWidth كقيمة بداية مُعطاة.
            @Override
            public void actionPerformed(ActionEvent e) {
                // التوقف عند الوصول لعدد الخطوات
                if (currentStep >= totalSteps) {
                    timer.stop();

                    // لضمان دقة العرض والموقع النهائي:
                    // العرض النهائي هو endWidth.
                    // الموقع X النهائي هو (الموضع الثابت - العرض النهائي).
                    int finalX = fixedRightEdgeX - endWidth;
                    component.setBounds(finalX, component.getY(), endWidth, component.getHeight());
                    return;
                }

                // حساب العرض الجديد:
                int newWidth = startWidth + (deltaPerStep * currentStep);

                // 💡 التعديل الأساسي هنا:
                // يجب أن يكون الموقع X الجديد هو: الحد الأيمن الثابت - العرض الجديد.
                int newX = fixedRightEdgeX - newWidth;

                // تحديث المكون بالموقع والعرض الجديدين.
                component.setBounds(newX, component.getY(), newWidth, component.getHeight());

                currentStep++;
            }
        });

        timer.start();
    }

    public static void animateWidthFromLeft(Component component, int startWidth, int endWidth, int duration) {

        // حساب مقدار التغير في العرض لكل خطوة.
        final int totalSteps = 30; // عدد الخطوات في الحركة (تقريبًا 60 إطار في الثانية).
        final int delay = duration / totalSteps; // التأخير بين كل خطوة.
        final int deltaPerStep = (endWidth - startWidth) / totalSteps;
        component.setVisible(true);
        // إنشاء Timer لتنفيذ الحركة.
        Timer timer = new Timer(delay, null);

        // تهيئة Listener للـ Timer.
        timer.addActionListener(new ActionListener() {
            private int currentStep = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                // إذا وصلت الحركة للنهاية، نتوقف.
                if (currentStep >= totalSteps) {
                    timer.stop();
                    // لضمان دقة العرض النهائي.
                    component.setBounds(component.getX(), component.getY(), endWidth, component.getHeight());
                    return;
                }

                // حساب العرض الجديد وتحديث المكون.
                int newWidth = startWidth + (deltaPerStep * currentStep);
                component.setBounds(component.getX(), component.getY(), newWidth, component.getHeight());

                // الانتقال إلى الخطوة التالية.
                currentStep++;
            }
        });

        // بدء الـ Timer.
        timer.start();

    }

    public static void animateWidthFromRight(Component component, int startWidth, int endWidth, int duration) {

        final int totalSteps = 30;
        final int delay = duration / totalSteps;
        final int deltaPerStep = (endWidth - startWidth) / totalSteps;

        // 1. تحديد الـ X الثابت من جهة اليمين بناءً على الموضع الحالي قبل بدء الأنميشن
        // هذه هي النقطة التي لن تتحرك أبداً.
        final int anchorRightX = component.getX() + component.getWidth();

        component.setVisible(true);
        Timer timer = new Timer(delay, null);

        timer.addActionListener(new ActionListener() {
            private int currentStep = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentStep >= totalSteps) {
                    timer.stop();
                    // التأكد من العرض النهائي وموضعه الصحيح
                    int finalX = anchorRightX - endWidth;
                    component.setBounds(finalX, component.getY(), endWidth, component.getHeight());
                    return;
                }

                // 2. حساب العرض المتغير
                int newWidth = startWidth + (deltaPerStep * currentStep);

                // 3. تحديث X ليكون دائماً (نقطة الارتكاز - العرض الحالي)
                // هذا يضمن أن الحافة اليمنى للمكون لا تتحرك من مكانها
                int newX = anchorRightX - newWidth;

                component.setBounds(newX, component.getY(), newWidth, component.getHeight());

                currentStep++;
            }
        });

        timer.start();
    }

    public static void ZoomAnimation(final JLabel label, final ImageIcon originalIcon, final int targetSize, final int zoomSpeed, final int delay) {

        // يجب أن تكون المتغيرات النهائية (final) للوصول إليها داخل ActionListener
        final Image originalImage = originalIcon.getImage();
        final int[] currentSize = {1}; // نستخدم مصفوفة للسماح بتغيير القيمة داخل الـ ActionListener

        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (currentSize[0] < targetSize) {
                    currentSize[0] += zoomSpeed;

                    // التأكد من عدم تجاوز الحجم النهائي
                    if (currentSize[0] > targetSize) {
                        currentSize[0] = targetSize;
                    }

                    // تغيير حجم الصورة
                    Image newImg = originalImage.getScaledInstance(currentSize[0], currentSize[0], Image.SCALE_SMOOTH);

                    // تحديث الـ JLabel
                    label.setIcon(new ImageIcon(newImg));
                } else {
                    // إيقاف المؤقت
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        timer.start();
    }

    public void loop() {
        try {
            for (int i = 0; i < 102; i++) {
                Thread.sleep(15);
                if (i == 30) {

                    loginLabl.setLocation(705, 160);
                    jPanel25.setLocation(705, 213);

                }
                if (i == 40) {

                    loginLabl.setLocation(705, 150);
                    jPanel25.setLocation(705, 203);

                }
                if (i == 50) {

                    loginLabl.setLocation(705, 140);
                    jPanel25.setLocation(705, 193);
                }

                if (i == 55) {

                    loginLabl.setLocation(705, 130);
                    jPanel25.setLocation(705, 183);
                }
                if (i == 60) {

                    loginLabl.setLocation(705, 120);
                    jPanel25.setLocation(705, 173);
                }
                if (i == 61) {

                    loginLabl.setLocation(705, 110);
                    jPanel25.setLocation(705, 163);

                }
                if (i == 80) {

                    loginLabl.setLocation(705, 100);
                    jPanel25.setLocation(705, 153);

                }

                if (i == 101) {

                    loginLabl.setVisible(false);
                    jPanel25.setVisible(false);
                }

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void showSuccessNotification() {

        // 1. إنشاء مربع الحوار. نمرر 'this' (الـ JFrame الحالي) كأب.
        // 💡 ملاحظة: يجب أن يكون الكلاس الذي يحتوي على هذه الدالة يمتد (extends) من JFrame.
        final SuccessNotificationDialog successDialog = new SuccessNotificationDialog(this, false);

        successDialog.setLocationRelativeTo(this);

        successDialog.setVisible(true);
//        animateWidth(jPanel1,0,221,100);

        int delay = 1800;

        Timer timer2 = new Timer(delay, (e) -> {

            ((Timer) e.getSource()).stop();
            successDialog.dispose();
        });

        timer2.setRepeats(false); // مرة واحدة فقط (تم التأكد منها في الباني لكن نكررها هنا)
        timer2.start();
    }

    private void show_popupMenu1(MouseEvent evt) {

        popupMenu.show(moreOption, evt.getX(), evt.getY());

    }

    private void show_popupMenu2(MouseEvent evt) {

        popupMenu2.show(moreOption2, evt.getX(), evt.getY());

    }

    public void KeyPressd(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_ENTER) {
            search_code();

        } else if (code == KeyEvent.VK_DOWN) {
            count.requestFocus();

        }

    }

    public static boolean checkActivation(int targetCode) {
        String query = "SELECT COUNT(*) FROM ActiveTable WHERE code = ?";
        try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {

            pstmt.setInt(1, targetCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // إذا كان العدد أكبر من صفر، فهذا يعني أن الرقم متطابق ومحظور (فشل التفعيل)
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("خطأ في التحقق من جدول التفعيل: " + e.getMessage());
        }
        return false;
    }

    public void performLogout() {
        jPanelMainContainer.setVisible(false);
        jPanelMainLogIn.setVisible(true);
        jPanel24.setVisible(true);

        userName.setText("");
        password.setText("");
    }

    private void ComboBoxShiftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboBoxShiftActionPerformed
        if (ComboBoxShift.getSelectedIndex() == 1) {

            labelShift.setText("1");

           shiftIconChanged();

        } else if (ComboBoxShift.getSelectedIndex() == 2) {
            labelShift.setText("2");
            shiftIconChanged();

        } else {
            labelShift.setText("");
            shiftIconChanged();

        }

    }//GEN-LAST:event_ComboBoxShiftActionPerformed

    private void SaveDailyReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveDailyReportActionPerformed

        jDialogDaily.setVisible(true);// جعل النافذة تتكيف مع محتوياتها، وتظهر في المنتصف، ثم إظهارها
        jDialogDaily.pack();
        jDialogDaily.setLocationRelativeTo(this);
        txt14.requestFocus();
        SaveDailyReport1.setEnabled(true);
        Edit3.setEnabled(false);
        jDialogDaily.setVisible(true);
        clear();


    }//GEN-LAST:event_SaveDailyReportActionPerformed

    private void tab1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tab1StateChanged

        if (tab1.getSelectedIndex() == 0) {
            jLabel11.setForeground(Color.blue);
            txt1.setForeground(Color.blue);
            txt2.setForeground(Color.blue);
            txt3.setForeground(Color.blue);
            txt4.setForeground(Color.blue);
            txt5.setForeground(Color.blue);
            txt6.setForeground(Color.blue);
            txt7.setForeground(Color.blue);
            txt8.setForeground(Color.blue);
            txt9.setForeground(Color.blue);
            txt10.setForeground(Color.blue);
            txt11.setForeground(Color.blue);
            txt12.setForeground(Color.blue);
            txt13.setForeground(Color.blue);
            txt14.setForeground(Color.blue);
            jLabel12.setForeground(Color.black);
            txt21.setForeground(Color.black);
            txt22.setForeground(Color.black);
            txt23.setForeground(Color.black);
            txt24.setForeground(Color.black);
            txt25.setForeground(Color.black);
            txt26.setForeground(Color.black);
            txt27.setForeground(Color.black);
            txt28.setForeground(Color.black);
            txt29.setForeground(Color.black);
            txt30.setForeground(Color.black);
            txt31.setForeground(Color.black);
            txt32.setForeground(Color.black);
            txt33.setForeground(Color.black);
            txt34.setForeground(Color.black);
            if (table.getSelectedRowCount() == 0) {
                Delete.setEnabled(false);
            }

        } else {
            jLabel12.setForeground(Color.blue);
            txt21.setForeground(Color.blue);
            txt22.setForeground(Color.blue);
            txt23.setForeground(Color.blue);
            txt24.setForeground(Color.blue);
            txt25.setForeground(Color.blue);
            txt26.setForeground(Color.blue);
            txt27.setForeground(Color.blue);
            txt28.setForeground(Color.blue);
            txt29.setForeground(Color.blue);
            txt30.setForeground(Color.blue);
            txt31.setForeground(Color.blue);
            txt32.setForeground(Color.blue);
            txt33.setForeground(Color.blue);
            txt34.setForeground(Color.blue);
            jLabel11.setForeground(Color.black);
            txt1.setForeground(Color.black);
            txt2.setForeground(Color.black);
            txt3.setForeground(Color.black);
            txt4.setForeground(Color.black);
            txt5.setForeground(Color.black);
            txt6.setForeground(Color.black);
            txt7.setForeground(Color.black);
            txt8.setForeground(Color.black);
            txt9.setForeground(Color.black);
            txt10.setForeground(Color.black);
            txt11.setForeground(Color.black);
            txt12.setForeground(Color.black);
            txt13.setForeground(Color.black);
            txt14.setForeground(Color.black);
            if (tablefor.getSelectedRowCount() == 0) {
                Delete.setEnabled(false);
            }

        }
    }//GEN-LAST:event_tab1StateChanged

    private void tableforMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableforMouseClicked

        Edit2.setEnabled(true);
        Edit3.setEnabled(true);
        SaveDailyReport.setEnabled(false);
        SaveDailyReport1.setEnabled(false);
        jLabel12.setForeground(Color.blue);
        jLabel11.setForeground(Color.black);

        int s = tablefor.getSelectedRow();
        if (s == -1) {
            JOptionPane.showMessageDialog(null, "يرجى تحديد صف من الجدول.");
            return;
        }

        String branchName = String.valueOf(tablefor.getValueAt(s, 18));
        int shiftNum = Integer.parseInt(tablefor.getValueAt(s, 17).toString());
        String date_search = String.valueOf(tablefor.getValueAt(s, 16));

        Connection conn;
        Statement stm;
        ResultSet rset;

        try {
            // 1. الحصول على الاتصال بقاعدة البيانات باستخدام الدالة الجديدة
            conn = DBConnection.getInstance().getConnection();

            // 2. التحقق من الاتصال
            if (conn == null || conn.isClosed()) {
                JOptionPane.showMessageDialog(null, "فشل الاتصال بقاعدة البيانات.");
                return;
            }

            String SQL = "SELECT * FROM Table2";

            stm = conn.createStatement();
            rset = stm.executeQuery(SQL);

            int found = 0;

            while (rset.next()) {
                if (date_search.equals(rset.getString("addDate")) && shiftNum == rset.getInt("id") && branchName.equals(rset.getString("branchName"))) {
                    found = 1;

                    String EditDate = rset.getString("EditDate");
                    String user = rset.getString("users");
                    String branchN = rset.getString("branchName");
                    int id_col = rset.getInt("id");
                    int cigNum = rset.getInt("cigNum");
                    int cigPrice = rset.getInt("cigPrice");
                    int winNum = rset.getInt("winNum");
                    int winprice = rset.getInt("winPrice");
                    int beerNum = rset.getInt("beerNum");
                    int beerPrice = rset.getInt("beerPrice");
                    int foodNum = rset.getInt("foodNum");
                    int foodprice = rset.getInt("foodPrice");
                    int prfuNum = rset.getInt("prfuNum");
                    int prfuprice = rset.getInt("prfuprice");
                    int deviceNum = rset.getInt("deviceNum");
                    int devicePrice = rset.getInt("devicePrice");
                    int gifNum = rset.getInt("gifNum");
                    int gifprice = rset.getInt("gifprice");
                    int wintaxD = rset.getInt("winTaxD");
                    int wintaxP = rset.getInt("winTaxP");

                    labelDate.setText(EditDate);
                    labelUser.setText("المستخدم" + " : " + user);
                    labelBranchName.setText(branchN);
                    txt34.setText(String.valueOf(cigNum));
                    txt33.setText(String.valueOf(cigPrice));
                    txt32.setText(String.valueOf(winNum));
                    txt31.setText(String.valueOf(winprice));
                    txt30.setText(String.valueOf(beerNum));
                    txt29.setText(String.valueOf(beerPrice));
                    txt28.setText(String.valueOf(foodNum));
                    txt27.setText(String.valueOf(foodprice));
                    txt26.setText(String.valueOf(prfuNum));
                    txt25.setText(String.valueOf(prfuprice));
                    txt24.setText(String.valueOf(deviceNum));
                    txt23.setText(String.valueOf(devicePrice));
                    txt22.setText(String.valueOf(gifNum));
                    txt21.setText(String.valueOf(gifprice));
                   
                    txt35.setText(String.valueOf(wintaxD));
                    txt36.setText(String.valueOf(wintaxP));
                     labelShift.setText(String.valueOf(id_col));
                    shiftIconChanged();
                    break;
                }
               
            }

            if (found == 0) {
                JOptionPane.showMessageDialog(null, "لايوجد نتائج مطابقة فى قاعدة البيانات");
            }

        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "حدث خطأ: " + e.getMessage());
//    } finally {
//        // إغلاق الموارد في النهاية
//        try {
//            if (rs != null) rs.close();
//            if (stmt != null) stmt.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        }

        Delete.setEnabled(true);
    }//GEN-LAST:event_tableforMouseClicked

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked

        Edit2.setEnabled(true);
        Edit3.setEnabled(true);
        SaveDailyReport.setEnabled(false);
        SaveDailyReport1.setEnabled(false);
        jLabel11.setForeground(Color.blue);
        jLabel12.setForeground(Color.black);

        int s = table.getSelectedRow();
        if (s == -1) {
            JOptionPane.showMessageDialog(null, "يرجى تحديد صف من الجدول.");
            return;
        }

        String branchName = String.valueOf(table.getValueAt(s, 18));
        int shiftNum = Integer.parseInt(table.getValueAt(s, 17).toString());
        String date_search = String.valueOf(table.getValueAt(s, 16));

        Connection conn;
        Statement stm;
        ResultSet rset;

        try {
            // 1. الحصول على الاتصال بقاعدة البيانات باستخدام الدالة الجديدة
            conn = DBConnection.getInstance().getConnection();

            // 2. التحقق من الاتصال
            if (conn == null || conn.isClosed()) {
                JOptionPane.showMessageDialog(null, "فشل الاتصال بقاعدة البيانات.");
                return;
            }

            String SQL = "SELECT * FROM Table1";

            stm = conn.createStatement();
            rset = stm.executeQuery(SQL);

            int found = 0;

            while (rset.next()) {
                if (date_search.equals(rset.getString("addDate")) && shiftNum == rset.getInt("id") && branchName.equals(rset.getString("branchName"))) {
                    found = 1;

                    String EditDate = rset.getString("EditDate");
                    String user = rset.getString("users");
                    String branchN = rset.getString("branchName");
                    int id_col = rset.getInt("id");
                    int cigNum = rset.getInt("cigNum");
                    int cigPrice = rset.getInt("cigPrice");
                    int winNum = rset.getInt("winNum");
                    int winprice = rset.getInt("winPrice");
                    int beerNum = rset.getInt("beerNum");
                    int beerPrice = rset.getInt("beerPrice");
                    int foodNum = rset.getInt("foodNum");
                    int foodprice = rset.getInt("foodPrice");
                    int prfuNum = rset.getInt("prfuNum");
                    int prfuprice = rset.getInt("prfuprice");
                    int deviceNum = rset.getInt("deviceNum");
                    int devicePrice = rset.getInt("devicePrice");
                    int gifNum = rset.getInt("gifNum");
                    int gifprice = rset.getInt("gifprice");
                    int wintaxD = rset.getInt("winTaxD");
                    int wintaxP = rset.getInt("winTaxP");

                    labelDate.setText(EditDate);
                    labelUser.setText("المستخدم" + " : " + user);
                    labelBranchName.setText(branchN);
                    txt14.setText(String.valueOf(cigNum));
                    txt13.setText(String.valueOf(cigPrice));
                    txt12.setText(String.valueOf(winNum));
                    txt11.setText(String.valueOf(winprice));
                    txt10.setText(String.valueOf(beerNum));
                    txt9.setText(String.valueOf(beerPrice));
                    txt8.setText(String.valueOf(foodNum));
                    txt7.setText(String.valueOf(foodprice));
                    txt6.setText(String.valueOf(prfuNum));
                    txt5.setText(String.valueOf(prfuprice));
                    txt4.setText(String.valueOf(deviceNum));
                    txt3.setText(String.valueOf(devicePrice));
                    txt2.setText(String.valueOf(gifNum));
                    txt1.setText(String.valueOf(gifprice));
                    labelShift.setText(String.valueOf(id_col));
//                    txt35.setText(String.valueOf(wintaxD));
//                    txt36.setText(String.valueOf(wintaxP));
                shiftIconChanged();

                    break; // نخرج من الحلقة بعد العثور على النتيجة
                    
                }
                 
               
            }

            if (found == 0) {
                JOptionPane.showMessageDialog(null, "لايوجد نتائج مطابقة فى قاعدة البيانات");
            }

        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "حدث خطأ: " + e.getMessage());
//    } finally {
//        // إغلاق الموارد في النهاية
//        try {
//            if (rs != null) rs.close();
//            if (stmt != null) stmt.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        }

        Delete.setEnabled(true);
    }//GEN-LAST:event_tableMouseClicked

    private void Details_of_DailySalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Details_of_DailySalesActionPerformed

        if (jPanelBills.isVisible()) {

            if (datechooser2.getDate() == null) {
                JOptionPane.showMessageDialog(null, " يجب اختيار التاريخ أولا.");

            } else {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                addDate = dateFormat.format(datechooser2.getDate());

                Details_of_DailySales();

            }
        } else if (jPanelReports.isVisible()) {

            if (datechooser2.getDate() == null) {
                JOptionPane.showMessageDialog(null, " يجب اختيار التاريخ أولا.");

            } else {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                addDate = dateFormat.format(datechooser2.getDate());

                Details_of_DailySales();
            }

        }

    }//GEN-LAST:event_Details_of_DailySalesActionPerformed

    private void tablecount1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablecount1MouseClicked

        saveOperation.setEnabled(false);
        Edit.setEnabled(true);

        int selectedRow = tablecount1.getSelectedRow();
        if (selectedRow == -1) {

            return;
        }

        try {
            // 1. الحصول على البيانات من الصف المختار
            int id_search = Integer.parseInt(tablecount1.getValueAt(selectedRow, 6).toString());
            int count_search = Integer.parseInt(tablecount1.getValueAt(selectedRow, 2).toString());
            String date_search = String.valueOf(tablecount1.getValueAt(selectedRow, 5));

            String query = "SELECT * FROM Table3 WHERE id = ? AND addDate = ? AND count = ?";

            try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {

                pstmt.setInt(1, id_search);
                pstmt.setString(2, date_search);
                pstmt.setInt(3, count_search);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {

                        String birthdate = rs.getString("birthDate");
                        String passport1 = rs.getString("passport");
                        String name1 = rs.getString("name");
                        int billnum1 = rs.getInt("billNum");
                        int id_col = rs.getInt("id");
                        int input1 = rs.getInt("input");
                        int output1 = rs.getInt("output");
                        int count2 = rs.getInt("count");
                        int wintaxp = rs.getInt("winTaxP");
                        int wintaxd = rs.getInt("winTaxD");
                        String notes1 = rs.getString("notes");
                        String addDate = rs.getString("addDate");
                        int price1 = rs.getInt("price");
                        String type1 = rs.getString("type");
                        String nation = rs.getString("nation");

                        birthDate.setText(birthdate);
                        passport.setText(passport1);
                        name.setText(name1);
                        billNum.setText(String.valueOf(billnum1));
                        input.setText(String.valueOf(input1));
                        output.setText(String.valueOf(output1));
                        count.setText(String.valueOf(count2));
                        winTaxP.setText(String.valueOf(wintaxp));
                        winTaxD.setText(String.valueOf(wintaxd));
                        notes.setText(notes1);
                        price.setText(String.valueOf(price1));
                        ComboB_type.setSelectedItem(type1);
                        id.setText(String.valueOf(id_col));
                        operation_date.setText(addDate);

                        if (nation == null) {
                            ComboB_national.setSelectedIndex(0);
                        } else {
                            ComboB_national.setSelectedItem(nation);
                        }

                        if (rs.getBytes("image") == null) {
                            labelImage.setIcon(null);
                            labelImage.setText("لايوجد صورة");
                        } else {
                            byte[] image = rs.getBytes("image");
                            ImageIcon image1 = new ImageIcon(new ImageIcon(image).getImage()
                                    .getScaledInstance(labelImage.getWidth(), labelImage.getHeight(), Image.SCALE_SMOOTH));
                            labelImage.setText("");
                            labelImage.setIcon(image1);
                        }
                        
                     

                    } else {

                        JOptionPane.showMessageDialog(null, "لا يوجد نتائج مطابقة");
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "حدث خطأ في تحويل البيانات. يرجى التأكد من صحة القيم في الجدول.", "خطأ في البيانات", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "حدث خطأ في قاعدة البيانات: " + e.getMessage(), "خطأ SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_tablecount1MouseClicked

    private void code_viewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_code_viewActionPerformed

        DefaultTableModel model2 = (DefaultTableModel) JFrameCodeSearch.tablecount2.getModel();
        model2.setRowCount(0);

        // 1. تحديد الاستعلام وطريقة البحث
        String query = null;
        int searchid = -1;
        String addDate = null;

        try {
            if (RadiCodeNum.isSelected()) {
                if ("".equals(id.getText())) {
                    String input = JOptionPane.showInputDialog("أدخل رقم الكود المراد عرضه.");
                    if (input == null || input.trim().isEmpty()) {
                        return;
                    }
                    searchid = Integer.parseInt(input);
                } else {
                    searchid = Integer.parseInt(id.getText());
                }
                query = "SELECT * FROM Table3 WHERE id = ?";
            } else if (RadioOPDate.isSelected()) {
                if (datechooser2.getDate() == null) {
                    JOptionPane.showMessageDialog(null, "يجب اختيار التاريخ أولاً.");
                    return;
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                addDate = dateFormat.format(datechooser2.getDate());
                query = "SELECT * FROM Table3 WHERE addDate = ?";
            } else {
                JOptionPane.showMessageDialog(null, "يجب اختيار طريقة البحث (رقم الكود أو التاريخ).");
                return;
            }

            try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {

                if (RadiCodeNum.isSelected()) {
                    pstmt.setInt(1, searchid);
                } else {
                    pstmt.setString(1, addDate);
                }

                try (ResultSet rs = pstmt.executeQuery()) {
                    boolean found = false;
                    while (rs.next()) {
                        found = true;
                        Object[] row = new Object[9];
                        row[0] = rs.getString("notes");
                        row[1] = rs.getString("type");
                        row[2] = rs.getInt("unitPrice");
                        row[3] = rs.getInt("price");
                        row[4] = rs.getInt("count");
                        row[5] = rs.getInt("output");
                        row[6] = rs.getInt("input");
                        row[7] = rs.getString("addDate");
                        row[8] = rs.getInt("id");
                        model2.addRow(row);
                    }

                    if (found) {
                        JFrameCodeSearch.setVisible(true);
//                        JFrameCodeSearch.setIconImage(img);
                    } else {
                        JOptionPane.showMessageDialog(null, "لا يوجد نتائج مطابقة");
                        JFrameCodeSearch.setVisible(false);
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "يرجى كتابة رقم كود صحيح بدون أحرف أو علامات .");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "حدث خطأ في قاعدة البيانات: " + e.getMessage());
            e.printStackTrace();
        }


    }//GEN-LAST:event_code_viewActionPerformed

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed

        search_code();


    }//GEN-LAST:event_searchActionPerformed

    private void saveOperationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveOperationActionPerformed

        int minus, plus, multi, priceU, account, total;

        try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO Table3(image,nation,notes,type,winTaxP,winTaxD,unitPrice,price,count,output,input,addDate,id,billNum,name,passport,birthDate,place,shift) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {

            if (RadiCodeNum.isSelected() || RadioDiscount.isSelected()) {
                if (numericTest(id.getText()) == false) {
                    JOptionPane.showMessageDialog(null, "يرجى كتابة رقم كود صحيح بدون أحرف أو علامات .");
                    return;
                }
            }

            if (RadioDiscount.isSelected()) {

                if (output.getText().isEmpty() || Integer.parseInt(output.getText()) <= 0) {
                    JOptionPane.showMessageDialog(null, "يرجى كتابة عدد الوحدات المبيعة .");
                    return;
                }
                if (ComboBoxShift.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "يرجى اختيار الوردية.");
                    return;
                }
                if (ComboB_national.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "يرجى اختيار الجنسية .");
                    return;
                }
                if (ComboB_national.getSelectedIndex() == 2 && ComboB_type.getSelectedIndex() == 2 && (("0".equals(winTaxP.getText()) && "0".equals(winTaxD.getText())) || ("".equals(winTaxP.getText()) && "".equals(winTaxD.getText())))) {
                    JOptionPane.showMessageDialog(null, " يجب إدخال ضريبة 1 % ");
                    return;
                }

                minus = Integer.parseInt(output.getText());
                account = Integer.parseInt(count.getText());

                if (account - minus < 0) {
                    JOptionPane.showMessageDialog(null, "! الرصيد الحالى لا يسمح.");
                    return;
                }

                priceU = Integer.parseInt(price.getText());
                total = account - minus;
                multi = minus * priceU;

                ps.setBytes(1, pImage);
                ps.setString(2, ComboB_national.getSelectedItem().toString());
                ps.setString(3, notes.getText());
                ps.setString(4, ComboB_type.getSelectedItem().toString());
                ps.setString(5, ComboB_national.getSelectedIndex() == 2 && !winTaxP.getText().isEmpty() ? winTaxP.getText() : "0");
                ps.setString(6, ComboB_national.getSelectedIndex() == 2 && !winTaxD.getText().isEmpty() ? winTaxD.getText() : "0");
                ps.setString(7, String.valueOf(priceU));
                ps.setString(8, String.valueOf(multi));
                ps.setString(9, String.valueOf(total));
                ps.setString(10, output.getText());
                ps.setString(11, "0");
                ps.setString(12, labelDate.getText());
                ps.setString(13, id.getText());
                ps.setString(14, billNum.getText().isEmpty() ? "0" : billNum.getText());
                ps.setString(15, name.getText());
                ps.setString(16, passport.getText());
                ps.setString(17, birthDate.getText());
                ps.setString(18, labelBranchName.getText());
                ps.setString(19, labelShift.getText());

            } else if (RadioImport.isSelected()) {

                if (ComboBoxShift.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "يرجى اختيار الوردية.");
                    return;
                }
                if (ComboB_type.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "يجب اختيار نوع الصنف");
                    return;
                }
                if (input.getText().isEmpty() || Integer.parseInt(input.getText()) <= 0) {
                    JOptionPane.showMessageDialog(null, "يجب إدخال عدد الوحدات المضافة.");
                    return;
                }
                if (count.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "يجب إدخال الرصيد السابق أو ضع صفر إذا كان لا يوجد.");
                    return;
                }
                if (price.getText().isEmpty() || Integer.parseInt(price.getText()) <= 0) {
                    JOptionPane.showMessageDialog(null, "يرجى إدخال سعر الصنف.");
                    return;
                }

                plus = Integer.parseInt(input.getText());
                account = Integer.parseInt(count.getText());
                total = plus + account;

                ps.setBytes(1, pImage);
                ps.setString(2, "");
                ps.setString(3, notes.getText());
                ps.setString(4, ComboB_type.getSelectedItem().toString());
                ps.setString(5, "0");
                ps.setString(6, "0");
                ps.setString(7, price.getText());
                ps.setString(8, "0");
                ps.setString(9, String.valueOf(total));
                ps.setString(10, "0");
                ps.setString(11, input.getText());
                ps.setString(12, labelDate.getText());
                ps.setString(13, id.getText());
                ps.setString(14, "0");
                ps.setString(15, "");
                ps.setString(16, "");
                ps.setString(17, "");
                ps.setString(18, labelBranchName.getText());
                ps.setString(19, labelShift.getText());
            }

            ps.executeUpdate();
//            JOptionPane.showMessageDialog(null, "تم الحفظ بنجاح.");
            show_data_of_count();
            showSuccessNotification();

            input.setText("");
            output.setText("");
            count.setText("");
            winTaxP.setText("");
            winTaxD.setText("");
            notes.setText("");
            price.setText("");
            billNum.setText("");
            name.setText("");
            passport.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "يرجى إدخال قيم رقمية صحيحة في الحقول المناسبة.", "خطأ في البيانات", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "حدث خطأ في قاعدة البيانات: " + e.getMessage(), "خطأ SQL", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "حدث خطأ غير متوقع: " + e.getMessage(), "خطأ عام", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }//GEN-LAST:event_saveOperationActionPerformed

    private void ComboB_nationalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboB_nationalActionPerformed

        if (ComboB_national.getSelectedIndex() == 2 && ComboB_type.getSelectedIndex() == 2) {

            jLabel29.setVisible(true);
            jLabel30.setVisible(true);
            winTaxP.setVisible(true);
            winTaxD.setVisible(true);
            //            ComboB_currancy.setVisible(true);
            winTaxP.requestFocus();
        } else {
            jLabel29.setVisible(false);
            jLabel30.setVisible(false);
            winTaxP.setVisible(false);
            winTaxD.setVisible(false);
            //            ComboB_currancy.setVisible(false);

        }
    }//GEN-LAST:event_ComboB_nationalActionPerformed

    private void RadioImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioImportActionPerformed

        jLabel21.setVisible(false);
        output.setVisible(false);
        ComboB_national.setVisible(false);
        jLabel20.setVisible(true);
        ComboB_national.setSelectedIndex(0);
        input.setVisible(true);
        jPanel13.setVisible(false);

    }//GEN-LAST:event_RadioImportActionPerformed

    private void RadioDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioDiscountActionPerformed

        jLabel20.setVisible(false);
        input.setVisible(false);
        jLabel21.setVisible(true);
        output.setVisible(true);
        ComboB_national.setVisible(true);
        jPanel13.setVisible(true);

    }//GEN-LAST:event_RadioDiscountActionPerformed

    private void moreOptionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_moreOptionMouseClicked

        show_popupMenu1(evt);


    }//GEN-LAST:event_moreOptionMouseClicked

    private void Save_EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Save_EditActionPerformed

        save_Edit();

    }//GEN-LAST:event_Save_EditActionPerformed

    private void view_Shift_SalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_view_Shift_SalesActionPerformed

        DefaultTableModel model = (DefaultTableModel) JFrameDailySales.EgyTable.getModel();
        DefaultTableModel model2 = (DefaultTableModel) JFrameDailySales.ForTabel.getModel();

        String sql = "SELECT DISTINCT id FROM Table3 ";

        model.setRowCount(0);
        model2.setRowCount(0);

        Object[] row = new Object[13];

        if (datechooser2.getDate() == null) {
            JOptionPane.showMessageDialog(null, " يجب اختيار التاريخ أولا.");

        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String addDate = dateFormat.format(datechooser2.getDate());

            for (int i = 0; i < tablecount1.getRowCount(); i++) {

                try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {

                    ResultSet rs = pstmt.executeQuery(sql);

                    String searchid6 = "مصرى";
                    String searchDate = addDate;
                    String searchid5 = "سجائر";
                    String searchid4 = "مشروبات كحولية";
                    String searchid3 = "مأكولات";
                    String searchid2 = "عطور";
                    String searchid1 = "هدايا";
                    int found = 0;
                    rs.absolute(0);
                    while (rs.next()) {

                        if (searchDate.equals(rs.getString("addDate")) && searchid6.equals(rs.getString("nation")) && searchid1.equals(rs.getString("type"))) {
                            found = 1;

                            int output1 = rs.getInt("output");
                            int price1 = rs.getInt("price");

                            row[0] = price1;
                            row[1] = output1;
                            row[2] = 0;
                            row[3] = 0;
                            row[4] = 0;
                            row[5] = 0;
                            row[6] = 0;
                            row[7] = 0;
                            row[8] = 0;
                            row[9] = 0;
                            model.addRow(row);

                        }

                        if (searchDate.equals(rs.getString("addDate")) && searchid6.equals(rs.getString("nation")) && searchid2.equals(rs.getString("type"))) {

                            found = 1;

                            int output1 = rs.getInt("output");

                            int price1 = rs.getInt("price");

                            row[0] = 0;
                            row[1] = 0;
                            row[2] = price1;
                            row[3] = output1;
                            row[4] = 0;
                            row[5] = 0;
                            row[6] = 0;
                            row[7] = 0;
                            row[8] = 0;
                            row[9] = 0;

                            model.addRow(row);

                        }
                        if (searchDate.equals(rs.getString("addDate")) && searchid6.equals(rs.getString("nation")) && searchid3.equals(rs.getString("type"))) {
                            found = 1;

                            int output1 = rs.getInt("output");

                            int price1 = rs.getInt("price");

                            row[0] = 0;
                            row[1] = 0;
                            row[2] = 0;
                            row[3] = 0;
                            row[4] = price1;
                            row[5] = output1;
                            row[6] = 0;
                            row[7] = 0;
                            row[8] = 0;
                            row[9] = 0;

                            model.addRow(row);

                        }
                        if (searchDate.equals(rs.getString("addDate")) && searchid6.equals(rs.getString("nation")) && searchid4.equals(rs.getString("type"))) {
                            found = 1;

                            int output1 = rs.getInt("output");
                            int price1 = rs.getInt("price");
                            row[0] = 0;
                            row[1] = 0;
                            row[2] = 0;
                            row[3] = 0;
                            row[4] = 0;
                            row[5] = 0;
                            row[6] = price1;
                            row[7] = output1;
                            row[8] = 0;
                            row[9] = 0;

                            model.addRow(row);

                        }
                        if (searchDate.equals(rs.getString("addDate")) && searchid6.equals(rs.getString("nation")) && searchid5.equals(rs.getString("type"))) {
                            found = 1;

                            int output1 = rs.getInt("output");
                            int price1 = rs.getInt("price");

                            row[0] = 0;
                            row[1] = 0;
                            row[2] = 0;
                            row[3] = 0;
                            row[4] = 0;
                            row[5] = 0;
                            row[6] = 0;
                            row[7] = 0;
                            row[8] = price1;
                            row[9] = output1;

                            model.addRow(row);

                        }
//                        jTabbedPane1.setSelectedIndex(1);
                        JFrameDailySales.setVisible(true);
                    }

                    if (found == 0) {

                        JOptionPane.showMessageDialog(null, "لايوجد نتائج مطابقة بالنسبة للمصرى.");
                        JFrameDailySales.setVisible(false);
                    }

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                break;

            }

            for (int i2 = 0; i2 < tablecount1.getRowCount(); i2++) {

                try {

                    String searchid7 = "أجنبى";
                    String searchDate = addDate;
                    String searchid6 = "سجائر";
                    String searchid5 = "مشروبات كحولية";
                    String searchid4 = "مأكولات";
                    String searchid3 = "عطور";
                    String searchid2 = "هدايا";
                    String searchid1 = "winTaxP";
                    int found = 0;
                    rs.absolute(0);
                    while (rs.next()) {

                        if (searchDate.equals(rs.getString("addDate")) && searchid7.equals(rs.getString("nation")) && searchid1.equals(rs.getString("winTaxD"))) {
                            found = 1;

                            int output1 = rs.getInt("winTaxD");

                            int price1 = rs.getInt("price");

                            row[0] = 0;
                            row[1] = 0;
                            row[2] = 0;
                            row[3] = 0;
                            row[4] = 0;
                            row[5] = 0;
                            row[6] = 0;
                            row[7] = 0;
                            row[8] = 0;
                            row[9] = 0;
                            row[10] = output1;
                            row[11] = 0;
                            model2.addRow(row);

                        }

                        if (searchDate.equals(rs.getString("addDate")) && searchid7.equals(rs.getString("nation")) && searchid2.equals(rs.getString("type"))) {

                            found = 1;

                            int output1 = rs.getInt("output");

                            int price1 = rs.getInt("price");

                            row[0] = 0;
                            row[1] = 0;
                            row[2] = price1;
                            row[3] = output1;
                            row[4] = 0;
                            row[5] = 0;
                            row[6] = 0;
                            row[7] = 0;
                            row[8] = 0;
                            row[9] = 0;
                            row[10] = 0;
                            row[11] = 0;
                            model2.addRow(row);

                        }
                        if (searchDate.equals(rs.getString("addDate")) && searchid7.equals(rs.getString("nation")) && searchid3.equals(rs.getString("type"))) {
                            found = 1;

                            int output1 = rs.getInt("output");

                            int price1 = rs.getInt("price");

                            row[0] = 0;
                            row[1] = 0;
                            row[2] = 0;
                            row[3] = 0;
                            row[4] = price1;
                            row[5] = output1;
                            row[6] = 0;
                            row[7] = 0;
                            row[8] = 0;
                            row[9] = 0;
                            row[10] = 0;
                            row[11] = 0;
                            model2.addRow(row);

                        }
                        if (searchDate.equals(rs.getString("addDate")) && searchid7.equals(rs.getString("nation")) && searchid4.equals(rs.getString("type"))) {
                            found = 1;

                            int output1 = rs.getInt("output");

                            int price1 = rs.getInt("price");
                            row[0] = 0;
                            row[1] = 0;
                            row[2] = 0;
                            row[3] = 0;
                            row[4] = 0;
                            row[5] = 0;
                            row[6] = price1;
                            row[7] = output1;
                            row[8] = 0;
                            row[9] = 0;
                            row[10] = 0;
                            row[11] = 0;

                            model2.addRow(row);

                        }
                        if (searchDate.equals(rs.getString("addDate")) && searchid7.equals(rs.getString("nation")) && searchid5.equals(rs.getString("type"))) {
                            found = 1;

                            int output1 = rs.getInt("output");

                            int price1 = rs.getInt("price");

                            row[0] = 0;
                            row[1] = 0;
                            row[2] = 0;
                            row[3] = 0;
                            row[4] = 0;
                            row[5] = 0;
                            row[6] = 0;
                            row[7] = 0;
                            row[8] = price1;
                            row[9] = output1;
                            row[10] = 0;
                            row[11] = 0;

                            model2.addRow(row);

                        }
                        if (searchDate.equals(rs.getString("addDate")) && searchid7.equals(rs.getString("nation")) && searchid6.equals(rs.getString("type"))) {
                            found = 1;

                            int output1 = rs.getInt("output");

                            int price1 = rs.getInt("price");

                            row[0] = 0;
                            row[1] = 0;
                            row[2] = 0;
                            row[3] = 0;
                            row[4] = 0;
                            row[5] = 0;
                            row[6] = 0;
                            row[7] = 0;
                            row[8] = 0;
                            row[9] = 0;
                            row[10] = price1;
                            row[11] = output1;

                            model2.addRow(row);

                        }
//                        jTabbedPane1.setSelectedIndex(1);
                    }

                    if (found == 0) {

                        JOptionPane.showMessageDialog(null, "لايوجد نتائج مطابقة بالنسبة للأجنبى.");
                    }

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                break;

            }

        }

        sum_Tables_Col(JFrameDailySales.EgyTable);
        sum_Tables_Col2(JFrameDailySales.ForTabel);

//    DefaultTableModel model = (DefaultTableModel) JFrameDailySales.table1.getModel();
//    DefaultTableModel model2 = (DefaultTableModel) JFrameDailySales.tablefor1.getModel();
//    DefaultTableModel model3 = (DefaultTableModel) JFrameDailySales.tableforTax.getModel();
//
//    // Clear existing table data
//    model.setRowCount(0);
//    model2.setRowCount(0);
//    model3.setRowCount(0);
//
//    // Check for a selected date
//    if (datechooser2.getDate() == null) {
//        JOptionPane.showMessageDialog(null, "يجب اختيار التاريخ أولاً.");
//        return;
//    }
//
//    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//    String addDate = dateFormat.format(datechooser2.getDate());
//
//    // Arrays to hold aggregated totals for each nationality
//    Object[] egyptianTotals = new Object[10];
//    Object[] foreignTotals = new Object[10];
//
//    // Initialize arrays with zero values to prevent NullPointerExceptions
//    for (int i = 0; i < 10; i++) {
//        egyptianTotals[i] = 0;
//        foreignTotals[i] = 0;
//    }
//
//    // هنا تم تصحيح السطر
//    List<Object[]> taxRows = new ArrayList<>();
//
//    String query = "SELECT * FROM Table3 WHERE addDate = ?";
//
//    try (Connection con = DBConnection.getInstance().getConnection();
//         PreparedStatement ps = con.prepareStatement(query)) {
//
//        ps.setString(1, addDate);
//
//        try (ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) {
//                String nation = rs.getString("nation");
//                String type = rs.getString("type");
//                int price = rs.getInt("price");
//                int output = rs.getInt("output");
//                
//                // Aggregate Egyptian sales
//                if ("مصرى".equals(nation)) {
//                    if ("هدايا".equals(type)) {
//                        egyptianTotals[0] = (int) egyptianTotals[0] + price;
//                        egyptianTotals[1] = (int) egyptianTotals[1] + output;
//                    } else if ("عطور".equals(type)) {
//                        egyptianTotals[2] = (int) egyptianTotals[2] + price;
//                        egyptianTotals[3] = (int) egyptianTotals[3] + output;
//                    } else if ("مأكولات".equals(type)) {
//                        egyptianTotals[4] = (int) egyptianTotals[4] + price;
//                        egyptianTotals[5] = (int) egyptianTotals[5] + output;
//                    } else if ("مشروبات كحولية".equals(type)) {
//                        egyptianTotals[6] = (int) egyptianTotals[6] + price;
//                        egyptianTotals[7] = (int) egyptianTotals[7] + output;
//                    } else if ("سجائر".equals(type)) {
//                        egyptianTotals[8] = (int) egyptianTotals[8] + price;
//                        egyptianTotals[9] = (int) egyptianTotals[9] + output;
//                    }
//                }
//                // Aggregate Foreign sales
//                else if ("أجنبى".equals(nation)) {
//                    if ("هدايا".equals(type)) {
//                        foreignTotals[0] = (int) foreignTotals[0] + price;
//                        foreignTotals[1] = (int) foreignTotals[1] + output;
//                    } else if ("عطور".equals(type)) {
//                        foreignTotals[2] = (int) foreignTotals[2] + price;
//                        foreignTotals[3] = (int) foreignTotals[3] + output;
//                    } else if ("مأكولات".equals(type)) {
//                        foreignTotals[4] = (int) foreignTotals[4] + price;
//                        foreignTotals[5] = (int) foreignTotals[5] + output;
//                    } else if ("مشروبات كحولية".equals(type)) {
//                        foreignTotals[6] = (int) foreignTotals[6] + price;
//                        foreignTotals[7] = (int) foreignTotals[7] + output;
//                    } else if ("سجائر".equals(type)) {
//                        foreignTotals[8] = (int) foreignTotals[8] + price;
//                        foreignTotals[9] = (int) foreignTotals[9] + output;
//                    }
//                }
//                
//                // Collect tax data
//                int wintaxP = rs.getInt("winTaxP");
//                int wintaxD = rs.getInt("winTaxD");
//                if (wintaxP > 0 || wintaxD > 0) {
//                    taxRows.add(new Object[]{wintaxP, wintaxD});
//                }
//            }
//        }
//    } catch (SQLException | HeadlessException e) {
//        JOptionPane.showMessageDialog(null, e);
//    }
//    
//    // Populate tables with aggregated data
//    // Only add a row if there is any data to show
//    boolean egyptianDataFound = false;
//    for (Object val : egyptianTotals) {
//        if ((int) val > 0) {
//            egyptianDataFound = true;
//            break;
//        }
//    }
//    if (egyptianDataFound) {
//        model.addRow(egyptianTotals);
//    } else {
//        JOptionPane.showMessageDialog(null, "لا يوجد نتائج مطابقة بالنسبة للمصرى.");
//    }
//    
//    boolean foreignDataFound = false;
//    for (Object val : foreignTotals) {
//        if ((int) val > 0) {
//            foreignDataFound = true;
//            break;
//        }
//    }
//    if (foreignDataFound) {
//        model2.addRow(foreignTotals);
//    } else {
//        JOptionPane.showMessageDialog(null, "لا يوجد نتائج مطابقة بالنسبة للأجنبى.");
//    }
//
//    if (!taxRows.isEmpty()) {
//        for (Object[] row : taxRows) {
//            model3.addRow(row);
//        }
//    } else {
//        JOptionPane.showMessageDialog(null, "لا يوجد ضريبة 1 %");
//    }
//    
//    // Show frame and calculate totals
//    if (egyptianDataFound || foreignDataFound || !taxRows.isEmpty()) {
//        jTabbedPane1.setSelectedIndex(1);
//        JFrameDailySales.setVisible(true);
//    }
//
//    sum_Tables_Col(JFrameDailySales.table1);
//    sum_Tables_Col2(JFrameDailySales.tablefor1);
//    sum_Tables_Col3(JFrameDailySales.tableforTax);

    }//GEN-LAST:event_view_Shift_SalesActionPerformed

    private void current_BalanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_current_BalanceActionPerformed

        int id_col = 0;
        String addDate1 = "";
        int input1 = 0;
        int output1 = 0;
        int count2 = 0;
        int price1 = 0;
        int unitprice = 0;
        String type1 = "";
        String notes1 = "";

        DefaultTableModel model = (DefaultTableModel) JFrameCodeSearch.tablecount2.getModel();
        model.setRowCount(0);
        Object[] row = new Object[12];
        ArrayList<Integer> idList = new ArrayList<Integer>();
        String searchword = ComboB_type.getSelectedItem().toString();
        String sql = "SELECT DISTINCT id FROM Table3 ";

        try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {

            rs = pstmt.executeQuery();
//            rs.absolute(0);
            while (rs.next()) {
                idList.add(rs.getInt("id"));
            }

        } catch (SQLException | NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);
        }
//System.out.println(idList);
        for (int i = 0; i < idList.size(); i++) {

            try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {

                rs = pstmt.executeQuery();
                int found = 0;

//                rs.absolute(0);
                while (rs.next()) {

                    if (idList.get(i) == rs.getInt("id")) {

                        found = 1;
                        id_col = rs.getInt("id");
                        addDate1 = rs.getString("addDate");
                        input1 = rs.getInt("input");
                        output1 = rs.getInt("output");
                        count2 = rs.getInt("count");
                        price1 = rs.getInt("price");
                        unitprice = rs.getInt("unitPrice");
                        type1 = rs.getString("type");
                        notes1 = rs.getString("notes");
                    }
                }
                row[0] = notes1;
                row[1] = type1;
                row[2] = unitprice;
                row[3] = price1;
                row[4] = count2;
                row[5] = output1;
                row[6] = input1;
                row[7] = addDate1;
                row[8] = id_col;

                model.addRow(row);

                if (found == 0) {

                    JOptionPane.showMessageDialog(null, "لايوجد نتائج مطابقة");
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }

        JFrameCodeSearch.setVisible(true);

//        int id_col = 0;
//        String addDate1 = "";
//        int input1 = 0;
//        int output1 = 0;
//        int count2 = 0;
//        int price1 = 0;
//        int unitprice = 0;
//        String type1 = "";
//        String notes1 = "";
////        ResultSet rs;
////         PreparedStatement pstmt;
//
//
//
//
//DefaultTableModel model = (DefaultTableModel) JFrameCodeSearch.tablecount2.getModel();
//        model.setRowCount(0);
//        Object[] row = new Object[12];
//        ArrayList<Integer> idList = new ArrayList<>();
//        String searchword = ComboB_type.getSelectedItem().toString();
//        String sql = "SELECT DISTINCT id, addDate, input, output, count, price, unitPrice, type, notes FROM Table3";
//
//        // الحلقة الأولى: لاسترداد معرفات "id"
//        try (Connection con = DBConnection.getInstance().getConnection();
//             PreparedStatement pstmt = con.prepareStatement(sql)) {
//
//            rs = pstmt.executeQuery();
//            while (rs.next()) {
//                idList.add(rs.getInt("id"));
//            }
//
//        } catch (SQLException | NumberFormatException | HeadlessException e) {
//            JOptionPane.showMessageDialog(null, e);
//        }
//
//        // الحلقة الثانية: لمعالجة البيانات بناءً على معرفات "id"
//        for (int i = 0; i < idList.size(); i++) {
//            try (Connection con = DBConnection.getInstance().getConnection();
//                 PreparedStatement pstmt = con.prepareStatement(sql)) {
//                
//                rs = pstmt.executeQuery();
//                int found = 0;
//                
//                while (rs.next()) {
//                    if (idList.get(i).equals(rs.getInt("id"))) {
//                        found = 1;
//                        id_col = rs.getInt("id");
//                        addDate1 = rs.getString("addDate");
//                        input1 = rs.getInt("input");
//                        output1 = rs.getInt("output");
//                        count2 = rs.getInt("count");
//                        price1 = rs.getInt("price");
//                        unitprice = rs.getInt("unitPrice");
//                        type1 = rs.getString("type");
//                        notes1 = rs.getString("notes");
//                        
//                        // إضافة الصف إلى النموذج هنا
//                        row[0] = notes1;
//                        row[1] = type1;
//                        row[2] = unitprice;
//                        row[3] = price1;
//                        row[4] = count2;
//                        row[5] = output1;
//                        row[6] = input1;
//                        row[7] = addDate1;
//                        row[8] = id_col;
//                        model.addRow(row);
//                        
//                        break; // الخروج من الحلقة الداخلية بمجرد العثور على العنصر
//                    }
//                }
//
//                if (found == 0) {
//                    JOptionPane.showMessageDialog(null, "لا يوجد نتائج مطابقة للـ ID: " + idList.get(i));
//                }
//
//            } catch (SQLException e) {
//                JOptionPane.showMessageDialog(null, e);
//            }
//        }
//        JFrameCodeSearch.setVisible(true);
//

    }//GEN-LAST:event_current_BalanceActionPerformed

    private void code_viewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_code_viewMouseClicked
//        show_popupMenu3(evt);
    }//GEN-LAST:event_code_viewMouseClicked

    private void labelImageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labelImageMouseClicked

        //         String filepath2 =this.getClass().getResource("/images/no_image.png").toString();
        try {
            JFileChooser JFileChooser = new JFileChooser();

            FileNameExtensionFilter filter = new FileNameExtensionFilter("Select a .JPG.PNG.GIF image", "jpg", "png", "gif");
            JFileChooser.setFileFilter(filter);
            int result = JFileChooser.showOpenDialog(null);

            File file = JFileChooser.getSelectedFile();

            if (JFileChooser.APPROVE_OPTION == result) {
                labelImage.setText("");
                filepath = file.getAbsolutePath();
                ImageIcon ImageIcon = new ImageIcon(new ImageIcon(filepath).getImage().getScaledInstance(labelImage.getWidth(), labelImage.getHeight(), Image.SCALE_SMOOTH));
                labelImage.setIcon(ImageIcon);
                File image = new File(filepath);
                FileInputStream fis = new FileInputStream(image);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                for (int readnum; (readnum = fis.read(buf)) != -1;) {
                    bos.write(buf, 0, readnum);
                }
                pImage = bos.toByteArray();
//                jLabel13.setVisible(true);
            } else {

                JOptionPane.showMessageDialog(null, "لم يتم إختيار صورة !");
//                jLabel13.setVisible(false);

            }
        } catch (FileNotFoundException e) {

            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }//GEN-LAST:event_labelImageMouseClicked

    private void jLabel13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseClicked

        labelImage.setIcon(null);
        pImage = null;
        labelImage.setText("لايوجد صورة");
//        jLabel13.setVisible(false);

    }//GEN-LAST:event_jLabel13MouseClicked

    private void moreOption2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_moreOption2MouseClicked
        show_popupMenu2(evt);
    }//GEN-LAST:event_moreOption2MouseClicked

    private void Save_Edit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Save_Edit2ActionPerformed

        Connection conn = null;
        PreparedStatement pstmt = null;

        if (tab1.getSelectedIndex() == 0) {
            if (table.getSelectedRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "يرجى تحديد اليومية من الجدول. ");
            } else {
                int row = table.getSelectedRow();
                String branchName = String.valueOf(table.getValueAt(row, 18));
                int shiftNum = Integer.parseInt(table.getValueAt(row, 17).toString());
                String date_search = String.valueOf(table.getValueAt(row, 16));

                try {
                    conn = DBConnection.getInstance().getConnection();
                    if (conn == null || conn.isClosed()) {
                        JOptionPane.showMessageDialog(null, "فشل الاتصال بقاعدة البيانات.");
                        return;
                    }

                    // استخدام استعلام UPDATE مباشر مع PreparedStatement
                    String sql = "UPDATE Table1 SET cigNum = ?, cigPrice = ?, winNum = ?, winprice = ?, beerNum = ?,beerPrice = ?,"
                            + "foodNum = ?, foodPrice = ?, prfuNum = ?, prfuprice = ?, deviceNum = ?,devicePrice = ?, gifNum = ?, gifPrice = ?, "
                            + "winTaxD = ?, winTaxP = ?,users = ?,EditDate=? WHERE branchName =? AND id = ? AND addDate = ? ";
                    pstmt = conn.prepareStatement(sql);
                    String EditDate2 = getTime() + " | " + EditDate;
                    String branchN = labelBranchName.getText();
                    int cigNum = Integer.parseInt(txt14.getText());
                    int cigPrice = Integer.parseInt(txt13.getText());
                    int winNum = Integer.parseInt(txt12.getText());
                    int winprice = Integer.parseInt(txt11.getText());
                    int beerNum = Integer.parseInt(txt10.getText());
                    int beerPrice = Integer.parseInt(txt9.getText());
                    int foodNum = Integer.parseInt(txt8.getText());
                    int foodPrice = Integer.parseInt(txt7.getText());
                    int prfuNum = Integer.parseInt(txt6.getText());
                    int prfuprice = Integer.parseInt(txt5.getText());
                    int deviceNum = Integer.parseInt(txt4.getText());
                    int devicePrice = Integer.parseInt(txt3.getText());
                    int gifNum = Integer.parseInt(txt2.getText());
                    int gifPrice = Integer.parseInt(txt1.getText());
                    int wintaxD = Integer.parseInt("0");
                    int wintaxP = Integer.parseInt("0");

                    // تعيين القيم في PreparedStatement
                    pstmt.setInt(1, cigNum);
                    pstmt.setInt(2, cigPrice);
                    pstmt.setInt(3, winNum);
                    pstmt.setInt(4, winprice);
                    pstmt.setInt(5, beerNum);
                    pstmt.setInt(6, beerPrice);
                    pstmt.setInt(7, foodNum);
                    pstmt.setInt(8, foodPrice);
                    pstmt.setInt(9, prfuNum);
                    pstmt.setInt(10, prfuprice);
                    pstmt.setInt(11, deviceNum);
                    pstmt.setInt(12, devicePrice);
                    pstmt.setInt(13, gifNum);
                    pstmt.setInt(14, gifPrice);
                    pstmt.setInt(15, wintaxD);
                    pstmt.setInt(16, wintaxP);
                    pstmt.setString(17, Name);
                    pstmt.setString(18, EditDate2);
                    pstmt.setString(19, branchName);
                    pstmt.setInt(20, shiftNum); // قيمة id لتحديد الصف
                    pstmt.setString(21, date_search); // قيمة date لتحديد الصف

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
//                        JOptionPane.showMessageDialog(null, "تم التعديل بنجاح ");
                        showSuccessNotification();
                    } else {
                        JOptionPane.showMessageDialog(null, "لم يتم العثور على السجل المراد تعديله.");
                    }
                } catch (SQLException | NumberFormatException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);
                } finally {
                    try {
                        if (pstmt != null) {
                            pstmt.close();
                        }
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                }
                showSales_data();
                showSuccessNotification();

            }
        } else {

            if (tablefor.getSelectedRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "يرجى تحديد اليومية من الجدول. ");
            } else {
                int row = tablefor.getSelectedRow();
                String branchName = String.valueOf(tablefor.getValueAt(row, 18));
                int shiftNum = Integer.parseInt(tablefor.getValueAt(row, 17).toString());
                String date_search = String.valueOf(tablefor.getValueAt(row, 16));
                try {
                    conn = DBConnection.getInstance().getConnection();
                    if (conn == null || conn.isClosed()) {
                        JOptionPane.showMessageDialog(null, "فشل الاتصال بقاعدة البيانات.");
                        return;
                    }

                    String sql = "UPDATE Table2 SET cigNum = ?, cigPrice = ?, winNum = ?, winprice = ?, beerNum = ?,beerPrice = ?,"
                            + "foodNum = ?, foodPrice = ?, prfuNum = ?, prfuprice = ?, deviceNum = ?,devicePrice = ?, gifNum = ?, gifPrice = ?, "
                            + "winTaxD = ?, winTaxP = ?,users = ?,EditDate=? WHERE branchName =? AND id = ? AND addDate = ? ";
                    pstmt = conn.prepareStatement(sql);

                    String EditDate2 = getTime() + " | " + EditDate;
                    int cigNum = Integer.parseInt(txt34.getText());
                    int cigPrice = Integer.parseInt(txt33.getText());
                    int winNum = Integer.parseInt(txt32.getText());
                    int winprice = Integer.parseInt(txt31.getText());
                    int beerNum = Integer.parseInt(txt30.getText());
                    int beerPrice = Integer.parseInt(txt29.getText());
                    int foodNum = Integer.parseInt(txt28.getText());
                    int foodPrice = Integer.parseInt(txt27.getText());
                    int prfuNum = Integer.parseInt(txt26.getText());
                    int prfuprice = Integer.parseInt(txt25.getText());
                    int deviceNum = Integer.parseInt(txt24.getText());
                    int devicePrice = Integer.parseInt(txt23.getText());
                    int gifNum = Integer.parseInt(txt22.getText());
                    int gifPrice = Integer.parseInt(txt21.getText());
                    int wintaxD = Integer.parseInt(txt35.getText());
                    int wintaxP = Integer.parseInt(txt36.getText());

                    // تعيين القيم في PreparedStatement
                    pstmt.setInt(1, cigNum);
                    pstmt.setInt(2, cigPrice);
                    pstmt.setInt(3, winNum);
                    pstmt.setInt(4, winprice);
                    pstmt.setInt(5, beerNum);
                    pstmt.setInt(6, beerPrice);
                    pstmt.setInt(7, foodNum);
                    pstmt.setInt(8, foodPrice);
                    pstmt.setInt(9, prfuNum);
                    pstmt.setInt(10, prfuprice);
                    pstmt.setInt(11, deviceNum);
                    pstmt.setInt(12, devicePrice);
                    pstmt.setInt(13, gifNum);
                    pstmt.setInt(14, gifPrice);
                    pstmt.setInt(15, wintaxD);
                    pstmt.setInt(16, wintaxP);
                    pstmt.setString(17, Name);
                    pstmt.setString(18, EditDate2);
                    pstmt.setString(19, branchName);
                    pstmt.setInt(20, shiftNum); // قيمة id لتحديد الصف
                    pstmt.setString(21, date_search); // قيمة date لتحديد الصف

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
//                        JOptionPane.showMessageDialog(null, "تم التعديل بنجاح ");
                        showSuccessNotification();
                    } else {
                        JOptionPane.showMessageDialog(null, "لم يتم العثور على السجل المراد تعديله.");
                    }

                } catch (SQLException | NumberFormatException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);
                } finally {
                    try {
                        if (pstmt != null) {
                            pstmt.close();
                        }
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                }
                showSalesFor_data();

            }
        }


    }//GEN-LAST:event_Save_Edit2ActionPerformed

    private void SaveAsExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveAsExcelActionPerformed

        try {

            JFileChooser JFileChooser = new JFileChooser();
            JFileChooser.showSaveDialog(this);
            File saveFile = JFileChooser.getSelectedFile();
            if (saveFile != null) {
                saveFile = new File(saveFile.toString() + ".xlsx");
                Workbook wb = new XSSFWorkbook();
                Sheet sheet = wb.createSheet("اليومية");

                Row rowcol = sheet.createRow(0);

                if (tab1.getSelectedIndex() == 0) {

                    for (int i = 0; i < table.getColumnCount(); i++) {
                        Cell cell;
                        cell = rowcol.createCell(i);

                        cell.setCellValue(table.getColumnName(i));
                    }
                    for (int jj = 0; jj < table.getRowCount(); jj++) {
                        Row row = sheet.createRow(jj + 1);

                        for (int k = 0; k < table.getColumnCount(); k++) {
                            Cell cell = row.createCell(k);
                            if (table.getValueAt(jj, k) != null) {
                                cell.setCellValue(table.getValueAt(jj, k).toString());

                            }

                        }

                    }

                    try (FileOutputStream out = new FileOutputStream(new File(saveFile.toString()))) {
                        wb.write(out);
                        wb.close();
                    }

                    //                openFile(saveFile.toString());
                    JOptionPane.showMessageDialog(null, "تم الحفظ بنجاح.");
                } else {
                    for (int i = 0; i < tablefor.getColumnCount(); i++) {
                        Cell cell;
                        cell = rowcol.createCell(i);

                        cell.setCellValue(tablefor.getColumnName(i));
                    }
                    for (int jj = 0; jj < tablefor.getRowCount(); jj++) {
                        Row row = sheet.createRow(jj + 1);

                        for (int k = 0; k < tablefor.getColumnCount(); k++) {
                            Cell cell = row.createCell(k);
                            if (tablefor.getValueAt(jj, k) != null) {
                                cell.setCellValue(tablefor.getValueAt(jj, k).toString());

                            }

                        }

                    }

                    FileOutputStream out = new FileOutputStream(new File(saveFile.toString()));
                    wb.write(out);
                    wb.close();
                    out.close();

                    //                openFile(saveFile.toString());
//                    JOptionPane.showMessageDialog(null, "تم الحفظ بنجاح.");
                    showSuccessNotification();

                }
            } else {
                JOptionPane.showMessageDialog(null, "لم يتم الحفظ");

            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, ex);
        } catch (IOException io) {

            System.out.println(io);
            JOptionPane.showMessageDialog(null, io);

        }
    }//GEN-LAST:event_SaveAsExcelActionPerformed

    private void OpenExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenExcelActionPerformed

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        //        DefaultTableModel model = new DefaultTableModel();
        //        table.setModel(model);

        File excelFile;
        FileInputStream excelFIS = null;
        BufferedInputStream excelBIS = null;
        XSSFWorkbook excelImportToJTable = null;
        String defaultCurrentDirectoryPath = "C:\\Users\\a\\Desktop";
        JFileChooser excelFileChooser = new JFileChooser(defaultCurrentDirectoryPath);
        excelFileChooser.setDialogTitle("Select Excel File");
        FileNameExtensionFilter fnef = new FileNameExtensionFilter("EXCEL FILES", "xls", "xlsx", "xlsm");
        excelFileChooser.setFileFilter(fnef);
        int excelChooser = excelFileChooser.showOpenDialog(null);
        if (excelChooser == JFileChooser.APPROVE_OPTION) {
            try {
                excelFile = excelFileChooser.getSelectedFile();
                excelFIS = new FileInputStream(excelFile);
                excelBIS = new BufferedInputStream(excelFIS);
                excelImportToJTable = new XSSFWorkbook(excelBIS);
                XSSFSheet excelSheet = excelImportToJTable.getSheetAt(0);

                for (int row = 1; row <= excelSheet.getLastRowNum(); row++) {

                    XSSFRow excelRow = excelSheet.getRow(row);

                    XSSFCell Row0 = excelRow.getCell(0);
                    XSSFCell Row1 = excelRow.getCell(1);
                    XSSFCell Row2 = excelRow.getCell(2);
                    XSSFCell Row3 = excelRow.getCell(3);
                    XSSFCell Row4 = excelRow.getCell(4);
                    XSSFCell Row5 = excelRow.getCell(5);
                    XSSFCell Row6 = excelRow.getCell(6);
                    XSSFCell Row7 = excelRow.getCell(7);
                    XSSFCell Row8 = excelRow.getCell(5);
                    XSSFCell Row9 = excelRow.getCell(6);
                    XSSFCell Row10 = excelRow.getCell(7);

                    model.addRow(new Object[]{Row0, Row1, Row1, Row2, Row3, Row4, Row5, Row6, Row7, Row8, Row9, Row10});
                    System.out.println(Row0);

                }

                JOptionPane.showMessageDialog(null, "تم استيراد الملف بنجاح.");
            } catch (IOException iOException) {
                JOptionPane.showMessageDialog(null, iOException.getMessage());
            } finally {
                try {
                    if (excelFIS != null) {
                        excelFIS.close();
                    }
                    if (excelBIS != null) {
                        excelBIS.close();
                    }
                    if (excelImportToJTable != null) {
                        excelImportToJTable.close();
                    }
                } catch (IOException iOException) {
                    JOptionPane.showMessageDialog(null, iOException.getMessage());
                }
            }
        }
    }//GEN-LAST:event_OpenExcelActionPerformed

    private void Delete2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Delete2ActionPerformed

        int joption = JOptionPane.showConfirmDialog(null, " سيتم مسح البيانات نهائيا ؟", "رسالة تأكيد ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (joption == JOptionPane.YES_OPTION) {

            Connection conn = null;
            PreparedStatement ps = null;

            try {
                conn = DBConnection.getInstance().getConnection();

                if (tab1.getSelectedIndex() == 0) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "يرجى تحديد اليومية المراد حذفها من الجدول.");
                        return;
                    }

                    String branchName = String.valueOf(table.getValueAt(selectedRow, 18));
                    int shiftNum = Integer.parseInt(table.getValueAt(selectedRow, 17).toString());
                    String date_search = String.valueOf(table.getValueAt(selectedRow, 16));
                    String sqlDelete = "DELETE FROM Table1 WHERE addDate = ? AND id = ? AND branchName =?";

                    ps = conn.prepareStatement(sqlDelete);
                    ps.setString(1, date_search);
                    ps.setInt(2, shiftNum);
                    ps.setString(3, branchName);

                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
//                        JOptionPane.showMessageDialog(null, "تم حذف البيانات بنجاح.");
                        showSuccessNotification();
                    } else {
                        JOptionPane.showMessageDialog(null, "لم يتم العثور على سجل للحذف.");
                    }

                } else {

                    int selectedRow = tablefor.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "يرجى تحديد الصف المراد حذفه.");
                        return;
                    }

                    String branchName = String.valueOf(tablefor.getValueAt(selectedRow, 18));
                    int shiftNum = Integer.parseInt(tablefor.getValueAt(selectedRow, 17).toString());
                    String date_search = String.valueOf(tablefor.getValueAt(selectedRow, 16));
                    String sqlDelete = "DELETE FROM Table2 WHERE addDate = ? AND id = ? AND branchName =?";

                    ps = conn.prepareStatement(sqlDelete);
                    ps.setString(1, date_search);
                    ps.setInt(2, shiftNum);
                    ps.setString(3, branchName);

                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
//                        JOptionPane.showMessageDialog(null, "تم حذف البيانات بنجاح.");
                        showSuccessNotification();
                    } else {
                        JOptionPane.showMessageDialog(null, "لم يتم العثور على سجل للحذف.");
                    }
                }

            } catch (SQLException | NumberFormatException | HeadlessException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "حدث خطأ: " + e.getMessage());
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            showSales_data();
            showSalesFor_data();

        }

        Delete.setEnabled(false);


    }//GEN-LAST:event_Delete2ActionPerformed

    private void BillsNavigatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BillsNavigatorActionPerformed
        showScreen(CARD_BILLS);
        NavigateToBills();
    }//GEN-LAST:event_BillsNavigatorActionPerformed

    private void RefrehActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefrehActionPerformed

        if (jPanelBills.isVisible()) {
            show_data_of_count();

        } else if (jPanelReports.isVisible()) {

            showSales_data();
            showSalesFor_data();
        }


    }//GEN-LAST:event_RefrehActionPerformed

    private void ClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearActionPerformed

        clear();

    }//GEN-LAST:event_ClearActionPerformed

    private void Show_imageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Show_imageActionPerformed

        ShowPictureFrame.setConnection(con);

        String SQL = "SELECT * FROM Table3";

        Connection conn;
        Statement stm;
        ResultSet rset;

        try {

            conn = DBConnection.getInstance().getConnection();

            if (conn == null || conn.isClosed()) {
                JOptionPane.showMessageDialog(null, "فشل الاتصال بقاعدة البيانات.");
                return;
            }

            String searchN = passport.getText();

            stm = conn.createStatement();
            rset = stm.executeQuery(SQL);

            int found = 0;
//        rs.absolute(0);
            while (rset.next()) {
                if (searchN == null ? rset.getString("passport") == null : searchN.equals(rset.getString("passport"))) {

                    found = 1;
                    ShowPictureFrame.setVisible(true);
//                    ShowPictureFrame.setIconImage(img);
                    String passport1 = rset.getString("passport");

                    if (rset.getBytes("image") == null) {
                        ShowPictureFrame.labelImage.setIcon(null);
                        ShowPictureFrame.labelImage.setText("لايوجد صورة");
                        ShowPictureFrame.setTitle("عرض الصورة");

                    } else {
                        byte[] image = rset.getBytes("image");
                        ImageIcon image1 = new ImageIcon(new ImageIcon(image).getImage()
                                .getScaledInstance(ShowPictureFrame.labelImage.getWidth(), ShowPictureFrame.labelImage.getHeight(), Image.SCALE_SMOOTH));
                        ShowPictureFrame.labelImage.setText("");
                        ShowPictureFrame.labelImage.setIcon(image1);
                    }
                    ShowPictureFrame.passportLabel.setText(passport1);

                    break;
                }
            }

            if (found == 0) {
                JOptionPane.showMessageDialog(null, "لايوجد نتائج مطابقة");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "يرجى إدخال رقم صحيح لكود الصنف.", "خطأ في الإدخال", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "حدث خطأ في قاعدة البيانات: " + e.getMessage(), "خطأ SQL", JOptionPane.ERROR_MESSAGE);

        }


    }//GEN-LAST:event_Show_imageActionPerformed

    private void LogInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogInActionPerformed

        UserName = userName.getText().trim();
        String pass = new String(password.getPassword());
        String newPass = new String(Newpassword.getPassword());

        // 1. التحقق من اسم المستخدم
        if (UserName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "لم يتم إدخال اسم المستخدم", "تنبيه", JOptionPane.ERROR_MESSAGE);
            userName.requestFocus();
            return;
        }

        // 2. التحقق من كلمة المرور الحالية
        if (pass.isEmpty()) {
            JOptionPane.showMessageDialog(null, "لم يتم إدخال كلمة المرور", "تنبيه", JOptionPane.ERROR_MESSAGE);
            password.requestFocus();
            return;
        }

        if (!numericTest(pass)) {
            password.setText("");
            JOptionPane.showMessageDialog(null, "أدخل رقم صحيح بدون أحرف أو علامات", "تنبيه", JOptionPane.ERROR_MESSAGE);
            password.requestFocus();
            return;
        }

        // 3. فحص هل نحن في وضع "تغيير كلمة المرور" (بناءً على هل الحقل ظاهر أم لا)
        boolean isChangeMode = Newpassword.isVisible();

        if (isChangeMode) {
            // --- وضع تغيير كلمة المرور ---
            if (newPass.isEmpty()) {
                JOptionPane.showMessageDialog(null, "لم يتم إدخال كلمة المرور الجديدة", "تنبيه", JOptionPane.ERROR_MESSAGE);
                Newpassword.requestFocus();
                return;
            }
            if (!numericTest(newPass)) {
                Newpassword.setText("");
                JOptionPane.showMessageDialog(null, "كلمة المرور الجديدة يجب أن تكون أرقاماً صحيحة", "تنبيه", JOptionPane.ERROR_MESSAGE);
                Newpassword.requestFocus();
                return;
            }

            // أ. التحقق من صحة المستخدم والباسورد القديم أولاً
            if (authenticateUser(UserName, pass)) {
                // ب. تحديث كلمة المرور الجديدة في قاعدة البيانات
                if (updatePasswordInDatabase(UserName, newPass)) {
//                JOptionPane.showMessageDialog(null, "تم تغيير وحفظ كلمة المرور بنجاح!", "نجاح", JOptionPane.INFORMATION_MESSAGE);
                    showSuccessNotification();
                    // إخفاء حقل كلمة المرور الجديدة وإعادة تعيينه
                    executeSuccessfulLogin(Name);
                    jPanelMainContainer.requestFocusInWindow();
                } else {
                    JOptionPane.showMessageDialog(null, "حدث خطأ أثناء حفظ كلمة المرور الجديدة", "خطأ", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                password.setText("");
                JOptionPane.showMessageDialog(null, "اسم المستخدم أو كلمة المرور الحالية غير صحيحة", "خطأ", JOptionPane.ERROR_MESSAGE);
                password.requestFocus();
            }

        } else {
            // --- وضع تسجيل الدخول العادي ---
            if (authenticateUser(UserName, pass)) {
                executeSuccessfulLogin(Name);
                applyUserPermissions();
            } else {
                password.setText("");
                JOptionPane.showMessageDialog(null, "كلمة مرور غير صحيحة", "خطأ", JOptionPane.ERROR_MESSAGE);
                password.requestFocus();
            }
        }

    }//GEN-LAST:event_LogInActionPerformed

    private void ExiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExiteActionPerformed

        System.exit(0);
    }//GEN-LAST:event_ExiteActionPerformed

    private void LogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogOutActionPerformed

        performLogout();

    }//GEN-LAST:event_LogOutActionPerformed

    private void searchByDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchByDateActionPerformed

        if (RadioGetDaily.isSelected()) {
            // 1. تعريف المكونات والبيانات الأساسية
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            DefaultTableModel model2 = (DefaultTableModel) tablefor.getModel();
            String selectedBranch = ComboB_SubBranch1.getSelectedItem().toString();

            // التحقق من حالة مربعات الجنسية
            boolean isEgySelected = checkBoxGetEgyNationality.isSelected();
            boolean isForSelected = checkBoxGetForNationality.isSelected();

            // 2. التحقق من اختيار التاريخ
            if (datechooser.getDate() == null || datechooser1.getDate() == null) {
                JOptionPane.showMessageDialog(null, "يجب اختيار تاريخ البداية والنهاية أولاً.");
                showSales_data();
                showSalesFor_data();
                return;
            }

            startDate = dateFormat.format(datechooser1.getDate());
            DateFrom = startDate;
            endDate = dateFormat.format(datechooser.getDate());
            DateTo = endDate;

            if (detectedBranch.equals(selectedBranch)) {

                // إذا لم يتم اختيار أجنبي (أو تم اختيار المصري فقط / أو كلاهما معاً)
                if (!isForSelected || (isEgySelected && isForSelected)) {
                    model.setRowCount(0);
                    String query = "SELECT * FROM Table1 WHERE addDate BETWEEN ? AND ?";
                    try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
                        ps.setString(1, startDate);
                        ps.setString(2, endDate);
                        try (ResultSet rst = ps.executeQuery()) {
                            boolean found = false;
                            while (rst.next()) {
                                found = true;
                                Object[] row = new Object[20];
                                row[0] = rst.getInt("winTaxP");
                                row[1] = rst.getInt("winTaxD");
                                row[2] = rst.getInt("gifprice");
                                row[3] = rst.getInt("gifNum");
                                row[4] = rst.getInt("devicePrice");
                                row[5] = rst.getInt("deviceNum");
                                row[6] = rst.getInt("prfuprice");
                                row[7] = rst.getInt("prfuNum");
                                row[8] = rst.getInt("foodprice");
                                row[9] = rst.getInt("foodNum");
                                row[10] = rst.getInt("devicePrice");
                                row[11] = rst.getInt("beerNum");
                                row[12] = rst.getInt("winprice");
                                row[13] = rst.getInt("winNum");
                                row[14] = rst.getInt("cigPrice");
                                row[15] = rst.getInt("cigNum");
                                row[16] = rst.getString("addDate");
                                row[17] = rst.getInt("Id");
                                row[18] = rst.getString("branchName");
                                model.addRow(row);
                            }
                        }
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "خطأ: " + e.getMessage());
                    }
                } else {
                    model.setRowCount(0);
                }

                // البحث في الجدول الثاني الأساسي (الأجنبي)
                if (!isEgySelected || (isEgySelected && isForSelected)) {
                    model2.setRowCount(0);
                    String query2 = "SELECT * FROM Table2 WHERE addDate BETWEEN ? AND ?";
                    try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query2)) {
                        ps.setString(1, startDate);
                        ps.setString(2, endDate);
                        try (ResultSet rst = ps.executeQuery()) {
                            while (rst.next()) {
                                Object[] row = new Object[20];
                                row[0] = rst.getInt("winTaxP");
                                row[1] = rst.getInt("winTaxD");
                                row[2] = rst.getInt("gifprice");
                                row[3] = rst.getInt("gifNum");
                                row[4] = rst.getInt("devicePrice");
                                row[5] = rst.getInt("deviceNum");
                                row[6] = rst.getInt("prfuprice");
                                row[7] = rst.getInt("prfuNum");
                                row[8] = rst.getInt("foodprice");
                                row[9] = rst.getInt("foodNum");
                                row[10] = rst.getInt("devicePrice");
                                row[11] = rst.getInt("beerNum");
                                row[12] = rst.getInt("winprice");
                                row[13] = rst.getInt("winNum");
                                row[14] = rst.getInt("cigPrice");
                                row[15] = rst.getInt("cigNum");
                                row[16] = rst.getString("addDate");
                                row[17] = rst.getInt("Id");
                                row[18] = rst.getString("branchName");
                                model2.addRow(row);
                            }
                        }
                    } catch (SQLException e) {
                        /* معالجة الخطأ */
                    }
                } else {
                    model2.setRowCount(0);
                }

                NavigateToReportwithoutRefrsh();

            } else if (compareBranchName(selectedBranch)) {

                JOptionPane.showMessageDialog(null, "هذا الفرع موجود في قاعدة البيانات الاحتياطية، جاري عرض بياناته...");

                // الفرع الاحتياطي (المصري SubTable1)
                if (!isForSelected || (isEgySelected && isForSelected)) {
                    model.setRowCount(0);
                    String subQuery1 = "SELECT * FROM SubTable1 WHERE addDate BETWEEN ? AND ? AND branchName = ?";
                    try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(subQuery1)) {
                        ps.setString(1, startDate);
                        ps.setString(2, endDate);
                        ps.setString(3, selectedBranch);
                        try (ResultSet rst = ps.executeQuery()) {
                            while (rst.next()) {
                                Object[] row = new Object[20];
                                row[0] = rst.getInt("winTaxP");
                                row[1] = rst.getInt("winTaxD");
                                row[2] = rst.getInt("gifprice");
                                row[3] = rst.getInt("gifNum");
                                row[4] = rst.getInt("devicePrice");
                                row[5] = rst.getInt("deviceNum");
                                row[6] = rst.getInt("prfuprice");
                                row[7] = rst.getInt("prfuNum");
                                row[8] = rst.getInt("foodprice");
                                row[9] = rst.getInt("foodNum");
                                row[10] = rst.getInt("devicePrice");
                                row[11] = rst.getInt("beerNum");
                                row[12] = rst.getInt("winprice");
                                row[13] = rst.getInt("winNum");
                                row[14] = rst.getInt("cigPrice");
                                row[15] = rst.getInt("cigNum");
                                row[16] = rst.getString("addDate");
                                row[17] = rst.getInt("Id");
                                row[18] = rst.getString("branchName");
                                model.addRow(row);
                            }
                        }
                    } catch (SQLException e) {
                        /* معالجة الخطأ */
                    }
                } else {
                    model.setRowCount(0);
                }

                // الفرع الاحتياطي (الأجنبي SubTable2)
                if (!isEgySelected || (isEgySelected && isForSelected)) {
                    model2.setRowCount(0);
                    String subQuery2 = "SELECT * FROM SubTable2 WHERE addDate BETWEEN ? AND ? AND branchName = ?";
                    try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(subQuery2)) {
                        ps.setString(1, startDate);
                        ps.setString(2, endDate);
                        ps.setString(3, selectedBranch);
                        try (ResultSet rst = ps.executeQuery()) {
                            while (rst.next()) {
                                Object[] row = new Object[20];
                                row[0] = rst.getInt("winTaxP");
                                row[1] = rst.getInt("winTaxD");
                                row[2] = rst.getInt("gifprice");
                                row[3] = rst.getInt("gifNum");
                                row[4] = rst.getInt("devicePrice");
                                row[5] = rst.getInt("deviceNum");
                                row[6] = rst.getInt("prfuprice");
                                row[7] = rst.getInt("prfuNum");
                                row[8] = rst.getInt("foodprice");
                                row[9] = rst.getInt("foodNum");
                                row[10] = rst.getInt("devicePrice");
                                row[11] = rst.getInt("beerNum");
                                row[12] = rst.getInt("winprice");
                                row[13] = rst.getInt("winNum");
                                row[14] = rst.getInt("cigPrice");
                                row[15] = rst.getInt("cigNum");
                                row[16] = rst.getString("addDate");
                                row[17] = rst.getInt("Id");
                                row[18] = rst.getString("branchName");
                                model2.addRow(row);
                            }
                        }
                    } catch (SQLException e) {
                        /* معالجة الخطأ */
                    }
                } else {
                    model2.setRowCount(0);
                }

                NavigateToReportwithoutRefrsh();
            } else {
                int confirm = JOptionPane.showConfirmDialog(null,
                        "هذا فرع خارجي وبياناته ليست موجودة حالياً. هل تريد تنزيل البيانات من السحابة؟",
                        "تنبيه: فرع خارجي", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    DownloadData();
                    JOptionPane.showMessageDialog(null, "بدأت عملية التنزيل، يرجى الانتظار ثم إعادة البحث.");
                }
            }

        } else if (RadioGetTax.isSelected()) {

            DefaultTableModel model = (DefaultTableModel) TaxQueryResultFrame.mainTaxTable.getModel();
            model.setRowCount(0);
            String query = "";
            String startDate, endDate;
            String selectedBranch = ComboB_SubBranch1.getSelectedItem().toString();

            // التحقق من حالة مربعات الجنسية
            boolean isEgySelected = checkBoxGetEgyNationality.isSelected();
            boolean isForSelected = checkBoxGetForNationality.isSelected();

            // 1. التحقق من اختيار التواريخ
            if (datechooser.getDate() == null || datechooser1.getDate() == null) {
                JOptionPane.showMessageDialog(null, "يجب اختيار تاريخ البداية والنهاية أولاً.");
                showSales_data();
                showSalesFor_data();
                return;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            startDate = dateFormat.format(datechooser1.getDate());
            endDate = dateFormat.format(datechooser.getDate());

            // 2. تحديد مصدر البيانات (السيناريو الثلاثي مع مراعاة الجنسية)
            boolean isLocal = detectedBranch.equals(selectedBranch);
            boolean isBackup = compareBranchName(selectedBranch);

            if (isLocal) {
                if (isEgySelected && !isForSelected) {
                    query = "SELECT * FROM Table1 WHERE addDate BETWEEN ? AND ?"; // المصري فقط
                } else if (isForSelected && !isEgySelected) {
                    query = "SELECT * FROM Table2 WHERE addDate BETWEEN ? AND ?"; // الأجنبي فقط
                } else {
                    query = "SELECT * FROM Table2 WHERE addDate BETWEEN ? AND ?"; // افتراضي أو كلاهما
                }
            } else if (isBackup) {
                if (isEgySelected && !isForSelected) {
                    query = "SELECT * FROM SubTable1 WHERE addDate BETWEEN ? AND ? AND branchName = ?";
                } else if (isForSelected && !isEgySelected) {
                    query = "SELECT * FROM SubTable2 WHERE addDate BETWEEN ? AND ? AND branchName = ?";
                } else {
                    query = "SELECT * FROM SubTable2 WHERE addDate BETWEEN ? AND ? AND branchName = ?";
                }
            } else {
                int confirm = JOptionPane.showConfirmDialog(null,
                        "بيانات هذا الفرع الخارجي غير موجودة حالياً. هل تريد تنزيلها من السحابة؟",
                        "تنزيل بيانات", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    DownloadData();
                    return;
                } else {
                    return;
                }
            }

            // 3. تنفيذ الاستعلام وجلب البيانات
            try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

                ps.setString(1, startDate);
                ps.setString(2, endDate);

                if (!isLocal && isBackup) {
                    ps.setString(3, selectedBranch);
                }

                try (ResultSet rst = ps.executeQuery()) {
                    boolean found = false;
                    int totalDollar = 0, totalPound = 0, totalBottels = 0;

                    while (rst.next()) {
                        found = true;
                        int winNum = rst.getInt("winNum") + rst.getInt("beerNum");
                        int winTaxP = rst.getInt("winTaxP");
                        int winTaxD = rst.getInt("winTaxD");

                        totalBottels += winNum;
                        totalPound += winTaxP;
                        totalDollar += winTaxD;

                        Object[] row = new Object[8];
                        row[0] = rst.getString("users");
                        row[1] = winNum;
                        row[2] = winTaxP;
                        row[3] = winTaxD;
                        row[4] = rst.getInt("id");
                        row[5] = rst.getString("addDate");
                        row[6] = rst.getString("branchName");
                        model.addRow(row);
                    }

                    if (found) {
                        Object[] totalRow = new Object[8];
                        totalRow[0] = "";
                        totalRow[1] = totalBottels;
                        totalRow[2] = totalPound;
                        totalRow[3] = totalDollar;
                        totalRow[4] = "";
                        totalRow[5] = "";
                        totalRow[6] = "الإجمالي الكلي";

                        model.addRow(totalRow);

                        TaxQueryResultFrame.setVisible(true);
                        TaxQueryResultFrame.setIconImage(img);

                        TaxQueryResultFrame.Total.setText("الإجمالى : " + totalDollar + " $" + " | " + totalPound + " ج" + " | " + totalBottels + " زجاجة");

                    } else {
                        JOptionPane.showMessageDialog(null, "لا يوجد نتائج مطابقة لهذه الفترة بالشروط المحددة.");
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "حدث خطأ في قاعدة البيانات: " + e.getMessage());
            }

        } else if (RadioGetReport.isSelected()) {

            if (datechooser.getDate() == null || datechooser1.getDate() == null) {
                JOptionPane.showMessageDialog(null, "يجب اختيار تاريخ البداية والنهاية أولاً.");
                return;
            }

            startDate = dateFormat.format(datechooser1.getDate());
            DateFrom = startDate;
            endDate = dateFormat.format(datechooser.getDate());
            DateTo = endDate;

            // تخزين حالة اختيار الجنسية في متغيرات عامة (Static) لتتمكن نافذة ReportsFrame من قراءتها وتطبيقها
            MainFrame.isEgyNationalitySelected = checkBoxGetEgyNationality.isSelected();
            MainFrame.isForNationalitySelected = checkBoxGetForNationality.isSelected();

            ReportsFrame ReportsFrame = new ReportsFrame();
            ReportsFrame.setVisible(true);
            ReportsFrame.setIconImage(img);
        }
    }//GEN-LAST:event_searchByDateActionPerformed

    private void TaxNavigatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TaxNavigatorActionPerformed
        showScreen(CARD_TAXS);
        NavigateToTax();

    }//GEN-LAST:event_TaxNavigatorActionPerformed

    private void GetTaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GetTaxActionPerformed

        saveOperation.setEnabled(true);
        Save_Edit.setEnabled(false);

        GetTaxQuary();


    }//GEN-LAST:event_GetTaxActionPerformed

    private void uploadDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadDataActionPerformed

        if (RadioGetOnlineDaily.isSelected()) {
           

            // 1. جلب الفرع المختار
            String selectedBranch = ComboB_SubBranch1.getSelectedItem().toString();

            if (selectedBranch.equals("اختر الفرع")) {
                JOptionPane.showMessageDialog(null, "يرجى اختيار الفرع.");
                return;
            }

            // 2. رسالة تحذير واضحة وصارمة قبل الرفع
            int confirm = JOptionPane.showConfirmDialog(null,
                    "تحذير هام جداً: سيتم حذف كافة البيانات الحالية الموجودة على الشبكة (السحابة) لفرع [" + selectedBranch + "] ووضع بيانات الجهاز المحلي بدلاً منها!\nهل تريد الاستمرار؟",
                    "تأكيد رفع واستبدال البيانات",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            new Thread(() -> {
                try {
                    // 3. التحقق من وجود اتصال بالإنترنت قبل البدء
                    if (!isInternetAvailable()) {
                        SwingUtilities.invokeLater(() -> {
                            progBar.setVisible(false);
                            upload_downLabel.setVisible(false);
                            JOptionPane.showMessageDialog(null,
                                    "فشل الاتصال بالإنترنت! يرجى التحقق من الشبكة ثم إعادة المحاولة.",
                                    "خطأ اتصال",
                                    JOptionPane.ERROR_MESSAGE);
                        });
                        return;
                    }

                    // 🚀 إظهار الـ Dialog الذي يحتوي على الـ progressBar
                    SwingUtilities.invokeLater(() -> {
                        progBar.setValue(0);
                        progBar.setVisible(true);
                        upload_downLabel.setVisible(true);
                        java.net.URL downloadImgUrl = getClass().getResource("/arrow_upload_progress_30dp_F19E39_FILL0_wght400_GRAD0_opsz24.png");

                        if (downloadImgUrl != null) {
                            upload_downLabel.setIcon(new ImageIcon(downloadImgUrl));
                            upload_downLabel.setText("");
                        } else {
                            upload_downLabel.setText("❌");
                            System.out.println("تحذير: لم يتم العثور على ملف الأيقونة في المسار المحدد!");
                        }
                    });

                    // 4. تحديث التقدم قبل البدء
                    updateProgress(10);

                    // رفع الجدول الأول مباشرة (إذا حدث خطأ فستقوم الدالة برمي استثناء ويتم الانتقال إلى catch تلقائياً)
                    DataSyncManager.syncTableToGoogle("Table1", this.idTable1, "Sheet1");
                    updateProgress(50); // وصلنا للمنتصف

                    // رفع الجدول الثاني مباشرة
                    DataSyncManager.syncTableToGoogle("Table2", this.idTable2, "Sheet1");
                    updateProgress(90); // قاربنا على الانتهاء

                    // إتمام العملية بنجاح تام
                    updateProgress(100);

                    // 5. إغلاق المؤشر وإظهار رسالة النجاح الحقيقية
                    SwingUtilities.invokeLater(() -> {
                        progBar.setVisible(false);
                        upload_downLabel.setVisible(false);
                        JOptionPane.showMessageDialog(null, "تمت عملية الرفع والمزامنة بنجاح.");
                    });

                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> {
                        progBar.setVisible(false);
                        upload_downLabel.setVisible(false);
                    });
                    e.printStackTrace();
                    // إظهار رسالة خطأ حقيقية ومنع ظهور رسالة النجاح الوهمية عند انقطاع الإنترنت
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "حدث خطأ أثناء الرفع أو انقطع الاتصال: " + e.getMessage(), "فشل المزامنة", JOptionPane.ERROR_MESSAGE);
                    });
                }
            }).start();

        } else if (RadioGetOnlineItems.isSelected()) {

           

            // 1. نافذة تأكيد قبل بدء الرفع
            int confirm = JOptionPane.showConfirmDialog(null,
                    "تأكيد رفع وتحديث بيانات الأصناف على الشبكة؟",
                    "تأكيد رفع الأصناف",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // 2. تشغيل عملية الرفع في Thread مستقل لعدم تجميد الشاشة
            new Thread(() -> {
                try {
                    // إظهار وإعداد شريط التقدم والأيقونة
                    SwingUtilities.invokeLater(() -> {
                        progBar.setVisible(true);
                        upload_downLabel.setVisible(true);
                        java.net.URL uploadImgUrl = getClass().getResource("/arrow_upload_progress_30dp_F19E39_FILL0_wght400_GRAD0_opsz24.png");

                        if (uploadImgUrl != null) {
                            upload_downLabel.setIcon(new ImageIcon(uploadImgUrl));
                            upload_downLabel.setText(""); // إزالة النص لظهر الأيقونة وحدها
                        } else {
                            upload_downLabel.setText("❌");
                            System.out.println("تحذير: لم يتم العثور على ملف الأيقونة في المسار المحدد!");
                        } // <--- إغلاق قوس الـ else بشكل صحيح

                        progBar.setValue(0);
                        progBar.setStringPainted(true);
                        progBar.setString("جاري بدء الاتصال لرفع بيانات الأصناف...");
                    });

                    String spreadsheetId = "1SgIdRVtqMdTeixRlRMdfqZq7_am39jhkPWMMBflAHWU"; // معرف شيت الأصناف
                    String googleSheetName = "Items";

                    // تحديث التقدم (30%)
                    updateProg(30, "جاري قراءة الأصناف من قاعدة البيانات المحلية...");

                    // استدعاء دالة الرفع من كلاس DataSyncManager بالترتيب الصحيح للأعمدة
                    DataSyncManager.syncItemsToGoogle(spreadsheetId, googleSheetName);

                    // تحديث التقدم عند الانتهاء (100%)
                    updateProg(100, "تم رفع الأصناف بنجاح!");

                    Thread.sleep(500); // مهلة قصيرة لرؤية الاكتمال

                    // إخفاء شريط التقدم وإظهار رسالة النجاح
                    SwingUtilities.invokeLater(() -> {
                        progBar.setVisible(false);
                        upload_downLabel.setVisible(false);
                        JOptionPane.showMessageDialog(null, "تمت عملية الرفع والمزامنة بنجاح.", "نجاح الرفع", JOptionPane.INFORMATION_MESSAGE);
                    });

                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        progBar.setVisible(false);
                        upload_downLabel.setVisible(false);
                    });
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "فشلت عملية رفع الأصناف: " + ex.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
                }
            }).start();
        }
    }

    private void updateProgress(int value) {
        SwingUtilities.invokeLater(() -> progBar.setValue(value));
    }//GEN-LAST:event_uploadDataActionPerformed

    private void ConnectionTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnectionTestActionPerformed

        // نضع الكود في Thread لكي لا تتجمد الواجهة أثناء انتظار رد جوجل
        new Thread(() -> {
            // ضع هنا أي ID من ملفاتك لتجربته
            String testId = "1tgX_nyTexoWL4IpMrNchRVxxAoUUTkV-wC8NogzLT-w";
            testGoogleConnection(testId);
        }).start();


    }//GEN-LAST:event_ConnectionTestActionPerformed

    private void downloadDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadDataActionPerformed

        if (RadioGetOnlineDaily.isSelected()) {
            
            DownloadData();
        } else if (RadioGetOnlineItems.isSelected()) {
           
            new Thread(() -> {
                try {
                    // إظهار وإعداد شريط التقدم والأيقونة بالطريقة القديمة
                    SwingUtilities.invokeLater(() -> {
                        progBar.setVisible(true);
                        upload_downLabel.setVisible(true);
                        java.net.URL downloadImgUrl = getClass().getResource("/downloading_30dp_F19E39_FILL0_wght400_GRAD0_opsz24.png");

                        if (downloadImgUrl != null) {
                            upload_downLabel.setIcon(new ImageIcon(downloadImgUrl));
                            upload_downLabel.setText(""); // إزالة النص إن وجد لضمان ظهور الأيقونة وحدها
                        } else {
                            upload_downLabel.setText("❌");
                            System.out.println("تحذير: لم يتم العثور على ملف الأيقونة في المسار المحدد!");
                        } // <--- تم إغلاق قوس الـ else بشكل صحيح هنا

                        progBar.setValue(0);
                        progBar.setStringPainted(true);
                        progBar.setString("جاري بدء مزامنة الأصناف...");
                    });

                    String spreadsheetId = "1SgIdRVtqMdTeixRlRMdfqZq7_am39jhkPWMMBflAHWU";
                    String googleSheetName = "Items";

                    // تحديث التقدم بالطريقة القديمة (مثلاً 30%)
                    updateProg(30, "جاري قراءة وتحميل الأصناف من السحابة...");

                    // استدعاء دالة المزامنة بالترتيب الجديد
                    DataSyncManager.syncItemsFromGoogleToAccess(spreadsheetId, googleSheetName);

                    // تحديث التقدم عند الانتهاء (100%)
                    updateProg(100, "تمت مزامنة وتحديث الأصناف بنجاح!");

                    Thread.sleep(500);

                    SwingUtilities.invokeLater(() -> {
                        progBar.setVisible(false);
                        upload_downLabel.setVisible(false);
                        JOptionPane.showMessageDialog(this, "تمت مزامنة وتحديث بيانات الأصناف!", "نجاح المزامنة", JOptionPane.INFORMATION_MESSAGE);
                    });

                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        progBar.setVisible(false);
                        upload_downLabel.setVisible(false);
                    });
                    JOptionPane.showMessageDialog(this, "فشلت عملية مزامنة بيانات الأصناف: " + ex.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
                }
            }).start();
        }
    }//GEN-LAST:event_downloadDataActionPerformed

    private void ComboB_SubBranch1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboB_SubBranch1ActionPerformed
        handleBranchSelection();

    }//GEN-LAST:event_ComboB_SubBranch1ActionPerformed

    private void outputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_outputActionPerformed

    private void ReportsNavigatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReportsNavigatorActionPerformed

        showScreen(CARD_REPORTS);
        NavigateToReport();

    }//GEN-LAST:event_ReportsNavigatorActionPerformed

    private void SettingsNavigator1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SettingsNavigator1ActionPerformed
//        showScreen(CARD_SETTINGS);
        NavigateToSetting();

    }//GEN-LAST:event_SettingsNavigator1ActionPerformed

    private void ComboB_type1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboB_type1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ComboB_type1ActionPerformed

    private void EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditActionPerformed
        save_Edit();
    }//GEN-LAST:event_EditActionPerformed

    private void view_Shift_Sales1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_view_Shift_Sales1ActionPerformed

        DefaultTableModel model = (DefaultTableModel) JFrameDailySales.EgyTable.getModel();
        DefaultTableModel model2 = (DefaultTableModel) JFrameDailySales.ForTabel.getModel();

        String sql = "SELECT DISTINCT id FROM Table3 ";

        model.setRowCount(0);
        model2.setRowCount(0);

        Object[] row = new Object[13];

        if (datechooser2.getDate() == null) {
            JOptionPane.showMessageDialog(null, " يجب اختيار التاريخ أولا.");

        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String addDate = dateFormat.format(datechooser2.getDate());

            for (int i = 0; i < tablecount1.getRowCount(); i++) {

                try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {

                    ResultSet rs = pstmt.executeQuery(sql);

                    String searchid6 = "مصرى";
                    String searchDate = addDate;
                    String searchid5 = "سجائر";
                    String searchid4 = "مشروبات كحولية";
                    String searchid3 = "مأكولات";
                    String searchid2 = "عطور";
                    String searchid1 = "هدايا";
                    int found = 0;
                    rs.absolute(0);
                    while (rs.next()) {

                        if (searchDate.equals(rs.getString("addDate")) && searchid6.equals(rs.getString("nation")) && searchid1.equals(rs.getString("type"))) {
                            found = 1;

                            int output1 = rs.getInt("output");
                            int price1 = rs.getInt("price");

                            row[0] = price1;
                            row[1] = output1;
                            row[2] = 0;
                            row[3] = 0;
                            row[4] = 0;
                            row[5] = 0;
                            row[6] = 0;
                            row[7] = 0;
                            row[8] = 0;
                            row[9] = 0;
                            model.addRow(row);

                        }

                        if (searchDate.equals(rs.getString("addDate")) && searchid6.equals(rs.getString("nation")) && searchid2.equals(rs.getString("type"))) {

                            found = 1;

                            int output1 = rs.getInt("output");

                            int price1 = rs.getInt("price");

                            row[0] = 0;
                            row[1] = 0;
                            row[2] = price1;
                            row[3] = output1;
                            row[4] = 0;
                            row[5] = 0;
                            row[6] = 0;
                            row[7] = 0;
                            row[8] = 0;
                            row[9] = 0;

                            model.addRow(row);

                        }
                        if (searchDate.equals(rs.getString("addDate")) && searchid6.equals(rs.getString("nation")) && searchid3.equals(rs.getString("type"))) {
                            found = 1;

                            int output1 = rs.getInt("output");

                            int price1 = rs.getInt("price");

                            row[0] = 0;
                            row[1] = 0;
                            row[2] = 0;
                            row[3] = 0;
                            row[4] = price1;
                            row[5] = output1;
                            row[6] = 0;
                            row[7] = 0;
                            row[8] = 0;
                            row[9] = 0;

                            model.addRow(row);

                        }
                        if (searchDate.equals(rs.getString("addDate")) && searchid6.equals(rs.getString("nation")) && searchid4.equals(rs.getString("type"))) {
                            found = 1;

                            int output1 = rs.getInt("output");
                            int price1 = rs.getInt("price");
                            row[0] = 0;
                            row[1] = 0;
                            row[2] = 0;
                            row[3] = 0;
                            row[4] = 0;
                            row[5] = 0;
                            row[6] = price1;
                            row[7] = output1;
                            row[8] = 0;
                            row[9] = 0;

                            model.addRow(row);

                        }
                        if (searchDate.equals(rs.getString("addDate")) && searchid6.equals(rs.getString("nation")) && searchid5.equals(rs.getString("type"))) {
                            found = 1;

                            int output1 = rs.getInt("output");
                            int price1 = rs.getInt("price");

                            row[0] = 0;
                            row[1] = 0;
                            row[2] = 0;
                            row[3] = 0;
                            row[4] = 0;
                            row[5] = 0;
                            row[6] = 0;
                            row[7] = 0;
                            row[8] = price1;
                            row[9] = output1;

                            model.addRow(row);

                        }
//                        jTabbedPane1.setSelectedIndex(1);
                        JFrameDailySales.setVisible(true);
                    }

                    if (found == 0) {

                        JOptionPane.showMessageDialog(null, "لايوجد نتائج مطابقة بالنسبة للمصرى.");
                        JFrameDailySales.setVisible(false);
                    }

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                break;

            }

            for (int i2 = 0; i2 < tablecount1.getRowCount(); i2++) {

                try {

                    String searchid7 = "أجنبى";
                    String searchDate = addDate;
                    String searchid6 = "سجائر";
                    String searchid5 = "مشروبات كحولية";
                    String searchid4 = "مأكولات";
                    String searchid3 = "عطور";
                    String searchid2 = "هدايا";
                    String searchid1 = "winTaxP";
                    int found = 0;
                    rs.absolute(0);
                    while (rs.next()) {

                        if (searchDate.equals(rs.getString("addDate")) && searchid7.equals(rs.getString("nation")) && searchid1.equals(rs.getString("winTaxD"))) {
                            found = 1;

                            int output1 = rs.getInt("winTaxD");

                            int price1 = rs.getInt("price");

                            row[0] = 0;
                            row[1] = 0;
                            row[2] = 0;
                            row[3] = 0;
                            row[4] = 0;
                            row[5] = 0;
                            row[6] = 0;
                            row[7] = 0;
                            row[8] = 0;
                            row[9] = 0;
                            row[10] = output1;
                            row[11] = 0;
                            model2.addRow(row);

                        }

                        if (searchDate.equals(rs.getString("addDate")) && searchid7.equals(rs.getString("nation")) && searchid2.equals(rs.getString("type"))) {

                            found = 1;

                            int output1 = rs.getInt("output");

                            int price1 = rs.getInt("price");

                            row[0] = 0;
                            row[1] = 0;
                            row[2] = price1;
                            row[3] = output1;
                            row[4] = 0;
                            row[5] = 0;
                            row[6] = 0;
                            row[7] = 0;
                            row[8] = 0;
                            row[9] = 0;
                            row[10] = 0;
                            row[11] = 0;
                            model2.addRow(row);

                        }
                        if (searchDate.equals(rs.getString("addDate")) && searchid7.equals(rs.getString("nation")) && searchid3.equals(rs.getString("type"))) {
                            found = 1;

                            int output1 = rs.getInt("output");

                            int price1 = rs.getInt("price");

                            row[0] = 0;
                            row[1] = 0;
                            row[2] = 0;
                            row[3] = 0;
                            row[4] = price1;
                            row[5] = output1;
                            row[6] = 0;
                            row[7] = 0;
                            row[8] = 0;
                            row[9] = 0;
                            row[10] = 0;
                            row[11] = 0;
                            model2.addRow(row);

                        }
                        if (searchDate.equals(rs.getString("addDate")) && searchid7.equals(rs.getString("nation")) && searchid4.equals(rs.getString("type"))) {
                            found = 1;

                            int output1 = rs.getInt("output");

                            int price1 = rs.getInt("price");
                            row[0] = 0;
                            row[1] = 0;
                            row[2] = 0;
                            row[3] = 0;
                            row[4] = 0;
                            row[5] = 0;
                            row[6] = price1;
                            row[7] = output1;
                            row[8] = 0;
                            row[9] = 0;
                            row[10] = 0;
                            row[11] = 0;

                            model2.addRow(row);

                        }
                        if (searchDate.equals(rs.getString("addDate")) && searchid7.equals(rs.getString("nation")) && searchid5.equals(rs.getString("type"))) {
                            found = 1;

                            int output1 = rs.getInt("output");

                            int price1 = rs.getInt("price");

                            row[0] = 0;
                            row[1] = 0;
                            row[2] = 0;
                            row[3] = 0;
                            row[4] = 0;
                            row[5] = 0;
                            row[6] = 0;
                            row[7] = 0;
                            row[8] = price1;
                            row[9] = output1;
                            row[10] = 0;
                            row[11] = 0;

                            model2.addRow(row);

                        }
                        if (searchDate.equals(rs.getString("addDate")) && searchid7.equals(rs.getString("nation")) && searchid6.equals(rs.getString("type"))) {
                            found = 1;

                            int output1 = rs.getInt("output");

                            int price1 = rs.getInt("price");

                            row[0] = 0;
                            row[1] = 0;
                            row[2] = 0;
                            row[3] = 0;
                            row[4] = 0;
                            row[5] = 0;
                            row[6] = 0;
                            row[7] = 0;
                            row[8] = 0;
                            row[9] = 0;
                            row[10] = price1;
                            row[11] = output1;

                            model2.addRow(row);

                        }
//                        jTabbedPane1.setSelectedIndex(1);
                    }

                    if (found == 0) {

                        JOptionPane.showMessageDialog(null, "لايوجد نتائج مطابقة بالنسبة للأجنبى.");
                    }

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
                break;

            }

        }

        sum_Tables_Col(JFrameDailySales.EgyTable);
        sum_Tables_Col2(JFrameDailySales.ForTabel);

    }//GEN-LAST:event_view_Shift_Sales1ActionPerformed

    private void current_Balance1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_current_Balance1ActionPerformed

        int id_col = 0;
        String addDate1 = "";
        int input1 = 0;
        int output1 = 0;
        int count2 = 0;
        int price1 = 0;
        int unitprice = 0;
        String type1 = "";
        String notes1 = "";

        DefaultTableModel model = (DefaultTableModel) JFrameCodeSearch.tablecount2.getModel();
        model.setRowCount(0);
        Object[] row = new Object[12];
        ArrayList<Integer> idList = new ArrayList<Integer>();
        String searchword = ComboB_type.getSelectedItem().toString();
        String sql = "SELECT DISTINCT id FROM Table3 ";

        try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {

            rs = pstmt.executeQuery();
//            rs.absolute(0);
            while (rs.next()) {
                idList.add(rs.getInt("id"));
            }

        } catch (SQLException | NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);
        }
//System.out.println(idList);
        for (int i = 0; i < idList.size(); i++) {

            try (Connection con = DBConnection.getInstance().getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {

                rs = pstmt.executeQuery();
                int found = 0;

//                rs.absolute(0);
                while (rs.next()) {

                    if (idList.get(i) == rs.getInt("id")) {

                        found = 1;
                        id_col = rs.getInt("id");
                        addDate1 = rs.getString("addDate");
                        input1 = rs.getInt("input");
                        output1 = rs.getInt("output");
                        count2 = rs.getInt("count");
                        price1 = rs.getInt("price");
                        unitprice = rs.getInt("unitPrice");
                        type1 = rs.getString("type");
                        notes1 = rs.getString("notes");
                    }
                }
                row[0] = notes1;
                row[1] = type1;
                row[2] = unitprice;
                row[3] = price1;
                row[4] = count2;
                row[5] = output1;
                row[6] = input1;
                row[7] = addDate1;
                row[8] = id_col;

                model.addRow(row);

                if (found == 0) {

                    JOptionPane.showMessageDialog(null, "لايوجد نتائج مطابقة");
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }

        }

        JFrameCodeSearch.setVisible(true);

//        int id_col = 0;
//        String addDate1 = "";
//        int input1 = 0;
//        int output1 = 0;
//        int count2 = 0;
//        int price1 = 0;
//        int unitprice = 0;
//        String type1 = "";
//        String notes1 = "";
////        ResultSet rs;
////         PreparedStatement pstmt;
//
//
//
//
//DefaultTableModel model = (DefaultTableModel) JFrameCodeSearch.tablecount2.getModel();
//        model.setRowCount(0);
//        Object[] row = new Object[12];
//        ArrayList<Integer> idList = new ArrayList<>();
//        String searchword = ComboB_type.getSelectedItem().toString();
//        String sql = "SELECT DISTINCT id, addDate, input, output, count, price, unitPrice, type, notes FROM Table3";
//
//        // الحلقة الأولى: لاسترداد معرفات "id"
//        try (Connection con = DBConnection.getInstance().getConnection();
//             PreparedStatement pstmt = con.prepareStatement(sql)) {
//
//            rs = pstmt.executeQuery();
//            while (rs.next()) {
//                idList.add(rs.getInt("id"));
//            }
//
//        } catch (SQLException | NumberFormatException | HeadlessException e) {
//            JOptionPane.showMessageDialog(null, e);
//        }
//
//        // الحلقة الثانية: لمعالجة البيانات بناءً على معرفات "id"
//        for (int i = 0; i < idList.size(); i++) {
//            try (Connection con = DBConnection.getInstance().getConnection();
//                 PreparedStatement pstmt = con.prepareStatement(sql)) {
//                
//                rs = pstmt.executeQuery();
//                int found = 0;
//                
//                while (rs.next()) {
//                    if (idList.get(i).equals(rs.getInt("id"))) {
//                        found = 1;
//                        id_col = rs.getInt("id");
//                        addDate1 = rs.getString("addDate");
//                        input1 = rs.getInt("input");
//                        output1 = rs.getInt("output");
//                        count2 = rs.getInt("count");
//                        price1 = rs.getInt("price");
//                        unitprice = rs.getInt("unitPrice");
//                        type1 = rs.getString("type");
//                        notes1 = rs.getString("notes");
//                        
//                        // إضافة الصف إلى النموذج هنا
//                        row[0] = notes1;
//                        row[1] = type1;
//                        row[2] = unitprice;
//                        row[3] = price1;
//                        row[4] = count2;
//                        row[5] = output1;
//                        row[6] = input1;
//                        row[7] = addDate1;
//                        row[8] = id_col;
//                        model.addRow(row);
//                        
//                        break; // الخروج من الحلقة الداخلية بمجرد العثور على العنصر
//                    }
//                }
//
//                if (found == 0) {
//                    JOptionPane.showMessageDialog(null, "لا يوجد نتائج مطابقة للـ ID: " + idList.get(i));
//                }
//
//            } catch (SQLException e) {
//                JOptionPane.showMessageDialog(null, e);
//            }
//        }
//        JFrameCodeSearch.setVisible(true);
//

    }//GEN-LAST:event_current_Balance1ActionPerformed

    private void DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteActionPerformed
        int joption = JOptionPane.showConfirmDialog(null,
                "تحذير: سيتم مسح السجلين (المصري والأجنبي) المرتبطين بهذه العملية نهائياً!\nهل تريد الاستمرار؟",
                "رسالة تأكيد الحذف الشامل",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (joption == JOptionPane.YES_OPTION) {

            Connection conn = null;
            PreparedStatement ps1 = null;
            PreparedStatement ps2 = null;

            try {
                conn = DBConnection.getInstance().getConnection();

                String branchName = "";
                int shiftNum = 0;
                String date_search = "";

                // التأكد من الجدول النشط حالياً لجلب البيانات المشتركة (المصري أو الأجنبي)
                if (tab1.getSelectedIndex() == 0) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "يرجى تحديد اليومية المراد حذفها من الجدول.");
                        return;
                    }
                    branchName = String.valueOf(table.getValueAt(selectedRow, 18));
                    shiftNum = Integer.parseInt(table.getValueAt(selectedRow, 17).toString());
                    date_search = String.valueOf(table.getValueAt(selectedRow, 16));
                } else {
                    int selectedRow = tablefor.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(null, "يرجى تحديد الصف المراد حذفه.");
                        return;
                    }
                    branchName = String.valueOf(tablefor.getValueAt(selectedRow, 18));
                    shiftNum = Integer.parseInt(tablefor.getValueAt(selectedRow, 17).toString());
                    date_search = String.valueOf(tablefor.getValueAt(selectedRow, 16));
                }

                // استعلامات الحذف للجدولين معاً (Table1 و Table2)
                String sqlDeleteTable1 = "DELETE FROM Table1 WHERE addDate = ? AND id = ? AND branchName = ?";
                String sqlDeleteTable2 = "DELETE FROM Table2 WHERE addDate = ? AND id = ? AND branchName = ?";

                // استخدام المعاملات (Transactions) لضمان تنفيذ الحذف في الجدولين معاً أو التراجع في حال حدوث خطأ
                conn.setAutoCommit(false);

                ps1 = conn.prepareStatement(sqlDeleteTable1);
                ps1.setString(1, date_search);
                ps1.setInt(2, shiftNum);
                ps1.setString(3, branchName);
                int rows1 = ps1.executeUpdate();

                ps2 = conn.prepareStatement(sqlDeleteTable2);
                ps2.setString(1, date_search);
                ps2.setInt(2, shiftNum);
                ps2.setString(3, branchName);
                int rows2 = ps2.executeUpdate();

                // تأكيد الحذف في قاعدة البيانات
                conn.commit();

                if (rows1 > 0 || rows2 > 0) {
                    showSuccessNotification();
                } else {
                    JOptionPane.showMessageDialog(null, "لم يتم العثور على سجلات مطابقة للحذف.");
                }

            } catch (SQLException | NumberFormatException | HeadlessException e) {
                if (conn != null) {
                    try {
                        conn.rollback(); // التراجع عن التغييرات في حال حدوث خطأ
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "حدث خطأ أثناء الحذف: " + e.getMessage());
            } finally {
                try {
                    if (ps1 != null) {
                        ps1.close();
                    }
                    if (ps2 != null) {
                        ps2.close();
                    }
                    if (conn != null) {
                        conn.setAutoCommit(true);
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            // تحديث بيانات الجداول بعد الحذف
            showSales_data();
            showSalesFor_data();
        }

        Delete.setEnabled(false);
    }//GEN-LAST:event_DeleteActionPerformed

    private void Edit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Edit2ActionPerformed

        jDialogDaily.setVisible(true);// جعل النافذة تتكيف مع محتوياتها، وتظهر في المنتصف، ثم إظهارها
        jDialogDaily.pack();
        jDialogDaily.setLocationRelativeTo(this);
        jDialogDaily.setVisible(true);


    }//GEN-LAST:event_Edit2ActionPerformed

    private void SaveAsExcel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveAsExcel1ActionPerformed
        try {

            JFileChooser JFileChooser = new JFileChooser();
            JFileChooser.showSaveDialog(this);
            File saveFile = JFileChooser.getSelectedFile();
            if (saveFile != null) {
                saveFile = new File(saveFile.toString() + ".xlsx");
                Workbook wb = new XSSFWorkbook();
                Sheet sheet = wb.createSheet("اليومية");

                Row rowcol = sheet.createRow(0);

                if (tab1.getSelectedIndex() == 0) {

                    for (int i = 0; i < table.getColumnCount(); i++) {
                        Cell cell;
                        cell = rowcol.createCell(i);

                        cell.setCellValue(table.getColumnName(i));
                    }
                    for (int jj = 0; jj < table.getRowCount(); jj++) {
                        Row row = sheet.createRow(jj + 1);

                        for (int k = 0; k < table.getColumnCount(); k++) {
                            Cell cell = row.createCell(k);
                            if (table.getValueAt(jj, k) != null) {
                                cell.setCellValue(table.getValueAt(jj, k).toString());

                            }

                        }

                    }

                    try (FileOutputStream out = new FileOutputStream(new File(saveFile.toString()))) {
                        wb.write(out);
                        wb.close();
                    }

                    //                openFile(saveFile.toString());
                    JOptionPane.showMessageDialog(null, "تم الحفظ بنجاح.");
                } else {
                    for (int i = 0; i < tablefor.getColumnCount(); i++) {
                        Cell cell;
                        cell = rowcol.createCell(i);

                        cell.setCellValue(tablefor.getColumnName(i));
                    }
                    for (int jj = 0; jj < tablefor.getRowCount(); jj++) {
                        Row row = sheet.createRow(jj + 1);

                        for (int k = 0; k < tablefor.getColumnCount(); k++) {
                            Cell cell = row.createCell(k);
                            if (tablefor.getValueAt(jj, k) != null) {
                                cell.setCellValue(tablefor.getValueAt(jj, k).toString());

                            }

                        }

                    }

                    FileOutputStream out = new FileOutputStream(new File(saveFile.toString()));
                    wb.write(out);
                    wb.close();
                    out.close();

                    //                openFile(saveFile.toString());
//                    JOptionPane.showMessageDialog(null, "تم الحفظ بنجاح.");
                    showSuccessNotification();

                }
            } else {
                JOptionPane.showMessageDialog(null, "لم يتم الحفظ");

            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, ex);
        } catch (IOException io) {

            System.out.println(io);
            JOptionPane.showMessageDialog(null, io);

        }

    }//GEN-LAST:event_SaveAsExcel1ActionPerformed

    private void GetFromExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GetFromExcelActionPerformed

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        //        DefaultTableModel model = new DefaultTableModel();
        //        table.setModel(model);

        File excelFile;
        FileInputStream excelFIS = null;
        BufferedInputStream excelBIS = null;
        XSSFWorkbook excelImportToJTable = null;
        String defaultCurrentDirectoryPath = "C:\\Users\\a\\Desktop";
        JFileChooser excelFileChooser = new JFileChooser(defaultCurrentDirectoryPath);
        excelFileChooser.setDialogTitle("Select Excel File");
        FileNameExtensionFilter fnef = new FileNameExtensionFilter("EXCEL FILES", "xls", "xlsx", "xlsm");
        excelFileChooser.setFileFilter(fnef);
        int excelChooser = excelFileChooser.showOpenDialog(null);
        if (excelChooser == JFileChooser.APPROVE_OPTION) {
            try {
                excelFile = excelFileChooser.getSelectedFile();
                excelFIS = new FileInputStream(excelFile);
                excelBIS = new BufferedInputStream(excelFIS);
                excelImportToJTable = new XSSFWorkbook(excelBIS);
                XSSFSheet excelSheet = excelImportToJTable.getSheetAt(0);

                for (int row = 1; row <= excelSheet.getLastRowNum(); row++) {

                    XSSFRow excelRow = excelSheet.getRow(row);

                    XSSFCell Row0 = excelRow.getCell(0);
                    XSSFCell Row1 = excelRow.getCell(1);
                    XSSFCell Row2 = excelRow.getCell(2);
                    XSSFCell Row3 = excelRow.getCell(3);
                    XSSFCell Row4 = excelRow.getCell(4);
                    XSSFCell Row5 = excelRow.getCell(5);
                    XSSFCell Row6 = excelRow.getCell(6);
                    XSSFCell Row7 = excelRow.getCell(7);
                    XSSFCell Row8 = excelRow.getCell(8);
                    XSSFCell Row9 = excelRow.getCell(9);
                    XSSFCell Row10 = excelRow.getCell(10);
                    XSSFCell Row11 = excelRow.getCell(11);
                    XSSFCell Row12 = excelRow.getCell(12);
                    XSSFCell Row13 = excelRow.getCell(13);
                    XSSFCell Row14 = excelRow.getCell(14);
                    XSSFCell Row15 = excelRow.getCell(15);
                    XSSFCell Row16 = excelRow.getCell(16);
                    XSSFCell Row17 = excelRow.getCell(17);
                    XSSFCell Row18 = excelRow.getCell(18);

                    model.addRow(new Object[]{Row0, Row1, Row2, Row3, Row4, Row5, Row6, Row7, Row8, Row9, Row10, Row11, Row12, Row13, Row14, Row15, Row16, Row17, Row18});
                    System.out.println(Row0);

                }

                JOptionPane.showMessageDialog(null, "تم استيراد الملف بنجاح.");
            } catch (IOException iOException) {
                JOptionPane.showMessageDialog(null, iOException.getMessage());
            } finally {
                try {
                    if (excelFIS != null) {
                        excelFIS.close();
                    }
                    if (excelBIS != null) {
                        excelBIS.close();
                    }
                    if (excelImportToJTable != null) {
                        excelImportToJTable.close();
                    }
                } catch (IOException iOException) {
                    JOptionPane.showMessageDialog(null, iOException.getMessage());
                }
            }
        }
    }//GEN-LAST:event_GetFromExcelActionPerformed

    private void setNewPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setNewPasswordActionPerformed

        boolean isVisible = !Newpassword.isVisible();
        Newpassword.setVisible(isVisible);

// إجبار اللوحة الحاوية على إعادة ترتيب ومراجعة المكونات
        Newpassword.getParent().revalidate();
        Newpassword.getParent().repaint();

        if (isVisible) {

            LogIn.setText("موافق"); // تغيير النص عند ظهور الحقل
        } else {
            LogIn.setText("");
        }

    }//GEN-LAST:event_setNewPasswordActionPerformed

    private void saveSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveSettingsActionPerformed

        try {
            // 1. قراءة القيم من حقول النص في الواجهة
            double cigar = Double.parseDouble(CigarPriceTxt.getText().trim());
            double cig = Double.parseDouble(CigPriceTxt.getText().trim());
            double exchange = Double.parseDouble(exchangePriceTxt.getText().trim());

            // 2. إرسالها لدالة الحفظ الموحدة (التي أصبحت تحفظ الأسعار والحالات معاً)
            boolean isSaved = saveSettings(cigar, cig, exchange);

            if (isSaved) {
                // تم استدعاء الإشعار أو رسالة النجاح بداخل دالة saveSettings مسبقاً
            } else {
                JOptionPane.showMessageDialog(this, "فشل الحفظ، تأكد من وجود صف بيانات في جدول SettingTable", "تنبيه", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "الرجاء إدخال أرقام صحيحة أو عشرية صحيحة في جميع الحقول", "خطأ إدخال", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_saveSettingsActionPerformed

    private void userNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userNameActionPerformed

    private void ItemsListEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ItemsListEditActionPerformed
        // تبديل الحالة: إذا كانت ظاهرة تختفي، وإذا كانت مخفية تظهر
        // تبديل حالة الظهور والإخفاء
        boolean currentStatus = jPanel32.isVisible();

        jPanel32.setVisible(!currentStatus);

       
        if (!currentStatus) {
            
            ItemsListEdit.setText("تخصيص قائمة الأصناف >"); 
        } else {
           
            ItemsListEdit.setText("تخصيص قائمة الأصناف <");
        }

        // إعادة الرسم وتحديث التخطيط لتظهر النتيجة فوراً
        jPanel32.revalidate();
        jPanel32.repaint();
    }//GEN-LAST:event_ItemsListEditActionPerformed

    private void ComboB_SubBranchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComboB_SubBranchActionPerformed

        Object selected = ComboB_SubBranch.getSelectedItem();

        // 1. التأكد من وجود اختيار حقيقي وليس "اختر الفرع" أو Null
        if (selected != null && !selected.toString().equals("اختر الفرع")) {
            String newBranch = selected.toString();
            String currentLocal = labelBranchName.getText(); // الفرع الحالي المعروض في الليبل

            // 2. إذا كان المختار هو نفسه الحالي، لا نفعل شيئاً
            if (newBranch.equals(currentLocal)) {
                return;
            }

            // 3. إظهار رسالة تأكيد
            int choice = JOptionPane.showConfirmDialog(this,
                    "هل تريد اعتماد فرع [" + newBranch + "] كفرع محلي لهذا الجهاز؟\n"
                    + "سيتم حفظ هذا الإعداد وتذكره عند فتح التطبيق المرة القادمة.",
                    "تأكيد تغيير الإعدادات",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                // تنفيذ الحفظ وتحديث الليبل
                saveAndSetLocalBranch(newBranch);
                ComboB_SubBranch1.setSelectedItem(newBranch);

                JOptionPane.showMessageDialog(this, "تم تغيير الفرع المحلي إلى: " + newBranch);
            } else {
                // إذا اختار "لا"، نعيد الكومبو بوكس للاختيار القديم (اختياري)
                ComboB_SubBranch.setSelectedItem(currentLocal);
            }
        }
    }//GEN-LAST:event_ComboB_SubBranchActionPerformed

    private void QuaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QuaryActionPerformed
        NavigateToQuary();
    }//GEN-LAST:event_QuaryActionPerformed

    private void SaveDailyReport1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveDailyReport1ActionPerformed

        // 1. التحقق من المدخلات الأساسية
        if (datechooser2.getDate() == null) {
            JOptionPane.showMessageDialog(null, "يجب اختيار التاريخ أولاً.");
            return;
        }

        if (ComboBoxShift.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "يجب اختيار الوردية.");
            return;
        }
        if ("".equals(labelBranchName.getText())) {
            JOptionPane.showMessageDialog(null, "أدخل أسم الفرع");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String addDate = dateFormat.format(datechooser2.getDate());

        // 2. استخدام try-with-resources لإدارة الاتصال والاستعلامات
        try (Connection conn = DBConnection.getInstance().getConnection()) {

            // التحقق من أن الاتصال صالح
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "فشل الاتصال بقاعدة البيانات.");
                return;
            }

            // 3. إعداد وتنفيذ استعلام الإدخال الأول (Table1)
            String query1 = "INSERT INTO Table1(winTaxP,winTaxD,gifPrice,gifNum,devicePrice,deviceNum,prfuPrice,prfuNum,foodPrice,foodNum,beerPrice,beerNum,winPrice,winNum,cigPrice,cigNum,addDate,id,branchName,users,EditDate) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps1 = conn.prepareStatement(query1)) {
                ps1.setString(1, "0");
                ps1.setString(2, "0");
                ps1.setString(3, txt1.getText().isEmpty() ? "0" : txt1.getText());
                ps1.setString(4, txt2.getText().isEmpty() ? "0" : txt2.getText());
                ps1.setString(5, txt3.getText().isEmpty() ? "0" : txt3.getText());
                ps1.setString(6, txt4.getText().isEmpty() ? "0" : txt4.getText());
                ps1.setString(7, txt5.getText().isEmpty() ? "0" : txt5.getText());
                ps1.setString(8, txt6.getText().isEmpty() ? "0" : txt6.getText());
                ps1.setString(9, txt7.getText().isEmpty() ? "0" : txt7.getText());
                ps1.setString(10, txt8.getText().isEmpty() ? "0" : txt8.getText());
                ps1.setString(11, txt9.getText().isEmpty() ? "0" : txt9.getText());
                ps1.setString(12, txt10.getText().isEmpty() ? "0" : txt10.getText());
                ps1.setString(13, txt11.getText().isEmpty() ? "0" : txt11.getText());
                ps1.setString(14, txt12.getText().isEmpty() ? "0" : txt12.getText());
                ps1.setString(15, txt13.getText().isEmpty() ? "0" : txt13.getText());
                ps1.setString(16, txt14.getText().isEmpty() ? "0" : txt14.getText());
                ps1.setString(17, addDate);
                ps1.setString(18, String.valueOf(ComboBoxShift.getSelectedIndex()));
                ps1.setString(19, labelBranchName.getText().isEmpty() ? "" : labelBranchName.getText());
                ps1.setString(20, Name);
                ps1.setString(21, getTime() + " | " + EditDate);

                ps1.executeUpdate();
            }

            // 4. إعداد وتنفيذ استعلام الإدخال الثاني (Table2)
            String query2 = "INSERT INTO Table2(winTaxP,winTaxD,gifPrice,gifNum,devicePrice,deviceNum,prfuPrice,prfuNum,foodPrice,foodNum,beerPrice,beerNum,winPrice,winNum,cigPrice,cigNum,addDate,id,branchName,users,EditDate) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps2 = conn.prepareStatement(query2)) {
                ps2.setString(1, txt36.getText().isEmpty() ? "0" : txt36.getText());
                ps2.setString(2, txt35.getText().isEmpty() ? "0" : txt35.getText());
                ps2.setString(3, txt21.getText().isEmpty() ? "0" : txt21.getText());
                ps2.setString(4, txt22.getText().isEmpty() ? "0" : txt22.getText());
                ps2.setString(5, txt23.getText().isEmpty() ? "0" : txt23.getText());
                ps2.setString(6, txt24.getText().isEmpty() ? "0" : txt24.getText());
                ps2.setString(7, txt25.getText().isEmpty() ? "0" : txt25.getText());
                ps2.setString(8, txt26.getText().isEmpty() ? "0" : txt26.getText());
                ps2.setString(9, txt27.getText().isEmpty() ? "0" : txt27.getText());
                ps2.setString(10, txt28.getText().isEmpty() ? "0" : txt28.getText());
                ps2.setString(11, txt29.getText().isEmpty() ? "0" : txt29.getText());
                ps2.setString(12, txt30.getText().isEmpty() ? "0" : txt30.getText());
                ps2.setString(13, txt31.getText().isEmpty() ? "0" : txt31.getText());
                ps2.setString(14, txt32.getText().isEmpty() ? "0" : txt32.getText());
                ps2.setString(15, txt33.getText().isEmpty() ? "0" : txt33.getText());
                ps2.setString(16, txt34.getText().isEmpty() ? "0" : txt34.getText());
                ps2.setString(17, addDate);
                ps2.setString(18, String.valueOf(ComboBoxShift.getSelectedIndex()));
                ps2.setString(19, labelBranchName.getText().isEmpty() ? "" : labelBranchName.getText());
                ps2.setString(20, Name);
                ps2.setString(21, getTime() + " | " + EditDate);
                ps2.executeUpdate();

            }
            jDialogDaily.dispose();
            showSuccessNotification();
            showSalesFor_data();
            showSales_data();

        } catch (SQLException | NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, "حدث خطأ: " + e.getMessage());
        }

    }//GEN-LAST:event_SaveDailyReport1ActionPerformed

    private void Edit3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Edit3ActionPerformed

        Connection conn = null;
        PreparedStatement pstmt = null;

        if (tab1.getSelectedIndex() == 0) {
            if (table.getSelectedRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "يرجى تحديد اليومية من الجدول. ");
            } else {
                int row = table.getSelectedRow();
                String branchName = String.valueOf(table.getValueAt(row, 18));
                int shiftNum = Integer.parseInt(table.getValueAt(row, 17).toString());
                String date_search = String.valueOf(table.getValueAt(row, 16));

                try {
                    conn = DBConnection.getInstance().getConnection();
                    if (conn == null || conn.isClosed()) {
                        JOptionPane.showMessageDialog(null, "فشل الاتصال بقاعدة البيانات.");
                        return;
                    }

                    // استخدام استعلام UPDATE مباشر مع PreparedStatement
                    String sql = "UPDATE Table1 SET cigNum = ?, cigPrice = ?, winNum = ?, winprice = ?, beerNum = ?,beerPrice = ?,"
                            + "foodNum = ?, foodPrice = ?, prfuNum = ?, prfuprice = ?, deviceNum = ?,devicePrice = ?, gifNum = ?, gifPrice = ?, "
                            + "winTaxD = ?, winTaxP = ?,users = ?,EditDate=? WHERE branchName =? AND id = ? AND addDate = ? ";
                    pstmt = conn.prepareStatement(sql);
                    String EditDate2 = getTime() + " | " + EditDate;
                    String branchN = labelBranchName.getText();
                    int cigNum = Integer.parseInt(txt14.getText());
                    int cigPrice = Integer.parseInt(txt13.getText());
                    int winNum = Integer.parseInt(txt12.getText());
                    int winprice = Integer.parseInt(txt11.getText());
                    int beerNum = Integer.parseInt(txt10.getText());
                    int beerPrice = Integer.parseInt(txt9.getText());
                    int foodNum = Integer.parseInt(txt8.getText());
                    int foodPrice = Integer.parseInt(txt7.getText());
                    int prfuNum = Integer.parseInt(txt6.getText());
                    int prfuprice = Integer.parseInt(txt5.getText());
                    int deviceNum = Integer.parseInt(txt4.getText());
                    int devicePrice = Integer.parseInt(txt3.getText());
                    int gifNum = Integer.parseInt(txt2.getText());
                    int gifPrice = Integer.parseInt(txt1.getText());
                    int wintaxD = Integer.parseInt("0");
                    int wintaxP = Integer.parseInt("0");

                    // تعيين القيم في PreparedStatement
                    pstmt.setInt(1, cigNum);
                    pstmt.setInt(2, cigPrice);
                    pstmt.setInt(3, winNum);
                    pstmt.setInt(4, winprice);
                    pstmt.setInt(5, beerNum);
                    pstmt.setInt(6, beerPrice);
                    pstmt.setInt(7, foodNum);
                    pstmt.setInt(8, foodPrice);
                    pstmt.setInt(9, prfuNum);
                    pstmt.setInt(10, prfuprice);
                    pstmt.setInt(11, deviceNum);
                    pstmt.setInt(12, devicePrice);
                    pstmt.setInt(13, gifNum);
                    pstmt.setInt(14, gifPrice);
                    pstmt.setInt(15, wintaxD);
                    pstmt.setInt(16, wintaxP);
                    pstmt.setString(17, UserName);
                    pstmt.setString(18, EditDate2);
                    pstmt.setString(19, branchName);
                    pstmt.setInt(20, shiftNum); // قيمة id لتحديد الصف
                    pstmt.setString(21, date_search); // قيمة date لتحديد الصف

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
//                        JOptionPane.showMessageDialog(null, "تم التعديل بنجاح ");
                        showSuccessNotification();
                    } else {
                        JOptionPane.showMessageDialog(null, "لم يتم العثور على السجل المراد تعديله.");
                    }
                } catch (SQLException | NumberFormatException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);
                } finally {
                    try {
                        if (pstmt != null) {
                            pstmt.close();
                        }
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                }
                showSales_data();

            }
        } else {

            if (tablefor.getSelectedRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "يرجى تحديد اليومية من الجدول. ");
            } else {
                int row = tablefor.getSelectedRow();
                String branchName = String.valueOf(tablefor.getValueAt(row, 18));
                int shiftNum = Integer.parseInt(tablefor.getValueAt(row, 17).toString());
                String date_search = String.valueOf(tablefor.getValueAt(row, 16));
                try {
                    conn = DBConnection.getInstance().getConnection();
                    if (conn == null || conn.isClosed()) {
                        JOptionPane.showMessageDialog(null, "فشل الاتصال بقاعدة البيانات.");
                        return;
                    }

                    String sql = "UPDATE Table2 SET cigNum = ?, cigPrice = ?, winNum = ?, winprice = ?, beerNum = ?,beerPrice = ?,"
                            + "foodNum = ?, foodPrice = ?, prfuNum = ?, prfuprice = ?, deviceNum = ?,devicePrice = ?, gifNum = ?, gifPrice = ?, "
                            + "winTaxD = ?, winTaxP = ?,users = ?,EditDate=? WHERE branchName =? AND id = ? AND addDate = ? ";
                    pstmt = conn.prepareStatement(sql);

                    String EditDate2 = getTime() + " | " + EditDate;
                    int cigNum = Integer.parseInt(txt34.getText());
                    int cigPrice = Integer.parseInt(txt33.getText());
                    int winNum = Integer.parseInt(txt32.getText());
                    int winprice = Integer.parseInt(txt31.getText());
                    int beerNum = Integer.parseInt(txt30.getText());
                    int beerPrice = Integer.parseInt(txt29.getText());
                    int foodNum = Integer.parseInt(txt28.getText());
                    int foodPrice = Integer.parseInt(txt27.getText());
                    int prfuNum = Integer.parseInt(txt26.getText());
                    int prfuprice = Integer.parseInt(txt25.getText());
                    int deviceNum = Integer.parseInt(txt24.getText());
                    int devicePrice = Integer.parseInt(txt23.getText());
                    int gifNum = Integer.parseInt(txt22.getText());
                    int gifPrice = Integer.parseInt(txt21.getText());
                    int wintaxD = Integer.parseInt(txt35.getText());
                    int wintaxP = Integer.parseInt(txt36.getText());

                    // تعيين القيم في PreparedStatement
                    pstmt.setInt(1, cigNum);
                    pstmt.setInt(2, cigPrice);
                    pstmt.setInt(3, winNum);
                    pstmt.setInt(4, winprice);
                    pstmt.setInt(5, beerNum);
                    pstmt.setInt(6, beerPrice);
                    pstmt.setInt(7, foodNum);
                    pstmt.setInt(8, foodPrice);
                    pstmt.setInt(9, prfuNum);
                    pstmt.setInt(10, prfuprice);
                    pstmt.setInt(11, deviceNum);
                    pstmt.setInt(12, devicePrice);
                    pstmt.setInt(13, gifNum);
                    pstmt.setInt(14, gifPrice);
                    pstmt.setInt(15, wintaxD);
                    pstmt.setInt(16, wintaxP);
                    pstmt.setString(17, UserName);
                    pstmt.setString(18, EditDate2);
                    pstmt.setString(19, branchName);
                    pstmt.setInt(20, shiftNum); // قيمة id لتحديد الصف
                    pstmt.setString(21, date_search); // قيمة date لتحديد الصف

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
//                        JOptionPane.showMessageDialog(null, "تم التعديل بنجاح ");
                        showSuccessNotification();
                    } else {
                        JOptionPane.showMessageDialog(null, "لم يتم العثور على السجل المراد تعديله.");
                    }

                } catch (SQLException | NumberFormatException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);
                } finally {
                    try {
                        if (pstmt != null) {
                            pstmt.close();
                        }
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
                }

                showSalesFor_data();

            }
        }
        jDialogDaily.dispose();
        showSuccessNotification();
    }//GEN-LAST:event_Edit3ActionPerformed

    private void AppendDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AppendDataActionPerformed

        // 1. جلب الفرع المختار
        String selectedBranch = ComboB_SubBranch1.getSelectedItem().toString();

        if (selectedBranch.equals("اختر الفرع")) {
            JOptionPane.showMessageDialog(null, "يرجى اختيار الفرع.");
            return;
        }

        // 2. رسالة تأكيد مخصصة للإضافة فقط (دون حذف القديم)
        int confirm = JOptionPane.showConfirmDialog(null,
                "تأكيد إضافة البيانات: سيتم رفع وتسجيل البيانات الجديدة فقط إلى السحابة لفرع [" + selectedBranch + "] ً.\nهل تريد الاستمرار؟",
                "تأكيد إضافة البيانات الجديدة",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        new Thread(() -> {
            try {
                // 3. التحقق من وجود اتصال بالإنترنت قبل البدء
                if (!isInternetAvailable()) {
                    SwingUtilities.invokeLater(() -> {
                        progBar.setVisible(false);
                        upload_downLabel.setVisible(false);
                        JOptionPane.showMessageDialog(null,
                                "فشل الاتصال بالإنترنت! يرجى التحقق من الشبكة ثم إعادة المحاولة.",
                                "خطأ اتصال",
                                JOptionPane.ERROR_MESSAGE);
                    });
                    return;
                }

                // 🚀 إظهار المؤشر والأيقونة في الواجهة الرسومية
                SwingUtilities.invokeLater(() -> {
                    progBar.setValue(0);
                    progBar.setVisible(true);
                    upload_downLabel.setVisible(true);
                    java.net.URL downloadImgUrl = getClass().getResource("/arrow_upload_progress_30dp_F19E39_FILL0_wght400_GRAD0_opsz24.png");

                    if (downloadImgUrl != null) {
                        upload_downLabel.setIcon(new ImageIcon(downloadImgUrl));
                        upload_downLabel.setText("");
                    } else {
                        upload_downLabel.setText("❌");
                        System.out.println("تحذير: لم يتم العثور على ملف الأيقونة في المسار المحدد!");
                    }
                });

                // 4. تحديث التقدم قبل البدء
                updateProgress(10);

                // إضافة بيانات الجدول الأول (Table1) - Append
                DataSyncManager.appendTableToGoogle("Table1", this.idTable1, "Sheet1");
                updateProgress(50); // وصلنا للمنتصف

                // إضافة بيانات الجدول الثاني (Table2) - Append (تم تصحيح اسم الجدول هنا)
                DataSyncManager.appendTableToGoogle("Table2", this.idTable2, "Sheet1");
                updateProgress(90); // قاربنا على الانتهاء

                // إتمام العملية بنجاح تام
                updateProgress(100);

                // 5. إغلاق المؤشر وإظهار رسالة النجاح
                SwingUtilities.invokeLater(() -> {
                    progBar.setVisible(false);
                    upload_downLabel.setVisible(false);
//                JOptionPane.showMessageDialog(null, "تمت إضافة البيانات الجديدة بنجاح.");
                    showSuccessNotification();
                });

            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    progBar.setVisible(false);
                    upload_downLabel.setVisible(false);
                });
                e.printStackTrace();
                // إظهار رسالة خطأ حقيقية عند فشل الاتصال أو العملية
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, "حدث خطأ أثناء إضافة البيانات أو انقطع الاتصال: " + e.getMessage(), "فشل الإضافة", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();

    }//GEN-LAST:event_AppendDataActionPerformed

    private void txt3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt3ActionPerformed

    private void BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackActionPerformed

        if ("Bills".equals(lastScreen)) {
            NavigateToBills();
        } else if ("Reports".equals(lastScreen)) {
            NavigateToReport();
        } else if ("Tax".equals(lastScreen)) {

            NavigateToTax();
        } else {

            NavigateToBills();
        }


    }//GEN-LAST:event_BackActionPerformed

    private void RadioGetOnlineDailyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioGetOnlineDailyActionPerformed
       
        AppendData.setVisible(true);
            jSeparator22.setVisible(true);
            
        
    }//GEN-LAST:event_RadioGetOnlineDailyActionPerformed

    private void RadioGetOnlineItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RadioGetOnlineItemsActionPerformed
         AppendData.setVisible(false);
            jSeparator22.setVisible(false);
    }//GEN-LAST:event_RadioGetOnlineItemsActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AppendData;
    public static javax.swing.JButton Back;
    public static javax.swing.JButton BillsNavigator;
    private javax.swing.JCheckBox CheckCig;
    private javax.swing.JCheckBox CheckCigar;
    private javax.swing.JCheckBox CheckDevice;
    private javax.swing.JCheckBox CheckFood;
    private javax.swing.JCheckBox CheckGift;
    private javax.swing.JCheckBox CheckPerfum;
    private javax.swing.JCheckBox CheckWin;
    private javax.swing.JCheckBox Checkbeer;
    public static javax.swing.JTextField CigPriceTxt;
    private javax.swing.JTextField CigarPriceTxt;
    public static javax.swing.JButton Clear;
    private javax.swing.JComboBox ComboB_SubBranch;
    public static javax.swing.JComboBox ComboB_SubBranch1;
    private javax.swing.JComboBox ComboB_national;
    private javax.swing.JComboBox ComboB_type;
    private javax.swing.JComboBox ComboB_type1;
    private javax.swing.JComboBox ComboBoxShift;
    private javax.swing.JButton ConnectionTest;
    private javax.swing.JTextField Count;
    public static javax.swing.JButton Delete;
    private javax.swing.JMenuItem Delete2;
    public static javax.swing.JTextField Description;
    public static javax.swing.JButton Details_of_DailySales;
    private javax.swing.JDialog Dialog;
    public static javax.swing.JButton Edit;
    public static javax.swing.JButton Edit2;
    public static javax.swing.JButton Edit3;
    private javax.swing.JButton Exite;
    public static javax.swing.JTextField FobValue;
    public static javax.swing.JButton GetFromExcel;
    private javax.swing.JButton GetTax;
    private javax.swing.JButton ItemsListEdit;
    private javax.swing.JButton LogIn;
    public static javax.swing.JButton LogOut;
    private javax.swing.JPasswordField Newpassword;
    private javax.swing.JMenuItem OpenExcel;
    public static javax.swing.JButton Quary;
    private javax.swing.JRadioButton RadiCodeNum;
    private javax.swing.JRadioButton RadioDiscount;
    private javax.swing.JRadioButton RadioGetDaily;
    private javax.swing.JRadioButton RadioGetOnlineDaily;
    private javax.swing.JRadioButton RadioGetOnlineItems;
    private javax.swing.JRadioButton RadioGetReport;
    private javax.swing.JRadioButton RadioGetTax;
    private javax.swing.JRadioButton RadioImport;
    private javax.swing.JRadioButton RadioOPDate;
    public static javax.swing.JButton Refreh;
    public static javax.swing.JButton ReportsNavigator;
    private javax.swing.JTextField Salesprice1;
    private javax.swing.JMenuItem SaveAsExcel;
    public static javax.swing.JButton SaveAsExcel1;
    public static javax.swing.JButton SaveDailyReport;
    public static javax.swing.JButton SaveDailyReport1;
    private javax.swing.JMenuItem Save_Edit;
    private javax.swing.JMenuItem Save_Edit2;
    public static javax.swing.JButton SettingsNavigator1;
    private javax.swing.JButton Show_image;
    public static javax.swing.JButton TaxNavigator;
    public static javax.swing.JTextField Value;
    private javax.swing.JTextField billNum;
    private javax.swing.JTextField birthDate;
    private javax.swing.JCheckBox checkBoxGetEgyNationality;
    private javax.swing.JCheckBox checkBoxGetForNationality;
    private javax.swing.JLabel codeLebel;
    private javax.swing.JButton code_view;
    private javax.swing.JLabel copyRightLab;
    private javax.swing.JLabel copyRightLab1;
    private javax.swing.JTextField count;
    private javax.swing.JMenuItem current_Balance;
    public static javax.swing.JButton current_Balance1;
    private javax.swing.JButton downloadData;
    private javax.swing.JPanel drawerPanel;
    public static javax.swing.JTextField exchangePriceTxt;
    private javax.swing.JTextField id;
    private javax.swing.JTextField input;
    private javax.swing.JDialog jDialogDaily;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelFrom;
    private javax.swing.JLabel jLabelTo;
    private javax.swing.JPanel jPanel001;
    private javax.swing.JPanel jPanel002;
    private javax.swing.JPanel jPanel003;
    private javax.swing.JPanel jPanel004;
    private javax.swing.JPanel jPanel005;
    private javax.swing.JPanel jPanel006;
    private javax.swing.JPanel jPanel007;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private static javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    public static javax.swing.JPanel jPanelBills;
    private javax.swing.JPanel jPanelContainer;
    private javax.swing.JPanel jPanelDateFrom;
    private javax.swing.JPanel jPanelDateTo;
    private javax.swing.JPanel jPanelDetailsBar;
    private javax.swing.JPanel jPanelLogInSreen;
    private javax.swing.JPanel jPanelMainContainer;
    private javax.swing.JPanel jPanelMainLogIn;
    private static javax.swing.JPanel jPanelOptionsBar;
    private javax.swing.JPanel jPanelQuary;
    public static javax.swing.JPanel jPanelReports;
    private javax.swing.JPanel jPanelScreens;
    private javax.swing.JPanel jPanelSettings;
    private javax.swing.JPanel jPanelShiftDate;
    private javax.swing.JPanel jPanelSpeace1;
    private javax.swing.JPanel jPanelSpeace2;
    private javax.swing.JPanel jPanelSpeace3;
    private javax.swing.JPanel jPanelTaxs;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator21;
    private javax.swing.JSeparator jSeparator22;
    private javax.swing.JSeparator jSeparator23;
    private javax.swing.JSeparator jSeparator24;
    private javax.swing.JSeparator jSeparator25;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    public javax.swing.JTextPane jTextPane2;
    private javax.swing.JLabel labelBranchName;
    private javax.swing.JLabel labelDate;
    private javax.swing.JLabel labelIcon;
    private javax.swing.JLabel labelImage;
    private javax.swing.JLabel labelShift;
    private javax.swing.JLabel labelUser;
    private javax.swing.JLabel lblPic;
    private javax.swing.JLabel lblPicDate;
    private javax.swing.JLabel lblPicShift;
    private javax.swing.JLabel loginLabelDate;
    private javax.swing.JLabel loginLabl;
    public static javax.swing.JButton moreOption;
    public static javax.swing.JButton moreOption2;
    private javax.swing.JTextField name;
    private javax.swing.JTextField notes;
    private javax.swing.JTextField operation_date;
    private javax.swing.JTextField output;
    private javax.swing.JTextField passport;
    private javax.swing.JPasswordField password;
    private javax.swing.JPopupMenu popupMenu;
    private javax.swing.JPopupMenu popupMenu2;
    private javax.swing.JTextField price;
    private javax.swing.JProgressBar progBar;
    public static javax.swing.JButton saveOperation;
    private javax.swing.JButton saveSettings;
    private javax.swing.JButton search;
    private javax.swing.JButton searchByDate;
    private javax.swing.JButton setNewPassword;
    private javax.swing.JPopupMenu suggestionPopup;
    private javax.swing.JTabbedPane tab1;
    public static javax.swing.JTable table;
    public javax.swing.JTable tablecount1;
    public static javax.swing.JTable tablefor;
    public static javax.swing.JTextField txt1;
    public static javax.swing.JTextField txt10;
    public static javax.swing.JTextField txt11;
    public static javax.swing.JTextField txt12;
    public static javax.swing.JTextField txt13;
    public static javax.swing.JTextField txt14;
    public static javax.swing.JTextField txt2;
    public static javax.swing.JTextField txt21;
    public static javax.swing.JTextField txt22;
    public static javax.swing.JTextField txt23;
    public static javax.swing.JTextField txt24;
    public static javax.swing.JTextField txt25;
    public static javax.swing.JTextField txt26;
    public static javax.swing.JTextField txt27;
    public static javax.swing.JTextField txt28;
    public static javax.swing.JTextField txt29;
    public static javax.swing.JTextField txt3;
    public static javax.swing.JTextField txt30;
    public static javax.swing.JTextField txt31;
    public static javax.swing.JTextField txt32;
    public static javax.swing.JTextField txt33;
    public static javax.swing.JTextField txt34;
    public static javax.swing.JTextField txt35;
    public static javax.swing.JTextField txt36;
    public static javax.swing.JTextField txt4;
    public static javax.swing.JTextField txt5;
    public static javax.swing.JTextField txt6;
    public static javax.swing.JTextField txt7;
    public static javax.swing.JTextField txt8;
    public static javax.swing.JTextField txt9;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtbranchName;
    private javax.swing.JButton uploadData;
    private javax.swing.JLabel upload_downLabel;
    private javax.swing.JTextField userName;
    private javax.swing.JMenuItem view_Shift_Sales;
    public static javax.swing.JButton view_Shift_Sales1;
    private javax.swing.JTextField winTaxD;
    private javax.swing.JTextField winTaxP;
    // End of variables declaration//GEN-END:variables

}
