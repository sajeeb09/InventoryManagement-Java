/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorymanagement;

import com.sun.glass.events.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author 09
 */
public class User extends javax.swing.JFrame {

    /**
     * Creates new form User
     */
    public User() {
        initComponents();
        getConnection();
        getProductTable();
        getCat();
        getBrand();
        jTextFieldDis.setText("0");
        setVisible(true);
    }
    
    Connection conn = null;
    Statement st = null;
    ResultSet rs;
    public static int count = 0;
    public int ID = 0, saleID = 0;
    public void getConnection(){
        try{
           conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/inventory_management","root","");
           //JOptionPane.showMessageDialog(null, "Connection Established.");
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    public void theQuery(String query){
        try{
            st = conn.createStatement();
            st.executeUpdate(query);
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    public void setUser(int id){
        ID = id;
        try{
            String sql = "SELECT User FROM login WHERE ID = "+id;
            rs = st.executeQuery(sql);
            while(rs.next()){
                jLabelUser.setText(rs.getString("User"));
            }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
   public void getProductTable(){
        DefaultTableModel product = new DefaultTableModel(new String[]{"ID","Category", "Brand", "Name", "Warranty", "Price", "Quantity"}, 0);
        try{
            st = conn.createStatement();
            String sql="SELECT * FROM product";
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                String col1 = rs.getString("Product_ID");
                String col2 = rs.getString("Category");
                String col3 = rs.getString("Brand");                
                String col4 = rs.getString("Product_Name");
                String col5 = rs.getString("Warranty");
                String col6 = rs.getString("Price");
                String col7 = rs.getString("Quantity");
                product.addRow(new Object[]{col1, col2, col3, col4, col5, col6, col7});
            }
            jTableProduct.setModel(product);
            rs.close();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
   
   public void getSaleInfoTable(){
        DefaultTableModel saleInfo = new DefaultTableModel(new String[]{"Product ID","Product","Warranty","Price","Quantity","Total"}, 0);
        int total = 0;
        try{
            st = conn.createStatement();
            String sql="SELECT * FROM sale_info WHERE Sale_ID = "+saleID;
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                String col6 = rs.getString("Product_ID");
                String col1 = rs.getString("Product");
                String col5 = rs.getString("Warranty");
                String col2 = rs.getString("Price");
                String col3 = rs.getString("Quantity");
                String col4 = rs.getString("Total");
                saleInfo.addRow(new Object[]{col6, col1, col5, col2, col3, col4});
            }
            jTableSaleInfo.setModel(saleInfo);
            for(int i =0; i< jTableSaleInfo.getModel().getRowCount(); i++){
                total += Integer.parseInt(jTableSaleInfo.getModel().getValueAt(i, 5).toString());
            }
            jTextFieldTotal.setText(total + "");
            rs.close();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
   
   private void addProduct1st(){
       if(!jTextFieldQttAdd.getText().equals("")){
           try{
               theQuery("INSERT INTO sales(Salesman_ID,Cust_Phone,Date) VALUES ("+ID+",'1111',CURRENT_DATE)");
               String sql = "SELECT Sale_ID FROM sales WHERE Cust_Phone = '1111'";
               rs = st.executeQuery(sql);
               while(rs.next()){
                   saleID = Integer.parseInt(rs.getString("Sale_ID"));
               }
               addProduct();
           }catch(Exception ex){
               JOptionPane.showMessageDialog(null, ex.getMessage());
           }
       }
       else{
           jLabelMsg.setText("Add Quantity !");
       }
   }
   
   private void addProduct(){
       if(jTableProduct.getSelectedRow()==-1){
            if (jTableProduct.getSelectedRow()==0){
                jLabelMsg.setText("Table is empty!!");
            }
            else{
                jLabelMsg.setText("You need to select a product");
            }
        }
       else{
           if(!jTextFieldQttAdd.getText().equals("")){
               try{
                   int selectedRowIndex = jTableProduct.getSelectedRow();
                   if(Integer.parseInt(jTableProduct.getModel().getValueAt(selectedRowIndex, 6).toString()) >= Integer.parseInt(jTextFieldQttAdd.getText())){
                       try{
                            theQuery("INSERT INTO sale_info(Sale_ID,Product_ID,Product,Warranty,Price,Quantity,Total) VALUES ("+saleID+",'"+jTableProduct.getModel().getValueAt(selectedRowIndex, 0)+"','"+jTableProduct.getModel().getValueAt(selectedRowIndex, 3)+"','"+jTableProduct.getModel().getValueAt(selectedRowIndex, 4)+"',"+jTableProduct.getModel().getValueAt(selectedRowIndex, 5)+","+jTextFieldQttAdd.getText()+","+(Integer.parseInt(jTableProduct.getModel().getValueAt(selectedRowIndex, 5).toString())*Integer.parseInt(jTextFieldQttAdd.getText()))+")");
                            jLabelMsg.setText("");
                            getSaleInfoTable();
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
                   }
                   else{
                       jLabelMsg.setText("Insufficient Product.");
                   }
               }catch(Exception ex){
                   JOptionPane.showMessageDialog(null, ex.getMessage());
               }
            }
            else{
                jLabelMsg.setText("Add Quantity !");
            }
       }
   }
   
   private void updateProduct(){
       if(jTableSaleInfo.getSelectedRow()==-1){
            if (jTableSaleInfo.getSelectedRow()==0){
                jLabelMsg.setText("Table is empty!!");
            }
            else{
                jLabelMsg.setText("You need to select a product");
            }
        }
       else{
          if(!jTextFieldQttUp.getText().equals("")){
               try{
                   int selectedRowIndex = jTableSaleInfo.getSelectedRow();
                   int qtt = 0;
                   try{
                       String sql = "select * from product where Product_ID = '"+jTableSaleInfo.getModel().getValueAt(selectedRowIndex, 0)+"'";
                       rs = st.executeQuery(sql);
                       while(rs.next()){
                           qtt = Integer.parseInt(rs.getString("Quantity"));
                       }
                   }catch(Exception ex){
                       
                   }
                   if(qtt >= Integer.parseInt(jTextFieldQttUp.getText())){
                       try{
                            theQuery("UPDATE sale_info SET Quantity ="+jTextFieldQttUp.getText()+",Total="+(Integer.parseInt(jTableSaleInfo.getModel().getValueAt(selectedRowIndex, 3).toString())*Integer.parseInt(jTextFieldQttUp.getText()))+" WHERE Product_ID='"+jTableSaleInfo.getModel().getValueAt(selectedRowIndex, 0)+"'");
                            jLabelMsg.setText("");
                            getSaleInfoTable();
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
                   }
                   else{
                       jLabelMsg.setText("Insufficient Product.");
                   }
               }catch(Exception ex){
                   JOptionPane.showMessageDialog(null, ex.getMessage());
               }
            }
            else{
                jLabelMsg.setText("Add Quantity !");
            } 
       }
   }
   
   private void deleteProduct(){
       if(jTableSaleInfo.getSelectedRow()==-1){
            if (jTableSaleInfo.getSelectedRow()==0){
                jLabelMsg.setText("Table is empty!!");
            }
            else{
                jLabelMsg.setText("You need to select an item");
            }
        }
        else{
            int del = JOptionPane.showConfirmDialog(null, "Do you really want to delete this item?", "DELETE", JOptionPane.YES_NO_OPTION);
            if(del==0){
                int selectedRowIndex = jTableSaleInfo.getSelectedRow();
                theQuery("delete from sale_info where Product_ID='"+jTableSaleInfo.getModel().getValueAt(selectedRowIndex, 0)+"'");
                JOptionPane.showMessageDialog(null, "Deleted !");
                jLabelMsg.setText("");
                getSaleInfoTable();
            }
        }
   }
   
   private void productCancel(){
       int cancel = JOptionPane.showConfirmDialog(null, "Are you sure?", "Cancel", JOptionPane.YES_NO_OPTION);
       if(cancel==0 && count!=0){
           theQuery("delete from sales where Sale_ID="+saleID);
           theQuery("delete from sale_info where Sale_ID="+saleID);
           dispose();
           new FirstPage().setVisible(true);
       }
   }
   
   private void searchProduct(){
        DefaultTableModel product = new DefaultTableModel(new String[]{"ID","Category", "Brand", "Name", "Warranty", "Price", "Quantity"}, 0);
        try{
            st = conn.createStatement();
            String sql= null;
            if(jComboBoxCat.getSelectedItem().equals("All") && jComboBoxBrand.getSelectedItem().equals("All")){
                sql = "SELECT * FROM product WHERE Product_Name like '"+jTextFieldSearch.getText()+"%'";
            }
            else if(!jComboBoxCat.getSelectedItem().equals("All") && jComboBoxBrand.getSelectedItem().equals("All")){
                sql = "SELECT * FROM product WHERE Product_Name like '"+jTextFieldSearch.getText()+"%' AND Category = '"+jComboBoxCat.getSelectedItem()+"'";
            }
            else if(jComboBoxCat.getSelectedItem().equals("All") && !jComboBoxBrand.getSelectedItem().equals("All")){
                sql = "SELECT * FROM product WHERE Product_Name like '"+jTextFieldSearch.getText()+"%' AND Brand = '"+jComboBoxBrand.getSelectedItem()+"'";
            }
            else{
                sql = "SELECT * FROM product WHERE Product_Name like '"+jTextFieldSearch.getText()+"%' AND Brand = '"+jComboBoxBrand.getSelectedItem()+"' AND Category = '"+jComboBoxCat.getSelectedItem()+"'";
            }
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                String col1 = rs.getString("Product_ID");
                String col2 = rs.getString("Category");
                String col3 = rs.getString("Brand");                
                String col4 = rs.getString("Product_Name");
                String col5 = rs.getString("Warranty");
                String col6 = rs.getString("Price");
                String col7 = rs.getString("Quantity");
                product.addRow(new Object[]{col1, col2, col3, col4, col5, col6, col7});
            }
            jTableProduct.setModel(product);
            rs.close();
            //st.close();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
   
   public void getCat(){
        try{
            st = conn.createStatement();
            String sql="SELECT * FROM category_table";
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                jComboBoxCat.addItem(rs.getString("Category"));
            }
            rs.close();
            //st.close();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
   
   public void getBrand(){
        try{
            st = conn.createStatement();
            String sql="SELECT * FROM brand_table";
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                jComboBoxBrand.addItem(rs.getString("Brand"));
            }
            rs.close();
            //st.close();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
   
   private void checkOut(){
       if(!jTextFieldCust.equals("") && !jTextFieldPhone.equals("") && !jTextFieldAddress.equals("")){
           int total = Integer.parseInt(jTextFieldTotal.getText()), qtt = 0, exist = 1;
           for(int i =0; i< jTableSaleInfo.getModel().getRowCount(); i++){
                qtt = Integer.parseInt(jTableSaleInfo.getModel().getValueAt(i, 4).toString());
                try{
                    theQuery("UPDATE product SET Quantity = ( Quantity - "+qtt+") WHERE Product_ID = '"+jTableSaleInfo.getModel().getValueAt(i, 0).toString()+"'");
                    theQuery("UPDATE sales SET Cust_Phone = '"+jTextFieldPhone.getText()+"' WHERE Sale_ID = "+saleID);
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
           try{
                String sql = "SELECT * FROM customer";
                rs = st.executeQuery(sql);
                while(rs.next()){
                    if(rs.getString("Cust_Phone").equals(jTextFieldPhone.getText())){
                        exist = 0;
                    }
                }
                if(exist == 1){
                    theQuery("INSERT INTO customer(Cust_Phone,Name,Address) VALUES('"+jTextFieldPhone.getText()+"','"+jTextFieldCust.getText()+"','"+jTextFieldAddress.getText()+"')");
                }
           }catch(Exception ex){
               JOptionPane.showMessageDialog(null, ex.getMessage());
           }
           if(!jTextFieldDis.getText().equals("")){
               jTextFieldTotal.setText((total - Integer.parseInt(jTextFieldDis.getText())) + "");
           }
           new BillInfo().setBill(ID,saleID,Integer.parseInt(jTextFieldDis.getText()),Integer.parseInt(jTextFieldTotal.getText()),jTextFieldPhone.getText(),jTextFieldCust.getText());
           dispose();
           new User().setUser(ID);
       }
       else{
           jLabelMsg.setText("Please fill the Customer Name, Phone & Address Field");
       }
   }
   
   private void refresh(){
       getProductTable();
       getSaleInfoTable();
       jLabelMsg.setText("");
       jTextFieldQttAdd.setText("");
       jTextFieldQttUp.setText("");
       jTextFieldSearch.setText("");
       jTextFieldDis.setText("");
   }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelUser = new javax.swing.JLabel();
        jButtonLogout = new javax.swing.JButton();
        jButtonProf = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldCust = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldPhone = new javax.swing.JTextField();
        jTextFieldAddress = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableProduct = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldSearch = new javax.swing.JTextField();
        jButtonSearch = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldQttAdd = new javax.swing.JTextField();
        jButtonAdd = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableSaleInfo = new javax.swing.JTable();
        jLabelMsg = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldQttUp = new javax.swing.JTextField();
        jButtonUpdate = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonCheck = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldTotal = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldDis = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        jComboBoxCat = new javax.swing.JComboBox<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        jComboBoxBrand = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabelUser.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabelUser.setForeground(new java.awt.Color(51, 0, 255));

        jButtonLogout.setText("Logout");
        jButtonLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogoutActionPerformed(evt);
            }
        });

        jButtonProf.setText("See Profile");
        jButtonProf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonProfActionPerformed(evt);
            }
        });

        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jLabel1.setText("Customer Name");

        jLabel2.setText("Phone Number");

        jLabel3.setText("Address");

        jTableProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTableProduct);

        jLabel4.setText("Search");

        jButtonSearch.setText("Search");
        jButtonSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchActionPerformed(evt);
            }
        });

        jLabel5.setText("Quantity");

        jTextFieldQttAdd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldQttAddKeyTyped(evt);
            }
        });

        jButtonAdd.setText("Add");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jTableSaleInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(jTableSaleInfo);

        jLabelMsg.setForeground(new java.awt.Color(255, 0, 0));

        jLabel6.setText("Quantity");

        jTextFieldQttUp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldQttUpKeyTyped(evt);
            }
        });

        jButtonUpdate.setText("Update");
        jButtonUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdateActionPerformed(evt);
            }
        });

        jButtonDelete.setText("Delete");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jButtonCheck.setText("Checkout");
        jButtonCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckActionPerformed(evt);
            }
        });

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jLabel7.setText("Total");

        jLabel8.setText("Discount");

        jTextFieldDis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldDisKeyTyped(evt);
            }
        });

        jComboBoxCat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All" }));
        jScrollPane4.setViewportView(jComboBoxCat);

        jComboBoxBrand.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All" }));
        jScrollPane5.setViewportView(jComboBoxBrand);

        jButton1.setText("Refresh");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(61, 61, 61)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldCust)
                                    .addComponent(jTextFieldPhone)
                                    .addComponent(jTextFieldAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE))))
                        .addGap(0, 123, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextFieldQttAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(jLabelMsg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jScrollPane2)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextFieldQttUp, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButtonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButtonCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldTotal)
                                    .addComponent(jTextFieldDis, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButtonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(20, 20, 20)))))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldCust, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane4)
                        .addComponent(jScrollPane5))
                    .addComponent(jButtonSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTextFieldQttAdd)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldDis)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonDelete, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldQttUp, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonUpdate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButtonCheck, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16))
        );

        jScrollPane3.setViewportView(jPanel1);

        jButton2.setText("Change Username or Password");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabelUser, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(jButtonProf, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 922, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelUser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonLogout, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonProf, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setSize(new java.awt.Dimension(940, 945));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldQttAddKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldQttAddKeyTyped
        char c =evt.getKeyChar();
        if(!(Character.isDigit(c) || c==KeyEvent.VK_BACKSPACE || c==KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldQttAddKeyTyped

    private void jTextFieldQttUpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldQttUpKeyTyped
        char c =evt.getKeyChar();
        if(!(Character.isDigit(c) || c==KeyEvent.VK_BACKSPACE || c==KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldQttUpKeyTyped

    private void jTextFieldDisKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldDisKeyTyped
        char c =evt.getKeyChar();
        if(!(Character.isDigit(c) || c==KeyEvent.VK_BACKSPACE || c==KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_jTextFieldDisKeyTyped

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        if(count==0){
            count++;
            addProduct1st();
        }
        else{
            addProduct();
        }
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdateActionPerformed
        updateProduct();
    }//GEN-LAST:event_jButtonUpdateActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        deleteProduct();
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        productCancel();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchActionPerformed
        searchProduct();
    }//GEN-LAST:event_jButtonSearchActionPerformed

    private void jButtonCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckActionPerformed
        checkOut();
    }//GEN-LAST:event_jButtonCheckActionPerformed

    private void jButtonProfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonProfActionPerformed
        new UserProfile().setEmp(ID);
    }//GEN-LAST:event_jButtonProfActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        refresh();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dispose();
        new ChangeUserPass().setID(ID);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButtonLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogoutActionPerformed
        dispose();
        new LoginUser().setVisible(true);
    }//GEN-LAST:event_jButtonLogoutActionPerformed

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(User.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new User().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonCheck;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonLogout;
    private javax.swing.JButton jButtonProf;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JButton jButtonUpdate;
    private javax.swing.JComboBox<String> jComboBoxBrand;
    private javax.swing.JComboBox<String> jComboBoxCat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabelMsg;
    private javax.swing.JLabel jLabelUser;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTableProduct;
    private javax.swing.JTable jTableSaleInfo;
    private javax.swing.JTextField jTextFieldAddress;
    private javax.swing.JTextField jTextFieldCust;
    private javax.swing.JTextField jTextFieldDis;
    private javax.swing.JTextField jTextFieldPhone;
    private javax.swing.JTextField jTextFieldQttAdd;
    private javax.swing.JTextField jTextFieldQttUp;
    private javax.swing.JTextField jTextFieldSearch;
    private javax.swing.JTextField jTextFieldTotal;
    // End of variables declaration//GEN-END:variables
}
