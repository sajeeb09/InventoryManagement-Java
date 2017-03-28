/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorymanagement;

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
public class Admin extends javax.swing.JFrame {

    /**
     * Creates new form Admin
     */
    public Admin() {
        initComponents();
        getConnection();
        getEmpTable();
        getProductTable();
        getCatTable();
        getBrandTable();
        getSaleTable();
        jTextFieldGTotal.setEditable(false);
        getCustTable();
    }
    
    Connection conn = null;
    Statement st = null;
    ResultSet rs;
    public void getConnection(){
        try{
           conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/inventory_management","root","");
           //JOptionPane.showMessageDialog(null, "Connection Established.");
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    
    public void showDetails(){
        int selectedRowIndex = jTableEmp.getSelectedRow();
        String id = jTableEmp.getModel().getValueAt(selectedRowIndex, 0).toString();
        new EmpDetails().showDetails(id);
    }
    
    public void updateEmp(){
        int selectedRowIndex = jTableEmp.getSelectedRow();
        String id = jTableEmp.getModel().getValueAt(selectedRowIndex, 0).toString();
        new UpdateEmployee().setEmp(id);
    }
    
    public void updateProduct(){
        int selectedRowIndex = jTableProduct.getSelectedRow();
        String id = jTableProduct.getModel().getValueAt(selectedRowIndex, 0).toString();
        new UpdateProduct().setProduct(id);
    }
    
    
    public void theQuery(String query){
        try{
            st = conn.createStatement();
            st.executeUpdate(query);
            st.close();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    public void getEmpTable(){
        DefaultTableModel emp = new DefaultTableModel(new String[]{"ID","Last Name", "First Name", "Department", "Job", "Salary"}, 0);
        try{
            st = conn.createStatement();
            String sql="SELECT * FROM employees";
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                String col1 = rs.getString("ID");
                String col2 = rs.getString("Last_Name");
                String col3 = rs.getString("First_Name");
                String col4 = rs.getString("Department");                
                String col5 = rs.getString("Job");
                String col6 = rs.getString("Salary");
                emp.addRow(new Object[]{col1, col2, col3, col4, col5, col6});
            }
            jTableEmp.setModel(emp);
            rs.close();
            st.close();
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
            st.close();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    public void getCatTable(){
        DefaultTableModel cat = new DefaultTableModel(new String[]{"ID","Category"}, 0);
        try{
            st = conn.createStatement();
            String sql="SELECT * FROM category_table";
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                String col1 = rs.getString("Category_ID");
                String col2 = rs.getString("Category");
                cat.addRow(new Object[]{col1, col2});
            }
            jTableCat.setModel(cat);
            rs.close();
            st.close();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    public void getBrandTable(){
        DefaultTableModel brand = new DefaultTableModel(new String[]{"ID","Brand"}, 0);
        try{
            st = conn.createStatement();
            String sql="SELECT * FROM brand_table";
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                String col1 = rs.getString("Brand_ID");
                String col2 = rs.getString("Brand");
                brand.addRow(new Object[]{col1, col2});
            }
            jTableBrand.setModel(brand);
            rs.close();
            st.close();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    public void getSaleTable(){
        DefaultTableModel sales = new DefaultTableModel(new String[]{"Sale_ID","Salesman_ID","Customer_Phone","Date"}, 0);
        try{
            st = conn.createStatement();
            String sql="SELECT * FROM sales";
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                String col1 = rs.getString("Sale_ID");
                String col2 = rs.getString("Salesman_ID");
                String col3 = rs.getString("Cust_Phone");
                String col4 = rs.getString("Date");
                sales.addRow(new Object[]{col1, col2, col3, col4});
            }
            jTableSaleID.setModel(sales);
            rs.close();
            st.close();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    public void getSaleInfoTable(){
        int selectedRow = jTableSaleID.getSelectedRow();
        String id = jTableSaleID.getModel().getValueAt(selectedRow, 0).toString();
        DefaultTableModel saleInfo = new DefaultTableModel(new String[]{"Product","Warranty","Price","Quantity","Total"}, 0);
        int total = 0;
        try{
            st = conn.createStatement();
            String sql="SELECT * FROM sale_info WHERE Sale_ID = "+id;
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                String col1 = rs.getString("Product");
                String col5 = rs.getString("Warranty");
                String col2 = rs.getString("Price");
                String col3 = rs.getString("Quantity");
                String col4 = rs.getString("Total");
                saleInfo.addRow(new Object[]{col1, col5, col2, col3, col4});
            }
            jTableSaleInfo.setModel(saleInfo);
            for(int i =0; i< jTableSaleInfo.getModel().getRowCount(); i++){
                total += Integer.parseInt(jTableSaleInfo.getModel().getValueAt(i, 4).toString());
            }
            jTextFieldGTotal.setText(total + "");
            rs.close();
            st.close();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    public void getCustTable(){
        DefaultTableModel cust = new DefaultTableModel(new String[]{"Customer_Phone","Name","Address"}, 0);
        try{
            st = conn.createStatement();
            String sql="SELECT * FROM customer";
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                String col1 = rs.getString("Cust_Phone");
                String col2 = rs.getString("Name");
                String col3 = rs.getString("Address");
                cust.addRow(new Object[]{col1, col2, col3});
            }
            jTableCust.setModel(cust);
            rs.close();
            st.close();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    private void searchCat(){
        DefaultTableModel cat = new DefaultTableModel(new String[]{"ID","Category"}, 0);
        try{
            st = conn.createStatement();
            String sql="SELECT * FROM category_table WHERE Category = '"+jTextFieldCatSearch.getText()+"%'";
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                String col1 = rs.getString("Category_ID");
                String col2 = rs.getString("Category");
                cat.addRow(new Object[]{col1, col2});
            }
            jTableCat.setModel(cat);
            rs.close();
            st.close();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    private void searchBrand(){
        DefaultTableModel brand = new DefaultTableModel(new String[]{"ID","Category"}, 0);
        try{
            st = conn.createStatement();
            String sql="SELECT * FROM brand_table WHERE Brand = '"+jTextFieldBrSearch.getText()+"%'";
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                String col1 = rs.getString("Brand_ID");
                String col2 = rs.getString("Brand");
                brand.addRow(new Object[]{col1, col2});
            }
            jTableCat.setModel(brand);
            rs.close();
            st.close();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    private void addCat(){
        if(!jTextFieldAddCat.getText().equals("")){
            try{
                theQuery("INSERT INTO category_table(Category) VALUES ('"+jTextFieldAddCat.getText().toUpperCase()+"')");
                jTextFieldAddCat.setText("");
                jLabelCatMsg.setText("");
                getCatTable();
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
        else{
            jLabelCatMsg.setText("Enter Category Name");
        }
    }
    
    private void addBrand(){
        if(!jTextFieldAddBr.getText().equals("")){
            try{
                theQuery("INSERT INTO brand_table(Brand) VALUES ('"+jTextFieldAddBr.getText().toUpperCase()+"')");
                jTextFieldAddBr.setText("");
                jLabelBrMsg.setText("");
                getBrandTable();
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
        else{
            jLabelCatMsg.setText("Enter Brand Name");
        }
    }
    
    private void updateCat(){
        if(jTableCat.getSelectedRow()==-1){
            if (jTableCat.getSelectedRow()==0){
                jLabelCatMsg.setText("Table is empty!!");
            }
            else{
                jLabelCatMsg.setText("You need to select a Category");
            }
        }
        else{
            if(!jTextFieldAddCat.getText().equals("")){
                int selectedRowIndex = jTableCat.getSelectedRow();
                theQuery("UPDATE category_table SET Category='"+jTextFieldAddCat.getText().toUpperCase()+"' WHERE Category_ID = "+jTableCat.getModel().getValueAt(selectedRowIndex, 0));
                JOptionPane.showMessageDialog(null, "Updated !");
                jLabelCatMsg.setText("");
                getCatTable();
            }
            else{
                jLabelCatMsg.setText("Enter Category Name");
            }
        }
    }
    
    private void updateBrand(){
        if(jTableBrand.getSelectedRow()==-1){
            if (jTableBrand.getSelectedRow()==0){
                jLabelBrMsg.setText("Table is empty!!");
            }
            else{
                jLabelBrMsg.setText("You need to select a Brand");
            }
        }
        else{
            if(!jTextFieldAddBr.getText().equals("")){
                int selectedRowIndex = jTableBrand.getSelectedRow();
                theQuery("UPDATE category_table SET Brand='"+jTextFieldAddBr.getText().toUpperCase()+"' WHERE Brand_ID = "+jTableBrand.getModel().getValueAt(selectedRowIndex, 0));
                JOptionPane.showMessageDialog(null, "Updated !");
                jLabelBrMsg.setText("");
                getBrandTable();
            }
            else{
                jLabelBrMsg.setText("Enter Brand Name");
            }
        }
    }
    
    private void searchEmp(){
        DefaultTableModel emp = new DefaultTableModel(new String[]{"ID","Employee_Name", "Department", "Job", "Salary"}, 0);
        try{
            st = conn.createStatement();
            String sql= null;
            if(jComboBoxSearch.getSelectedItem().equals("Search By : Name")){
                sql = "SELECT * FROM employees WHERE Last_Name like '"+jTextFieldSearch.getText()+"%' or First_Name like '"+jTextFieldSearch.getText()+"%'";
            }
            else if(jComboBoxSearch.getSelectedItem().equals("ID")){
                sql = "SELECT * FROM employees WHERE ID like '"+jTextFieldSearch.getText()+"%'";
            }
            else if(jComboBoxSearch.getSelectedItem().equals("Department")){
                sql = "SELECT * FROM employees WHERE Department like '"+jTextFieldSearch.getText()+"%'";
            }
            else{
                sql = "SELECT * FROM employees WHERE Job like '"+jTextFieldSearch.getText()+"%'";
            }
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                String col1 = rs.getString("ID");
                String col2 = rs.getString("Last_Name");
                String col3 = rs.getString("First_Name");
                String col4 = rs.getString("Department");                
                String col5 = rs.getString("Job");
                String col6 = rs.getString("Salary");
                emp.addRow(new Object[]{col1, col2, col3, col4, col5, col6});
            }
            jTableEmp.setModel(emp);
            rs.close();
            st.close();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    private void searchProduct(){
        DefaultTableModel product = new DefaultTableModel(new String[]{"ID","Category", "Brand", "Name", "Warranty", "Price", "Quantity"}, 0);
        try{
            st = conn.createStatement();
            String sql= null;
            if(jComboBoxPrSearch.getSelectedItem().equals("Search By : Name")){
                sql = "SELECT * FROM product WHERE Product_Name like '"+jTextFieldPrSearch.getText()+"%'";
            }
            else if(jComboBoxPrSearch.getSelectedItem().equals("Category")){
                sql = "SELECT * FROM product WHERE Category like '"+jTextFieldPrSearch.getText()+"%'";
            }
            else if(jComboBoxPrSearch.getSelectedItem().equals("Brand")){
                sql = "SELECT * FROM product WHERE Brand like '"+jTextFieldPrSearch.getText()+"%'";
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
            st.close();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    private void searchSale(){
        DefaultTableModel sales = new DefaultTableModel(new String[]{"Sale_ID","Salesman_ID","Customer_Phone","Date"}, 0);
            jTableSaleID.setModel(sales);
        try{
            st = conn.createStatement();
            String sql= null;
            if(jComboBoxSale.getSelectedItem().equals("Sale_ID")){
                sql = "SELECT * FROM sales WHERE Sale_ID like '"+jTextFieldSaleID.getText()+"%'";
            }
            else if(jComboBoxSale.getSelectedItem().equals("Phone Number")){
                sql = "SELECT * FROM sales WHERE Cust_Phone like '"+jTextFieldSaleID.getText()+"%'";
            }
            else if(jComboBoxSale.getSelectedItem().equals("Date")){
                sql = "SELECT * FROM sales WHERE Date = '"+jTextFieldSaleID.getText()+"'";
            }
            else{
                sql = "SELECT * FROM sales WHERE Salesman_ID like '"+jTextFieldSaleID.getText()+"%'";
            }
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                String col1 = rs.getString("Sale_ID");
                String col2 = rs.getString("Salesman_ID");
                String col3 = rs.getString("Cust_Phone");
                String col4 = rs.getString("Date");
                sales.addRow(new Object[]{col1, col2, col3, col4});
            }
            jTableSaleID.setModel(sales);
            rs.close();
            st.close();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    private void searchCust(){
        DefaultTableModel cust = new DefaultTableModel(new String[]{"Customer_Phone","Name","Address"}, 0);
        try{
            st = conn.createStatement();
            String sql= null;
            if(jComboBoxCust.getSelectedItem().equals("Name")){
                sql = "SELECT * FROM customer WHERE Name like '"+jTextFieldCustSearch.getText()+"%'";
            }
            else{
                sql = "SELECT * FROM customer WHERE Cust_Phone like '"+jTextFieldCustSearch.getText()+"%'";
            }
            rs = st.executeQuery(sql);
            while(rs.next())
            {
                String col1 = rs.getString("Cust_Phone");
                String col2 = rs.getString("Name");
                String col3 = rs.getString("Address");
                cust.addRow(new Object[]{col1, col2, col3});
            }
            jTableCust.setModel(cust);
            rs.close();
            st.close();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    private void deleteEmp(){
        if(jTableEmp.getSelectedRow()==-1){
            if (jTableEmp.getSelectedRow()==0){
                jLabelMessage.setText("Table is empty!!");
            }
            else{
                jLabelMessage.setText("You need to select an employee");
            }
        }
        else{
            int del = JOptionPane.showConfirmDialog(null, "Do you really want to delete this employee?", "DELETE", JOptionPane.YES_NO_OPTION);
            if(del==0){
                int selectedRowIndex = jTableEmp.getSelectedRow();
                theQuery("delete from employees where ID='"+jTableEmp.getModel().getValueAt(selectedRowIndex, 0)+"'");
                theQuery("delete from login where ID='"+jTableEmp.getModel().getValueAt(selectedRowIndex, 0)+"'");
                JOptionPane.showMessageDialog(null, "Deleted !");
                refreshEmp();
            }
        }
    }
    
    private void deleteProduct(){
        if(jTableProduct.getSelectedRow()==-1){
            if (jTableProduct.getSelectedRow()==0){
                jLabelMessage.setText("Table is empty!!");
            }
            else{
                jLabelMessage.setText("You need to select a product");
            }
        }
        else{
            int del = JOptionPane.showConfirmDialog(null, "Do you really want to delete this product?", "DELETE", JOptionPane.YES_NO_OPTION);
            if(del==0){
                int selectedRowIndex = jTableProduct.getSelectedRow();
                theQuery("delete from Product where Product_ID='"+jTableProduct.getModel().getValueAt(selectedRowIndex, 0)+"'");
                JOptionPane.showMessageDialog(null, "Deleted !");
                refreshProduct();
            }
        }
    }
    
    private void deleteCat(){
        if(jTableCat.getSelectedRow()==-1){
            if (jTableCat.getSelectedRow()==0){
                jLabelCatMsg.setText("Table is empty!!");
            }
            else{
                jLabelCatMsg.setText("You need to select a Category");
            }
        }
        else{
            int del = JOptionPane.showConfirmDialog(null, "Do you really want to delete this category?", "DELETE", JOptionPane.YES_NO_OPTION);
            if(del==0){
                int selectedRowIndex = jTableCat.getSelectedRow();
                theQuery("delete from category_table where Category_ID="+jTableCat.getModel().getValueAt(selectedRowIndex, 0));
                JOptionPane.showMessageDialog(null, "Deleted !");
                jLabelCatMsg.setText("");
                getCatTable();
            }
        }
    }
    
    private void deleteBrand(){
        if(jTableBrand.getSelectedRow()==-1){
            if (jTableBrand.getSelectedRow()==0){
                jLabelBrMsg.setText("Table is empty!!");
            }
            else{
                jLabelBrMsg.setText("You need to select a Brand");
            }
        }
        else{
            int del = JOptionPane.showConfirmDialog(null, "Do you really want to delete this brand?", "DELETE", JOptionPane.YES_NO_OPTION);
            if(del==0){
                int selectedRowIndex = jTableBrand.getSelectedRow();
                theQuery("delete from brand_table where Brand_ID="+jTableBrand.getModel().getValueAt(selectedRowIndex, 0));
                JOptionPane.showMessageDialog(null, "Deleted !");
                jLabelBrMsg.setText("");
                getBrandTable();
            }
        }
    }
    
    private void deleteCust(){
        if(jTableCust.getSelectedRow()==-1){
            if (jTableCust.getSelectedRow()==0){
                jLabelCustMsg.setText("Table is empty!!");
            }
            else{
                jLabelCustMsg.setText("You need to select a customer.");
            }
        }
        else{
            int del = JOptionPane.showConfirmDialog(null, "Do you really want to delete this customer?", "DELETE", JOptionPane.YES_NO_OPTION);
            if(del==0){
                int selectedRowIndex = jTableCust.getSelectedRow();
                theQuery("delete from customer where Cust_Phone="+jTableCust.getModel().getValueAt(selectedRowIndex, 0));
                JOptionPane.showMessageDialog(null, "Deleted !");
                jLabelCustMsg.setText("");
                getCustTable();
            }
        }
    }
    
    private void deleteSaleInfo(){
        if(jTableSaleID.getSelectedRow()==-1){
            if (jTableSaleID.getSelectedRow()==0){
                jLabelSaleMsg.setText("Table is empty!!");
            }
            else{
                jLabelSaleMsg.setText("You need to select an item");
            }
        }
        else{
            int del = JOptionPane.showConfirmDialog(null, "Do you really want to delete this item?", "DELETE", JOptionPane.YES_NO_OPTION);
            if(del==0){
                int selectedRowIndex = jTableSaleID.getSelectedRow();
                theQuery("delete from sales where Sale_ID="+jTableSaleID.getModel().getValueAt(selectedRowIndex, 0));
                theQuery("delete from sale_info where Sale_ID="+jTableSaleID.getModel().getValueAt(selectedRowIndex, 0));
                JOptionPane.showMessageDialog(null, "Deleted !");
                jLabelSaleMsg.setText("");
                getSaleTable();
            }
        }
    }
    
    private void saleInfo(){
        if(jTableSaleID.getSelectedRow()==-1){
            if(jTableSaleID.getSelectedRow()==0){
                jLabelSaleMsg.setText("Table is empty!!");
            }
            else{
                jLabelSaleMsg.setText("You need to select an item.");
            }
        }
        else{
            getSaleInfoTable();
        }
    }
    
    private void refreshEmp(){
        jTextFieldSearch.setText("");
        jLabelMessage.setText("");
        getEmpTable();
    }
    
    private void refreshProduct(){
        jTextFieldPrSearch.setText("");
        jLabelPrMsg.setText("");
        jComboBoxPrSearch.setSelectedItem("Search BY : Name");
        getProductTable();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonLogOut = new javax.swing.JButton();
        jButtonChangePass = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableCat = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldCatSearch = new javax.swing.JTextField();
        jButtonCatSearch = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldAddCat = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButtonUpCat = new javax.swing.JButton();
        jButtonDelCat = new javax.swing.JButton();
        jLabelCatMsg = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableBrand = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldBrSearch = new javax.swing.JTextField();
        jButtonBrSearch = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldAddBr = new javax.swing.JTextField();
        jButtonAddBr = new javax.swing.JButton();
        jButtonUpBr = new javax.swing.JButton();
        jButtonDelBr = new javax.swing.JButton();
        jLabelBrMsg = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldPrSearch = new javax.swing.JTextField();
        jComboBoxPrSearch = new javax.swing.JComboBox<>();
        jButtonPrSearch = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableProduct = new javax.swing.JTable();
        jLabelPrMsg = new javax.swing.JLabel();
        jButtonAddPr = new javax.swing.JButton();
        jButtonUpdatePr = new javax.swing.JButton();
        jButtonDeletePr = new javax.swing.JButton();
        jButtonRefreshPr = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableSaleID = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldSaleID = new javax.swing.JTextField();
        jComboBoxSale = new javax.swing.JComboBox<>();
        jButtonSale = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTableSaleInfo = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabelSaleMsg = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldGTotal = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldCustSearch = new javax.swing.JTextField();
        jComboBoxCust = new javax.swing.JComboBox<>();
        jButtonCustSearch = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableCust = new javax.swing.JTable();
        jLabelCustMsg = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableEmp = new javax.swing.JTable();
        jButtonAdd = new javax.swing.JButton();
        jButtonUpdate = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jLabelMessage = new javax.swing.JLabel();
        jButtonDetails = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jTextFieldSearch = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jComboBoxSearch = new javax.swing.JComboBox<>();
        jButtonRefresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButtonLogOut.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonLogOut.setForeground(new java.awt.Color(255, 0, 0));
        jButtonLogOut.setText("LogOut");
        jButtonLogOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLogOutActionPerformed(evt);
            }
        });

        jButtonChangePass.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonChangePass.setText("Change Password");
        jButtonChangePass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonChangePassActionPerformed(evt);
            }
        });

        jTableCat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(jTableCat);

        jLabel3.setText("Search");

        jButtonCatSearch.setText("Search");
        jButtonCatSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCatSearchActionPerformed(evt);
            }
        });

        jLabel4.setText("Category");

        jButton1.setText("Add New Category");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButtonUpCat.setText("Update Category");
        jButtonUpCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpCatActionPerformed(evt);
            }
        });

        jButtonDelCat.setText("Delete");
        jButtonDelCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDelCatActionPerformed(evt);
            }
        });

        jLabelCatMsg.setForeground(new java.awt.Color(255, 0, 51));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldCatSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonCatSearch))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButtonDelCat, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(115, 115, 115)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelCatMsg, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButtonUpCat, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jTextFieldAddCat, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldCatSearch)
                    .addComponent(jButtonCatSearch, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldAddCat, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(37, 37, 37)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonUpCat, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabelCatMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButtonDelCat, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );

        jTabbedPane1.addTab("Categories", jPanel3);

        jTableBrand.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(jTableBrand);

        jLabel5.setText("Search");

        jButtonBrSearch.setText("Search");
        jButtonBrSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBrSearchActionPerformed(evt);
            }
        });

        jLabel6.setText("Brand");

        jButtonAddBr.setText("Add New Brand");
        jButtonAddBr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddBrActionPerformed(evt);
            }
        });

        jButtonUpBr.setText("Update Brand");
        jButtonUpBr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpBrActionPerformed(evt);
            }
        });

        jButtonDelBr.setText("Delete");
        jButtonDelBr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDelBrActionPerformed(evt);
            }
        });

        jLabelBrMsg.setForeground(new java.awt.Color(255, 0, 51));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldBrSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonBrSearch))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButtonDelBr, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(115, 115, 115)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel9Layout.createSequentialGroup()
                                        .addComponent(jButtonAddBr, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(57, 57, 57)
                                        .addComponent(jButtonUpBr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jTextFieldAddBr, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabelBrMsg, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldBrSearch)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonBrSearch, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldAddBr, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonAddBr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonUpBr, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addComponent(jLabelBrMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButtonDelBr, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 990, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 562, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Brands", jPanel4);

        jLabel2.setText("Search");

        jTextFieldPrSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldPrSearchActionPerformed(evt);
            }
        });

        jComboBoxPrSearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Search By : Name", "Category", "Brand" }));
        jComboBoxPrSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPrSearchActionPerformed(evt);
            }
        });

        jButtonPrSearch.setText("Search");
        jButtonPrSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldPrSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBoxPrSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(jButtonPrSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 169, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTextFieldPrSearch, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxPrSearch)
                    .addComponent(jButtonPrSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)))
        );

        jTableProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(jTableProduct);

        jLabelPrMsg.setForeground(new java.awt.Color(255, 0, 0));

        jButtonAddPr.setText("Add New Product");
        jButtonAddPr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddPrActionPerformed(evt);
            }
        });

        jButtonUpdatePr.setText("Update Existing Product");
        jButtonUpdatePr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdatePrActionPerformed(evt);
            }
        });

        jButtonDeletePr.setText("Delete Product");
        jButtonDeletePr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeletePrActionPerformed(evt);
            }
        });

        jButtonRefreshPr.setText("Refresh");
        jButtonRefreshPr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshPrActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addComponent(jLabelPrMsg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButtonAddPr, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonUpdatePr, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonDeletePr, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonRefreshPr, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonAddPr, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                    .addComponent(jButtonUpdatePr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonDeletePr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonRefreshPr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(jLabelPrMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Products", jPanel2);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jTabbedPane2.addTab("Inventory", jPanel6);

        jTableSaleID.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane5.setViewportView(jTableSaleID);

        jLabel7.setText("Search");

        jTextFieldSaleID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSaleIDActionPerformed(evt);
            }
        });

        jComboBoxSale.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sale ID", "Phone Number", "Date", "Salesman" }));

        jButtonSale.setText("Search");
        jButtonSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldSaleID)
                .addGap(18, 18, 18)
                .addComponent(jComboBoxSale, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonSale, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jTextFieldSaleID, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBoxSale, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButtonSale, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTableSaleInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane6.setViewportView(jTableSaleInfo);

        jButton2.setText("Details");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Delete");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabelSaleMsg.setForeground(new java.awt.Color(255, 0, 0));

        jLabel9.setText("Grand Total");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldGTotal))))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62)
                .addComponent(jLabelSaleMsg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextFieldGTotal))))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelSaleMsg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(26, 26, 26))
        );

        jTabbedPane3.addTab("Sale History", jPanel10);

        jLabel8.setText("Search");

        jTextFieldCustSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCustSearchActionPerformed(evt);
            }
        });

        jComboBoxCust.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Name", "Phone" }));

        jButtonCustSearch.setText("Search");
        jButtonCustSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCustSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextFieldCustSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBoxCust, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jButtonCustSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(229, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jTextFieldCustSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jComboBoxCust, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButtonCustSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTableCust.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane7.setViewportView(jTableCust);

        jButton5.setText("Delete Customer");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane7)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(293, 293, 293)
                        .addComponent(jLabelCustMsg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(380, 380, 380))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelCustMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane3.addTab("Customer", jPanel11);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 592, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Sales", jPanel7);

        jTableEmp.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTableEmp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTableEmp);

        jButtonAdd.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonAdd.setText("Add New Employee");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jButtonUpdate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonUpdate.setText("Update Existing Employee");
        jButtonUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdateActionPerformed(evt);
            }
        });

        jButtonDelete.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonDelete.setText("Delete Employee");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jLabelMessage.setForeground(new java.awt.Color(255, 0, 0));

        jButtonDetails.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonDetails.setText("Show Details");
        jButtonDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDetailsActionPerformed(evt);
            }
        });

        jTextFieldSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSearchActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton7.setText("Search");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Search");

        jComboBoxSearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Search By : Name", "ID", "Department", "Job" }));
        jComboBoxSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jComboBoxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTextFieldSearch, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxSearch)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jButtonRefresh.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonRefresh.setText("Refresh");
        jButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabelMessage, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 133, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabelMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Employees", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonChangePass)
                        .addGap(43, 43, 43)
                        .addComponent(jButtonLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonChangePass, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                    .addComponent(jButtonLogOut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLogOutActionPerformed
        dispose();
        new FirstPage().setVisible(true);
    }//GEN-LAST:event_jButtonLogOutActionPerformed

    private void jButtonChangePassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonChangePassActionPerformed
        dispose();
        new ChangeAdminPass().setVisible(true);
    }//GEN-LAST:event_jButtonChangePassActionPerformed

    private void jButtonRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshActionPerformed
        refreshEmp();
    }//GEN-LAST:event_jButtonRefreshActionPerformed

    private void jComboBoxSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxSearchActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        searchEmp();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTextFieldSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchActionPerformed
        searchEmp();
    }//GEN-LAST:event_jTextFieldSearchActionPerformed

    private void jButtonDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDetailsActionPerformed
        if(jTableEmp.getSelectedRow()==-1){
            if(jTableEmp.getSelectedRow()==0){
                jLabelMessage.setText("Table is empty!!");
            }
            else{
                jLabelMessage.setText("You need to select an Employee");
            }
        }
        else{
            showDetails();
        }
    }//GEN-LAST:event_jButtonDetailsActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        deleteEmp();
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdateActionPerformed
        if(jTableEmp.getSelectedRow()==-1){
            if(jTableEmp.getSelectedRow()==0){
                jLabelMessage.setText("Table is empty!!");
            }
            else{
                jLabelMessage.setText("You need to select an Employee");
            }
        }
        else{
            updateEmp();
        }
    }//GEN-LAST:event_jButtonUpdateActionPerformed

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        new AddEmployee().setVisible(true);
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonRefreshPrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshPrActionPerformed
        refreshProduct();
    }//GEN-LAST:event_jButtonRefreshPrActionPerformed

    private void jButtonDeletePrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeletePrActionPerformed
        deleteProduct();
    }//GEN-LAST:event_jButtonDeletePrActionPerformed

    private void jButtonUpdatePrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdatePrActionPerformed
        if(jTableProduct.getSelectedRow()==-1){
            if(jTableEmp.getSelectedRow()==0){
                jLabelPrMsg.setText("Table is empty!!");
            }
            else{
                jLabelPrMsg.setText("You need to select a Product");
            }
        }
        else{
            updateProduct();
        }
    }//GEN-LAST:event_jButtonUpdatePrActionPerformed

    private void jButtonAddPrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddPrActionPerformed
        new Product().setVisible(true);
    }//GEN-LAST:event_jButtonAddPrActionPerformed

    private void jButtonPrSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrSearchActionPerformed
        searchProduct();
    }//GEN-LAST:event_jButtonPrSearchActionPerformed

    private void jComboBoxPrSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPrSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxPrSearchActionPerformed

    private void jTextFieldPrSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldPrSearchActionPerformed
        searchProduct();
    }//GEN-LAST:event_jTextFieldPrSearchActionPerformed

    private void jButtonDelBrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDelBrActionPerformed
        deleteBrand();
    }//GEN-LAST:event_jButtonDelBrActionPerformed

    private void jButtonUpBrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpBrActionPerformed
        updateBrand();
    }//GEN-LAST:event_jButtonUpBrActionPerformed

    private void jButtonAddBrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddBrActionPerformed
        addBrand();
    }//GEN-LAST:event_jButtonAddBrActionPerformed

    private void jButtonBrSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBrSearchActionPerformed
        searchBrand();
    }//GEN-LAST:event_jButtonBrSearchActionPerformed

    private void jButtonDelCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDelCatActionPerformed
        deleteCat();
    }//GEN-LAST:event_jButtonDelCatActionPerformed

    private void jButtonUpCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpCatActionPerformed
        updateCat();
    }//GEN-LAST:event_jButtonUpCatActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        addCat();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButtonCatSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCatSearchActionPerformed
        searchCat();
    }//GEN-LAST:event_jButtonCatSearchActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        saleInfo();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        deleteSaleInfo();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButtonSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaleActionPerformed
        searchSale();
    }//GEN-LAST:event_jButtonSaleActionPerformed

    private void jTextFieldSaleIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSaleIDActionPerformed
        searchSale();
    }//GEN-LAST:event_jTextFieldSaleIDActionPerformed

    private void jButtonCustSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCustSearchActionPerformed
        searchCust();
    }//GEN-LAST:event_jButtonCustSearchActionPerformed

    private void jTextFieldCustSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCustSearchActionPerformed
        searchCust();
    }//GEN-LAST:event_jTextFieldCustSearchActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        deleteCust();
    }//GEN-LAST:event_jButton5ActionPerformed

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
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonAddBr;
    private javax.swing.JButton jButtonAddPr;
    private javax.swing.JButton jButtonBrSearch;
    private javax.swing.JButton jButtonCatSearch;
    private javax.swing.JButton jButtonChangePass;
    private javax.swing.JButton jButtonCustSearch;
    private javax.swing.JButton jButtonDelBr;
    private javax.swing.JButton jButtonDelCat;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonDeletePr;
    private javax.swing.JButton jButtonDetails;
    private javax.swing.JButton jButtonLogOut;
    private javax.swing.JButton jButtonPrSearch;
    private javax.swing.JButton jButtonRefresh;
    private javax.swing.JButton jButtonRefreshPr;
    private javax.swing.JButton jButtonSale;
    private javax.swing.JButton jButtonUpBr;
    private javax.swing.JButton jButtonUpCat;
    private javax.swing.JButton jButtonUpdate;
    private javax.swing.JButton jButtonUpdatePr;
    private javax.swing.JComboBox<String> jComboBoxCust;
    private javax.swing.JComboBox<String> jComboBoxPrSearch;
    private javax.swing.JComboBox<String> jComboBoxSale;
    private javax.swing.JComboBox<String> jComboBoxSearch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelBrMsg;
    private javax.swing.JLabel jLabelCatMsg;
    private javax.swing.JLabel jLabelCustMsg;
    private javax.swing.JLabel jLabelMessage;
    private javax.swing.JLabel jLabelPrMsg;
    private javax.swing.JLabel jLabelSaleMsg;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTable jTableBrand;
    private javax.swing.JTable jTableCat;
    private javax.swing.JTable jTableCust;
    private javax.swing.JTable jTableEmp;
    private javax.swing.JTable jTableProduct;
    private javax.swing.JTable jTableSaleID;
    private javax.swing.JTable jTableSaleInfo;
    private javax.swing.JTextField jTextFieldAddBr;
    private javax.swing.JTextField jTextFieldAddCat;
    private javax.swing.JTextField jTextFieldBrSearch;
    private javax.swing.JTextField jTextFieldCatSearch;
    private javax.swing.JTextField jTextFieldCustSearch;
    private javax.swing.JTextField jTextFieldGTotal;
    private javax.swing.JTextField jTextFieldPrSearch;
    private javax.swing.JTextField jTextFieldSaleID;
    private javax.swing.JTextField jTextFieldSearch;
    // End of variables declaration//GEN-END:variables
}
